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
package com.google.gwt.sample.dynatablemvp.server.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

/**
 * Hold relevant data for a time slot.
 */
@Entity
public class TimeSlot {

	private Integer id;
	private int endMinutes;

	private int startMinutes;

	private int zeroBasedDayOfWeek;

//	@NotNull
//	@DecimalMin("0")
	private Integer versionT;
	private Schedule schedule;

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public TimeSlot() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@NotNull
	@DecimalMin("0")
	@DecimalMax("6")
	public int getDayOfWeek() {
		return zeroBasedDayOfWeek;
	}

	public void setDayOfWeek(int zeroBasedDayOfWeek) {
		this.zeroBasedDayOfWeek = zeroBasedDayOfWeek;
	}

	 @ManyToOne
	 @JoinColumn
	 public Schedule getSchedule(){
		return this.schedule;
	}
	
	public int getEndMinutes() {
		return endMinutes;
	}

	public int getStartMinutes() {
		return startMinutes;
	}

	public void setEndMinutes(int endMinutes) {
		this.endMinutes = endMinutes;
	}

	public void setStartMinutes(int startMinutes) {
		this.startMinutes = startMinutes;
	}

	@Version
	public Integer getVersionT() {
		return versionT;
	}

	public void setVersionT(Integer version) {
		this.versionT = version;
	}
}
