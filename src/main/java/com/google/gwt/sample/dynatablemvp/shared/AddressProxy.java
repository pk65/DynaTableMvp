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
package com.google.gwt.sample.dynatablemvp.shared;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.gwt.sample.dynatablemvp.server.domain.Address;
import com.google.gwt.sample.dynatablemvp.server.loc.AddressEntityLocator;

/**
 * Represents an Address in the client code.
 */
@ProxyFor(value = Address.class, locator = AddressEntityLocator.class)
public interface AddressProxy extends EntityProxy {
	Integer getId();

	String getCity();

	String getState();

	String getStreet();

	String getZip();

	String getEmail();

	Integer getVersion();

	void setId(Integer id);

	void setCity(String city);

	void setState(String state);

	void setStreet(String street);

	void setZip(String zip);

	void setEmail(String email);

	void setVersion(Integer version);
}
