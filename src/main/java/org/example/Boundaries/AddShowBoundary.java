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
import java.time.LocalDateTime;

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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

@SuppressWarnings("serial")
public class AddShowBoundary extends ContentManagerDisplayBoundary implements Initializable, Serializable{

    @FXML // fx:id="GoBackToMainBtn"
    private Button GoBackToMainBtn; // Value injected by FXMLLoader

    @FXML // fx:id="movieChoice"
    private ChoiceBox<Movie> movieChoice; // Value injected by FXMLLoader

    @FXML // fx:id="hoursChoice"
    private ChoiceBox<Integer> hoursChoice; // Value injected by FXMLLoader

    @FXML // fx:id="minsChoice"
    private ChoiceBox<Integer> minsChoice; // Value injected by FXMLLoader

    @FXML // fx:id="priceTextField"
    private TextField priceTextField; // Value injected by FXMLLoader

    @FXML // fx:id="cinemaChoice"
    private ChoiceBox<Cinema> cinemaChoice; // Value injected by FXMLLoader

    @FXML // fx:id="hallChoice"
    private ChoiceBox<Hall> hallChoice; // Value injected by FXMLLoader

    @FXML // fx:id="AddShowBtn"
    private Button AddShowBtn; // Value injected by FXMLLoader
    
    @FXML // fx:id="date"
    private DatePicker datePicker; // Value injected by FXMLLoader

    
    public static Boolean ShowAdded = true;	// holds if the show is added yet
    // add show in DataBase and brings the Shows from the DataBase and updates 
 	// the ShowsData local list
    void AddShow(Show show) {
		ShowAdded = false;	// show isn't added yet
		// create message and send it to the server
    	LinkedList<Object> message = new LinkedList<Object>();
		message.add("AddShow");
		message.add(show);
		synchronized(CinemaClient.ShowsDataLock)
		{	
			CinemaClientCLI.sendMessage(message);
							
			// wait for Data to be changed
			while(!ShowAdded) {
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
    
    void resetChoiceBoxes() {
    	// disable button
  		CheckIfFilled();
  		// reset choices
    	movieChoice.setValue(null);
    	datePicker.setValue(null);	
  		hoursChoice.setValue(null);
  		minsChoice.setValue(null);  		
  		hallChoice.setValue(null);
  		cinemaChoice.setValue(null);
  		priceTextField.setText(null);
  		
  		hallChoice.setDisable(true);
    }
    
    @FXML
    void clickAddShowBtn(ActionEvent event) throws IOException {
    	// get chosen attributes
    	Movie movie = movieChoice.getValue();
    	LocalDate date = datePicker.getValue();  		
  		Integer hour = hoursChoice.getValue();
  		Integer min = minsChoice.getValue();
  		LocalTime time = LocalTime.of(hour, min);	// create time object
  		Hall hall = hallChoice.getValue();
  		// Cinema cinema = cinemaChoice.getValue();
  		Double price = Double.valueOf(priceTextField.getText());
  		
  		// create show object
  		Show show = new Show(LocalDateTime.of(date, time), "AVAILABLE", price, movie, hall);
  		
  		try {
	  		// add new show object to database
	  		AddShow(show);
	  		MessageBoundaryEmployee.displayInfo("Show successfully added.");
  		}catch(Exception e) {
  			MessageBoundaryEmployee.displayError("An error occured. Show couldn't be added.");
  		}
		
		resetChoiceBoxes();
		// update data
		UpdateMoviesData();
		UpdateHallsData();
		UpdateCinemasData();
    }

    @FXML
    void clickGoBackToMainBtn(ActionEvent event) throws IOException {
    	App.setRoot("ContentManagerMB",null);
    }

  	void CheckIfFilled() {
  		// if all choiceboxes were filled
  		if(movieChoice.getValue()!= null && datePicker.getValue()!= null &&
  				hoursChoice.getValue()!= null && minsChoice.getValue()!= null &&
  				hallChoice.getValue()!= null &&
  				cinemaChoice.getValue()!= null && priceTextField.getText()!=null) {
  			AddShowBtn.setDisable(false);
  		}
  		else {
  			AddShowBtn.setDisable(true);
  		}
  	}
   
  	@Override
	public void initialize(URL url, ResourceBundle rb) {
  		
		// update data
		UpdateMoviesData();
		UpdateHallsData();
		UpdateCinemasData();
		
    	// initialize movie choice box
    	for (Movie movie:CinemaClient.MoviesData) {
        	movieChoice.getItems().add(movie);
        }
    	
    	// initialize time choice boxes
    	for (int hour = 0; hour<=23; hour++) {
        	hoursChoice.getItems().add(hour);
        }
    	for (int min = 0; min<=59; min++) {
        	minsChoice.getItems().add(min);
        }
    	
    	// initialize cinema choice box
    	for (Cinema cinema:CinemaClient.CinemasData) {
        	cinemaChoice.getItems().add(cinema);
        }
    	
    	// disable button
  		CheckIfFilled();
  		hallChoice.setDisable(true);
    	
    	//choice box listener
    	movieChoice.setOnAction((event) -> {
    	    CheckIfFilled();
    	});
    	datePicker.setOnAction((event) -> {
    	    CheckIfFilled();
    	});
    	hoursChoice.setOnAction((event) -> {
    	    CheckIfFilled();
    	});
    	minsChoice.setOnAction((event) -> {
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
    	    	hallChoice.setDisable(false);
    	    	List<Hall> cinemas_halls = new LinkedList<Hall>();
    	    	for(Hall hall:CinemaClient.HallsData) {	// if hall is in chosen cinema then add it
    	    		if(hall.getCinema()==cinema) {
    	    			cinemas_halls.add(hall);
    	    		}
    	    	}
	            hallChoice.setItems((ObservableList<Hall>) cinemas_halls);
    	    }
    	    else {
    	    	hallChoice.setItems(null);
    	    	hallChoice.setDisable(true);
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
					MessageBoundaryEmployee.displayError("Price must be a non-negative number.");
					priceTextField.setText(null);
	    		}
				CheckIfFilled();
			}
    	});
    }

}
