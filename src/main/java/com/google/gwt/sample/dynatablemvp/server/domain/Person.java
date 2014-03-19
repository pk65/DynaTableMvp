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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Version;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Hold relevant data for Person.
 */
@Entity
public class Person {

	@NotNull
	private Address address;

	@NotNull
	private Schedule classSchedule;

	@NotNull
	private String description;

	private Person mentor;

	@NotNull
	@Size(min = 2, message = "Persons aren't just characters")
	private String name;

	// May be null if the person is newly-created
	private Integer id;

	@NotNull
	@DecimalMin("0")
	private Integer version = 0;

	private String note;

	private Byte daysFilter;

	private String firstName;
	private String lastName;
	private String displayName;

	/**
	 * The RequestFactory requires a Long id property for each proxied type.
	 * <p>
	 * The requirement for some kind of id object with proper hash / equals
	 * semantics is not going away, but it should become possible to use types
	 * other than Long, and properties other than "id".
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Person() {
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true)
	public Address getAddress() {
		return address;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true)
	public Schedule getClassSchedule() {
		return classSchedule;
	}

	public String getDescription() {
		return description;
	}

	@OneToOne(targetEntity = Person.class)
	public Person getMentor() {
		return mentor;
	}

	public void setMentor(Person mentor) {
		this.mentor = mentor;
	}

	public String getName() {
		return name;
	}

	public String getNote() {
		return note;
	}

	/**
	 * The RequestFactory requires an Integer version property for each proxied
	 * type, but makes no good use of it. This requirement will be removed soon.
	 */
	@Version
	public Integer getVersion() {
		return version;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public void setClassSchedule(Schedule schedule) {
		this.classSchedule = schedule;
	}

	public Byte getDaysFilter() {
		return daysFilter;
	}

	public void setDaysFilter(Byte daysFilter) {
		this.daysFilter = daysFilter;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}
