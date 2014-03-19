package com.google.gwt.sample.dynatablemvp.server.dao;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.stereotype.Repository;

import com.google.gwt.sample.dynatablemvp.server.domain.TimeSlot;

@Repository("timeSlotDao")
@Transactional(propagation = Propagation.REQUIRED)
public class TimeSlotDao  extends  DataAccessObject<Integer,TimeSlot> {
}