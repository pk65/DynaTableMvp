package com.google.gwt.sample.dynatablemvp.client.event;

//import pegasus.bop.sprint.shared.PersonProxy;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.sample.dynatablemvp.shared.PersonProxy;

public class ContactUpdatedEvent extends GwtEvent<ContactUpdatedEventHandler>{
  public static Type<ContactUpdatedEventHandler> TYPE = new Type<ContactUpdatedEventHandler>();
  private final PersonProxy updatedContact;
  
  public ContactUpdatedEvent(PersonProxy contact) {
    this.updatedContact = contact;
  }
  
  public PersonProxy getUpdatedContact() { return updatedContact; }
  

  @Override
  public Type<ContactUpdatedEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(ContactUpdatedEventHandler handler) {
    handler.onContactUpdated(this);
  }
}
