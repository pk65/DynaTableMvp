/*
 * Copyright 2010 Google Inc.
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

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.sample.dynatablemvp.client.presenter.SummaryPresenter;
import com.google.gwt.sample.dynatablemvp.shared.PersonProxy;
import com.google.gwt.sample.dynatablemvp.shared.ScheduleProxy;
import com.google.gwt.sample.dynatablemvp.shared.TimeSlotProxy;
import com.google.gwt.sample.dynatablemvp.shared.TimeTools;
import com.google.gwt.sample.dynatablemvp.shared.WeekDayStorage;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

/**
 * A paging table with summaries of all known people.
 */
public class SummaryWidget extends Composite implements
		SummaryPresenter.Display {

	interface Binder extends UiBinder<Widget, SummaryWidget> {
	}

	interface Style extends CssResource {
	}

	interface TableResources extends DataGrid.Resources {
		@Override
		@Source(value = { DataGrid.Style.DEFAULT_CSS, "DataGridPatch.css" })
		DataGrid.Style dataGridStyle();
	}

	private static final Logger logger = Logger.getLogger(SummaryWidget.class.getName());
	
	private class DescriptionColumn extends Column<PersonProxy, String> {
		public DescriptionColumn() {
			super(new TextCell());
		}

		@Override
		public String getValue(PersonProxy object) {
			if(object==null)
				return "";
			return object.getDescription();
		}
	}

	private class NameColumn extends Column<PersonProxy, String> {
		public NameColumn() {
			super(new TextCell());
		}

		@Override
		public String getValue(PersonProxy object) {
			if(object==null)
				return "";
			return object.getName();
		}
	}

	private class ScheduleColumn extends Column<PersonProxy, String> {
		public ScheduleColumn() {
			super(new TextCell());
		}

		@Override
		public String getValue(PersonProxy person) {
			if(person==null)
				return "";
			
			final ScheduleProxy schedule = person.getClassSchedule();
			final String scheduleDescription;
			if(schedule!=null){
				scheduleDescription = getScheduleDescription(schedule);
			} else
				scheduleDescription="";
			return scheduleDescription;
		}
	}
	
	private String getScheduleDescription(final ScheduleProxy schedule) {
		List<TimeSlotProxy> timeSlots = schedule.getTimeSlots();
		Byte daysFilter = schedule.getDaysFilter();
		byte dFilter=(daysFilter!=null?daysFilter.byteValue():WeekDayStorage.ALL_DAYS);
		final WeekDayStorage filter=new WeekDayStorage(dFilter);
		ArrayList<TimeSlotProxy> sortedSlots = new ArrayList<TimeSlotProxy>(timeSlots);
		Collections.sort(sortedSlots,new Comparator<TimeSlotProxy>(){
			@Override
			public int compare(TimeSlotProxy o1, TimeSlotProxy o2) {
				if(o1.getDayOfWeek()>o2.getDayOfWeek())
					return 1;
				if(o1.getDayOfWeek()<o2.getDayOfWeek())
					return -1;
				if(o1.getStartMinutes()>o2.getStartMinutes())
					return 1;
				if(o1.getStartMinutes()<o2.getStartMinutes())
					return -1;
				if(o1.getEndMinutes()>o2.getEndMinutes())
					return 1;
				if(o1.getEndMinutes()<o2.getEndMinutes())
					return -1;
				return 0;
			}});
		String s=null;
		for (TimeSlotProxy timeSlot : sortedSlots) {
			if (filter.isWeekDayChecked(timeSlot.getDayOfWeek())) {
				final TimeTools timeTools = new TimeTools(timeSlot.getDayOfWeek(),timeSlot.getStartMinutes(),timeSlot.getEndMinutes());
		        if (s == null) {
		          s = timeTools.getDescription();
		        } else {
		          s += ", " + timeTools.getDescription();
		        }
			}
		}
		if(s==null)
			s="";
		return s;
	}

	@UiField
	Button create;

	@UiField
	DockLayoutPanel dock;

	@UiField(provided=true)
	SimplePager pager;

	@UiField(provided=true)
	DataGrid<PersonProxy> table;

	private final SingleSelectionModel<PersonProxy> selectionModel = new SingleSelectionModel<PersonProxy>();

	private Integer selectedItem=-1;
	private int previousSelected=-1;

	@UiConstructor
	public SummaryWidget() {
		pager=new SimplePager(SimplePager.TextLocation.CENTER);
		table = new DataGrid<PersonProxy>(0,
				GWT.<TableResources> create(TableResources.class));
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));

		Column<PersonProxy, String> nameColumn = new NameColumn();
		table.addColumn(nameColumn, "Name");
		table.setColumnWidth(nameColumn, "25ex");
		Column<PersonProxy, String> descriptionColumn = new DescriptionColumn();
		table.addColumn(descriptionColumn, "Description");
		table.setColumnWidth(descriptionColumn, "40ex");
		table.addColumn(new ScheduleColumn(), "Schedule");
		table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		
		table.setRowStyles(new RowStyles<PersonProxy>() {
			@Override
			public String getStyleNames(PersonProxy row, int rowIndex) {
				if(selectedItem!=null){
					if(row.getId()==selectedItem){
						previousSelected=rowIndex;
						selectedItem=-1;
						return "selectedLine";
					}
				}
				return null;
			}

			});
	}

	@Override
	public void redrawLastSelectedLine() {
		if(previousSelected>=0){
			int idx=previousSelected;
			previousSelected=-1;
			Range range = table.getVisibleRange();
			if(idx>=range.getStart() && idx<range.getStart()+range.getLength())
				table.redrawRow(idx);
		}
	}
		
	@Override
	public HasData<PersonProxy> getTable() {
		return table;
	}

	@Override
	public SelectionModel<PersonProxy> getSelectionModel() {
		return selectionModel;
	}

	@Override
	public HasClickHandlers getCreateButton() {
		return create;
	}

	@Override
	public void setPageStart(int start) {
		pager.setPageStart(start);
	}

	@Override
	public int getPageStart() {
		return table.getPageStart();
	}

	@Override
	public List<PersonProxy> getVisibleItems() {
		return table.getVisibleItems();
	}

	@Override
	public PersonProxy getSelectedObject() {
		return selectionModel.getSelectedObject();
	}

	public int getRowCount() {
		return table.getRowCount();
	}

	public void setPageSize(int rows){
		pager.setPageSize(rows);		
		logger.fine(" setPageSize("+rows+")");
	}
	
	@Override
	public int getPageSize() {
		return pager.getPageSize();
	}

	@Override
	public void setSelectedItem(Integer selectedItem) {
		this.selectedItem=selectedItem;
	}

}
