package com.google.gwt.sample.dynatablemvp.client.presenter;

//import pegasus.bop.sprint.client.event.ContactUpdatedEvent;
//import pegasus.bop.sprint.client.event.EditContactCancelledEvent;
//import pegasus.bop.sprint.shared.DynaTableRequestFactory;
//import pegasus.bop.sprint.shared.PersonProxy;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.sample.dynatablemvp.client.event.ContactUpdatedEvent;
import com.google.gwt.sample.dynatablemvp.client.event.EditContactCancelledEvent;
import com.google.gwt.sample.dynatablemvp.shared.DynaTableRequestFactory;
import com.google.gwt.sample.dynatablemvp.shared.PersonProxy;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class EditContactPresenter implements Presenter {
	public interface Display {
		HasClickHandlers getSaveButton();

		HasClickHandlers getCancelButton();

		HasValue<String> getFirstName();

		HasValue<String> getLastName();

		HasValue<String> getEmailAddress();

		Widget asWidget();
	}

	private PersonProxy contact;
	private final DynaTableRequestFactory requests;
	private final HandlerManager eventBus;
	private final Display display;

	public EditContactPresenter(DynaTableRequestFactory rpcService,
			HandlerManager eventBus, Display display) {
		this.requests = rpcService;
		this.eventBus = eventBus;
		this.contact = null;
		this.display = display;
		bind();
	}

	public EditContactPresenter(DynaTableRequestFactory rpcService,
			HandlerManager eventBus, Display display, String id) {
		this.requests = rpcService;
		this.eventBus = eventBus;
		this.display = display;
		bind();

//		Request<PersonProxy> person = rpcService.schoolCalendarRequest().findPerson(id);
		
		rpcService.schoolCalendarRequest().findPerson(id)
				.fire(new Receiver<PersonProxy>() {
					@Override
					public void onFailure(ServerFailure error) {
						// TODO Auto-generated method stub
						super.onFailure(error);
						Window.alert("Error retrieving contact");
					}

					@Override
					public void onSuccess(PersonProxy result) {
						contact = result;
						EditContactPresenter.this.display.getFirstName()
								.setValue(contact.getFirstName());
						EditContactPresenter.this.display.getLastName()
								.setValue(contact.getLastName());
						EditContactPresenter.this.display.getEmailAddress()
								.setValue(contact.getAddress().getEmail());
					}
				});

	}

	public void bind() {
		this.display.getSaveButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				doSave();
			}
		});

		this.display.getCancelButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new EditContactCancelledEvent());
			}
		});
	}

	public void go(final HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}

	private void doSave() {
		if (contact == null) {
			Window.alert("contact is null!");
			return;
		}
		contact.setFirstName(display.getFirstName().getValue());
		contact.setLastName(display.getLastName().getValue());
		contact.getAddress().setEmail(display.getEmailAddress().getValue());
		requests.personRequest().persist().using(contact).fire(new Receiver<Void>() {

			@Override
			public void onFailure(ServerFailure error) {
				// TODO Auto-generated method stub
				super.onFailure(error);
				Window.alert("Error updating contact");
			}

			@Override
			public void onSuccess(Void response) {
				eventBus.fireEvent(new ContactUpdatedEvent(contact));
			}
		});
	}

}
