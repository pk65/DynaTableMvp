package com.google.gwt.sample.dynatablemvp.server.dao;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.google.gwt.sample.dynatablemvp.server.domain.Schedule;
import com.google.gwt.sample.dynatablemvp.server.domain.TimeSlot;
import com.google.gwt.sample.dynatablemvp.server.domain.TimeSlot_;

@Repository("timeSlotDao")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Transactional(propagation = Propagation.REQUIRED)
public class TimeSlotDao  extends  DataAccessObject<Integer,TimeSlot> {

	public List<TimeSlot> findByParent(Schedule parent,int startPosition, int maxResult) {
        CriteriaBuilder cb =getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TimeSlot> cq= cb.createQuery(TimeSlot.class);
        Root<TimeSlot> entityRoot = cq.from(TimeSlot.class);
		final Path<Schedule> schedule = entityRoot.get(TimeSlot_.schedule);
		cq.where(cb.equal(schedule, parent));
        return getResultList(startPosition, maxResult, cq, entityRoot);
	}
	
	public List<Integer> findWithoutParent(int startPosition, int maxResult) {
        CriteriaBuilder cb =getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Integer> cq= cb.createQuery(Integer.class);
        Root<TimeSlot> entityRoot = cq.from(TimeSlot.class);
		final Path<Schedule> schedule = entityRoot.get(TimeSlot_.schedule);
        final Path<Integer> id = entityRoot.get(TimeSlot_.id);
		cq.select(id);
		cq.where(cb.isNull(schedule));
        TypedQuery<Integer> q = getEntityManager().createQuery(cq);
        if(startPosition>=0)
        	q.setFirstResult(startPosition);
        if(maxResult>0)
        	q.setMaxResults(maxResult);
        return q.getResultList();
	}
}