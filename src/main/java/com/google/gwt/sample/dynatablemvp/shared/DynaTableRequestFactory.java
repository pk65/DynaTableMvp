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

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.LoggingRequest;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.Service;
import com.google.gwt.sample.dynatablemvp.server.loc.AddressServiceLocator;
import com.google.gwt.sample.dynatablemvp.server.loc.PersonServiceLocator;
import com.google.gwt.sample.dynatablemvp.server.loc.ScheduleServiceLocator;
import com.google.gwt.sample.dynatablemvp.server.loc.SchoolCalendarServiceLocator;
import com.google.gwt.sample.dynatablemvp.server.svc.ScheduleService;
import com.google.gwt.sample.dynatablemvp.server.svc.AddressService;
import com.google.gwt.sample.dynatablemvp.server.svc.PersonService;
import com.google.gwt.sample.dynatablemvp.server.svc.SchoolCalendarService;

/**
 * Request factory for the DynaTable sample. Instantiate via
 * {@link com.google.gwt.core.client.GWT#create}.
 */
public interface DynaTableRequestFactory extends RequestFactory {  
	  /**
	   * Source of request objects for the Person class.
	   */
	  @Service(value=PersonService.class,locator=PersonServiceLocator.class)
	  interface PersonRequest extends RequestContext {
		  Request<Integer> persist(PersonProxy person);
		  Request<Integer> persist(PersonProxy person,AddressProxy address,ScheduleProxy schedule,PersonProxy mentor);
	  }

	  @Service(value=AddressService.class,locator=AddressServiceLocator.class)
	  interface AddressRequest extends RequestContext {
		  Request<Integer> persist(AddressProxy address);
	  }
	  
	  /**
	   * Source of request objects for Schedule entities.
	   */
	  @Service(value = ScheduleService.class, locator = ScheduleServiceLocator.class)
	  interface ScheduleRequest extends RequestContext {
		  Request<TimeSlotProxy> createTimeSlot(int zeroBasedDayOfWeek, int startMinutes, int endMinutes);
		  Request<Integer> persist(ScheduleProxy schedule);
	  }

	  
	  /**
	   * Source of request objects for the SchoolCalendarFilter.
	   */
	  @Service(value=SchoolCalendarService.class, locator=SchoolCalendarServiceLocator.class)
	  interface SchoolCalendarRequest extends RequestContext {

	    Request<List<PersonProxy>> getAllPeople();
	    Request<List<PersonProxy>> getPeople(List<PersonRelation> personRelations,Integer startIndex, Integer maxCount,Byte dayFilter);
	    
	    Request<PersonProxy> findPerson(Integer id,List<PersonRelation> personRelations);

	    Request<PersonProxy> getRandomPerson();
	  }

	  LoggingRequest loggingRequest();

	  PersonRequest personRequest();
	  AddressRequest addressRequest();
	  ScheduleRequest scheduleRequest();
	  SchoolCalendarRequest schoolCalendarRequest();

}
