package com.google.gwt.sample.dynatablemvp.server.svc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gwt.sample.dynatablemvp.server.dao.TimeSlotDao;
import com.google.gwt.sample.dynatablemvp.server.domain.TimeSlot;

@Service
public class TimeSlotService {

	public TimeSlotService() {
	}

	@Autowired
	private TimeSlotDao timeSlotDao;

	public Integer persist(TimeSlot timeSlot) {
		final Integer id = timeSlot.getId();
		if(id==null)
			timeSlotDao.insert(timeSlot, false);
		return id;
	}

	public List<TimeSlot> fetchAllTimeSlotes() {
		return timeSlotDao.selectAll();
	}

	public TimeSlot find(int id) {
		return timeSlotDao.findByPrimaryKey(id);
	}

}