package com.google.gwt.sample.dynatablemvp.server.svc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gwt.sample.dynatablemvp.server.dao.ScheduleDao;
import com.google.gwt.sample.dynatablemvp.server.domain.Schedule;
import com.google.gwt.sample.dynatablemvp.server.domain.TimeSlot;

@Service
public class ScheduleService {
 
    public ScheduleService() {
	}

    @Autowired
	private ScheduleDao scheduleDao;

    @Autowired
	private TimeSlotService timeSlotService;
    
    public List<Schedule> fetchAllSchedulees() {
        return scheduleDao.selectAll();
    }
    
    public Schedule find(Integer id){
    	Schedule schedule = scheduleDao.findByPrimaryKey(id);
		return schedule;
    }
    
	public Integer persist(Schedule schedule) {
		if (schedule != null
				&& schedule.getClass().getName()
						.equals(Schedule.class.getName())) {

			final List<TimeSlot> timeSlots = schedule.getTimeSlots();
			if (timeSlots != null)
				for (TimeSlot timeSlot : timeSlots)
					timeSlotService.persist(timeSlot);
			scheduleDao.insert(schedule, schedule.getKey() != null,true);
			return schedule.getKey();
		}
		return null;
	}
    

	public TimeSlot createTimeSlot(int zeroBasedDayOfWeek, int startMinutes,
			int endMinutes) {
		final TimeSlot timeSlot = new TimeSlot();
		timeSlot.setDayOfWeek(zeroBasedDayOfWeek);
		timeSlot.setStartMinutes(startMinutes);
		timeSlot.setEndMinutes(endMinutes);
		return timeSlot;
	}
	
}