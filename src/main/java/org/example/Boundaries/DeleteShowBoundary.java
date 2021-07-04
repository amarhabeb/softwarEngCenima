package org.example.Boundaries;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import org.example.App;
import org.example.OCSF.CinemaClient;
import org.example.OCSF.CinemaClientCLI;
import org.example.entities.Show;

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
	@FXML private TableColumn<Show, Boolean> online;
	@FXML private TableColumn<Show, Double> price;
	@FXML private TableColumn<Show, String> cinema;
	
	Show selected_show = null;

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
    
    public static Boolean ShowDeleted = true;	// holds if the show is deleted yet
    // delete show in DataBase and brings the Shows from the DataBase and updates 
 	// the ShowsData local list
    synchronized void DeleteShow(int show_id) {
		ShowDeleted = false;	// show isn't deleted yet
		// create message and send it to the server
    	LinkedList<Object> message = new LinkedList<Object>();
		message.add("DeleteShow");
		message.add(show_id);
		synchronized(CinemaClient.ShowsDataLock)
		{	
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
		}	
		// update ShowData
		UpdateShowsData();
	}
    
    @FXML
    void clickDeleteShowBtn(ActionEvent event) {
    	int show_id = selected_show.getID();
		
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

    @FXML
    void clickGoBackToMainBtn(ActionEvent event)  throws IOException {
    	App.setRoot("ContentManagerMB");
    }

    @FXML
    void clickRefreshBtn2(ActionEvent event) {
    	UpdateShowsData();
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
		online.setCellValueFactory(new PropertyValueFactory<Show, Boolean>("online"));
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
		
		// update ShowData
		UpdateShowsData();
		// set items in table
		ObservableList<Show> DataList = FXCollections.observableArrayList(CinemaClient.ShowsData);
		ShowsTable.setItems(DataList);
	}

}

