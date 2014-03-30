package com.google.gwt.sample.dynatablemvp.server.svc;

import java.util.List;

import com.google.gwt.sample.dynatablemvp.server.domain.Person;
import com.google.gwt.sample.dynatablemvp.shared.PersonRelation;

public interface LazyLoader {
	
	public void activateRelations(List<PersonRelation> personRelations,
			final Person person) ;	
}
