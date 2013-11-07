package com.google.gwt.sample.dynatablemvp.client.presenter;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.sample.dynatablemvp.shared.DynaTableRequestFactory;
import com.google.gwt.sample.dynatablemvp.shared.ScheduleProxy;
import com.google.gwt.sample.dynatablemvp.shared.TimeSlotProxy;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class ScheduleEditorPresenter implements Presenter,
		TakesValue<ScheduleProxy> {
	public interface Display {
		TimeSlotListPresenter.Display getTimeSlotList();
		void cleanForm();
		Widget asWidget();
	}

	@SuppressWarnings("unused")
	private final DynaTableRequestFactory requestFactory;
	@SuppressWarnings("unused")
	private final HandlerManager eventBus;
	private final Display display;

	private ScheduleProxy schedule;
	private final TimeSlotListPresenter timeSlotList;

	public ScheduleEditorPresenter(DynaTableRequestFactory requestFactory,
			HandlerManager eventBus, Display view) {
		this.requestFactory = requestFactory;
		this.eventBus = eventBus;
		this.display = view;
		timeSlotList = new TimeSlotListPresenter(requestFactory, eventBus,
				this.display.getTimeSlotList());
	}

	@Override
	public void go(HasWidgets container) {
		timeSlotList.go(container);
	}

	@Override
	public void setValue(ScheduleProxy value) {
		this.schedule = value;
		List<TimeSlotProxy> scheduleTimeSlots =null;
		if(schedule!=null)
			scheduleTimeSlots = this.schedule.getTimeSlots();
		if(scheduleTimeSlots==null)
			scheduleTimeSlots=new ArrayList<TimeSlotProxy>();
		timeSlotList.setValue(scheduleTimeSlots);		
	}

	@Override
	public ScheduleProxy getValue() {
		return schedule;
	}
}
