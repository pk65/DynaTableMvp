package com.google.gwt.sample.dynatablemvp.client;

/*
 * Copyright 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
//import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.sample.dynatablemvp.shared.DynaTableRequestFactory;
//import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The entry point class which performs the initial loading of the DynaTableRf
 * application.
 */
public class DynaTableMvp implements EntryPoint {

  private static final Logger log = Logger.getLogger(DynaTableMvp.class.getName());

//  private static final boolean _OLD_PART = false;
  
  final EventBus eventBus = new SimpleEventBus();

/*  @UiField(provided = true)
  SummaryWidget calendar;


  @UiField(provided = true)
  FavoritesWidget favorites;

  @UiField(provided = true)
  DayFilterWidget filter;*/

  /**
   * This method sets up the top-level services used by the application.
   */
  public void onModuleLoad() {
    GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
      public void onUncaughtException(Throwable e) {
        log.log(Level.SEVERE, e.getMessage(), e);
      }
    });

    final DynaTableRequestFactory requests = GWT.create(DynaTableRequestFactory.class);
    requests.initialize(eventBus);

    
    HandlerManager eventBusHandler = new HandlerManager(null);
    AppController appViewer = new AppController(requests, eventBusHandler);
    appViewer.go(RootLayoutPanel.get());

    // Fast test to see if the sample is not being run from devmode
    if (GWT.getHostPageBaseURL().startsWith("file:")) {
      log.log(Level.SEVERE, "The DynaTableMvp sample cannot be run without its"
          + " server component.  If you are running the sample from a"
          + " GWT distribution, use the 'ant devmode' target to launch"
          + " the DTRF server.");
    }
  }
}
