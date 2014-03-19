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
package com.google.gwt.sample.dynatablemvp.server.svc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gwt.sample.dynatablemvp.server.domain.Address;
import com.google.gwt.sample.dynatablemvp.server.domain.Person;
import com.google.gwt.sample.dynatablemvp.server.domain.Schedule;
import com.google.gwt.sample.dynatablemvp.server.domain.TimeSlot;
import com.google.gwt.sample.dynatablemvp.shared.PersonRelation;
import com.google.gwt.sample.dynatablemvp.shared.WeekDayStorage;
import com.google.gwt.sample.dynatablemvp.server.PersonFuzzer;

/**
 * The server side service class.
 */
@Service
public class SchoolCalendarService {
	
	private static final Logger log = LoggerFactory
			.getLogger(SchoolCalendarService.class);

	@Autowired
	private AddressService addressService;
	@Autowired
	private PersonService personService;
	@Autowired
	private ScheduleService scheduleService; 
	@Autowired
	private TimeSlotService timeSlotService;

	private boolean activated;
	
	private final HashMap<String, Person> people = new HashMap<String,Person>();
	
	public void loadDb() {
		log.debug("loadDb executed");
		if(activated){
			people.clear();
			for (Person generated : PersonFuzzer
					.generateRandomPeople()) {
				if (people.containsKey(generated.getName()))
					continue;
				createPerson(generated);
				people.put(generated.getName(), generated);
			}
			log.debug("loadDb created people");
		}
	}

	public HashMap<String, Person> getPeople() {
		return people;
	}

	public void createPerson(Person generated) {
		personService.persist(generated);
	}

	
	public Person findPerson(Integer id,List<PersonRelation> personRelations) {
		final Person person = personService.findPerson(id);
		if(personRelations!=null && personRelations.isEmpty()==false)
			loadLazyRelations(personRelations, person);	
		return person;
	}

	public void loadLazyRelations(List<PersonRelation> personRelations,
			final Person person) {
		for(PersonRelation relation : personRelations){
			switch(relation){
			case ADDRESS :
				final Address p_address = person.getAddress();
				if(p_address!=null && p_address.getId()!=null
						&& p_address.getClass().getName().equals(Address.class.getName())==false){
					Address address = addressService.find(p_address.getId());
					person.setAddress(address);
				}
				break;
			case MENTOR:
				final Person p_mentor = person.getMentor();
				if(p_mentor!=null && p_mentor.getId()!=null
						&& p_mentor.getClass().getName().equals(Person.class.getName())==false){
					Person mentor=personService.findPerson(p_mentor.getId());
					person.setMentor(mentor);
				}
				break;
			case SHEDULE:
				final Schedule classSchedule = person.getClassSchedule();
				if (classSchedule != null && classSchedule.getKey()!=null 
						&& classSchedule.getClass().getName() .equals(Schedule.class.getName())==false) {
					Schedule schedule = scheduleService.find(classSchedule.getKey());
/*					final List<TimeSlot> timeSlots = schedule.getTimeSlots();
					if(timeSlots==null)
						schedule.setTimeSlots(new ArrayList<TimeSlot>());*/
					person.setClassSchedule(schedule);
				}
				break;
			default:
				break;
			}
		}
	}

	public List<Person> getAllPeople() {
		return personService.fetchAllPersons();
	}
	
	public List<Person> getPeople(List<PersonRelation> personRelations,Integer startIndex, Integer maxCount, Byte filter) throws NoResultException {
		final List<Person> resultList;
        if(filter!=null && filter!=WeekDayStorage.ALL_DAYS){
			int index=0;
			final List<Person> allPersons = personService.fetchAllPersons() ;
        	resultList = new ArrayList<Person>();
        	for(Person person : allPersons){
        		loadLazyRelations(Arrays.asList(PersonRelation.SHEDULE), person);
        		final Schedule classSchedule = person.getClassSchedule();
        		if(classSchedule!=null){
        			final ArrayList<TimeSlot> resultTimes = new ArrayList<TimeSlot>();
					List<TimeSlot> timeSlots = classSchedule.getTimeSlots();
	        		WeekDayStorage weekDayStorage = new WeekDayStorage();
	        		for(TimeSlot timeSlot : timeSlots){
	        			weekDayStorage.setWeekDayBits((byte) 0);
	        			weekDayStorage.setWeekDayValue(timeSlot.getDayOfWeek(), true);
	        			if((weekDayStorage.getWeekDayBits() & filter.byteValue())!=0){
	        				resultTimes.add(timeSlot);
	        			}
	        		}
	        		if(resultTimes.isEmpty()==false){
	        			if((startIndex<0?true:index>=startIndex) && (maxCount>0?resultList.size()<maxCount:true)){
	        				classSchedule.setTimeSlots(resultTimes);
	        	        	loadLazyRelations(personRelations, person);
	        				resultList.add(person);
	        			}
						index++;
	        		}
        		}
        	}
        	return resultList;
        } else {
        	resultList = personService.fetchAllPersons(startIndex,maxCount) ;
			if(personRelations!=null && personRelations.isEmpty()==false)
				for(Person person : resultList){
					loadLazyRelations(personRelations, person);
				}
        }
		return resultList;
	}

	public Person getRandomPerson() {
		Long countAll = personService.countAll();
		if (countAll == null)
			return null;
		return getPeople(null,new Random().nextInt(countAll.intValue()),
				1, WeekDayStorage.ALL_DAYS).get(0);
	}

/*	public Integer persist(Person person){
		personService.persist(person);		
		return person.getId();
	}
*/
/*	public Address findPersonAddress(Person p) {
		final Integer addressId = p.getAddress().getId();
		return addressService.find(addressId);
	}
*/
/*	public Address findPersonAddress(Integer id) {
		Address address = addressService.find(id);
		log.debug("adress id="+id+"; city=\""+address.getCity()+"\"; street=\""+address.getStreet()+"\"; version=\""+address.getVersion()+"\"");
		return address;
	}
*/	
/*	public Schedule findPersonSchedule(Person p) {
		final Integer classScheduleId = p.getClassSchedule().getKey();
		return scheduleService.find(classScheduleId);
	}*/
	
	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}
}
