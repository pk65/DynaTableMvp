package com.google.gwt.sample.dynatablemvp.client.presenter;

import java.util.ArrayList;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.sample.dynatablemvp.client.event.ChooseMentorEvent;
import com.google.gwt.sample.dynatablemvp.shared.AddressProxy;
import com.google.gwt.sample.dynatablemvp.shared.DynaTableRequestFactory;
import com.google.gwt.sample.dynatablemvp.shared.DynaTableRequestFactory.PersonRequest;
import com.google.gwt.sample.dynatablemvp.shared.PersonProxy;
import com.google.gwt.sample.dynatablemvp.shared.ScheduleProxy;
import com.google.gwt.sample.dynatablemvp.shared.TimeSlotProxy;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class PersonEditorPresenter implements Presenter , TakesValue<PersonProxy> {

	public interface Display {
		AddressPresenter.Display getAddress();
		MentorSelectorPresenter.Display getMentorSelector();
		ScheduleEditorPresenter.Display getScheduleEditor();
		TakesValue<String> getName();
		TakesValue<String> getNote();
		TakesValue<String> getDescription();
		void cleanForm();
		void focus();
		Widget asWidget();
	}

	private final DynaTableRequestFactory requestFactory;
	private final HandlerManager eventBus;
	private final Display display;
	private final AddressPresenter addressPresenter;
	private final ScheduleEditorPresenter scheduleEditor;
	private final MentorSelectorPresenter mentorSelector;

	private PersonProxy person;
	private PersonRequest personContext;
	
	public PersonEditorPresenter(DynaTableRequestFactory requestFactory,
			HandlerManager eventBus, Display view) {
		this.requestFactory = requestFactory;
		this.eventBus = eventBus;
		this.display = view;
		addressPresenter=new AddressPresenter(this.requestFactory,this.eventBus,display.getAddress());
		scheduleEditor = new ScheduleEditorPresenter(requestFactory, eventBus,display.getScheduleEditor());
		mentorSelector = new MentorSelectorPresenter(requestFactory, eventBus,display.getMentorSelector());
	}

	@Override
	public void go(HasWidgets container) {
		addressPresenter.go(container);
		mentorSelector.go(container);
		scheduleEditor.go(container);
		eventBus.addHandler(ChooseMentorEvent.TYPE,
				new ChooseMentorEvent.Handler() {
					@Override
					public void onChooseMentor(ChooseMentorEvent event) {
						PersonProxy mentor = personContext.edit(event.getPerson());
						mentorSelector.setValue(mentor);
					}
				});

	}

	@Override
	public void setValue(PersonProxy value) {
		createMutablePerson(value);
		addressPresenter.setValue(person.getAddress());
		display.getName().setValue(person.getName());
		display.getNote().setValue(person.getNote());
		display.getDescription().setValue(person.getDescription());
		mentorSelector.setValue(person.getMentor());
		scheduleEditor.setValue(person.getClassSchedule());
	}

	private void createMutablePerson(PersonProxy value) {
		AddressProxy address =null;
		ScheduleProxy schedule =null;
		display.cleanForm();
		personContext=requestFactory.personRequest();
		if(value!=null){
			person=personContext.edit(value);
			address =person.getAddress();
			schedule =person.getClassSchedule();
		} else
			person=personContext.edit(personContext.create(PersonProxy.class));		
		if(address==null){
			address = personContext.create(AddressProxy.class);
//			addressPresenter.setValue(address);
			person.setAddress(address);
		}
		if(schedule==null){
			schedule = personContext.create(ScheduleProxy.class);
			schedule.setTimeSlots(new ArrayList<TimeSlotProxy>());
			person.setClassSchedule(schedule);
//			scheduleEditor.setValue(schedule);
		}
	}

	@Override
	public PersonProxy getValue() {
		if(person==null)
			return person;
		person.setAddress(addressPresenter.getValue());
		person.setName(display.getName().getValue());
		person.setNote(display.getNote().getValue());
		person.setDescription(display.getDescription().getValue());
		person.setMentor(mentorSelector.getValue());
		person.setClassSchedule(scheduleEditor.getValue());
		return person;
	}

	public PersonRequest getContext() {
		return personContext;
	}


	public void focus(){
		display.focus();
	}
}
