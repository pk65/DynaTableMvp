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

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.editor.ui.client.ValueBoxEditorDecorator;
import com.google.gwt.sample.dynatablemvp.client.presenter.MentorSelectorPresenter;
import com.google.gwt.sample.dynatablemvp.client.presenter.PersonEditorPresenter;
import com.google.gwt.sample.dynatablemvp.client.presenter.ScheduleEditorPresenter;
import com.google.gwt.sample.dynatablemvp.client.presenter.AddressPresenter.Display;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Edits People.
 */
public class PersonEditor extends Composite implements PersonEditorPresenter.Display {
	interface Binder extends UiBinder<Widget, PersonEditor> {
	}

	@UiField
	AddressEditor address;
	@UiField
	ValueBoxEditorDecorator<String> description;

	@UiField(provided = false)
	MentorSelector mentor;

	@UiField
	ValueBoxEditorDecorator<String> name;

	@UiField
	ValueBoxEditorDecorator<String> note;

	@UiField
	TextBox nameBox;
	
	@UiField
	TextArea note_value;
	
	@UiField
	TextBox description_value;

	@UiField(provided = false)
	ScheduleEditor classSchedule;


	@UiConstructor
	public PersonEditor() {
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
	}

	@Override
	public void focus() {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				nameBox.setFocus(true);
			}
		});
	}

	@Override
	public Display getAddress() {
		return address;
	}

	@Override
	public TakesValue<String> getName() {
		return nameBox;
	}

	@Override
	public TakesValue<String> getNote() {
		return note_value;
	}

	@Override
	public TakesValue<String> getDescription() {
		return description_value;
	}

	@Override
	public MentorSelectorPresenter.Display getMentorSelector() {
		return mentor;
	}

	@Override
	public ScheduleEditorPresenter.Display getScheduleEditor() {
		return classSchedule;
	}

	@Override
	public void cleanForm() {
		address.cleanForm();
		nameBox.setText("");
		note_value.setText("");
		description_value.setText("");
		if(mentor!=null)
			mentor.setText("");
		if(classSchedule!=null)
			classSchedule.cleanForm();
	}


}
