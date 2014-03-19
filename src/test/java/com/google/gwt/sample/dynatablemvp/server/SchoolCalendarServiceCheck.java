package com.google.gwt.sample.dynatablemvp.server;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.persistence.NoResultException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gwt.sample.dynatablemvp.server.domain.Address;
import com.google.gwt.sample.dynatablemvp.server.domain.Person;
import com.google.gwt.sample.dynatablemvp.server.domain.Schedule;
import com.google.gwt.sample.dynatablemvp.server.domain.TimeSlot;
import com.google.gwt.sample.dynatablemvp.server.svc.PersonService;
import com.google.gwt.sample.dynatablemvp.server.svc.ScheduleService;
import com.google.gwt.sample.dynatablemvp.server.svc.SchoolCalendarService;
import com.google.gwt.sample.dynatablemvp.shared.PersonRelation;
import com.google.gwt.sample.dynatablemvp.shared.WeekDayStorage;

public class SchoolCalendarServiceCheck {
	private static final Logger log = LoggerFactory.getLogger(SchoolCalendarServiceCheck.class);

	@Autowired
	private SchoolCalendarService schoolCalendarService;

	@Before
	public void setUp() throws Exception {
		schoolCalendarService.setActivated(true);
		schoolCalendarService.loadDb();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testCreatePerson() {
		try {
			boolean foundAny = false;
			HashMap<String, Person> people = schoolCalendarService.getPeople();
			for (Person p : schoolCalendarService.getAllPeople()) {
				Person generated = people.get(p.getName());
				Assert.assertNotNull(generated);
				schoolCalendarService.loadLazyRelations(Arrays.asList(PersonRelation.ADDRESS
						,PersonRelation.SHEDULE), p);
				Assert.assertEquals(generated.getDescription(),	p.getDescription());
				final Address address =p.getAddress();
				Assert.assertEquals(generated.getAddress().getCity(), address.getCity());
				Assert.assertEquals(generated.getAddress().getStreet(), address.getStreet());
				Schedule schedule = p.getClassSchedule();
				Assert.assertNotNull(schedule);
				Assert.assertNotNull(schedule.getTimeSlots());
				Assert.assertEquals(generated.getClassSchedule().getTimeSlots().size(), schedule.getTimeSlots().size());				
				foundAny = true;
			}
			people.clear();
			Assert.assertTrue("No record was found!", foundAny);
		} catch(NoResultException e){
			log.error(e.getLocalizedMessage(), e);
			fail("there is no person in database!");
		} catch (org.springframework.beans.BeansException bex) {
			Assert.fail(bex.getLocalizedMessage());
			log.error(bex.getLocalizedMessage(), bex);
		}
		
	}

	@Test
	public void testNextPageAllDays() {
		final WeekDayStorage weekDayStorage = new WeekDayStorage(WeekDayStorage.ALL_DAYS);
		int start=15;
		int pageSize=15;
		final List<Person> people = schoolCalendarService.getPeople(Collections.singletonList(PersonRelation.SHEDULE)
				,start, pageSize, weekDayStorage.getWeekDayBits());
		Assert.assertNotNull(people);
		Assert.assertEquals("People are missing on next page.",pageSize,people.size());
	}

	@Test
	public void testNextPageFilteredDays() {
		final WeekDayStorage weekDayStorage = new WeekDayStorage(WeekDayStorage.ALL_DAYS);
		int start=15;
		int pageSize=15;
		weekDayStorage.setWeekDayValue(3, false);
		final List<Person> people = schoolCalendarService.getPeople(Collections.singletonList(PersonRelation.SHEDULE)
				,start, pageSize, weekDayStorage.getWeekDayBits());
		Assert.assertNotNull(people);
		Assert.assertEquals("People are missing on next page.",pageSize,people.size());
	}

	@Autowired
	private PersonService personService;
	
	@Autowired
	private ScheduleService scheduleService;
	
	@Test
	public void testUpdatePersonCalendar() {
		int start=0;
		int pageSize=15;
		final List<Person> people = schoolCalendarService.getPeople(Collections.singletonList(PersonRelation.SHEDULE)
				,start, pageSize, WeekDayStorage.ALL_DAYS);
		Assert.assertNotNull(people);
		Assert.assertEquals("People are missing on first page.",pageSize,people.size());
		Person randomPerson = people.get(new Random().nextInt(pageSize));
		final int timeSlotsAmount = randomPerson.getClassSchedule().getTimeSlots().size();
		final Schedule classSchedule = randomPerson.getClassSchedule();
		final List<TimeSlot> timeSlots = classSchedule.getTimeSlots();
		final TimeSlot newTimeSlot = scheduleService.createTimeSlot(6, 7*60, 9*60);
		timeSlots.add(newTimeSlot);
		personService.persist(randomPerson);
		final Person person = schoolCalendarService.findPerson(randomPerson.getId(), Collections.singletonList(PersonRelation.SHEDULE));
		Assert.assertEquals(timeSlotsAmount+1,person.getClassSchedule().getTimeSlots().size());
	}
	
}