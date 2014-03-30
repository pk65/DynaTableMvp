package com.google.gwt.sample.dynatablemvp.server.vendor;

import java.util.List;

import com.google.gwt.sample.dynatablemvp.server.domain.Person;
import com.google.gwt.sample.dynatablemvp.server.svc.LazyLoader;
import com.google.gwt.sample.dynatablemvp.shared.PersonRelation;

public class DummyLazyLoader implements LazyLoader {

	@Override
	public void activateRelations(List<PersonRelation> personRelations,
			Person person) {

	}

}
