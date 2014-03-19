package com.google.gwt.sample.dynatablemvp.client;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.sample.dynatablemvp.client.event.AddContactEvent;
import com.google.gwt.sample.dynatablemvp.client.event.AddContactEventHandler;
import com.google.gwt.sample.dynatablemvp.client.event.ContactUpdatedEvent;
import com.google.gwt.sample.dynatablemvp.client.event.ContactUpdatedEventHandler;
import com.google.gwt.sample.dynatablemvp.client.event.EditContactCancelledEvent;
import com.google.gwt.sample.dynatablemvp.client.event.EditContactCancelledEventHandler;
import com.google.gwt.sample.dynatablemvp.client.event.EditContactEvent;
import com.google.gwt.sample.dynatablemvp.client.event.EditContactEventHandler;
import com.google.gwt.sample.dynatablemvp.client.presenter.DynaTableMvpPresenter;
import com.google.gwt.sample.dynatablemvp.client.presenter.EditContactPresenter;
import com.google.gwt.sample.dynatablemvp.client.presenter.PersonEditorWorkflowPresenter;
import com.google.gwt.sample.dynatablemvp.client.presenter.Presenter;
import com.google.gwt.sample.dynatablemvp.client.view.DynaTableMvpView;
import com.google.gwt.sample.dynatablemvp.client.view.EditContactView;
import com.google.gwt.sample.dynatablemvp.client.view.PersonEditorWorkflow;
import com.google.gwt.sample.dynatablemvp.client.widgets.ErrorDialog;
import com.google.gwt.sample.dynatablemvp.shared.DynaTableRequestFactory;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryLogHandler;
import com.google.web.bindery.requestfactory.shared.LoggingRequest;

public class AppController implements Presenter, ValueChangeHandler<String> {
  private final HandlerManager eventBus;
  private final DynaTableRequestFactory requests; 
  private HasWidgets container;
private PersonEditorWorkflowPresenter personEditor;
  
  public AppController(DynaTableRequestFactory requests, HandlerManager eventBus) {
    this.eventBus = eventBus;
    this.requests = requests;
    bind();
  }
  
  private void bind() {
    History.addValueChangeHandler(this);

    eventBus.addHandler(AddContactEvent.TYPE,
        new AddContactEventHandler() {
          public void onAddContact(AddContactEvent event) {
            doAddNewContact();
          }
        });  

    eventBus.addHandler(EditContactEvent.TYPE,
        new EditContactEventHandler() {
          public void onEditContact(EditContactEvent event) {
            doEditContact(event.getId());
          }
        });  

    eventBus.addHandler(EditContactCancelledEvent.TYPE,
        new EditContactCancelledEventHandler() {
          public void onEditContactCancelled(EditContactCancelledEvent event) {
            doEditContactCancelled();
          }
        });  

    eventBus.addHandler(ContactUpdatedEvent.TYPE,
        new ContactUpdatedEventHandler() {
          public void onContactUpdated(ContactUpdatedEvent event) {
            doContactUpdated();
          }
        });  
  }
  
  private void doAddNewContact() {
    History.newItem("add");
  }
  
  private void doEditContact(Integer id) {
    History.newItem("edit", false);
    Presenter presenter = new EditContactPresenter(requests, eventBus, new EditContactView(), id);
    presenter.go(container);
  }
  
  private void doEditContactCancelled() {
    History.newItem("list");
  }
  
  private void doContactUpdated() {
    History.newItem("list");
  }
  
  @Override
  public void go(final HasWidgets container) {
    this.container = container;
    
    if ("".equals(History.getToken())) {
      History.newItem("list");
    }
    else {
      History.fireCurrentHistoryState();
    }
  }

  @Override
  public void onValueChange(ValueChangeEvent<String> event) {
    String token = event.getValue();
    
    if (token != null) {
    	DynaTableMvpPresenter presenter = null;

      if (token!=null && token.equalsIgnoreCase("list")) {
    	    // Add remote logging handler
    	    RequestFactoryLogHandler.LoggingRequestProvider provider = new RequestFactoryLogHandler.LoggingRequestProvider() {
    	      public LoggingRequest getLoggingRequest() {
    	        return requests.loggingRequest();
    	      }
    	    };
    	    final ErrorDialog errorDialog = new ErrorDialog();
			Logger.getLogger("").addHandler(errorDialog.getHandler());
    	    Logger.getLogger("").addHandler(
    	        new RequestFactoryLogHandler(provider, Level.WARNING,
    	            new ArrayList<String>()));
    	    container.add(errorDialog);
    	  
		presenter = new DynaTableMvpPresenter(requests, eventBus,new DynaTableMvpView(),new BrowserManager());
		personEditor=new PersonEditorWorkflowPresenter(requests, eventBus,new PersonEditorWorkflow(),presenter.getFavoritesManager());
      }
      
      if (presenter != null) {
        presenter.go(container);
        personEditor.go(container);
      }
    }
  } 
}
