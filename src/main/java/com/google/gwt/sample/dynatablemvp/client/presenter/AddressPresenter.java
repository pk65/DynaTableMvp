package com.google.gwt.sample.dynatablemvp.client.presenter;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.sample.dynatablemvp.shared.AddressProxy;
import com.google.gwt.sample.dynatablemvp.shared.DynaTableRequestFactory;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class AddressPresenter implements Presenter , TakesValue<AddressProxy> {
	public interface Display {
		TakesValue<java.lang.String> getStreet();
		TakesValue<java.lang.String> getCity();
		TakesValue<java.lang.String> getState(); 
		TakesValue<java.lang.String> getZip();
		void cleanForm();
		Widget asWidget();
	}

	@Override
	public void go(HasWidgets container) {
	}

	@SuppressWarnings("unused")
	private final DynaTableRequestFactory requests;
	@SuppressWarnings("unused")
	private final HandlerManager eventBus;
	private final Display display;
	private AddressProxy address;
	
	public AddressPresenter(DynaTableRequestFactory requests,
			HandlerManager eventBus, Display display) {
		this.requests = requests;
		this.eventBus = eventBus;
		this.display = display;
	}

	@Override
	public void setValue(AddressProxy value) {
		this.address=value;
		display.getStreet().setValue(address.getStreet());
		display.getCity().setValue(address.getCity());
		display.getState().setValue(address.getState());
		display.getZip().setValue(address.getZip());
	}

	@Override
	public AddressProxy getValue() {
		address.setStreet(display.getStreet().getValue());
		address.setCity(display.getCity().getValue());
		address.setState(display.getState().getValue());
		address.setZip(display.getZip().getValue());
		return this.address;
	}
}
