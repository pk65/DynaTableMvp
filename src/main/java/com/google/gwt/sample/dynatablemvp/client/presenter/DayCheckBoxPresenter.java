package com.google.gwt.sample.dynatablemvp.client.presenter;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.sample.dynatablemvp.client.event.FilterChangeEvent;
import com.google.gwt.sample.dynatablemvp.shared.DynaTableRequestFactory;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class DayCheckBoxPresenter implements Presenter {
//	private static final Logger logger = Logger
//			.getLogger(DayCheckBoxPresenter.class.getName());

	@SuppressWarnings("unused")
	private final DynaTableRequestFactory rpcService;
	private final HandlerManager eventBus;
	private final Display display;

	public interface Display {
		HasValueChangeHandlers<Boolean> getCheckBox();

		int getDay();

		void setValue(boolean value);

		Widget asWidget();
	}

	public DayCheckBoxPresenter(DynaTableRequestFactory rpcService,
			HandlerManager eventBus, Display view) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.display = view;

	}

	protected void bind() {
//		logger.fine("bind() executed for day: " + display.getDay() + ";");
		display.getCheckBox().addValueChangeHandler(
				new ValueChangeHandler<Boolean>() {
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						eventBus.fireEvent(new FilterChangeEvent(display
								.getDay(), event.getValue()));
					}
				});
		eventBus.addHandler(FilterChangeEvent.TYPE,
				new FilterChangeEvent.Handler() {
					@Override
					public void onFilterChanged(FilterChangeEvent e) {
						if (e.getDay() == display.getDay()) {
							display.setValue(e.isSelected());
//							logger.fine("onFilterChanged() executed: day="
//									+ e.getDay() + "; isSelected="
//									+ e.isSelected()+";");
						}
					}
				});

	}

	@Override
	public void go(HasWidgets container) {
		bind();
	}

}
