package com.google.gwt.sample.dynatablemvp.server.svc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gwt.sample.dynatablemvp.server.dao.PersonDao;
import com.google.gwt.sample.dynatablemvp.server.domain.Address;
import com.google.gwt.sample.dynatablemvp.server.domain.Person;
import com.google.gwt.sample.dynatablemvp.server.domain.Schedule;

@Service
public class PersonService {
	@Autowired
    private PersonDao personDao;
	
	@Autowired
	private AddressService addressService;
	
	@Autowired
	private ScheduleService scheduleService;
	
    protected PersonService() {
	}

    public Integer persist(Person person) {
    	if(!(person!=null  && person.getClass().getName().equals(Person.class.getName())))
    		return null;

    	final Address address =  person.getAddress();
    	 addressService.persist(address);
		final Schedule classSchedule = person.getClassSchedule();
		scheduleService.persist(classSchedule);
		final Person mentor = person.getMentor();
		if(mentor!=null  && mentor.getClass().getName().equals(Person.class.getName()))
			personDao.insert(mentor,mentor.getId()!=null);
    	personDao.insert(person,person.getId()!=null);
    	return person.getId();
    }

    public Integer persist(Person person,Address address,Schedule classSchedule,Person mentor) {
    	addressService.persist(address);
		scheduleService.persist(classSchedule);
		if(mentor!=null  && mentor.getClass().getName().equals(Person.class.getName()))
			personDao.insert(mentor,mentor.getId()!=null);
		if(person!=null  && person.getClass().getName().equals(Person.class.getName())){
			person.setAddress(address);
			person.setClassSchedule(classSchedule);
			person.setMentor(mentor);
			personDao.insert(person,person.getId()!=null);
		}
    	return person.getId();
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