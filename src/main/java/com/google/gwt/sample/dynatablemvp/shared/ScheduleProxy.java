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
package com.google.gwt.sample.dynatablemvp.shared;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.gwt.sample.dynatablemvp.server.domain.Schedule;
import com.google.gwt.sample.dynatablemvp.server.loc.ScheduleEntityLocator;

import java.util.List;

/**
 * Schedule DTO.
 */
@ProxyFor(value = Schedule.class, locator = ScheduleEntityLocator.class)
public interface ScheduleProxy extends EntityProxy {

	Integer getKey();

	void setKey(Integer key);

	Integer getRevision();

	void setRevision(Integer revision);

	List<TimeSlotProxy> getTimeSlots();

	void setTimeSlots(List<TimeSlotProxy> slots);

	Byte getDaysFilter() ;
	
//	String getDescription();

}
