package com.google.gwt.sample.dynatablemvp.server.dao;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.google.gwt.sample.dynatablemvp.server.domain.Schedule;

@Repository("scheduleDao")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Transactional(propagation = Propagation.REQUIRED)
public class ScheduleDao extends  DataAccessObject<Integer,Schedule> {
}