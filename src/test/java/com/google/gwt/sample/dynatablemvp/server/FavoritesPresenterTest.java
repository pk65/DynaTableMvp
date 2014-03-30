package com.google.gwt.sample.dynatablemvp.server;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.gwt.sample.dynatablemvp.client.FavoritesManager;
import com.google.gwt.sample.dynatablemvp.client.presenter.FavoritesPresenter;
import com.google.gwt.sample.dynatablemvp.client.presenter.FavoritesPresenter.Display;
import com.google.gwt.sample.dynatablemvp.client.presenter.FavoritesPresenter.NameElement;
import com.google.gwt.sample.dynatablemvp.shared.AddressProxy;
import com.google.gwt.sample.dynatablemvp.shared.DynaTableRequestFactory;
import com.google.gwt.sample.dynatablemvp.shared.PersonProxy;
import com.google.gwt.sample.dynatablemvp.shared.ScheduleProxy;

public class FavoritesPresenterTest {
	private FavoritesPresenter favorites = null;

	@Before
	public void setUp() throws Exception {
		DynaTableRequestFactory rpcService=Mockito.mock(DynaTableRequestFactory.class);
		this.favorites = new FavoritesPresenter(rpcService,  new HandlerManager(null),new SimpleDisplay(), new BrowserManager());
		favorites.go(null);
	}


	@Test
	public void testAddPersonToList() {
		PersonProxy firstPerson = new PersonBody("Z First Person",null);
		PersonProxy secondPerson =  new PersonBody("K Second Person",null);
		PersonProxy thirdPerson = new PersonBody("A third Person",null);
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

	private class PersonBody implements PersonProxy {
		private  String name;
		private  Integer id;
		private Byte daysFilter;
		private Integer version;
		
		private final PersonId personid;
		
		public PersonBody(String name,Integer id){
			this.name=name;
			this.id=id;
			this.personid=new PersonId(this);
		}
		
		@Override
		public Integer getId() {
			return id;
		}

		@Override
		public AddressProxy getAddress() {
			return null;
		}

		@Override
		public ScheduleProxy getClassSchedule() {
			return null;
		}

		@Override
		public String getDescription() {
			return null;
		}

		@Override
		public PersonProxy getMentor() {
			return null;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String getNote() {
			return null;
		}

		@Override
		public void setAddress(AddressProxy address) {
		}

		@Override
		public void setClassSchedule(ScheduleProxy schedule) {
		}

		@Override
		public void setDescription(String description) {
		}

		@Override
		public void setNote(String note) {
		}

		@Override
		public EntityProxyId<PersonProxy> stableId() {
			return personid;
		}

		@Override
		public String getFirstName() {
			return null;
		}

		@Override
		public String getLastName() {
			return null;
		}

		@Override
		public String getDisplayName() {
			return null;
		}

		@Override
		public void setFirstName(String firstName) {
		}

		@Override
		public void setLastName(String lastName) {
		}

		@Override
		public void setDisplayName(String displayName) {
		}

		@Override
		public Integer getVersionP() {
			return this.version;
		}

		@Override
		public Byte getDaysFilter() {
			return this.daysFilter;
		}


		@Override
		public void setDaysFilter(Byte daysFilter) {
			this.daysFilter=daysFilter;
		}

		@Override
		public void setVersionP(Integer version) {
			this.version=version;
		}

		@Override
		public void setName(String name) {
			this.name=name;
		}

		@Override
		public void setId(Integer id) {
			this.id=id;
		}

		@Override
		public void setMentor(PersonProxy mentor) {
			
		}
	}
	
	private NameElement getNewElement() {

		return new NameElement() {
			private String label = null;
			private int index;
			
			@Override
			public String getText() {
				return label;
			}

			@Override
			public void setText(String text) {
				label = text;

			}

			@Override
			public Widget asWidget() {
				return null;
			}

			@Override
			public HandlerRegistration addClickHandler(ClickHandler handler) {
				return new HandlerRegistration(){
					@Override
					public void removeHandler() {
					}};
			}

			@Override
			public void fireEvent(GwtEvent<?> event) {
			}

			@Override
			public int getIndex() {
				return index;
			}

			@Override
			public void setIndex(int index) {
				this.index=index;
			}

		};
	}

	private class NamesPanel extends ArrayList<NameElement> {
		private static final long serialVersionUID = -9192368462554214196L;

	}

	private class PersonId implements EntityProxyId<PersonProxy> {
		private final PersonProxy person;

		public PersonId(PersonProxy person) {
			this.person = person;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Class<PersonProxy> getProxyClass() {
			return (Class<PersonProxy>) person.getClass();
		}
	}

	private class BrowserManager implements FavoritesManager.CookieStorage {
		private final HashMap<String, String> cookies = new HashMap<String, String>();

		@Override
		public String getCookie(String name) {
			return cookies.get(name);
		}

		@Override
		public void setCookie(String name, String value) {
			cookies.put(name, value);
		}

		@Override
		public void addWindowClosingHandler(ClosingHandler handler) {
		}

	}
	
	private class SimpleDisplay implements Display {
		private final NamesPanel container = new NamesPanel();

		@Override
		public Widget asWidget() {
			return null;
		}

		@Override
		public String getStyle() {
			return "any_style";
		}

		@Override
		public List<NameElement> getContainer() {
			return container;
		}

		@Override
		public NameElement getNewElement() {
			return FavoritesPresenterTest.this.getNewElement();
		}
		
	}
}
