/*
 * Copyright 2011 Google Inc.
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
package com.google.gwt.sample.dynatablemvp.server.loc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.sample.dynatablemvp.server.dao.AddressDao;
import com.google.gwt.sample.dynatablemvp.server.domain.Address;

/**
 * This class serves as an example of implementing a Locator to allow
 * RequestFactory to work with entities that don't conform to its expectations of
 * static find*() methods, and getId() and getVersion() methods. In a production
 * application such a Locator might be the bridge to your dependency injection
 * framework, or a data access object.
 * <p>
 * There is a reference to this class in a {@literal @}Service annotation in
 * {@link pegasus.bop.sprint.shared.DynaTableRequestFactory}
 */
public class AddressEntityLocator extends ProxyObjectLocator<Address, Integer,AddressDao> {
	private static final Logger log = LoggerFactory
			.getLogger(AddressEntityLocator.class);

	public AddressEntityLocator() {
		super();
		log.debug("++++ inside constructor ++++");
	}

	@Override
	public Address create(Class<? extends Address> clazz) {
		final Address address = super.create(clazz);
		return address;
	}

	@Override
	public Address find(Class<? extends Address> clazz, Integer id) {
		final Address address = super.find(clazz, id);
		return address;
	}

	@Override
	public Integer getId(Address domainObject) {
		final Integer id = super.getId(domainObject);
		return id;
	}
 
}
