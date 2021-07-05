package org.example.Boundaries;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;


import javafx.fxml.Initializable;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.io.Serializable;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import org.example.App;
import org.example.OCSF.CinemaClient;
import org.example.OCSF.CinemaClientCLI;
import org.example.entities.Show;
import org.example.entities.UpdatePriceRequest;


@SuppressWarnings("serial")
public class UpdatePriceBoundary extends ContentManagerDisplayBoundary implements Initializable, Serializable{
	
	@FXML private TableView<Show> ShowsTable;
	@FXML private TableColumn<Show, String> movie_name;
	@FXML private TableColumn<Show, String> date;
	@FXML private TableColumn<Show, String> time;
	@FXML private TableColumn<Show, Integer> hall_number;
	@FXML private TableColumn<Show, Double> price;
	@FXML private TableColumn<Show, String> cinema;
	
	@FXML // fx:id="refreshBtn2"
    private Button refreshBtn2; // Value injected by FXMLLoader
	 
	public static Boolean ShowsPriceChanged = true;	// holds if the shows price is changed yet
	// send request for price updating to the server, which will notify the chain manager
	synchronized void ChangeShowPrice(int show_id, Double NewPrice) {
		// create message and send it to the server
    	LinkedList<Object> message = new LinkedList<Object>();
		message.add("ChangePriceRequest");
		UpdatePriceRequest updatePriceReq = new UpdatePriceRequest(null, show_id, NewPrice);
		message.add(updatePriceReq);
		CinemaClientCLI.sendMessage(message);
	}
	
    @FXML
    void clickRefreshBtn2(ActionEvent event) {
    	UpdateShowsData();
		// set items in table
		ObservableList<Show> DataList = FXCollections.observableArrayList(CinemaClient.ShowsData);
		ShowsTable.setItems(DataList);
		System.out.println("refreshed");
    }
    
    @FXML // fx:id="GoBackToMainBtn"
    private Button GoBackToMainBtn; // Value injected by FXMLLoader

    @FXML
    void clickGoBackToMainBtn(ActionEvent event)  throws IOException {
    	App.setRoot("ContentManagerMB",null);
    }
    
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
		hall_number.setCellValueFactory(new Callback<CellDataFeatures<Show, Integer>, ObservableValue<Integer>>() {
		     public ObservableValue<Integer> call(CellDataFeatures<Show, Integer> show) {
		         return (new SimpleIntegerProperty(show.getValue().getHall().getNumber()).asObject());
		     }
		  });
		price.setCellValueFactory(new PropertyValueFactory<Show, Double>("price"));
		price.setCellFactory(TextFieldTableCell.<Show, Double>forTableColumn(new DoubleStringConverter()));
		cinema.setCellValueFactory(new Callback<CellDataFeatures<Show, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<Show, String> show) {
		         return (new SimpleStringProperty(show.getValue().getHall().getCinema().getBranch_name()));
		     }
		  });
		
		price.setOnEditCommit(new EventHandler <CellEditEvent<Show, Double>>() {
            @Override
            public void handle(CellEditEvent<Show, Double> price) {
            	int show_id = ((Show) price.getTableView().getItems().get(
                        price.getTablePosition().getRow()))
                        .getID();
            	try {
            		double NewPrice = price.getNewValue();
            		try {
                		ChangeShowPrice(show_id, NewPrice);
            			MessageBoundaryEmployee.displayInfo("Show's price successfully updated.");
            		}catch (Exception e) {	// server threw exception while trying to update
            			MessageBoundaryEmployee.displayError("An error occured. Show's price couldn't be updated.");
            		}
            	}catch(NumberFormatException e) {
            		MessageBoundaryEmployee.displayError("Price must be a non-negative number.");
            	}
            	
            	// set items in table
        		ObservableList<Show> DataList = FXCollections.observableArrayList(CinemaClient.ShowsData);
        		ShowsTable.setItems(DataList);
            }
    	});
		
		// update ShowData
		UpdateShowsData();
		// set items in table
		ObservableList<Show> DataList = FXCollections.observableArrayList(CinemaClient.ShowsData);
		ShowsTable.setItems(DataList);
	}
}
