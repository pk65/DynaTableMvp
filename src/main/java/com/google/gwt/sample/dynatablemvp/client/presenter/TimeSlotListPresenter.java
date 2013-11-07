package com.google.gwt.sample.dynatablemvp.client.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

//import com.google.gwt.sample.dynatablemvp.client.widgets.TimeSlotKey;
//import com.google.gwt.sample.dynatablemvp.client.widgets.TimeSlotListWidget.WeekDay;
//import com.google.gwt.sample.dynatablemvp.client.widgets.ScheduleRequest;
//import com.google.gwt.sample.dynatablemvp.client.widgets.TimeSlotListWidget;
//import com.google.gwt.sample.dynatablemvp.client.widgets.TimeSlotListWidget.ScheduleRow;
//import com.google.gwt.sample.dynatablemvp.client.widgets.TimeSlotListWidget.TimeSlotKey;
//import com.google.gwt.sample.dynatablemvp.client.widgets.TimeSlotListWidget.WeekDay;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.sample.dynatablemvp.shared.DynaTableRequestFactory;
import com.google.gwt.sample.dynatablemvp.shared.TimeSlotProxy;
import com.google.gwt.sample.dynatablemvp.shared.DynaTableRequestFactory.ScheduleRequest;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.shared.Receiver;

public class TimeSlotListPresenter implements Presenter {
	public interface Display {
		void setRowData(List<ScheduleRow> rows);

		void redrawTable();

		void setAcceptClicks(boolean acceptClick);
		void cleanForm();

		Widget asWidget();
	}

	public class ScheduleRow {
		int hour;

		public ScheduleRow(int hour) {
			this.hour = hour;
		}

		public int getHour() {
			return hour;
		}

		public boolean isInUse(WeekDay day) {
			return currentSchedule.contains(new TimeSlotKey(day, hour));
		}

		public void toggleInUse(WeekDay day) { 
			final TimeSlotKey key = new TimeSlotKey(day, hour);
			if (currentSchedule.contains(key)) {
				currentSchedule.remove(key);
				view.redrawTable();
			} else if (!existingSlots.containsKey(key)) {
				view.setAcceptClicks(false);
				ScheduleRequest context = requestFactory.scheduleRequest();
				context.createTimeSlot(day.ordinal(), hour * 60, hour * 60 + 50)
						.fire(new Receiver<TimeSlotProxy>() {
							@Override
							public void onSuccess(TimeSlotProxy slot) {
								existingSlots.put(key, slot);
								backing.add(slot);
								currentSchedule.add(key);
								view.redrawTable();
								view.setAcceptClicks(true);
							}
						});
			} else {
				currentSchedule.add(key);
				view.redrawTable();
			}
		}
	}

	private static class TimeSlotKey {
		private int hour;
		private WeekDay day;

		TimeSlotKey(WeekDay day, int hour) {
			this.day = day;
			this.hour = hour;
		}

		TimeSlotKey(TimeSlotProxy slot) {
			day = WeekDay.fromInt(slot.getDayOfWeek());
			hour = slot.getStartMinutes() / 60;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			TimeSlotKey other = (TimeSlotKey) obj;
			if (day == null) {
				if (other.day != null) {
					return false;
				}
			} else if (!day.equals(other.day)) {
				return false;
			}
			if (hour != other.hour) {
				return false;
			}
			return true;
		}

		@Override
		public int hashCode() {
			return 31 * (31 + ((day == null) ? 0 : day.hashCode())) + hour;
		}
	}

	public enum WeekDay {
		SUNDAY("Su"), MONDAY("Mo"), TUESDAY("Tu"), WEDNESDAY("We"), THURSDAY(
				"Th"), FRIDAY("Fr"), SATURDAY("Sa");

		public static WeekDay fromInt(int ordinal) {
			return values()[ordinal];
		}

		private String shortName;

		WeekDay(String shortName) {
			this.shortName = shortName;
		}

		public String getShortName() {
			return shortName;
		}
	}

	// private PersonProxy person;
	private final DynaTableRequestFactory requestFactory;
	private final Display view;
	@SuppressWarnings("unused")
	private final HandlerManager eventBus;

	public TimeSlotListPresenter(DynaTableRequestFactory requestFactory,
			HandlerManager eventBus, Display view) {
		this.requestFactory = requestFactory;
		this.view = view;
		this.eventBus = eventBus;
	}

	private List<TimeSlotProxy> backing;

	private HashSet<TimeSlotKey> currentSchedule;
	private HashMap<TimeSlotKey, TimeSlotProxy> existingSlots;
	private HashSet<TimeSlotKey> initialSchedule;

	public void flush() {
		HashMap<TimeSlotProxy, TimeSlotKey> index = new HashMap<TimeSlotProxy, TimeSlotKey>();

		for (TimeSlotProxy slot : backing) {
			index.put(slot, new TimeSlotKey(slot));
		}

		// Compute slots that need to be removed from the backing
		initialSchedule.removeAll(currentSchedule);

		for (Iterator<TimeSlotProxy> iterator = backing.iterator(); iterator
				.hasNext();) {
			TimeSlotProxy slot = iterator.next();
			TimeSlotKey key = index.get(slot);
			if (initialSchedule.contains(key)) {
				iterator.remove();
			}
		}
	}

	@Override
	public void go(HasWidgets container) {
		// TODO Auto-generated method stub

	}

	public static final int ROWS_IN_A_DAY = 9;
	private static final int FIRST_HOUR = 8;

	public void setValue(List<TimeSlotProxy> value) {
		backing = value;
		currentSchedule = new HashSet<TimeSlotKey>();
		existingSlots = new HashMap<TimeSlotKey, TimeSlotProxy>();

		initialSchedule = new HashSet<TimeSlotKey>();

		for (TimeSlotProxy slot : backing) {
			TimeSlotKey key = new TimeSlotKey(slot);
			currentSchedule.add(key);
			existingSlots.put(key, slot);
			initialSchedule.add(new TimeSlotKey(slot));
		}
		ArrayList<TimeSlotListPresenter.ScheduleRow> rows = new ArrayList<TimeSlotListPresenter.ScheduleRow>(
				ROWS_IN_A_DAY);
		for (int i = 0; i < ROWS_IN_A_DAY; i++) {
			rows.add(new TimeSlotListPresenter.ScheduleRow(FIRST_HOUR + i));
		}
		view.setRowData(rows);
	}

}
