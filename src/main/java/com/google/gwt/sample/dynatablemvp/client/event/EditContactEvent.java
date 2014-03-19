package com.google.gwt.sample.dynatablemvp.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class EditContactEvent extends GwtEvent<EditContactEventHandler>{
  public static Type<EditContactEventHandler> TYPE = new Type<EditContactEventHandler>();
  private final Integer id;
  
  public EditContactEvent(Integer id) {
    this.id = id;
  }
  
  public Integer getId() { return id; }
  
  @Override
  public Type<EditContactEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(EditContactEventHandler handler) {
    handler.onEditContact(this);
  }
}
