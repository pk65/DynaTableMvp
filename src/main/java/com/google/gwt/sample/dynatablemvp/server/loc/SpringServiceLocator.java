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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;
import com.google.web.bindery.requestfactory.shared.ServiceLocator;

/**
 * This class provides an example of implementing a ServiceLocator to allow
 * RequestFactory to work with instances of service objects, instead of its default
 * behavior of mapping service calls to static methods.
 * <p>
 * There is a reference to this class in an {@literal @}Service annotation in
 * {@link com.nsn.sprint.shared.DynaTableRequestFactory}
 */
class SpringServiceLocator<E> implements ServiceLocator {

	
	final private Class<E> entityClass;
	
	@SuppressWarnings("unchecked")
	public SpringServiceLocator(){
		Type genSup = getClass().getGenericSuperclass();
		if(genSup instanceof ParameterizedType){
			ParameterizedType genericSuperclass = (ParameterizedType) getClass()
					.getGenericSuperclass();
			this.entityClass = (Class<E>) genericSuperclass
					.getActualTypeArguments()[0];
		} else
			this.entityClass =null;	
		
		
	}
	
	public Class<E> getEntityClass(){
		return entityClass;
	}
	
	@Override
	public Object getInstance(Class<?> clazz) {
		final Object bean;
		if (clazz.equals(entityClass)) {
			HttpServletRequest request = RequestFactoryServlet
					.getThreadLocalRequest();
			ApplicationContext context = WebApplicationContextUtils
					.getWebApplicationContext(request.getSession()
							.getServletContext());
			bean = context.getBean(entityClass);
		} else
			bean = null;
		return bean;
	}

}
