package org.example.Boundaries;

import java.awt.event.ActionEvent;
import javafx.event.EventHandler;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.time.LocalTime;
import java.time.LocalDate;

import org.example.App;
import org.example.OCSF.CinemaClient;
import org.example.OCSF.CinemaClientCLI;
import org.example.entities.Cinema;
import org.example.entities.Hall;
import org.example.entities.Movie;
import org.example.entities.Show;

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
    private ChoiceBox<Integer> hoursChoice; // Value injected by FXMLLoader

    @FXML // fx:id="minsChoice"
    private ChoiceBox<Integer> minsChoice; // Value injected by FXMLLoader

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
    
    void resetChoiceBoxes() {
    	// disable button
  		CheckIfFilled();
  		// reset choices
    	movieChoice.setValue(null);
    	dayChoice.setValue(null);
  		monthChoice.setValue(null);
  		yearChoice.setValue(null);  		
  		hoursChoice.setValue(null);
  		minsChoice.setValue(null);  		
  		onlineChoice.setValue(false);
  		hallChoice.setValue(null);
  		cinemaChoice.setValue(null);
  		priceTextField.setText(null);
  		
  		hallChoice.setDisable(false);
    }
    
    @FXML
    void clickAddShowBtn(ActionEvent event) throws IOException {
    	// get chosen attributes
    	Movie movie = movieChoice.getValue();
    	
    	Integer day = dayChoice.getValue();
  		Integer month = monthChoice.getValue();
  		Integer year = yearChoice.getValue();
  		LocalDate date = LocalDate.of(year, month, day);	// create time object
  		
  		Integer hour = hoursChoice.getValue();
  		Integer min = minsChoice.getValue();
  		LocalTime time = LocalTime.of(hour, min);	// create time object
  		
  		Boolean online = onlineChoice.getValue();
  		Hall hall = hallChoice.getValue();
  		// Cinema cinema = cinemaChoice.getValue();
  		Double price = Double.valueOf(priceTextField.getText());
  		
  		// create show object
  		Show show = new Show(date, time, online, "AVAILABLE", price, movie, hall);
  		
  		// add new show object to database
  		// create message and send it to the server
    	LinkedList<Object> message = new LinkedList<Object>();
		message.add("AddShow");
		message.add(show);
		CinemaClientCLI.sendMessage(message);
		
		resetChoiceBoxes();
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
        	hoursChoice.getItems().add(hour);
        }
    	for (int min = 0; min<=59; min++) {
        	minsChoice.getItems().add(min);
        }
    	
    	//Initialize online choice box
    	onlineChoice.getItems().addAll(true, false);
    	onlineChoice.setValue(false);	// default value
    	
    	// initialize cinema choice box
    	for (Cinema cinema:CinemaClient.CinemasData) {
        	cinemaChoice.getItems().add(cinema);
        }
    	
    	// disable button
  		CheckIfFilled();
  		hallChoice.setDisable(false);
    	
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
    	    // initialize hall choice box
    	    Cinema cinema = cinemaChoice.getValue();
    	    if(cinema!=null) {
    	    	hallChoice.setDisable(true);
    	    	List<Hall> cinemas_halls = new LinkedList<Hall>();
    	    	for(Hall hall:CinemaClient.HallsData) {	// if hall is in chosen cinema then add it
    	    		if(hall.getCinema()==cinema) {
    	    			cinemas_halls.add(hall);
    	    		}
    	    	}
	            hallChoice.setItems(CinemaClient.HallsData);
    	    }
    	    else {
    	    	hallChoice.setItems(null);
    	    	hallChoice.setDisable(false);
    	    }
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
