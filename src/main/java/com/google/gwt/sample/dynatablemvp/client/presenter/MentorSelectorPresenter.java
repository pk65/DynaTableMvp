package com.google.gwt.sample.dynatablemvp.client.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.sample.dynatablemvp.client.event.ChooseMentorEvent;
import com.google.gwt.sample.dynatablemvp.shared.DynaTableRequestFactory;
import com.google.gwt.sample.dynatablemvp.shared.PersonProxy;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.shared.Receiver;

public class MentorSelectorPresenter implements Presenter , TakesValue<PersonProxy> {
	public interface Display {
		HasClickHandlers getChoose();
		HasClickHandlers getClear();

		HasClickHandlers getEditor();

		void setEnabled(boolean enabled);

		HasText getLabel();
		HasVisibility getView();
		
		Widget asWidget();
	}

	private final DynaTableRequestFactory requestFactory;
	private final HandlerManager eventBus;
	private final Display view;

	private PersonProxy person;
	
	public MentorSelectorPresenter(DynaTableRequestFactory requestFactory,
			HandlerManager eventBus, Display view) {
		this.requestFactory = requestFactory;
		this.eventBus = eventBus;
		this.view = view;
	}

	protected void bind() {
		view.getChoose().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				view.setEnabled(false);
				requestFactory.schoolCalendarRequest().getRandomPerson()
						.to(new Receiver<PersonProxy>() {
							@Override
							public void onSuccess(PersonProxy mentor) {
								final HasText label = view.getLabel();
								if(label!=null && mentor!=null){
									final String mentorName = mentor.getName();
									if(mentorName!=null)
										label.setText(mentorName);
								}
								view.setEnabled(true);
								eventBus.fireEvent(new ChooseMentorEvent(mentor));
							}
						}).fire();
			}

		});
		view.getClear().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setValue(null);				
			}			
		});
		/*view.getEditor().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(view.getLabel()!=null)
					eventBus.fireEvent(new ChooseMentorEvent(view.getLabel().getText()));
			}
		});*/

	}

	@Override
	public void go(HasWidgets container) {
		bind();

	}

	@Override
	public void setValue(PersonProxy value) {
		this.person=value;
		if(view.getLabel()!=null){
			String name=null;
			if(person!=null)
				name = person.getName();
			if(name==null)
				name="";
			view.getLabel().setText(name);
		}
	}

	@Override
	public PersonProxy getValue() {
		if(person!=null){
			String text="";
			if(view.getLabel()!=null) 
				text = view.getLabel().getText();			
			person.setName(text);
		}
		return person;
	}


	
}
