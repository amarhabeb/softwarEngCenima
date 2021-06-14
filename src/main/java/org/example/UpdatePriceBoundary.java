package org.example;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.util.converter.DoubleStringConverter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.io.Serializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;


@SuppressWarnings("serial")
public class UpdatePriceBoundary implements Initializable, Serializable{
	@FXML private TableView<Show> ShowsTable;
	@FXML private TableColumn<Show, String> movie_name;
	@FXML private TableColumn<Show, String> date;
	@FXML private TableColumn<Show, String> time;
	@FXML private TableColumn<Show, Integer> hall_number;
	@FXML private TableColumn<Show, Boolean> online;
	@FXML private TableColumn<Show, Double> price;
	
	@FXML // fx:id="refreshBtn2"
    private Button refreshBtn2; // Value injected by FXMLLoader
	 
	static Boolean ShowsPriceChanged = true;	// holds if the shows time is changed yet
	// change time of show in DataBase and brings the Shows from the DataBase and updates 
	// the ShowsData local list
	void ChangeShowPrice(int show_id, Double NewPrice) {
		// create message and send it to the server
    	LinkedList<Object> message = new LinkedList<Object>();
		message.add("ChangePriceRequest");
		UpdatePriceRequest updatePriceReq = new UpdatePriceRequest(null, show_id, NewPrice);
		message.add(updatePriceReq);
		EmployeeClientCLI.sendMessage(message);
	}
	
	// brings the Shows from the DataBase and updates the ShowsData local list
	void UpdateShowsData() {
		// add message to ClientInput so it could be sent to server
		LinkedList<Object> message = new LinkedList<Object>();
		message.add("LoadShows");
		synchronized(EmployeeClient.ShowsDataLock)
		{	
			EmployeeClientCLI.sendMessage(message);
							
			// wait for Data to be updated
			while(!EmployeeClient.ShowsDataUpdated) {
				try {
						EmployeeClient.ShowsDataLock.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}	
		}	
	}
	
    @FXML
    void clickRefreshBtn2(ActionEvent event) {
    	if(!EmployeeClient.ShowsDataUpdated) {
    		UpdateShowsData();
    	}
		// set items in table
		ObservableList<Show> DataList = FXCollections.observableArrayList(EmployeeClient.ShowsData);
		ShowsTable.setItems(DataList);
		System.out.println("refreshed");
    }
    
    @FXML // fx:id="GoBackToMainBtn"
    private Button GoBackToMainBtn; // Value injected by FXMLLoader

    @FXML
    void clickGoBackToMainBtn(ActionEvent event)  throws IOException {
    	App.setRoot("ContentManagerMB");
    }
    
    @FXML
    void editTime(ActionEvent event) {
    	
    }

    
    @Override
	public void initialize(URL url, ResourceBundle rb) {
    	ShowsTable.setEditable(true);
    	
		// set-up the columns in the table
		movie_name.setCellValueFactory(new PropertyValueFactory<Show, String>("movie_name"));
		date.setCellValueFactory(new PropertyValueFactory<Show, String>("date"));
		time.setCellValueFactory(new PropertyValueFactory<Show, String>("time"));
		hall_number.setCellValueFactory(new PropertyValueFactory<Show, Integer>("hall_number"));
		online.setCellValueFactory(new PropertyValueFactory<Show, Boolean>("online"));
		price.setCellValueFactory(new PropertyValueFactory<Show, Double>("price"));
		price.setCellFactory(TextFieldTableCell.<Show, Double>forTableColumn(new DoubleStringConverter()));
		
		price.setOnEditCommit(new EventHandler <CellEditEvent<Show, Double>>() {
            @Override
            public void handle(CellEditEvent<Show, Double> price) {
            	int show_id = ((Show) price.getTableView().getItems().get(
                        price.getTablePosition().getRow()))
                        .getID();
            	double NewPrice = price.getNewValue();
            	
            	ChangeShowPrice(show_id, NewPrice);
            	
            	// CODE FOR WINDOW POPUP THAT SAYS "UPDATING PRICE REQUEST WAS SENT" //
            	
            	// set items in table
        		ObservableList<Show> DataList = FXCollections.observableArrayList(EmployeeClient.ShowsData);
        		ShowsTable.setItems(DataList);
            }
    	});
		
		System.out.println("ShowDataUpdated: "+EmployeeClient.ShowsDataUpdated);
		// update ShowData if necessary
		if(!EmployeeClient.ShowsDataUpdated) {
			UpdateShowsData();
		}
		// set items in table
		ObservableList<Show> DataList = FXCollections.observableArrayList(EmployeeClient.ShowsData);
		ShowsTable.setItems(DataList);
	}
}
