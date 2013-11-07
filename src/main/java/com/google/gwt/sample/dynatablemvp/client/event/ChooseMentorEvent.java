package com.google.gwt.sample.dynatablemvp.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.sample.dynatablemvp.shared.PersonProxy;

public class ChooseMentorEvent extends GwtEvent<ChooseMentorEvent.Handler> {
	public static Type<Handler> TYPE = new Type<Handler>();
	private final String nameLabel;
	private final PersonProxy person;
	
	public interface Handler extends EventHandler {
		void onChooseMentor(ChooseMentorEvent event);
	};
	
	public ChooseMentorEvent(String nameLabel) {
		super();
		this.nameLabel = nameLabel;
		this.person = null;
	}

	public ChooseMentorEvent(PersonProxy person) {
		super();
		this.person = person;
		this.nameLabel = person.getName();
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;	
	}
	
	@Override
	protected void dispatch(Handler handler) {
		handler.onChooseMentor(this);		
	}

	public String getNameLabel() {
		return nameLabel;
	}

	public PersonProxy getPerson() {
		return person;
	}

}
