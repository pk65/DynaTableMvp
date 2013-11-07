package com.google.gwt.sample.dynatablemvp.client.view;

/*
 import pegasus.bop.sprint.client.FavoritesManager;
 import pegasus.bop.sprint.client.widgets.DayFilterWidget;
 import pegasus.bop.sprint.client.widgets.FavoritesWidget;
 import pegasus.bop.sprint.client.widgets.PersonEditorWorkflow;
 import pegasus.bop.sprint.client.widgets.SummaryWidget;*/

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.sample.dynatablemvp.client.presenter.DynaTableMvpPresenter;
import com.google.gwt.sample.dynatablemvp.client.presenter.DayFilterPresenter;
import com.google.gwt.sample.dynatablemvp.client.presenter.FavoritesPresenter;
import com.google.gwt.sample.dynatablemvp.client.presenter.SummaryPresenter.Display;
import com.google.gwt.sample.dynatablemvp.client.widgets.DayFilterWidget;
import com.google.gwt.sample.dynatablemvp.client.widgets.FavoritesWidget;
import com.google.gwt.sample.dynatablemvp.client.widgets.SummaryWidget;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

public class DynaTableMvpView extends Composite implements DynaTableMvpPresenter.Display {
	interface Binder extends UiBinder<Widget, DynaTableMvpView> {
	}

	// @UiField
	// TextBox button1;
//	@UiField
//	CheckBox button2;
//	@UiField
//	Label button3;

	@UiField(provided = false)
	SummaryWidget calendar;

	// EventBus eventBus = new SimpleEventBus();

	@UiField(provided = false)
	FavoritesWidget favorites;

	@UiField(provided = false)
	DayFilterWidget filter;
//	private final EventBus eventBus;

	private final Button addButton;
	private final Button deleteButton;
	private final FlexTable contactsTable;

	// private final FlexTable contentTable;

	@UiConstructor
	public DynaTableMvpView(/*EventBus eventBus*/) {
//		this.eventBus = eventBus;
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
		addButton = new Button("Add");
		deleteButton = new Button("Delete");
		// contentTable=new FlexTable();
		contactsTable = new FlexTable();

		/*
		 * FavoritesManager manager = new FavoritesManager();
		 * PersonEditorWorkflow.register(manager);
		 * 
		 * calendar = new SummaryWidget(15); favorites = new
		 * FavoritesWidget(manager); filter = new DayFilterWidget();
		 */
	}

	/*@UiFactory
	DayFilterWidget makeDayFilterWidget() {
		return new DayFilterWidget();
	}*/

	@Override
	public HasClickHandlers getAddButton() {
		return addButton;
	}

	@Override
	public HasClickHandlers getDeleteButton() {
		return deleteButton;
	}

	@Override
	public void setData(List<String> data) {
		contactsTable.removeAllRows();

		for (int i = 0; i < data.size(); ++i) {
			contactsTable.setWidget(i, 0, new CheckBox());
			contactsTable.setText(i, 1, data.get(i));
		}
	}

	@Override
	public int getClickedRow(ClickEvent event) {
		return 0;
	}

	@Override
	public List<Integer> getSelectedRows() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public HasClickHandlers getList() {
		return contactsTable;
	}

	@Override
	public DayFilterPresenter.Display getFilter() {
		return filter;
	}

	@Override
	public Display getSummary() {
		return calendar;
	}

	@Override
	public FavoritesPresenter.Display getFavorites() {
		return favorites;
	}

}
