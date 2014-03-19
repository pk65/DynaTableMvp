package com.google.gwt.sample.dynatablemvp.client.presenter;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import javax.validation.ConstraintViolation;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.sample.dynatablemvp.client.FavoritesManager;
import com.google.gwt.sample.dynatablemvp.client.event.CreatePersonEvent;
import com.google.gwt.sample.dynatablemvp.client.event.EditPersonEvent;
import com.google.gwt.sample.dynatablemvp.client.event.MarkFavoriteEvent;
import com.google.gwt.sample.dynatablemvp.client.event.PersonProxyChangeEvent;
import com.google.gwt.sample.dynatablemvp.shared.DiagTools;
import com.google.gwt.sample.dynatablemvp.shared.DynaTableRequestFactory;
import com.google.gwt.sample.dynatablemvp.shared.DynaTableRequestFactory.SchoolCalendarRequest;
import com.google.gwt.sample.dynatablemvp.shared.PersonProxy;
import com.google.gwt.sample.dynatablemvp.shared.PersonRelation;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class PersonEditorWorkflowPresenter implements Presenter {
	private static final Logger logger = Logger.getLogger(PersonEditorWorkflowPresenter.class.getName());
	
	public interface Display {
		HasValueChangeHandlers<java.lang.Boolean> getFavorite();
		void setFavorite(boolean value);

		void setDialogVisible(boolean visible);

		HasText getDialogText();

		HasClickHandlers getSave();

		HasClickHandlers getCancel();

		PersonEditorPresenter.Display getPersonEditor();

		Widget getContentsWidget();

	}

	@Override
	public void go(HasWidgets container) {
		personEditor.go(container);
		bind();
	}

	protected void bind() {
		view.getSave().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				onSave(event);
			}
		});
		view.getCancel().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				view.setDialogVisible(false);
			}
		});
		eventBus.addHandler(EditPersonEvent.TYPE,
				new EditPersonEvent.Handler() {
					public void startEdit(PersonProxy person) {
						view.setFavorite(favoritesManager.isFavorite(person));
						fetchAndEdit(person.getId());
					}
				});
		view.getContentsWidget().addDomHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
					view.setDialogVisible(false);
				}
			}
		}, KeyUpEvent.getType());
		eventBus.addHandler(CreatePersonEvent.TYPE,
				new CreatePersonEvent.Handler() {
					public void startEdit() {
						personEditor.setValue(null);
						view.setDialogVisible(true);
					}
				});
		view.getFavorite().addValueChangeHandler(new ValueChangeHandler<Boolean>(){

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				final PersonProxy person = personEditor.getValue();
				final Boolean fav = event.getValue();
				if(person!=null){
					eventBus.fireEvent(new MarkFavoriteEvent(person.stableId(),fav==null?false:fav.booleanValue()));
					favoritesManager.setFavorite(person.stableId(),fav==null?false:fav.booleanValue());
				}
			}});
	}




	private final DynaTableRequestFactory requestFactory;
	private final HandlerManager eventBus;
	private final Display view;
	private final PersonEditorPresenter personEditor;
	private final FavoritesManager favoritesManager;
	
	public PersonEditorWorkflowPresenter(
			DynaTableRequestFactory requestFactory, HandlerManager eventBus,
			Display view, FavoritesManager favoritesManager) {
		this.requestFactory = requestFactory;
		this.eventBus = eventBus;

		this.view = view;
		this.favoritesManager=favoritesManager;
		personEditor=new PersonEditorPresenter(requestFactory, eventBus,this.view.getPersonEditor());
	}

	private void fetchAndEdit(Integer id) {
		final SchoolCalendarRequest fetchRequest=requestFactory.schoolCalendarRequest();
		fetchRequest.findPerson(id,Arrays.asList(PersonRelation.ADDRESS,PersonRelation.SHEDULE,PersonRelation.MENTOR))
			.with("address","classSchedule.timeSlots","mentor").fire(new Receiver<PersonProxy>() {
			@Override
			public void onFailure(ServerFailure error) {
				logger.severe(error.getMessage());
			}

			@Override
			public void onConstraintViolation(
					Set<ConstraintViolation<?>> violations) {
				Iterator<ConstraintViolation<?>> violIter = violations.iterator();
				while(violIter.hasNext())
					logger.severe(violIter.next().getMessage());						
			}

			@Override
			public void onSuccess(PersonProxy person) {
				personEditor.setValue(person);
				view.setDialogVisible(true);
				personEditor.focus();				
			}
		});
	}

	/**
	 * Called by the edit dialog's save button. This method will flush the
	 * contents of the UI into the PersonProxy that is being edited, check for
	 * errors, and send the request to the server.
	 */
	void onSave(ClickEvent event) {
		final PersonProxy personToSave = personEditor.getValue();
		personEditor.getContext().persist(personToSave
				,personToSave.getAddress()
				,personToSave.getClassSchedule()
				,personToSave.getMentor())
		.fire(new Receiver<Integer> () {
			@Override
			public void onFailure(ServerFailure error) {
				view.getDialogText().setText(error.getMessage()+"\n"+error.getStackTraceString()+"\n");
			}
			@Override
			public void onConstraintViolation(Set<ConstraintViolation<?>> errors) {
				// Otherwise, show ConstraintViolations in the UI
				String text = DiagTools.getViolationText("Errors detected on the server: ",errors);
				view.getDialogText().setText(text);
			}
			@Override
			public void onSuccess(Integer response) {
				view.setDialogVisible(false);
				eventBus.fireEvent(new PersonProxyChangeEvent(response,(personToSave.getId()!=null),personToSave.stableId()));
			}
		});
	}

}
