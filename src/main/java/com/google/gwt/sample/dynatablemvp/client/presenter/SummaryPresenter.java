package com.google.gwt.sample.dynatablemvp.client.presenter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.validation.ConstraintViolation;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.sample.dynatablemvp.client.event.CreatePersonEvent;
import com.google.gwt.sample.dynatablemvp.client.event.EditPersonEvent;
import com.google.gwt.sample.dynatablemvp.client.event.FilterChangeEvent;
import com.google.gwt.sample.dynatablemvp.client.event.PersonProxyChangeEvent;
import com.google.gwt.sample.dynatablemvp.shared.AddressProxy;
import com.google.gwt.sample.dynatablemvp.shared.DynaTableRequestFactory;
import com.google.gwt.sample.dynatablemvp.shared.DynaTableRequestFactory.SchoolCalendarRequest;
import com.google.gwt.sample.dynatablemvp.shared.PersonProxy;
import com.google.gwt.sample.dynatablemvp.shared.PersonRelation;
import com.google.gwt.sample.dynatablemvp.shared.ScheduleProxy;
import com.google.gwt.sample.dynatablemvp.shared.TimeSlotProxy;
import com.google.gwt.sample.dynatablemvp.shared.TimeTools;
import com.google.gwt.sample.dynatablemvp.shared.WeekDayStorage;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class SummaryPresenter implements Presenter {
	private static final Logger logger = Logger.getLogger(SummaryPresenter.class.getName());
	
	public interface Display {
		HasData<PersonProxy> getTable();

		SelectionModel<PersonProxy> getSelectionModel();

		HasClickHandlers getCreateButton();

		Widget asWidget();

		void setPageStart(int start);

		int getPageStart();
		int getPageSize();

		List<PersonProxy> getVisibleItems();

		PersonProxy getSelectedObject();

		void setSelectedItem(Integer selectedItem);
		
		void redrawLastSelectedLine() ;

	}

	private final DynaTableRequestFactory requests;
	private final HandlerManager eventBus;
	private final Display display;

	private final WeekDayStorage weekDayStorage;
	private int lastFetch;
	private boolean pending;


	public SummaryPresenter(DynaTableRequestFactory rpcService,
			HandlerManager eventBus, Display display) {
		this.requests = rpcService;
		this.eventBus = eventBus;
		this.display = display;
		weekDayStorage = new WeekDayStorage();
		weekDayStorage.setAllDaysChecked();
	}

	public void bind() {
		logger.fine("bind() executed");
		this.display.getCreateButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new CreatePersonEvent());
			}
		});
		this.display.getTable().setSelectionModel(
				this.display.getSelectionModel());
		this.display.getSelectionModel().addSelectionChangeHandler(
				new SelectionChangeEvent.Handler() {
					@Override
					public void onSelectionChange(SelectionChangeEvent event) {
						SummaryPresenter.this.refreshSelection();
					}
				});
		this.display.getTable().addRangeChangeHandler(
				new RangeChangeEvent.Handler() {

					@Override
					public void onRangeChange(RangeChangeEvent event) {
						Range r = event.getNewRange();
						int start = r.getStart();
						fetch(start);
					}
				});
		eventBus.addHandler(PersonProxyChangeEvent.TYPE,
				new PersonProxyChangeEvent.Handler() {
					@Override
					public void onPersonChanged(PersonProxyChangeEvent e) {
						SummaryPresenter.this.onPersonChanged(e);
					}
				});

		eventBus.addHandler(FilterChangeEvent.TYPE,
				new FilterChangeEvent.Handler() {
					@Override
					public void onFilterChanged(FilterChangeEvent e) {
						weekDayStorage.setWeekDayValue(e.getDay(), e.isSelected());
						logger.fine("onFilterChanged() executed: day="
								+ e.getDay() + "; isSelected=" + e.isSelected()+";"
								+" weekDayBits="+weekDayStorage.getWeekDayBits()+ ";");
						if (!pending) {
							pending = true;
							Scheduler.get().scheduleFinally(new ScheduledCommand() {
								@Override
								public void execute() {
									pending = false;
									fetch(0);
								}
							});
						}
					}
				});
		
	}

	public void go(final HasWidgets container) {
		bind();
		fetch(0);
	}

	protected void restoreCheckBoxValues() {
		for (int day = 0; day < 7; day++) {
			eventBus.fireEvent(new FilterChangeEvent(day, weekDayStorage.isWeekDayChecked(day)));
		}
	}

	private void fetch(final int start) {
		lastFetch = start;
		logger.fine("fetch("+start+") executed");
		final int pageSize = this.display.getPageSize();
		final boolean rowCountExact = SummaryPresenter.this.display.getTable().isRowCountExact();
		SchoolCalendarRequest schoolCalendarRequest = this.requests.schoolCalendarRequest();
		schoolCalendarRequest.getPeople(Arrays.asList(PersonRelation.SHEDULE)
				,start, pageSize, weekDayStorage.getWeekDayBits())
				.with("classSchedule.timeSlots")
				.fire(new Receiver<List<PersonProxy>>() {
					@Override
					public void onFailure(ServerFailure error) {
						logger.severe(error.getMessage());
					}

					@Override
					public void onConstraintViolation(
							Set<ConstraintViolation<?>> violations) {
						Iterator<ConstraintViolation<?>> violIter = violations.iterator();
						while(violIter.hasNext())
							logger.severe(violIter.next().getMessage());						
					}

					@Override
					public void onSuccess(List<PersonProxy> response) {
//						CHECKDATA(response);
						if (lastFetch != start) {
							return;
						}

						final int responses = response.size();
						final int allRowsCount = SummaryPresenter.this.display.getTable().getRowCount();
						logger.fine("schoolCalendarRequest().getPeople returns "+responses+" elements.");
						SummaryPresenter.this.display.getTable().setRowData(start, response);
						SummaryPresenter.this.display.setPageStart(start);						
						if (start == 0	|| !rowCountExact) {
							final int rowCount = start + responses;
							final boolean exact = responses<pageSize;

							logger.fine("allRowsCount="+allRowsCount+"; start="+start+"; responses="+responses+"; rowCount="+rowCount+"; exact="+exact+";");
							if(rowCountExact==false && exact || start==0)
								SummaryPresenter.this.display.getTable().setRowCount(rowCount,exact);
						}
					}
					
					@SuppressWarnings("unused")
					private void CHECKDATA(List<PersonProxy> people) {
						for(PersonProxy person: people){
							String msg = "CHECKDATA: "+person.getName()+"("+person.getId()+")";
							final PersonProxy mentor = person.getMentor();
							if(mentor!=null)
								msg+=" [mentor ("+mentor.getId()+"): "+mentor.getName()+"]";
							final AddressProxy address = person.getAddress();
							if(address!=null)
								msg+=" [address: "+address.getStreet()+"]";
							final ScheduleProxy classSchedule = person.getClassSchedule();
							if(classSchedule!=null){
								msg+=" [schedule: "+classSchedule.getKey()+" { ";
								List<TimeSlotProxy> timeSlots = classSchedule.getTimeSlots();
								if(timeSlots!=null)
									for(TimeSlotProxy timeSlot : timeSlots){
										final TimeTools timeTools = new TimeTools(timeSlot.getDayOfWeek(),timeSlot.getStartMinutes(),timeSlot.getEndMinutes());
										msg += timeSlot.getId()+":"+timeTools.getDescription()+",";
									}
								else
									msg+="timeslot is null";
								msg+=" }]";
							}
							logger.fine(msg);
						}
					}
				});
	}


	protected void onPersonChanged(PersonProxyChangeEvent e) {
		if(e.isMerged()){

			// Is the changing record onscreen?
			int displayOffset = offsetOf(e.getEntityProxyId());
			if (displayOffset != -1) {
				// Record is onscreen and may differ from our data
				requests.schoolCalendarRequest().findPerson(e.getPersonId(), Collections.singletonList(PersonRelation.SHEDULE))
				.with("classSchedule.timeSlots")
				.fire(new Receiver<PersonProxy>(){
					@Override
					public void onSuccess(PersonProxy person) {
						// Re-check offset in case of changes while waiting for
						// data
						int offset = offsetOf(person.stableId());
						if (offset != -1) {
							logger.fine("onPersonChanged: "+person.getName());
							SummaryPresenter.this.display.getTable()
									.setRowData(
											SummaryPresenter.this.display
													.getPageStart() + offset,
											Collections.singletonList(person));
							SummaryPresenter.this.display.setSelectedItem(person.getId());
						}
					}
					
				});;
						
			}			
		} else {
			logger.fine("onPersonChanged: PERSIST lastFetch="+lastFetch+";");
			// Re-fetch if we're already displaying the last page
			if (this.display.getTable().isRowCountExact()) {
				int rowCount=display.getTable().getRowCount();
				int lastPageOffset=rowCount - display.getPageSize();
				if(lastFetch<lastPageOffset)
					display.getTable().setRowCount(rowCount,false);
				fetch(lastFetch+1);
			} 
			
		}
	}

	private int offsetOf(EntityProxyId<PersonProxy> personId) {
		List<PersonProxy> displayedItems = SummaryPresenter.this.display
				.getVisibleItems();
		for (int offset = 0, j = displayedItems.size(); offset < j; offset++) {
			if (personId.equals(displayedItems.get(offset).stableId())) {
				return offset;
			}
		}
		return -1;
	}

	void refreshSelection() {
		PersonProxy person = SummaryPresenter.this.display
				.getSelectedObject();
		if (person == null) {
			return;
		}
		eventBus.fireEvent(new EditPersonEvent(person));
		SummaryPresenter.this.display.redrawLastSelectedLine();
		SummaryPresenter.this.display.getSelectionModel().setSelected(
				person, false);
	}

}