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
package com.google.gwt.sample.dynatablemvp.server.loc;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;
import com.google.web.bindery.requestfactory.shared.Locator;
import com.google.gwt.sample.dynatablemvp.server.dao.DataAccessObject;

/**
 * This class serves as an example of implementing a Locator to allow
 * RequestFactory to work with entities that don't conform to its expectations
 * of static find*() methods, and getId() and getVersion() methods. In a
 * production application such a Locator might be the bridge to your dependency
 * injection framework, or a data access object.
 * <p>
 * There is a reference to this class in a {@literal @}Service annotation in
 * {@link pegasus.bop.sprint.shared.DynaTableRequestFactory}
 */
public class ProxyObjectLocator<E, K, D> extends Locator<E, K> {
	private static final Logger log = LoggerFactory.getLogger(ProxyObjectLocator.class);

	private final D objectDao;
	private final Class<K> keyTypeClass;

	@SuppressWarnings("unchecked")
	public ProxyObjectLocator() {
		final Type genericSuperclass2 = getClass().getGenericSuperclass();
		if(genericSuperclass2 instanceof ParameterizedType){
			ParameterizedType genericSuperclass = (ParameterizedType) genericSuperclass2;
			final Type entityType = genericSuperclass.getActualTypeArguments()[1];
			final Type daoType = genericSuperclass.getActualTypeArguments()[2];
			HttpServletRequest request = RequestFactoryServlet.getThreadLocalRequest();
			ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
			objectDao = (D) getTarget(context.getBean((Class<D>) daoType));
			this.keyTypeClass = (Class<K>) entityType;
		} else {
			this.keyTypeClass =null;
			objectDao=null;
		}
	}

	@Override
	public boolean isLive(E domainObject) {
		return getId(domainObject)!=null && getVersion(domainObject)!=null;
	}


	@SuppressWarnings("unchecked")
	@Override
	public E create(Class<? extends E> clazz) {
		return ((DataAccessObject<K,E>)objectDao).create();
	}

	@SuppressWarnings("unchecked")
	@Override
	public E find(Class<? extends E> clazz, K id) {
		final E primaryKey = ((DataAccessObject<K,E>)objectDao).findByPrimaryKey(id);
		return primaryKey;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<E> getDomainType() {
		return ((DataAccessObject<K,E>)objectDao).getEntityClass();
	}

	@Override
	public K getId(E domainObject) {
		@SuppressWarnings("unchecked")
		K result = (K) getFieldValueWithAnnotation(domainObject,"javax.persistence.Id");
		return result;
	}

	@Override
	public Class<K> getIdType() {
		return keyTypeClass;
	}

	@Override
	public Integer getVersion(E domainObject) {
		return (Integer)getFieldValueWithAnnotation(domainObject,"javax.persistence.Version");
	}

	@SuppressWarnings("unchecked")
	public K persist(E domainObject) {
		((DataAccessObject<K,E>)objectDao).insert(domainObject,getId(domainObject)!=null);
		return getId(domainObject);
	}

	private Object getFieldValueWithAnnotation(E domainObject, String annotation) {
		Object result = null;
		Method[] meths = domainObject.getClass().getMethods();
		Method idMethod = null;
		for (Method m : meths) {
			for (Annotation a : m.getAnnotations()) {
				String annotName = a.annotationType().getName();
				if (annotName.equalsIgnoreCase(annotation)) {
					idMethod = m;
					break;
				}
			}
			if (idMethod != null) {
				try {
					result = idMethod.invoke(domainObject);
				} catch (IllegalAccessException e) {
					log.error(e.getLocalizedMessage(),e);
					result = null;
				} catch (IllegalArgumentException e) {
					log.error(e.getLocalizedMessage(),e);
					result = null;
				} catch (InvocationTargetException e) {
					log.error(e.getLocalizedMessage(),e);
					result = null;
				}
				break;
			}
		}
		return result;
	}

	private Object getTarget(Object advisedObject) {
		Object target=advisedObject;
		if(AopUtils.isAopProxy(advisedObject) && advisedObject instanceof Advised) {
			try {
			    target = ((Advised)advisedObject).getTargetSource().getTarget();
			} catch (Exception e) {
				target =null;
			}
		}
		return target;
	}

}
