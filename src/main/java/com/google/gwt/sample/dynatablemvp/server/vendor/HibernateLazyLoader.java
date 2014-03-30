package com.google.gwt.sample.dynatablemvp.server.vendor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gwt.sample.dynatablemvp.server.domain.Address;
import com.google.gwt.sample.dynatablemvp.server.domain.Person;
import com.google.gwt.sample.dynatablemvp.server.domain.Schedule;
import com.google.gwt.sample.dynatablemvp.server.domain.TimeSlot;
import com.google.gwt.sample.dynatablemvp.server.svc.AddressService;
import com.google.gwt.sample.dynatablemvp.server.svc.LazyLoader;
import com.google.gwt.sample.dynatablemvp.server.svc.PersonService;
import com.google.gwt.sample.dynatablemvp.server.svc.ScheduleService;
import com.google.gwt.sample.dynatablemvp.server.svc.TimeSlotService;
import com.google.gwt.sample.dynatablemvp.shared.PersonRelation;

@Component
public class HibernateLazyLoader implements LazyLoader {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory
			.getLogger(HibernateLazyLoader.class);


	@Autowired
	private AddressService addressService;
	@Autowired
	private PersonService personService;
	@Autowired
	private ScheduleService scheduleService; 
	@Autowired
	private TimeSlotService timeSlotService;
	
	public void activateRelations(List<PersonRelation> personRelations,
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
				if (classSchedule != null){
					if(classSchedule.getKey()!=null 
						&& classSchedule.getClass().getName() .equals(Schedule.class.getName())==false) {
						Schedule schedule = scheduleService.find(classSchedule.getKey());
						if(schedule.getKey()!=null){
							schedule.setPerson(person);
							activateTimeSlots(schedule);
						}
						person.setClassSchedule(schedule);
					} else
						activateTimeSlots(classSchedule);
				}
				break;
			default:
				break;
			}
		}
	}

	private void activateTimeSlots(final Schedule schedule) {
		final List<TimeSlot> newTimeSlots = new ArrayList<TimeSlot>();
		final List<TimeSlot> timeSlots = schedule.getTimeSlots();
		Iterator<TimeSlot> iter1 = null;
		boolean useFind=false;
		try {
			iter1 = timeSlots.iterator();
		} catch(RuntimeException e){
			useFind=true;
//			log.error(e.getLocalizedMessage(),e);
		}
		if(useFind){
			final List<TimeSlot> ts = timeSlotService.findByParent(schedule);
			iter1=ts.iterator();
		}
		while(iter1.hasNext()){
			final TimeSlot timeSlot = iter1.next();
			if(timeSlot.getId()!=null){
				if(timeSlot.getClass().getName().equals(TimeSlot.class.getName())==false){
					final TimeSlot newTimeSlot = timeSlotService.find(timeSlot.getId());
					newTimeSlot.setSchedule(schedule);
					newTimeSlots.add(newTimeSlot);
				} else
					timeSlot.setSchedule(schedule);
				if(useFind)
					newTimeSlots.add(timeSlot);
			}
		}
		if(newTimeSlots.isEmpty()==false)
			schedule.setTimeSlots(newTimeSlots);
	}
	
}
