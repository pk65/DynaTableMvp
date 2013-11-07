package com.google.gwt.sample.dynatablemvp.client.presenter;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.sample.dynatablemvp.client.FavoritesManager;
import com.google.gwt.sample.dynatablemvp.client.event.AddContactEvent;
import com.google.gwt.sample.dynatablemvp.client.event.EditContactEvent;
import com.google.gwt.sample.dynatablemvp.shared.DynaTableRequestFactory;
import com.google.gwt.sample.dynatablemvp.shared.PersonProxy;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class DynaTableMvpPresenter  implements Presenter {

	  public interface Display {
	    HasClickHandlers getAddButton();
	    HasClickHandlers getDeleteButton();
	    void setData(List<String> data);
	    int getClickedRow(ClickEvent event);
	    List<Integer> getSelectedRows();
	    Widget asWidget();
		HasClickHandlers getList();
		DayFilterPresenter.Display getFilter();
		SummaryPresenter.Display getSummary();
		FavoritesPresenter.Display getFavorites();
	  }
	  
	  @SuppressWarnings("unused")
	private final DynaTableRequestFactory rpcService;
	  private final HandlerManager eventBus;
	  private final Display display;
	  private List<PersonProxy> contactDetails;
	  private final DayFilterPresenter filter;
	  private final SummaryPresenter summary;
	  private final FavoritesPresenter favorites;
	  
		public FavoritesManager getFavoritesManager() {
			return favorites.getFavoritesManager();
		}
		
	  
	  public DynaTableMvpPresenter(DynaTableRequestFactory rpcService, HandlerManager eventBus, Display view,FavoritesManager.CookieStorage browserManager) {
	    this.rpcService = rpcService;
	    this.eventBus = eventBus;
	    this.display = view;
	    this.filter=new DayFilterPresenter(rpcService, eventBus, view.getFilter());
	    this.summary=new SummaryPresenter(rpcService, eventBus, view.getSummary());
	    this.favorites=new FavoritesPresenter(rpcService, eventBus, view.getFavorites(),browserManager);
/*	    final FavoritesManager manager = new FavoritesManager(rpcService);
//	    PersonEditorWorkflow.register(rpcService.getEventBus(),rpcService,manager);
	    rpcService.getEventBus().addHandler(EditPersonEvent.TYPE, new EditPersonEvent.Handler() {
	        public void startEdit(PersonProxy person, RequestContext requestContext) {
	        	PersonEditorWorkflowPresenter presenter=new PersonEditorWorkflowPresenter(DynaTableMvpPresenter.this.rpcService, manager, person);
	          presenter.edit(requestContext);
	        }
	      });	    
	
	    calendar = new SummaryWidget(15);
	    favorites = new FavoritesWidget(manager);
	    filter = new DayFilterWidget();		  */
	    
	  }
	  
	protected void bind() {
		    display.getAddButton().addClickHandler(new ClickHandler() {   
		      public void onClick(ClickEvent event) {
		        eventBus.fireEvent(new AddContactEvent());
		      }
		    });

		    display.getDeleteButton().addClickHandler(new ClickHandler() {   
		      public void onClick(ClickEvent event) {
		        deleteSelectedContacts();
		      }
		    });
		    
		    display.getList().addClickHandler(new ClickHandler() {
		      public void onClick(ClickEvent event) {
		        int selectedRow = display.getClickedRow(event);
		        
		        if (selectedRow >= 0) {
		          String id = contactDetails.get(selectedRow).getId();
		          eventBus.fireEvent(new EditContactEvent(id));
		        }
		      }
		    });
	  }
		  

	@Override
	public void go(HasWidgets container) {
	    final Widget backLogWidget = display.asWidget();
	    bind();
	    container.clear();
    	container.add(backLogWidget);
	    this.filter.go(container);
	    this.summary.go(container);
	    this.favorites.go(container);
//	    fetchContactDetails();
	}

	  
	  public void sortContactDetails() {
		    
		    // Yes, we could use a more optimized method of sorting, but the 
		    //  point is to create a test case that helps illustrate the higher
		    //  level concepts used when creating MVP-based applications. 
		    //
		    for (int i = 0; i < contactDetails.size(); ++i) {
		      for (int j = 0; j < contactDetails.size() - 1; ++j) {
		        if (contactDetails.get(j).getDescription().compareToIgnoreCase(contactDetails.get(j + 1).getDescription()) >= 0) {
		          PersonProxy tmp = contactDetails.get(j);
		          contactDetails.set(j, contactDetails.get(j + 1));
		          contactDetails.set(j + 1, tmp);
		        }
		      }
		    }
		  }

/*	  private void fetchContactDetails() {
		  rpcService.schoolCalendarRequest().getPeople(0, 100
				  ,DynaTableRequestFactory.SchoolCalendarRequest.ALL_DAYS).fire(new Receiver<List<PersonProxy>>(){

			@Override
					public void onFailure(ServerFailure error) {
						super.onFailure(error);
						Window.alert("Error fetching contact details");
					}

			@Override
			public void onSuccess(List<PersonProxy> result) {
		          contactDetails = result;
		          sortContactDetails();
		          List<String> data = new ArrayList<String>();

		          for (int i = 0; i < result.size(); ++i) {
		            data.add(contactDetails.get(i).getDescription());
		          }
		     
		          display.setData(data);
			}});
	      
	  }	
*/
	  private void deleteSelectedContacts() {
		 /*   List<Integer> selectedRows = display.getSelectedRows();
		    ArrayList<String> ids = new ArrayList<String>();
		    
		    for (int i = 0; i < selectedRows.size(); ++i) {
		      ids.add(contactDetails.get(selectedRows.get(i)).getId());
		    }
		    rpcService.schoolCalendarRequest().deletePeople(ids).fire(new Receiver<List<PersonProxy>>(){

				@Override
				public void onFailure(ServerFailure error) {
					// TODO Auto-generated method stub
					super.onFailure(error);
			        Window.alert("Error deleting selected contacts");
				}

				@Override
				public void onSuccess(List<PersonProxy> result) {
			        contactDetails = result;
			        sortContactDetails();
			        List<String> data = new ArrayList<String>();

			        for (int i = 0; i < result.size(); ++i) {
			          data.add(contactDetails.get(i).getDescription());
			        }
			        
			        display.setData(data);
				}});*/
		  }
}
