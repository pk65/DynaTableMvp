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

import com.google.gwt.core.client.GWT;
import com.google.gwt.sample.dynatablemvp.client.presenter.ScheduleEditorPresenter;
import com.google.gwt.sample.dynatablemvp.client.presenter.TimeSlotListPresenter;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * Edits a persons's schedule.
 */
public class ScheduleEditor extends Composite implements ScheduleEditorPresenter.Display {

	interface ScheduleEditorUiBinder extends UiBinder<Widget, ScheduleEditor> {
	}

	private static ScheduleEditorUiBinder uiBinder = GWT
			.create(ScheduleEditorUiBinder.class);

	@UiField(provided = false)
	TimeSlotListWidget timeSlots;

	public ScheduleEditor(/* TimeSlotListWidget timeSlotEditor */) {
		// timeSlots = timeSlotEditor;
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public TimeSlotListPresenter.Display getTimeSlotList() {
		return timeSlots;
	}

	@Override
	public void cleanForm() {
		if(timeSlots!=null)
			timeSlots.cleanForm();
	}

}