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
package com.google.gwt.sample.dynatablemvp.client.widgets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.sample.dynatablemvp.client.presenter.FavoritesPresenter;
import com.google.gwt.sample.dynatablemvp.client.presenter.FavoritesPresenter.NameElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * Displays Person objects that the user has selected as a favorite. This
 * demonstrates a read-only "editor" that receives update notifications.
 */
public class FavoritesWidget extends Composite implements
		FavoritesPresenter.Display {

	interface Binder extends UiBinder<Widget, FavoritesWidget> {
	}
//	private static final Logger logger = Logger.getLogger(FavoritesWidget.class.getName());

	/**
	 * A driver that accepts a List of PersonProxy objects, controlled by a
	 * ListEditor of PersonProxy instances, displayed using NameLabels.
	 */
/*	public interface Driver extends RequestFactoryEditorDriver<List<PersonProxy>, //
			ListEditor<PersonProxy, NameLabel>> {
	}*/

	public interface Style extends CssResource {
		String favorite();
	}


	@UiField
	FlowPanel container;

	@UiField
	Style style;

	/**
	 * This list is a facade provided by the ListEditor. Structural
	 * modifications to this list (e.g. add(), set(), remove()) will trigger UI
	 * update events.
	 */

	private final NamesPanel namesPanel;
	
	@UiConstructor
	public FavoritesWidget() {
		initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
		this.namesPanel=new NamesPanel();
		container.setVisible(true);
	}
	


	@Override
	public String getStyle() {
		return style.favorite();
	}

	@Override
	public List<NameElement> getContainer() {
		return namesPanel;
	}
	
	@Override
	public FavoritesPresenter.NameElement getNewElement() {
		 return new NameElement(){
			private final Label label=new Label();
			private int index;
			
			@Override
			public String getText() {
				return label.getText();
			}

			@Override
			public void setText(String text) {
				label.setText(text);
				
			}

			@Override
			public Widget asWidget() {
				return label;
			}

			@Override
			public HandlerRegistration addClickHandler(ClickHandler handler) {
				return label.addClickHandler(handler);
			}

			@Override
			public void fireEvent(GwtEvent<?> event) {
				label.fireEvent(event);				
			}

			@Override
			public int getIndex() {
				// TODO Auto-generated method stub
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

		@Override
		public boolean add(NameElement e) {
			container.add(e.asWidget());			
			return super.add(e);
		}

		@Override
		protected void removeRange(int fromIndex, int toIndex) {
			for(int idx=fromIndex;idx<=toIndex;idx++)
				container.remove(idx);
			super.removeRange(fromIndex, toIndex);
		}

		@Override
		public void add(int index, NameElement element) {
			container.insert(element.asWidget(), index);
			super.add(index, element);
		}

		@Override
		public boolean remove(Object o) {
			container.remove(((NameElement)o).asWidget());
			return super.remove(o);
		}

		@Override
		public boolean addAll(Collection<? extends NameElement> c) {
			Iterator<? extends NameElement> iterC = c.iterator();
			while(iterC.hasNext())
				container.add(iterC.next().asWidget());
			return super.addAll(c);
		}

		@Override
		public boolean addAll(int index, Collection<? extends NameElement> c) {
			int i=index;
			Iterator<? extends NameElement> iterC = c.iterator();
			while(iterC.hasNext()){
				container.insert(iterC.next().asWidget(),i);
				i++;
			}
			return super.addAll(index,c);
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			Iterator<?> iterC = c.iterator();
			ArrayList<Integer> toRemove = new ArrayList<Integer>();
			while(iterC.hasNext()){
				NameElement e=(NameElement) iterC.next();
				final int indexOf = super.indexOf(e);
				if(indexOf>=0)
					toRemove.add(indexOf);
			}
			Collections.sort(toRemove, new Comparator<Integer>(){
				@Override
				public int compare(Integer o1, Integer o2) {
					return o2.compareTo(o1);
				}});
			for(Integer idx : toRemove)
				container.remove(idx);
			return super.removeAll(c);
		}

		@Override
		public void clear() {
			container.clear();
			super.clear();
		}

		@Override
		public NameElement set(int index, NameElement element) {
			element.asWidget().removeFromParent();
			final boolean shouldBeAdded = index==size()-1;
			int i=index;
			if(element.getIndex()<index) 
				i--;			
			if(shouldBeAdded)
				container.add(element.asWidget());
			else
				container.insert(element.asWidget(), i);
			
			return super.set(index, element);
		}

		@Override
		public NameElement remove(int index) {
			container.remove(index);
			return super.remove(index);
		}
		
	}
}
