package com.google.gwt.sample.dynatablemvp.server.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

@Transactional(propagation = Propagation.REQUIRED)
public class DataAccessObject<K,E> {
	private static final Logger log = LoggerFactory
			.getLogger(DataAccessObject.class);


	@PersistenceContext
	private EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}


    private final Class<E> entityClass;

	@SuppressWarnings("unchecked")
	public DataAccessObject() {
		final Type genericSuperclass2 = getClass().getGenericSuperclass();
		if(genericSuperclass2 instanceof ParameterizedType){
			ParameterizedType genericSuperclass = (ParameterizedType) genericSuperclass2;
			final Type entityType = genericSuperclass.getActualTypeArguments()[1];
			this.entityClass = (Class<E>) entityType;
		} else
			this.entityClass =null;
	}

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Class<E> getEntityClass(){
    	return entityClass;
    }
    
    public E create(){
    	E newInstance=null ;
    	try {
			newInstance = entityClass.newInstance();
		} catch (InstantiationException e) {
			log.error(e.getLocalizedMessage(),e);
		} catch (IllegalAccessException e) {
			log.error(e.getLocalizedMessage(),e);
		}
		return newInstance;
    }
    
    public void insert(E entityObject,boolean merge) {
        try {
        	if(merge)
        		entityManager.merge(entityObject);
        	else
        		entityManager.persist(entityObject);
        	entityManager.flush();
        } catch(Exception e){
        	log.error(e.getLocalizedMessage(),e);
        }
    }
 
    public List<E> selectAll() {
        return selectAll(0,0);
    }

    public List<E> selectAll(int startPosition, int maxResult) {
        CriteriaBuilder cb =entityManager.getCriteriaBuilder();
        CriteriaQuery<E> cq= cb.createQuery(entityClass);
        Root<E> entityRoot = cq.from(entityClass);
        cq.select(entityRoot);
        TypedQuery<E> q = entityManager.createQuery(cq);
        if(startPosition>=0)
        	q.setFirstResult(startPosition);
        if(maxResult>0)
        	q.setMaxResults(maxResult);
        return q.getResultList();
    }
    
    public E findByPrimaryKey(K primaryKey){
        return entityManager.find(entityClass, primaryKey);
    }
    
    public Long countAll(){
    	CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    	CriteriaQuery<Long> cq = cb.createQuery(Long.class);
    	cq.select(cb.count(cq.from(entityClass)));

    	return entityManager.createQuery(cq).getSingleResult();
    }
    
}