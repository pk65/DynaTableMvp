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
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.sample.dynatablemvp.client.presenter.MentorSelectorPresenter;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * This demonstrates how an editor can be constructed to handle optional fields.
 * The Person domain object's mentor property is initially <code>null</code>.
 * This type delegates editing control to an instance of the
 * {@link OptionalValueEditor} adapter class.
 */
public class MentorSelector extends Composite implements MentorSelectorPresenter.Display, HasText,HasVisibility {

  interface Binder extends UiBinder<Widget, MentorSelector> {
  }

  @UiField
  Button choose;

  @UiField
  Button clear;

  @UiField
  Label nameLabel;


  @UiConstructor
  public MentorSelector() {
    initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
  }

  @Override
  public void setEnabled(boolean enabled) {
    choose.setEnabled(enabled);
    clear.setEnabled(enabled);
  }

  @Override
  protected void onUnload() {
  }

	@Override
	public HasClickHandlers getChoose() {
		return choose;
	}

	@Override
	public String getText() {
		return nameLabel.getText();
	}

	@Override
	public void setText(String text) {
		nameLabel.setText(text);		
	}

	@Override
	public HasText getLabel() {
		return nameLabel;
	}

	@Override
	public HasClickHandlers getEditor() {
		return nameLabel;
	}

	@Override
	public HasVisibility getView() {
		return nameLabel;
	}

	@Override
	public HasClickHandlers getClear() {
		return clear;
	}


}
