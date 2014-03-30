package com.google.gwt.sample.dynatablemvp.server.svc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gwt.sample.dynatablemvp.server.dao.PersonDao;
import com.google.gwt.sample.dynatablemvp.server.domain.Address;
import com.google.gwt.sample.dynatablemvp.server.domain.Person;
import com.google.gwt.sample.dynatablemvp.server.domain.Schedule;
import com.google.gwt.sample.dynatablemvp.server.domain.TimeSlot;

@Service
public class PersonService {
	@Autowired
    private PersonDao personDao;
	
	@Autowired
	private AddressService addressService;
	
	@Autowired
	private ScheduleService scheduleService;
	
	@Autowired
	private TimeSlotService timeSlotService;
	
    protected PersonService() {
	}

	public Integer persist(Person person) {
		organizeParents(person);
		final Schedule classSchedule = person.getClassSchedule();
		boolean deletedAny = deleteTimeSlots(classSchedule);
		personDao.insert(person, person.getId() != null,deletedAny);
		timeSlotService.deleteOrphans();
		return person.getId();
	}

	public Integer persist(Person person, Address address,
			Schedule classSchedule, Person mentor) {
		person.setAddress(address);
		person.setClassSchedule(classSchedule);
		person.setMentor(mentor);
		return persist(person);
	}

	private boolean deleteTimeSlots(Schedule classSchedule) {
		if(classSchedule.getKey()==null)
			return false;
		List<TimeSlot> newTimeSlots=classSchedule.getTimeSlots();
		if(newTimeSlots==null)
			newTimeSlots=new ArrayList<TimeSlot>(); 
		final Comparator<TimeSlot> cmp = new Comparator<TimeSlot>(){
			@Override
			public int compare(TimeSlot o1, TimeSlot o2) {
				if(o1.getId()==null || o2.getId()==null)
					return -1;
				return o1.getId().compareTo(o2.getId());
			}};
		List<TimeSlot> timeSlots = timeSlotService.findByParent(classSchedule);
		if(timeSlots.size()==newTimeSlots.size())
			return false;
		List<TimeSlot> resultList = new ArrayList<TimeSlot>(newTimeSlots);
		Collections.sort(newTimeSlots, cmp);
		boolean deletedAny=false;
		for(TimeSlot timeSlot : timeSlots){
			int idx = Collections.binarySearch(newTimeSlots, timeSlot, cmp);
			if(idx<0){
				deletedAny=true;
				timeSlot.setSchedule(null);
				resultList.add(timeSlot);
			}
		}
		if(deletedAny)
			classSchedule.setTimeSlots(resultList);
		return deletedAny;
	}

	private void organizeParents(Person person) {
		Address address=person.getAddress();
		Schedule classSchedule=person.getClassSchedule();
		final List<TimeSlot> timeSlots = classSchedule.getTimeSlots();
		for(TimeSlot timeSlot : timeSlots){
			if(timeSlot.getId()==null)
				timeSlot.setSchedule(classSchedule);
		}
		if(address.getId()==null)
			address.setPerson(person);
		if(classSchedule.getKey()==null)
			classSchedule.setPerson(person);
	}
    
    public List<Person> fetchAllPersons() {
        List<Person> selectAll = personDao.selectAll();
		return selectAll;
    }

    public Person findPerson(Integer id) {
		return  personDao.findByPrimaryKey(id);
	}

	public Long countAll() {
		return personDao.countAll();
	}

	public List<Person> fetchAllPersons(Integer startIndex, Integer maxCount) {
		return personDao.selectAll(startIndex,maxCount);
	}
}