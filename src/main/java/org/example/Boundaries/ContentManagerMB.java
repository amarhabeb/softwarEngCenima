package org.example.Boundaries;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.io.Serializable;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import org.example.App;
import org.example.OCSF.CinemaClient;
import org.example.OCSF.CinemaClientCLI;
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
	@FXML private TableColumn<Show, Double> price;
	@FXML private TableColumn<Show, String> cinema;
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
		synchronized(CinemaClient.ShowsDataLock) {
			UpdateShowsData();
			// set items in table
			ObservableList<Show> DataList = FXCollections.observableArrayList(CinemaClient.ShowsData);
			ShowsTable.setItems(DataList);
		}
	}

	@FXML
	void clickUpdateShowsTimesBtn(ActionEvent event) throws IOException {
		 App.setRoot("UpdateTimeBoundary",null);
	}
	
	@FXML
    void clickAddMoviesBtn(ActionEvent event) {

    }

    @FXML
    void clickAddShowsBtn(ActionEvent event) throws IOException{
    	App.setRoot("AddShowBoundary",null);
    }
    
    @FXML
    void clickRemoveMoviesBtn(ActionEvent event) throws IOException {
    	App.setRoot("DeleteShowBoundary",null);
    }

    @FXML
    void clickRemoveShowsBtn(ActionEvent event) {

    }

    @FXML
    void clickUpdateShowsPricesBtn(ActionEvent event) throws IOException{
    	App.setRoot("UpdatePriceBoundary",null);
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
		
		synchronized(CinemaClient.ShowsDataLock) {
			UpdateShowsData();
			// set items in table
			ObservableList<Show> DataList = FXCollections.observableArrayList(CinemaClient.ShowsData);
			ShowsTable.setItems(DataList);
		}
	}
}
