package org.example.Boundaries;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.ResourceBundle;

import org.example.App;
import org.example.OCSF.CinemaClient;
import org.example.OCSF.CinemaClientCLI;
import org.example.entities.ContentManager;
import org.example.entities.Show;
import org.example.entities.UpdatePriceRequest;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;

@SuppressWarnings("serial")
public class PriceUpdatingRequestsBoundary extends EmployeeMainBoundary implements Initializable, Serializable{
	
	@FXML private TableView<UpdatePriceRequest> UpdatePriceRequestsTable;
	@FXML private TableColumn<UpdatePriceRequest, String> movie_name;
	@FXML private TableColumn<UpdatePriceRequest, String> date;
	@FXML private TableColumn<UpdatePriceRequest, String> time;
	@FXML private TableColumn<UpdatePriceRequest, Integer> hall_number;
	@FXML private TableColumn<UpdatePriceRequest, String> cinema;
	private TableColumn<UpdatePriceRequest, String> requested_by; // Value injected by FXMLLoader
    @FXML // fx:id="old_price"
    private TableColumn<UpdatePriceRequest, Double> old_price; // Value injected by FXMLLoader
    @FXML // fx:id="new_requested_price"
    private TableColumn<UpdatePriceRequest, Double> new_requested_price; // Value injected by FXMLLoader
    @FXML // fx:id="approveBtn"
    private Button approveSelectedBtn; // Value injected by FXMLLoader
    @FXML // fx:id="approveBtn"
    private Button approveAllBtn; // Value injected by FXMLLoader
    @FXML // fx:id="selectedRequestText"
    private Text selectedRequestText; // Value injected by FXMLLoader
    
    UpdatePriceRequest selected_request = null;
    
    public static Boolean ShowsPriceChanged = true;	// holds if the shows time is changed yet
	// change price of show in DataBase and brings the Shows from the DataBase and updates 
	// the ShowsData local list
	synchronized void ChangeShowPrice(int show_id, Double NewPrice) {
		ShowsPriceChanged = false;	// show time isn't changed yet
		// create message and send it to the server
    	LinkedList<Object> message = new LinkedList<Object>();
		message.add("ChangeShowPrice");
		message.add(show_id);
		message.add(NewPrice);
		synchronized(CinemaClient.ShowsDataLock)
		{	
			CinemaClientCLI.sendMessage(message);
							
			// wait for Data to be changed
			while(!ShowsPriceChanged) {
				try {
					CinemaClient.ShowsDataLock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
			// update ShowData
			UpdateShowsData();
		}	
	}
	
	public static Boolean RequestApprovedAndDeleted = true;	// holds if the show is deleted yet
    // delete request in DataBase and brings the Shows from the DataBase and updates 
 	// the RequestsData local list
    synchronized void ApproveAndDeleteRequest(int request_id) {
    	RequestApprovedAndDeleted = false;	// show isn't deleted yet
		// create message and send it to the server
    	LinkedList<Object> message = new LinkedList<Object>();
		message.add("ApproveAndDeleteRequest");
		message.add(request_id);
		synchronized(CinemaClient.UpdatePriceRequestsDataLock)
		{	
			CinemaClientCLI.sendMessage(message);
							
			// wait for Data to be changed
			while(!RequestApprovedAndDeleted) {
				try {
					CinemaClient.UpdatePriceRequestsDataLock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
			// update Data
			UpdateUpdatePriceRequestsData();
		}	
	}
    
    @FXML
    void clickGoBackToMainBtn(ActionEvent event) throws IOException {
    	App.setRoot("CinemaManagerMB",null);
    }
    
    @FXML
    void clickApproveSelectedBtn(ActionEvent event) {
    	int show_id = selected_request.getShow_id();
		Double new_price = selected_request.getUpdatedPrice();
		synchronized(UpdatePriceRequestsDataLock) {
	    	try {
	    		// change show entity
	        	ChangeShowPrice(show_id, new_price);
	    		MessageBoundaryEmployee.displayInfo("Show's price successfully changed.");
	    	}catch(Exception e) {	// server threw exception while trying to delete show
	    		MessageBoundaryEmployee.displayError("An error occured. Show couldn't be updated.");
	    	}
			
	    	// approve and delete request
	    	ApproveAndDeleteRequest(selected_request.getID());
	    	
	    	// set items in table
	    	ObservableList<UpdatePriceRequest> DataList = FXCollections.observableArrayList(CinemaClient.UpdatePriceRequestsData);
	    	UpdatePriceRequestsTable.setItems(DataList);
		}
    }
    
    @FXML
    void clickApproveAllBtn(ActionEvent event) {
    	synchronized(UpdatePriceRequestsDataLock) {
	    	try {
		    	for (UpdatePriceRequest upr:CinemaClient.UpdatePriceRequestsData) {
		        	// change show entity
		            ChangeShowPrice(upr.getShow_id(), upr.getUpdatedPrice());
		    		
		        	// approve and delete request
		        	ApproveAndDeleteRequest(upr.getID());
		    	}
		    	MessageBoundaryEmployee.displayInfo("Shows' prices successfully changed.");
	    	}catch(Exception e) {	// server threw exception while trying to delete show
	    		MessageBoundaryEmployee.displayError("An error occured. Shows couldn't be updated.");
	    	}
    	}
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		// set-up the columns in the table
		movie_name.setCellValueFactory(new Callback<CellDataFeatures<UpdatePriceRequest, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<UpdatePriceRequest, String> upr) {
		    	 Show show = idToShow(upr.getValue().getShow_id());
		         return (new SimpleStringProperty(show.getMovie().getName_en()));
		     }
		  });
		date.setCellValueFactory(new Callback<CellDataFeatures<UpdatePriceRequest, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<UpdatePriceRequest, String> upr) {
		    	 Show show = idToShow(upr.getValue().getShow_id());
		         return (new SimpleStringProperty(show.getDate().toString()));
		     }
		  });
		time.setCellValueFactory(new Callback<CellDataFeatures<UpdatePriceRequest, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<UpdatePriceRequest, String> upr) {
		    	 Show show = idToShow(upr.getValue().getShow_id());
		         return (new SimpleStringProperty(show.getTime().toString()));
		     }
		  });
		hall_number.setCellValueFactory(new Callback<CellDataFeatures<UpdatePriceRequest, Integer>, ObservableValue<Integer>>() {
		     public ObservableValue<Integer> call(CellDataFeatures<UpdatePriceRequest, Integer> upr) {
		    	 Show show = idToShow(upr.getValue().getShow_id());
		         return (new SimpleIntegerProperty(show.getHall().getNumber()).asObject());
		     }
		  });
		cinema.setCellValueFactory(new Callback<CellDataFeatures<UpdatePriceRequest, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<UpdatePriceRequest, String> upr) {
		    	 Show show = idToShow(upr.getValue().getShow_id());
		         return (new SimpleStringProperty(show.getHall().getCinema().getBranch_name()));
		     }
		  });
		old_price.setCellValueFactory(new Callback<CellDataFeatures<UpdatePriceRequest, Double>, ObservableValue<Double>>() {
		     public ObservableValue<Double> call(CellDataFeatures<UpdatePriceRequest, Double> upr) {
		    	 Show show = idToShow(upr.getValue().getShow_id());
		         return (new SimpleDoubleProperty(show.getPrice()).asObject());
		     }
		  });
		new_requested_price.setCellValueFactory(new Callback<CellDataFeatures<UpdatePriceRequest, Double>, ObservableValue<Double>>() {
		     public ObservableValue<Double> call(CellDataFeatures<UpdatePriceRequest, Double> upr) {
		         return (new SimpleDoubleProperty(upr.getValue().getUpdatedPrice()).asObject());
		     }
		  });
		requested_by.setCellValueFactory(new Callback<CellDataFeatures<UpdatePriceRequest, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<UpdatePriceRequest, String> upr) {
		         return (new SimpleStringProperty(upr.getValue().getRequestedBy().toString()));
		     }
		  });
		
		// disable button
		approveSelectedBtn.setDisable(true);
				
		// get selected request from table
		UpdatePriceRequestsTable.setOnMouseClicked((event) -> {
			selected_request = UpdatePriceRequestsTable.getSelectionModel().getSelectedItem();
			if (selected_request != null) {
				Show show = idToShow(selected_request.getID());
				selectedRequestText.setText(show.getMovie().toString() + ", change from " + 
						Double.toString(show.getPrice()) + " to " + Double.toString(selected_request.getUpdatedPrice()));
				approveSelectedBtn.setDisable(false);
			} else {
				selectedRequestText.setText("*no request selected*");
			}
		});
		
		synchronized(UpdatePriceRequestsDataLock) {
			// update Data
			UpdateUpdatePriceRequestsData();
			// set items in table
			ObservableList<UpdatePriceRequest> DataList = FXCollections.observableArrayList(CinemaClient.UpdatePriceRequestsData);
			UpdatePriceRequestsTable.setItems(DataList);
		}
	}

}
