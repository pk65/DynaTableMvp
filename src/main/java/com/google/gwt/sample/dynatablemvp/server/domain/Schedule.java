/*
 * Copyright 2008 Google Inc.
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
package com.google.gwt.sample.dynatablemvp.server.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Version;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

/**
 * Holds the relevant data for a Schedule entity. This entity does not follow
 * the usual pattern of providing getId(), getVersion() and findSchedule()
 * methods for RequestFactory's use.
 * {@link ScheduleEntityLocator.bop.sprint.server.ScheduleLocator} handles those
 * responsibilities instead.
 */
@Entity
public class Schedule {

	private List<TimeSlot> timeSlots;

	private Integer key;

	@NotNull
	@DecimalMin("0")
	private Integer revision = 0;

	private Byte daysFilter;

	public Schedule() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	@Version
	public Integer getRevision() {
		return revision;
	}

	public void setRevision(Integer revision) {
		this.revision = revision;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinColumn
	public List<TimeSlot> getTimeSlots() {
		return timeSlots;
	}

	public void setTimeSlots(List<TimeSlot> timeSlots) {
		this.timeSlots = timeSlots;
	}

	public Byte getDaysFilter() {
		return daysFilter;
	}

	public void setDaysFilter(Byte daysFilter) {
		this.daysFilter = daysFilter;
	}

}
