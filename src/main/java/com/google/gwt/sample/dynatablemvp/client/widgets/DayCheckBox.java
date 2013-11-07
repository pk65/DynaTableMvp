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

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.sample.dynatablemvp.client.presenter.DayCheckBoxPresenter;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;

/**
 * Used by DayFilterWidget.
 */
public class DayCheckBox extends Composite implements
		DayCheckBoxPresenter.Display {
	private final CheckBox cb = new CheckBox();
	private int day;

	public DayCheckBox() {
		initWidget(cb);
		cb.setValue(true);
	}

	@Override
	public int getDay() {
		return day;
	}

	public boolean getValue() {
		return cb.getValue();
	}

	public void setCaption(String caption) {
		cb.setText(caption);
	}

	public void setDay(int day) {
		this.day = day;
	}

	@Override
	public void setValue(boolean value) {
		cb.setValue(value);
	}

	@Override
	public HasValueChangeHandlers<Boolean> getCheckBox() {
		return cb;
	}
}