package org.example.Boundaries;

import static org.example.OCSF.CinemaClient.UpdatePriceRequestsDataLock;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import org.example.App;
import org.example.OCSF.CinemaClient;
import org.example.OCSF.CinemaClientCLI;
import org.example.entities.Show;
import org.example.entities.UpdatePriceRequest;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Callback;

@SuppressWarnings("serial")
public class DeleteShowBoundary extends ContentManagerDisplayBoundary implements Initializable, Serializable{

	@FXML private TableView<Show> ShowsTable;
	@FXML private TableColumn<Show, String> movie_name;
	@FXML private TableColumn<Show, String> date;
	@FXML private TableColumn<Show, String> time;
	@FXML private TableColumn<Show, Integer> hall_number;
	@FXML private TableColumn<Show, Double> price;
	@FXML private TableColumn<Show, String> cinema;
	
	Show selected_show = null;
	List<Integer> uprs_of_selected_show = new LinkedList<Integer>();
	
    @FXML // fx:id="refreshBtn2"
    private Button refreshBtn2; // Value injected by FXMLLoader

    @FXML // fx:id="GoBackToMainBtn"
    private Button GoBackToMainBtn; // Value injected by FXMLLoader

    @FXML // fx:id="DeleteShowBtn"
    private Button DeleteShowBtn; // Value injected by FXMLLoader
    
    @FXML // fx:id="selectedShowText"
    private Text selectedShowText; // Value injected by FXMLLoader
    
    @FXML // fx:id="selectedDayText"
    private Text selectedDateText; // Value injected by FXMLLoader

    @FXML // fx:id="selectedTimeText"
    private Text selectedTimeText; // Value injected by FXMLLoader
    
    public static Boolean RequestDeclined = true;	// holds if the show is deleted yet
    // delete request in DataBase and brings the Shows from the DataBase and updates 
 	// the RequestsData local list
    synchronized void DeclineRequest(int request_id) {	
		// create message and send it to the server
    	LinkedList<Object> message = new LinkedList<Object>();
		message.add("DeclineRequest");
		message.add(request_id);
		synchronized(UpdatePriceRequestsDataLock)
		{	
			RequestDeclined = false;
			CinemaClientCLI.sendMessage(message);
							
			// wait for Data to be changed
			while(!RequestDeclined) {
				try {
					UpdatePriceRequestsDataLock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
			// update Data
			org.example.Boundaries.Boundary.UpdateUpdatePriceRequestsData();
		}	
	}
    
    
    public static Boolean ShowDeleted = true;	// holds if the show is deleted yet
    // delete show in DataBase and brings the Shows from the DataBase and updates 
 	// the ShowsData local list
    synchronized void DeleteShow(int show_id) {
		// create message and send it to the server
    	LinkedList<Object> message = new LinkedList<Object>();
		message.add("DeleteShow");
		message.add(show_id);
		synchronized(CinemaClient.ShowsDataLock)
		{	
			ShowDeleted = false;	// show isn't deleted yet
			CinemaClientCLI.sendMessage(message);
							
			// wait for Data to be changed
			while(!ShowDeleted) {
				try {
					CinemaClient.ShowsDataLock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
			// update ShowData
			org.example.Boundaries.Boundary.UpdateShowsData();
		}	
	}
    
    @FXML
    void clickDeleteShowBtn(ActionEvent event) {
    	int show_id = selected_show.getID();
		
    	synchronized(CinemaClient.ShowsDataLock) {
    		
	    	try {
	    		// delete show entity
	        	DeleteShow(show_id);
	    		MessageBoundaryEmployee.displayInfo("Show successfully deleted.");
	    	}catch(Exception e) {	// server threw exception while trying to delete show
	    		MessageBoundaryEmployee.displayError("An error occured. Show couldn't be deleted.");
	    	}
			
	    	// set items in table
	    	ObservableList<Show> DataList = FXCollections.observableArrayList(CinemaClient.ShowsData);
	    	ShowsTable.setItems(DataList);
    	}
		
    }

    @FXML
    void clickRefreshBtn2(ActionEvent event) {
    	synchronized(CinemaClient.ShowsDataLock) {
	    	org.example.Boundaries.Boundary.UpdateShowsData();
	    	DeleteShowBtn.setDisable(true);
	    	selected_show = null;
	    	selectedShowText.setText("*no show selected*");
	    	selectedDateText.setText("");
	    	selectedTimeText.setText("");
			// set items in table
			ObservableList<Show> DataList = FXCollections.observableArrayList(CinemaClient.ShowsData);
			ShowsTable.setItems(DataList);
			System.out.println("refreshed");
    	}
    }
    
    @Override
	public void initialize(URL url, ResourceBundle rb) {
		// set-up the columns in the table
		movie_name.setCellValueFactory(new Callback<CellDataFeatures<Show, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<Show, String> show) {
		         return (new SimpleStringProperty(show.getValue().getMovie().getName_en()));
		     }
		  });
		date.setCellValueFactory(new Callback<CellDataFeatures<Show, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<Show, String> show) {
		         return (new SimpleStringProperty(show.getValue().getDate().toString()));
		     }
		  });
		time.setCellValueFactory(new Callback<CellDataFeatures<Show, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<Show, String> show) {
		         return (new SimpleStringProperty(show.getValue().getTime().toString()));
		     }
		  });
		hall_number.setCellValueFactory(new Callback<CellDataFeatures<Show, Integer>, ObservableValue<Integer>>() {
		     public ObservableValue<Integer> call(CellDataFeatures<Show, Integer> show) {
		         return (new SimpleIntegerProperty(show.getValue().getHall().getNumber()).asObject());
		     }
		  });
		price.setCellValueFactory(new PropertyValueFactory<Show, Double>("price"));
		cinema.setCellValueFactory(new Callback<CellDataFeatures<Show, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<Show, String> show) {
		         return (new SimpleStringProperty(show.getValue().getHall().getCinema().getBranch_name()));
		     }
		  });
		
		// disable button
		DeleteShowBtn.setDisable(true);
		
		// get selected show from table
		ShowsTable.setOnMouseClicked((event) -> {
			selected_show = ShowsTable.getSelectionModel().getSelectedItem();
			if (selected_show != null) {
		        selectedShowText.setText(selected_show.getMovie().toString());
		        selectedDateText.setText(selected_show.getDate().toString());
		    	selectedTimeText.setText(selected_show.getTime().toString());
		        DeleteShowBtn.setDisable(false);
		    } else {
		    	selectedShowText.setText("*no show selected*");
		    	selectedDateText.setText("");
		    	selectedTimeText.setText("");
		    }
		});
		
		synchronized(CinemaClient.ShowsDataLock) {
			// update ShowData
			org.example.Boundaries.Boundary.UpdateShowsData();
			// set items in table
			ObservableList<Show> DataList = FXCollections.observableArrayList(CinemaClient.ShowsData);
			ShowsTable.setItems(DataList);
		}
	}

}

