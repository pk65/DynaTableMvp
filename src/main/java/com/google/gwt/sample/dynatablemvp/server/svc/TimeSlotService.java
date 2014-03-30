package com.google.gwt.sample.dynatablemvp.server.svc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gwt.sample.dynatablemvp.server.dao.TimeSlotDao;
import com.google.gwt.sample.dynatablemvp.server.domain.Schedule;
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
			timeSlotDao.insert(timeSlot, false,false);
		return id;
	}

	public List<TimeSlot> fetchAllTimeSlotes() {
		return timeSlotDao.selectAll();
	}

	public TimeSlot find(Integer id) {
		return timeSlotDao.findByPrimaryKey(id);
	}

	public List<TimeSlot> findByParent(Schedule parent) {
		return timeSlotDao.findByParent(parent,0,0);
	}

	public TimeSlot findChildOfParent(Schedule classSchedule,TimeSlot searchTimeSlot) {
		TimeSlot result=null;
		List<TimeSlot> slotList= findByParent(classSchedule);
		for(TimeSlot timeSlot : slotList){
			if(timeSlot.getDayOfWeek()==searchTimeSlot.getDayOfWeek()
					&& timeSlot.getStartMinutes()==searchTimeSlot.getStartMinutes()
					&& timeSlot.getEndMinutes()==searchTimeSlot.getEndMinutes()	)
				result=timeSlot;
		}
		return result;
	}
	
	public void deleteOrphans(){
		List<Integer> toDelete=timeSlotDao.findWithoutParent(0, 0);
		for(Integer primaryKey : toDelete)
			timeSlotDao.delete(primaryKey);
	}

}