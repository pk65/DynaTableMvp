/*
 * Copyright 2011 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.sample.dynatablemvp.client.widgets;

import java.util.List;

import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.sample.dynatablemvp.client.presenter.TimeSlotListPresenter;
import com.google.gwt.sample.dynatablemvp.client.presenter.TimeSlotListPresenter.ScheduleRow;
import com.google.gwt.sample.dynatablemvp.client.presenter.TimeSlotListPresenter.WeekDay;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * Edits a list of time slots.
 */
public class TimeSlotListWidget extends Composite implements
		TimeSlotListPresenter.Display {

	interface TableResources extends CellTable.Resources {
		@Override
		@Source(value = { CellTable.Style.DEFAULT_CSS, "CellTablePatch.css" })
		CellTable.Style cellTableStyle();
	}

	interface TimeSlotListWidgetUiBinder extends
			UiBinder<Widget, TimeSlotListWidget> {
	}

	private class WeekDayColumn extends
			Column<TimeSlotListPresenter.ScheduleRow, String> {
		private WeekDay day;

		public WeekDayColumn(WeekDay day) {
			super(new ClickableTextCell());
			this.day = day;
		}

		@Override
		public String getValue(TimeSlotListPresenter.ScheduleRow row) {
			if (day == null) {
				int hour = row.getHour();
				return Integer.toString(hour <= 12 ? hour : hour - 12) + ":00"
						+ ((hour < 12) ? "AM" : "PM");
			}
			return row.isInUse(day) ? "X" : ".";
		}
	}

	@UiField(provided = true)
	CellTable<TimeSlotListPresenter.ScheduleRow> table;

	private boolean acceptClicks = true;

	@UiConstructor
	public TimeSlotListWidget() {
		table = new CellTable<TimeSlotListPresenter.ScheduleRow>(
				TimeSlotListPresenter.ROWS_IN_A_DAY,
				GWT.<TableResources> create(TableResources.class));
		table.addColumn(new WeekDayColumn(null), "Hour");
		final WeekDay[] dayValues = WeekDay.values();
		for (WeekDay day : dayValues) {
			WeekDayColumn col = new WeekDayColumn(day);

			class Updater implements
					FieldUpdater<TimeSlotListPresenter.ScheduleRow, String> {
				private WeekDay columnDay;

				public Updater(WeekDay day) {
					columnDay = day;
				}

				@Override
				public void update(int index,
						TimeSlotListPresenter.ScheduleRow row, String value) {
					if (acceptClicks) {
						row.toggleInUse(columnDay);
					}
				}
			}

			FieldUpdater<TimeSlotListPresenter.ScheduleRow, String> fieldUpdater = new Updater(
					day);
			col.setFieldUpdater(fieldUpdater);
			table.addColumn(col, day.getShortName());
		}

		table.setRowCount(TimeSlotListPresenter.ROWS_IN_A_DAY, false);
		table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		initWidget(GWT.<TimeSlotListWidgetUiBinder> create(
				TimeSlotListWidgetUiBinder.class).createAndBindUi(this));
	}

	@Override
	public void setRowData(List<ScheduleRow> rows) {
		table.setRowData(rows);
	}

	@Override
	public void redrawTable() {
		table.redraw();
	}

	@Override
	public void setAcceptClicks(boolean acceptClicks) {
		this.acceptClicks = acceptClicks;
	}

	@Override
	public void cleanForm() {
		for(TimeSlotListPresenter.ScheduleRow row : table.getVisibleItems()){
			for(WeekDay d : WeekDay.values()){
				if(row.isInUse(d))
					row.toggleInUse(d);
			}
		}
	}

}
