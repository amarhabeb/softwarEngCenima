package org.example;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.io.Serializable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import org.example.entities.Show;


@SuppressWarnings("serial")
public class ContentManagerMB extends EmployeeMainBoundary implements Initializable, Serializable{
	
	@FXML // fx:id="refreshBtn"
    private Button refreshBtn; // Value injected by FXMLLoader

	@FXML // fx:id="UpdateShowsTimesBtn"
	private Button UpdateShowsTimesBtn; // Value injected by FXMLLoader
	
	@FXML
    private Button UpdateShowsPricesBtn;

    @FXML
    private Button AddShowsBtn;

    @FXML
    private Button RemoveShowsBtn;

    @FXML
    private Button AddMoviesBtn;

    @FXML
    private Button RemoveMoviesBtn;
	
    
	@FXML private TableView<Show> ShowsTable;
	@FXML private TableColumn<Show, String> movie_name;
	@FXML private TableColumn<Show, String> date;
	@FXML private TableColumn<Show, String> time;
	@FXML private TableColumn<Show, Integer> hall_number;
	@FXML private TableColumn<Show, Boolean> online;
	@FXML private TableColumn<Show, Double> price;
    @FXML private ImageView Background;
	
	
	// brings the Shows from the DataBase and updates the ShowsData local list
	void UpdateShowsData() {
		// add message to ClientInput so it could be sent to server
		LinkedList<Object> message = new LinkedList<Object>();
		message.add("LoadShows");
		synchronized(CinemaClient.ShowsDataLock)
		{	
			CinemaClientCLI.sendMessage(message);
						
			// wait for Data to be updated
			while(!CinemaClient.ShowsDataUpdated) {
				try {
						CinemaClient.ShowsDataLock.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}	
		}	
	}
	
		
	
	@FXML
    void clickRefreshBtn(ActionEvent event) {
		if(!CinemaClient.ShowsDataUpdated) {
			UpdateShowsData();
    	}
		// set items in table
		ObservableList<Show> DataList = FXCollections.observableArrayList(CinemaClient.ShowsData);
		ShowsTable.setItems(DataList);
	}

	@FXML
	void clickUpdateShowsTimesBtn(ActionEvent event) throws IOException {
		 App.setRoot("UpdateTimeBoundary");
	}
	
	@FXML
    void clickAddMoviesBtn(ActionEvent event) {

    }

    @FXML
    void clickAddShowsBtn(ActionEvent event) {

    }
    
    @FXML
    void clickRemoveMoviesBtn(ActionEvent event) {

    }

    @FXML
    void clickRemoveShowsBtn(ActionEvent event) {

    }

    @FXML
    void clickUpdateShowsPricesBtn(ActionEvent event) throws IOException{
    	App.setRoot("UpdatePriceBoundary");
    }
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// set-up the columns in the table
		movie_name.setCellValueFactory(new PropertyValueFactory<Show, String>("movie_name"));
		date.setCellValueFactory(new PropertyValueFactory<Show, String>("date"));
		time.setCellValueFactory(new PropertyValueFactory<Show, String>("time"));
		hall_number.setCellValueFactory(new PropertyValueFactory<Show, Integer>("hall_number"));
		online.setCellValueFactory(new PropertyValueFactory<Show, Boolean>("online"));
		price.setCellValueFactory(new PropertyValueFactory<Show, Double>("price"));
		
		System.out.println("ShowDataUpdated: "+CinemaClient.ShowsDataUpdated);
		if(!CinemaClient.ShowsDataUpdated) {
			UpdateShowsData();
		}
		// set items in table
		ObservableList<Show> DataList = FXCollections.observableArrayList(CinemaClient.ShowsData);
		ShowsTable.setItems(DataList);
	}
}
