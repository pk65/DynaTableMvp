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
package com.google.gwt.sample.dynatablemvp.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.sample.dynatablemvp.client.presenter.PersonEditorWorkflowPresenter;
import com.google.gwt.sample.dynatablemvp.client.presenter.PersonEditorPresenter.Display;
import com.google.gwt.sample.dynatablemvp.client.widgets.PersonEditor;
import com.google.gwt.sample.dynatablemvp.shared.PersonProxy;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;

/**
 * This class shows how the UI for editing a person is wired up to the
 * RequestryEditorDelegate. It is also responsible for showing and
 * dismissing the PersonEditor. The use of the FavoriteManager shows integration
 * between a remote service and a local service.
 */
public class PersonEditorWorkflow implements PersonEditorWorkflowPresenter.Display {
  interface Binder extends UiBinder<DialogBox, PersonEditorWorkflow> {
    Binder BINDER = GWT.create(Binder.class);
  }

  public interface Driver extends
      RequestFactoryEditorDriver<PersonProxy, Editor<PersonProxy>> {
  }

  @UiField
  HTMLPanel contents;

  @UiField
  DialogBox dialog;

  @UiField
  CheckBox favorite;

  @UiField
  Button save;
  
  @UiField
  Button cancel;

  @UiField(provided = false)
  PersonEditor personEditor;

	@UiConstructor
  public PersonEditorWorkflow() {
    Binder.BINDER.createAndBindUi(this);
  }

  @UiHandler("cancel")
  void onCancel(ClickEvent event) {
    dialog.setVisible(false);
  }

	@Override
	public HasValueChangeHandlers<Boolean> getFavorite() {
		return favorite;
	}


	@Override
	public HasClickHandlers getSave() {
		return save;
	}


	@Override
	public HasText getDialogText() {		
		return dialog;
	}

	@Override
	public void setDialogVisible(boolean visible) {
		if(visible)
			dialog.center();
		else
			dialog.hide();		
	}


	@Override
	public Widget getContentsWidget() {
		return contents;
	}

	@Override
	public HasClickHandlers getCancel() {
		return cancel;
	}

	@Override
	public Display getPersonEditor() {
		return personEditor;
	}

	@Override
	public void setFavorite(boolean value) {
		favorite.setValue(value);		
	}


}
