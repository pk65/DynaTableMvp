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
package com.google.gwt.sample.dynatablemvp.client.event;

//import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
//import com.google.gwt.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.gwt.sample.dynatablemvp.shared.PersonProxy;

/**
 * An event to indicate a change in the filter options.
 */
public class PersonProxyChangeEvent extends
		GwtEvent<PersonProxyChangeEvent.Handler> {
	/**
	 * Handles {@link PersonProxyChangeEvent}.
	 */
	public interface Handler extends EventHandler {
		void onPersonChanged(PersonProxyChangeEvent e);
	}

	public static final Type<Handler> TYPE = new Type<Handler>();

	public static HandlerRegistration register(EventBus eventBus,
			Handler handler) {
		return eventBus.addHandler(TYPE, handler);
	}

	private final int personId;
	private final boolean merged;
	private final EntityProxyId<PersonProxy> entityProxyId;
	
	public PersonProxyChangeEvent(int personId, boolean merged, EntityProxyId<PersonProxy> entityProxyId) {
		this.personId = personId;
		this.merged=merged;
		this.entityProxyId=entityProxyId;
	}

	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onPersonChanged(this);
	}

	public int getPersonId() {
		return personId;
	}

	public boolean isMerged() {
		return merged;
	}

	public EntityProxyId<PersonProxy> getEntityProxyId() {
		return entityProxyId;
	}
}
