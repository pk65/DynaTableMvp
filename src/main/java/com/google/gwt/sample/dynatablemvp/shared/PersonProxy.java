/*
 * Copyright 2007 Google Inc.
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

//import pegasus.bop.sprint.domain.Person;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.gwt.sample.dynatablemvp.server.domain.Person;
import com.google.gwt.sample.dynatablemvp.server.loc.PersonEntityLocator;

/**
 * Person DTO.
 */
@ProxyFor(value = Person.class, locator = PersonEntityLocator.class)
public interface PersonProxy extends EntityProxy {
	
	Integer getId();

	EntityProxyId<PersonProxy> stableId();

	String getFirstName();

	String getLastName();

	String getDisplayName();

	AddressProxy getAddress();

	ScheduleProxy getClassSchedule();

	String getDescription();

	PersonProxy getMentor();

	String getName();

	String getNote();


	Integer getVersion();

	Byte getDaysFilter();

	void setAddress(AddressProxy address);

	void setClassSchedule(ScheduleProxy schedule);

	void setDescription(String description);

	void setMentor(PersonProxy personProxy);

	void setName(String name);

	void setNote(String note);

	void setId(Integer id);

	void setFirstName(String firstName);

	void setLastName(String lastName);

	void setDisplayName(String displayName);

	void setDaysFilter(Byte daysFilter);

	void setVersion(Integer version);

}
