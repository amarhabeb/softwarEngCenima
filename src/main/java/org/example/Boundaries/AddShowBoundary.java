package org.example.Boundaries;

import java.awt.event.ActionEvent;
import javafx.event.EventHandler;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import org.example.App;
import org.example.OCSF.CinemaClient;
import org.example.OCSF.CinemaClientCLI;
import org.example.entities.Cinema;
import org.example.entities.Hall;
import org.example.entities.Movie;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

@SuppressWarnings("serial")
public class AddShowBoundary implements Initializable, Serializable{

    @FXML // fx:id="GoBackToMainBtn"
    private Button GoBackToMainBtn; // Value injected by FXMLLoader

    @FXML // fx:id="movieChoice"
    private ChoiceBox<Movie> movieChoice; // Value injected by FXMLLoader

    @FXML // fx:id="dayChoice"
    private ChoiceBox<Integer> dayChoice; // Value injected by FXMLLoader

    @FXML // fx:id="monthChoice"
    private ChoiceBox<Integer> monthChoice; // Value injected by FXMLLoader

    @FXML // fx:id="hoursChoice"
    private ChoiceBox<String> hoursChoice; // Value injected by FXMLLoader

    @FXML // fx:id="minsChoice"
    private ChoiceBox<String> minsChoice; // Value injected by FXMLLoader

    @FXML // fx:id="yearChoice"
    private ChoiceBox<Integer> yearChoice; // Value injected by FXMLLoader

    @FXML // fx:id="onlineChoice"
    private ChoiceBox<Boolean> onlineChoice; // Value injected by FXMLLoader

    @FXML // fx:id="priceTextField"
    private TextField priceTextField; // Value injected by FXMLLoader

    @FXML // fx:id="cinemaChoice"
    private ChoiceBox<Cinema> cinemaChoice; // Value injected by FXMLLoader

    @FXML // fx:id="hallChoice"
    private ChoiceBox<Hall> hallChoice; // Value injected by FXMLLoader

    @FXML // fx:id="AddShowBtn"
    private Button AddShowBtn; // Value injected by FXMLLoader

    @FXML
    void clickAddShowBtn(ActionEvent event) throws IOException {
    	Movie movie = movieChoice.getValue();
    	Integer day = dayChoice.getValue();
  		Integer month = monthChoice.getValue();
  		Integer year = yearChoice.getValue();
  		String hour = hoursChoice.getValue();
  		String min = minsChoice.getValue();
  		Boolean online = onlineChoice.getValue();
  		Hall hall = hallChoice.getValue();
  		Cinema cinema = cinemaChoice.getValue();
  		Double price = Double.valueOf(priceTextField.getText());
    }

    @FXML
    void clickGoBackToMainBtn(ActionEvent event) throws IOException {
    	App.setRoot("ContentManagerMB");
    }
    
 // brings the Movies from the DataBase and updates the MoviesData local list
 	void UpdateMoviesData() {
 		// add message to ClientInput so it could be sent to server
 		LinkedList<Object> message = new LinkedList<Object>();
 		message.add("LoadMovies");
 		synchronized(CinemaClient.MoviesDataLock)
 		{	
 			CinemaClientCLI.sendMessage(message);
 							
 			// wait for Data to be updated
 			while(!CinemaClient.MoviesDataUpdated) {
 				try {
 						CinemaClient.MoviesDataLock.wait();
 					} catch (InterruptedException e) {
 						// TODO Auto-generated catch block
 						e.printStackTrace();
 					}
 			}	
 		}	
 	}
 	
 // brings the Halls from the DataBase and updates the MoviesData local list
  	void UpdateHallsData() {
  		// add message to ClientInput so it could be sent to server
  		LinkedList<Object> message = new LinkedList<Object>();
  		message.add("LoadHalls");
  		synchronized(CinemaClient.HallsDataLock)
  		{	
  			CinemaClientCLI.sendMessage(message);
  							
  			// wait for Data to be updated
  			while(!CinemaClient.HallsDataUpdated) {
  				try {
  						CinemaClient.HallsDataLock.wait();
  					} catch (InterruptedException e) {
  						// TODO Auto-generated catch block
  						e.printStackTrace();
  					}
  			}	
  		}	
  	}
  	
 // brings the Movies from the DataBase and updates the CinemasData local list
  	void UpdateCinemasData() {
  		// add message to ClientInput so it could be sent to server
  		LinkedList<Object> message = new LinkedList<Object>();
  		message.add("LoadCinemas");
  		synchronized(CinemaClient.CinemasDataLock)
  		{	
  			CinemaClientCLI.sendMessage(message);
  							
  			// wait for Data to be updated
  			while(!CinemaClient.CinemasDataUpdated) {
  				try {
  						CinemaClient.CinemasDataLock.wait();
  					} catch (InterruptedException e) {
  						// TODO Auto-generated catch block
  						e.printStackTrace();
  					}
  			}	
  		}	
  	}

  	void CheckIfFilled() {
  		// if all choiceboxes were filled
  		if(movieChoice.getValue()!= null && dayChoice.getValue()!= null &&
  				monthChoice.getValue()!= null && yearChoice.getValue()!= null &&
  				hoursChoice.getValue()!= null && minsChoice.getValue()!= null &&
  				onlineChoice.getValue()!= null && hallChoice.getValue()!= null &&
  				cinemaChoice.getValue()!= null && priceTextField.getText()!=null) {
  			AddShowBtn.setDisable(true);
  		}
  		else {
  			AddShowBtn.setDisable(false);
  		}
  	}
   
  	@Override
	public void initialize(URL url, ResourceBundle rb) {
  		
		// update MoviesData if necessary
		if(!CinemaClient.MoviesDataUpdated) {
			UpdateMoviesData();
		}
		if(!CinemaClient.HallsDataUpdated) {
			UpdateHallsData();
		}
		if(!CinemaClient.CinemasDataUpdated) {
			UpdateCinemasData();
		}
		
    	// initialize movie choice box
    	for (Movie movie:CinemaClient.MoviesData) {
        	movieChoice.getItems().add(movie);
        }
    	
    	// initialize date choice boxes
    	for (int day = 1; day<=31; day++) {
        	dayChoice.getItems().add(day);
        }
    	for (int month = 1; month<=12; month++) {
        	monthChoice.getItems().add(month);
        }
    	for (int year = 0; year<=10; year++) {
        	yearChoice.getItems().add(2021+year);
        }
    	
    	// initialize time choice boxes
    	for (int hour = 0; hour<=23; hour++) {
    		String hour_str = Integer.toString(hour);
    		if(hour<=9)
    			hour_str = "0" + hour_str;
        	hoursChoice.getItems().add(hour_str);
        }
    	for (int min = 0; min<=59; min++) {
    		String min_str = Integer.toString(min);
    		if(min<=9)
    			min_str = "0" + min_str;
        	minsChoice.getItems().add(min_str);
        }
    	
    	//Initialize online choice box
    	onlineChoice.getItems().addAll(true, false);
    	onlineChoice.setValue(false);	// default value
    	
    	// initialize hall choice box
    	for (Hall hall:CinemaClient.HallsData) {
        	hallChoice.getItems().add(hall);
        }
    	
    	// initialize cinema choice box
    	for (Cinema cinema:CinemaClient.CinemasData) {
        	cinemaChoice.getItems().add(cinema);
        }
    	
    	// disable button
  		CheckIfFilled();
    	
    	//choice box listener
    	movieChoice.setOnAction((event) -> {
    	    CheckIfFilled();
    	});
    	dayChoice.setOnAction((event) -> {
    	    CheckIfFilled();
    	});
    	monthChoice.setOnAction((event) -> {
    	    CheckIfFilled();
    	});
    	yearChoice.setOnAction((event) -> {
    	    CheckIfFilled();
    	});
    	hoursChoice.setOnAction((event) -> {
    	    CheckIfFilled();
    	});
    	minsChoice.setOnAction((event) -> {
    	    CheckIfFilled();
    	});
    	onlineChoice.setOnAction((event) -> {
    	    CheckIfFilled();
    	});
    	hallChoice.setOnAction((event) -> {
    	    CheckIfFilled();
    	});
    	cinemaChoice.setOnAction((event) -> {
    	    CheckIfFilled();
    	});
    	priceTextField.textProperty().addListener((observable, oldValue, newValue) -> {
    		double val;
    		try {
    			val = Double.valueOf(newValue);
    		}catch (NumberFormatException e) {
				val = -1;	
			}finally {
				if(val<0) {	// invalid input
	    			//* CODE FOR MESSAGE POP-UP *//
					priceTextField.setText(null);
	    		}
				CheckIfFilled();
			}
    	});
    }

}
