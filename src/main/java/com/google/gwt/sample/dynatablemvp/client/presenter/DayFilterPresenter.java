package com.google.gwt.sample.dynatablemvp.client.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.sample.dynatablemvp.client.event.FilterChangeEvent;
import com.google.gwt.sample.dynatablemvp.shared.DynaTableRequestFactory;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class DayFilterPresenter implements Presenter {

//	private static final Logger logger = Logger
//			.getLogger(DayFilterPresenter.class.getName());

	public interface Display {
		HasClickHandlers getAllButton();

		HasClickHandlers getNoneButton();

		DayCheckBoxPresenter.Display getSundayWidget();

		DayCheckBoxPresenter.Display getMondayWidget();

		DayCheckBoxPresenter.Display getTuesdayWidget();

		DayCheckBoxPresenter.Display getWednesdayWidget();

		DayCheckBoxPresenter.Display getThursdayWidget();

		DayCheckBoxPresenter.Display getFridayWidget();

		DayCheckBoxPresenter.Display getSaturdayWidget();

		Widget asWidget();
	}

	@SuppressWarnings("unused")
	private final DynaTableRequestFactory rpcService;
	private final HandlerManager eventBus;
	private final Display display;
	private final DayCheckBoxPresenter sundayPresenter;
	private final DayCheckBoxPresenter mondayPresenter;
	private final DayCheckBoxPresenter tuesdayPresenter;
	private final DayCheckBoxPresenter wednesdayPresenter;
	private final DayCheckBoxPresenter thursdayPresenter;
	private final DayCheckBoxPresenter fridayPresenter;
	private final DayCheckBoxPresenter saturdayPresenter;



	public DayFilterPresenter(DynaTableRequestFactory rpcService,
			HandlerManager eventBus, Display view) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.display = view;
		this.sundayPresenter = new DayCheckBoxPresenter(rpcService, eventBus,
				view.getSundayWidget());
		this.mondayPresenter = new DayCheckBoxPresenter(rpcService, eventBus,
				view.getMondayWidget());
		this.tuesdayPresenter = new DayCheckBoxPresenter(rpcService, eventBus,
				view.getTuesdayWidget());
		this.wednesdayPresenter = new DayCheckBoxPresenter(rpcService,
				eventBus, view.getWednesdayWidget());
		this.thursdayPresenter = new DayCheckBoxPresenter(rpcService, eventBus,
				view.getThursdayWidget());
		this.fridayPresenter = new DayCheckBoxPresenter(rpcService, eventBus,
				view.getFridayWidget());
		this.saturdayPresenter = new DayCheckBoxPresenter(rpcService, eventBus,
				view.getSaturdayWidget());
	}

	protected void bind() {
//		logger.fine("bind() executed");
		display.getAllButton().addClickHandler(
				new ClickHandler() {
					public void onClick(ClickEvent event) {
						setAllCheckBoxes(true);
					}
				});
		display.getNoneButton().addClickHandler(
				new ClickHandler() {
					public void onClick(ClickEvent event) {
						setAllCheckBoxes(false);
					}
				});
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		sundayPresenter.go(container);
		mondayPresenter.go(container);
		tuesdayPresenter.go(container);
		wednesdayPresenter.go(container);
		thursdayPresenter.go(container);
		fridayPresenter.go(container);
		saturdayPresenter.go(container);
	}

	private void setAllCheckBoxes(boolean checked) {
		for (int day = 0; day < 7; day++) {
			eventBus.fireEvent(new FilterChangeEvent(day, checked));
		}
	}

}
