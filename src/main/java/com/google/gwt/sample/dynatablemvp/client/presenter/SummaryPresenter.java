package com.google.gwt.sample.dynatablemvp.client.presenter;

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
import com.google.gwt.sample.dynatablemvp.shared.DynaTableRequestFactory;
import com.google.gwt.sample.dynatablemvp.shared.PersonProxy;
import com.google.gwt.sample.dynatablemvp.shared.WeekDayStorage;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.web.bindery.requestfactory.shared.EntityProxyChange;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.google.web.bindery.requestfactory.shared.WriteOperation;

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
		com.google.web.bindery.event.shared.EventBus eventBusRf = this.requests.getEventBus();
		EntityProxyChange.registerForProxyType(eventBusRf, PersonProxy.class,
				new EntityProxyChange.Handler<PersonProxy>() {
					@Override
					public void onProxyChange(
							EntityProxyChange<PersonProxy> event) {
						SummaryPresenter.this.onPersonChanged(event);
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
		this.requests.schoolCalendarRequest()
				.getPeople(start, pageSize, weekDayStorage.getWeekDayBits())
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
				});
	}

	void onPersonChanged(EntityProxyChange<PersonProxy> event) {
		if (WriteOperation.PERSIST.equals(event.getWriteOperation())) {
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
		if (WriteOperation.UPDATE.equals(event.getWriteOperation())) {
			EntityProxyId<PersonProxy> personId = event.getProxyId();

			// Is the changing record onscreen?
			int displayOffset = offsetOf(personId);
			if (displayOffset != -1) {
				// Record is onscreen and may differ from our data
				requests.find(personId).fire(new Receiver<PersonProxy>() {
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
						}
					}
				});
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
		SummaryPresenter.this.display.getSelectionModel().setSelected(
				person, false);
	}

}