/*
 * Copyright 2011 Google Inc.
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
package com.google.gwt.sample.dynatablemvp.server;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

import com.google.gwt.sample.dynatablemvp.server.domain.Schedule;
import com.google.gwt.sample.dynatablemvp.server.domain.TimeSlot;

/**
 * Generates a list of random schedules. To facilitate the UI
 * TimeSlots are defined in whole hour blocks.
 */
public class ScheduleFuzzer {

  private static final int MAX_SCHED_ENTRIES = 5;

  private static final int MIN_SCHED_ENTRIES = 1;
  
  private static final int CLASS_LENGTH_MINS = 50;

  private static final int NUMBER_OF_SCHEDULES = PersonFuzzer.MAX_PEOPLE;

  public static Schedule generateRandomSchedule(Random rnd) {
    int range = MAX_SCHED_ENTRIES - MIN_SCHED_ENTRIES + 1;
    int howMany = MIN_SCHED_ENTRIES + rnd.nextInt(range);

    List<TimeSlot> timeSlots = generateTimeSlots(rnd, howMany);

    Schedule sched = new Schedule();
//    sched.setRevision(0);
    for(TimeSlot timeSlot : timeSlots)
    	timeSlot.setSchedule(sched);
    sched.setTimeSlots(timeSlots);
    return sched;
  }

  public static Schedule[] generateSchedules() {
    Random rnd = new Random();
    Schedule[] toReturn = new Schedule[NUMBER_OF_SCHEDULES];
    for (int i = 0; i < NUMBER_OF_SCHEDULES; i++) {
      Schedule sched = generateRandomSchedule(rnd);
      toReturn[i] = sched;
    }
    return toReturn;
  }

  private static List<TimeSlot> generateTimeSlots(Random rnd, int howMany) {
    TreeSet<TimeSlot> timeSlots = new TreeSet<TimeSlot>(new Comparator<TimeSlot>(){
		@Override
		public int compare(TimeSlot o1, TimeSlot o2) {
			if(o1.getDayOfWeek()>o2.getDayOfWeek())
				return 1;
			if(o1.getDayOfWeek()<o2.getDayOfWeek())
				return -1;
			if(o1.getStartMinutes()>o2.getStartMinutes())
				return 1;
			if(o1.getStartMinutes()<o2.getStartMinutes())
				return -1;
			if(o1.getEndMinutes()>o2.getEndMinutes())
				return 1;
			if(o1.getEndMinutes()<o2.getEndMinutes())
				return -1;
			return 0;
		}});

    for (int i = 0; i < howMany; ++i) {
      int startHrs = 8 + rnd.nextInt(9); // 8 am - 5 pm
      int dayOfWeek = 1 + rnd.nextInt(5); // Mon - Fri

      int absStartMins = 60 * startHrs; // convert to minutes
      int absStopMins = absStartMins + CLASS_LENGTH_MINS;

      TimeSlot timeSlot = new TimeSlot();
      timeSlot.setDayOfWeek(dayOfWeek);
      timeSlot.setStartMinutes(absStartMins);
      timeSlot.setEndMinutes(absStopMins);
//      timeSlot.setVersion(0);
      timeSlots.add(timeSlot);
    }
    
    return new ArrayList<TimeSlot>(timeSlots);
  }

}
