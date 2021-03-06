package org.example.Boundaries;

import javafx.fxml.Initializable;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

import java.io.Serializable;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.LinkedList;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import org.example.OCSF.CinemaClient;
import org.example.OCSF.CinemaClientCLI;
import org.example.entities.Show;


@SuppressWarnings("serial")
public class UpdateTimeBoundary extends ContentManagerDisplayBoundary implements Initializable, Serializable{
	
	@FXML private TableView<Show> ShowsTable;
	@FXML private TableColumn<Show, String> movie_name;
	@FXML private TableColumn<Show, String> date;
	@FXML private TableColumn<Show, String> time;
	@FXML private TableColumn<Show, Integer> hall_number;
	@FXML private TableColumn<Show, Double> price;
	@FXML private TableColumn<Show, String> cinema;
	
	@FXML // fx:id="refreshBtn2"
    private Button refreshBtn2; // Value injected by FXMLLoader
	 
	public static Boolean ShowsTimeChanged = true;	// holds if the shows time is changed yet
	// change time of show in DataBase and brings the Shows from the DataBase and updates 
	// the ShowsData local list
	synchronized void ChangeShowTime(int show_id, LocalTime NewTime) {
		ShowsTimeChanged = false;	// show time isn't changed yet
		// create message and send it to the server
    	LinkedList<Object> message = new LinkedList<Object>();
		message.add("ChangeShowTime");
		message.add(show_id);
		message.add(NewTime);
		synchronized(CinemaClient.ShowsDataLock)
		{	
			CinemaClientCLI.sendMessage(message);
							
			// wait for Data to be changed
			while(!ShowsTimeChanged) {
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
    void clickRefreshBtn2(ActionEvent event) {
    	synchronized(CinemaClient.ShowsDataLock) {
	    	org.example.Boundaries.Boundary.UpdateShowsData();
			// set items in table
			ObservableList<Show> DataList = FXCollections.observableArrayList(CinemaClient.ShowsData);
			ShowsTable.setItems(DataList);
    	}
    }
    
    @FXML // fx:id="GoBackToMainBtn"
    private Button GoBackToMainBtn; // Value injected by FXMLLoader
    
    @FXML
    void editTime(ActionEvent event) {
    	
    }

    
    @Override
	public void initialize(URL url, ResourceBundle rb) {
    	ShowsTable.setEditable(true);
    	
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
		time.setCellFactory(TextFieldTableCell.forTableColumn());
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
		
		time.setOnEditCommit(new EventHandler <CellEditEvent<Show, String>>() {
            @Override
            public void handle(CellEditEvent<Show, String> time) {
            	synchronized(CinemaClient.ShowsDataLock) {
	            	int show_id = ((Show) time.getTableView().getItems().get(
	                        time.getTablePosition().getRow()))
	                        .getID();
	            	String NewTime_Str = time.getNewValue();
	            	try {
	            		LocalTime NewTime = LocalTime.parse(NewTime_Str);
	            		try {
	            			ChangeShowTime(show_id, NewTime);
	            			MessageBoundaryEmployee.displayInfo("Show's time successfully updated.");
	            		}catch(Exception e) {
	            			MessageBoundaryEmployee.displayError("An error occured. Show's time couldn't be updated.");
	            		}
	            		
	            	} catch (DateTimeParseException e){	// invalid time input
	            		MessageBoundaryEmployee.displayError("Time must be of the form HH:MM.");
	            	}finally {
	            		// set items in table
	            		ObservableList<Show> DataList = FXCollections.observableArrayList(CinemaClient.ShowsData);
	            		ShowsTable.setItems(DataList);
	            	}
            	}
            }
    	});
		
		// update ShowData
		org.example.Boundaries.Boundary.UpdateShowsData();
		
		// set items in table
		ObservableList<Show> DataList = FXCollections.observableArrayList(CinemaClient.ShowsData);
		ShowsTable.setItems(DataList);
	}
}

