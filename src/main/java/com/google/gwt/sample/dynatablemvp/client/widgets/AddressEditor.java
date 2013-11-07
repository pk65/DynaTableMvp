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
import com.google.gwt.sample.dynatablemvp.client.presenter.AddressPresenter;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

;

/**
 * Allows an AddressProxy to be edited.
 */
public class AddressEditor extends Composite implements
		AddressPresenter.Display {
	interface Binder extends UiBinder<Widget, AddressEditor> {
	}

	@UiField
	TextBox street_value;
	@UiField
	TextBox city_value;
	@UiField
	TextBox state_value;
	@UiField
	ZipPlusFourBox zipbox;

	public AddressEditor() {
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));

	}

	@Override
	public TakesValue<String> getStreet() {
		return street_value;
	}

	@Override
	public TakesValue<String> getCity() {
		return city_value;
	}

	@Override
	public TakesValue<String> getState() {
		return state_value;
	}

	@Override
	public TakesValue<String> getZip() {
		return zipbox;
	}

	@Override
	public void cleanForm() {
		street_value.setText("");
		city_value.setText("");
		state_value.setText("");
		zipbox.setText("");		
	}
}
