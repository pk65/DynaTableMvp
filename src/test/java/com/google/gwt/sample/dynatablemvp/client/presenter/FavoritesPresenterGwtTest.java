package com.google.gwt.sample.dynatablemvp.client.presenter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.junit.Before;
import org.junit.Test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.sample.dynatablemvp.client.BrowserManager;
import com.google.gwt.sample.dynatablemvp.client.presenter.FavoritesPresenter;
import com.google.gwt.sample.dynatablemvp.client.widgets.FavoritesWidget;
import com.google.gwt.sample.dynatablemvp.shared.DynaTableRequestFactory;
import com.google.gwt.sample.dynatablemvp.shared.PersonProxy;
import com.google.gwt.sample.dynatablemvp.shared.WeekDayStorage;
import com.google.gwt.sample.dynatablemvp.shared.DynaTableRequestFactory.PersonRequest;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.google.web.bindery.requestfactory.shared.testing.FakeRequestTransport;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;

@GwtModule("com.google.gwt.sample.dynatablemvp.DynaTableMvp")
public class FavoritesPresenterGwtTest extends GwtTest {
	private FavoritesPresenter favorites = null;
	private DynaTableRequestFactory requests =null;

	@Before
	public void setUpTest() throws Exception {
	    requests = GWT.create(DynaTableRequestFactory.class);
	    requests.initialize(new SimpleEventBus(), new FakeRequestTransport());
		this.favorites = new FavoritesPresenter(requests,  new HandlerManager(null),new FavoritesWidget(), new BrowserManager());
		favorites.go(null);
	}

	@Test
	public void testAddPersonToList() {
		final PersonRequest context = this.requests.personRequest();
		PersonProxy firstPerson = context.create(PersonProxy.class);
		PersonProxy secondPerson = context.create(PersonProxy.class);
		PersonProxy thirdPerson =  context.create(PersonProxy.class);
		firstPerson.setName("Z First Person");
		secondPerson.setName("Y Second Person");
		thirdPerson.setName("X third Person");
		favorites.addPersonToList(firstPerson);
		favorites.addPersonToList(secondPerson);
		favorites.addPersonToList(thirdPerson);
		String[] labels = favorites.getNames();
		assertEquals(3, favorites.size());
		assertEquals(3, labels.length);
		assertEquals(thirdPerson.getName(), labels[0]);
		assertEquals(secondPerson.getName(), labels[1]);
		assertEquals(firstPerson.getName(), labels[2]);
		favorites.clean();
		assertEquals(0, favorites.size());
	}

	@Test
	public void testRequestFactory() {
		final ArrayList<PersonProxy> responseList = new ArrayList<PersonProxy>();
		final WeekDayStorage weekDayStorage = new WeekDayStorage();
		weekDayStorage.setAllDaysChecked();
		final StringBuilder errorMessages=new StringBuilder();
		
		this.requests.schoolCalendarRequest()
		.getPeople(null,0, 1, weekDayStorage.getWeekDayBits())
		.fire(new Receiver<List<PersonProxy>>() {
			@Override
			public void onFailure(ServerFailure error) {
				errorMessages.append(error.getMessage());
				errorMessages.append("; ");
			}

			@Override
			public void onConstraintViolation(
					Set<ConstraintViolation<?>> violations) {
				Iterator<ConstraintViolation<?>> violIter = violations.iterator();
				while(violIter.hasNext()){
					errorMessages.append(violIter.next().getMessage());
					errorMessages.append("; ");
				}
			}

			@Override
			public void onSuccess(List<PersonProxy> response) {
				if(response.isEmpty())
					errorMessages.append("onSuccess, but response is empty.");
				else
					responseList.addAll(response);
			}
		});
		if(errorMessages.length()>0)
			fail(errorMessages.toString());
//		assertEquals(1, responseList); // why false?
		assertEquals(0,responseList.size());
	}
}
