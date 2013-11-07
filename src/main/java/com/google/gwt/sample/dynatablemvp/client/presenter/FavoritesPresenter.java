package com.google.gwt.sample.dynatablemvp.client.presenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.sample.dynatablemvp.client.FavoritesManager;
import com.google.gwt.sample.dynatablemvp.client.event.EditPersonEvent;
import com.google.gwt.sample.dynatablemvp.client.event.MarkFavoriteEvent;
import com.google.gwt.sample.dynatablemvp.shared.DynaTableRequestFactory;
import com.google.gwt.sample.dynatablemvp.shared.PersonProxy;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.Receiver;

public class FavoritesPresenter implements Presenter {
	private static final Logger logger = Logger.getLogger(FavoritesPresenter.class.getName());
	
	public interface NameElement extends HasText, IsWidget, HasClickHandlers {
		int getIndex();
		void setIndex(int index);
	}
	
	public interface Display {
		Widget asWidget();
		String getStyle();
		List<NameElement> getContainer();
		NameElement getNewElement();
	}

	private final DynaTableRequestFactory requests;
	private final HandlerManager eventBus;
	private final Display display;
	private final FavoritesManager manager;
	
	public FavoritesManager getFavoritesManager() {
		return manager;
	}


	private final NamesList namesList;

	public class NamesList {
		private final List<EntityProxyId<PersonProxy>> choosenPersons=new ArrayList<EntityProxyId<PersonProxy>> ();
		private final HashMap<EntityProxyId<PersonProxy>,HandlerRegistration> handlerMap=new HashMap<EntityProxyId<PersonProxy>,HandlerRegistration>();
		
		public void add(final PersonProxy person){
			final EntityProxyId<PersonProxy> stableId = person.stableId();
			if(choosenPersons.contains(stableId))
				return;
			NameElement label=display.getNewElement();
			label.setText(person.getName());
			HandlerRegistration handler = label.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					eventBus.fireEvent(new EditPersonEvent(person));
					
				}				
			});
			handlerMap.put(stableId, handler);
			FavoritesPresenter.this.display.getContainer().add(label);
			choosenPersons.add(stableId);
		}
		

		public void remove(EntityProxyId<PersonProxy> id){
			int index=choosenPersons.indexOf(id);
			if(index>=0){
				final List<NameElement> container = FavoritesPresenter.this.display.getContainer();
				handlerMap.get(id).removeHandler();
				boolean existed=container.remove(index)!=null;			
				choosenPersons.remove(index);
				if(!existed)
					logger.warning("person doesn't exist on index: "+index);
			}
		}

		public boolean contains(EntityProxyId<PersonProxy> id){
			if(id==null)
				return false;			
			return choosenPersons.contains(id);
		}
		
		public int size(){
			return choosenPersons.size();
		}
	
		public void sort(){
			if(size()<2)
				return;
			final List<EntityProxyId<PersonProxy>> srtList=new ArrayList<EntityProxyId<PersonProxy>> ();
			srtList.addAll(choosenPersons);
			final List<NameElement> container = FavoritesPresenter.this.display.getContainer();
			Collections.sort(srtList, new Comparator<EntityProxyId<PersonProxy>>() {
				@Override
				public int compare(EntityProxyId<PersonProxy> o1,	EntityProxyId<PersonProxy> o2) {
					String title1=getTtext(o1);
					String title2=getTtext(o2);
					return title1.compareToIgnoreCase(title2);
				}

				private String  getTtext(EntityProxyId<PersonProxy> id) {
					int index=choosenPersons.indexOf(id);
					NameElement label=(NameElement) container.get(index);
					return label.getText();
				}
			});
			for(int i=0;i<size();i++){
				EntityProxyId<PersonProxy> id =srtList.get(i);
				int dstIndex=choosenPersons.indexOf(id);
				if(dstIndex!=i){
					EntityProxyId<PersonProxy> srcId =choosenPersons.get(i);
					choosenPersons.set(dstIndex, srcId);
					choosenPersons.set(i, id);
					NameElement srcLabel=container.get(i);
					NameElement dstLabel=container.get(dstIndex);
					srcLabel.setIndex(i);
					dstLabel.setIndex(dstIndex);
					container.set(dstIndex, srcLabel);
					container.set( i, dstLabel);
				}
			}			
		}


		public String[] getNames(){
			final List<NameElement> container = FavoritesPresenter.this.display.getContainer();
			String[] result=new String[size()];
			for(int i=0;i<size();i++){
					NameElement label=container.get(i);
					result[i]=label.getText();
			}			
			return result;
		}


		public void clean() {
			final List<NameElement> container = FavoritesPresenter.this.display.getContainer();
			int max=size()-1;
			for(int i=max;i>=0;i--){
				EntityProxyId<PersonProxy> id =choosenPersons.get(i);
				handlerMap.get(id).removeHandler();
				container.remove(i);	
			}
			choosenPersons.clear();
		}
	}
	
	public FavoritesPresenter(DynaTableRequestFactory rpcService,
	HandlerManager eventBus, Display display,FavoritesManager.CookieStorage browserManager) {
		
		this.requests = rpcService;
		this.eventBus = eventBus;
		this.display = display;
		this.manager = new FavoritesManager(this.requests ,this.eventBus,browserManager);
		namesList=new NamesList();
	}
	
	protected void bind(){
		
		eventBus.addHandler(MarkFavoriteEvent.TYPE,new MarkFavoriteEvent.Handler() {
			@Override
			public void onMarkFavorite(MarkFavoriteEvent event) {
				FavoritesPresenter.this.onMarkFavorite(event);
			}
		});
		 		
		// Initialize the UI with the existing list of favorites
		for (EntityProxyId<PersonProxy> id : manager.getFavoriteIds()) {
			onMarkFavorite(new MarkFavoriteEvent(id, true));
		}
	}
	
	@Override	
	public void go(HasWidgets container) {
		bind();
	}

	public void onMarkFavorite(MarkFavoriteEvent event) {
		EntityProxyId<PersonProxy> id = event.getId();
		if (id == null) {
			return;
		}

		if (event.isFavorite() && namesList.contains(id)==false) {
			requests.find(id).to(new Receiver<PersonProxy>() {
				@Override
				public void onSuccess(PersonProxy response) {
					addPersonToList(response);
				}
			}).fire();
		} else if (!event.isFavorite() && namesList.contains(id)) {
			namesList.remove(id);
			logger.fine("remove favorite");
		}
	}

	public void addPersonToList(PersonProxy response) {
		namesList.add(response);
		namesList.sort();
		logger.fine("add favorite: "+response.getName());
	}

	public int size(){
		return namesList.size();
	}
	
	public String[] getNames(){
		return namesList.getNames();
	}
	
	public void clean(){
		namesList.clean();
	}
}
