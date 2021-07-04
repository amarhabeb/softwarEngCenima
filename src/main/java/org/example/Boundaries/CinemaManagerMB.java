package org.example.Boundaries;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

import org.example.App;
import org.example.OCSF.CinemaClient;
import org.example.entities.Cinema;
import org.example.entities.Hall;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

@SuppressWarnings("serial")
public class CinemaManagerMB extends EmployeeMainBoundary implements Initializable, Serializable{

    @FXML // fx:id="ViewPriceUpdatingRequestsBtn"
    private Button ViewPriceUpdatingRequestsBtn; // Value injected by FXMLLoader

    @FXML // fx:id="report_type"
    private ChoiceBox<String> report_typeChoice; // Value injected by FXMLLoader

    @FXML // fx:id="month"
    private ChoiceBox<Integer> monthChoice; // Value injected by FXMLLoader

    @FXML // fx:id="cinema"
    private ChoiceBox<Cinema> cinemaChoice; // Value injected by FXMLLoader

    @FXML // fx:id="viewReportBtn"
    private Button viewReportBtn; // Value injected by FXMLLoader

    @FXML // fx:id="year"
    private ChoiceBox<Integer> yearChoice; // Value injected by FXMLLoader

    @FXML
    void clickViewPriceUpdatingRequestsBtn(ActionEvent event) {

    }

    @FXML
    void clickViewReportBtn(ActionEvent event) throws IOException {
    	Integer month = monthChoice.getValue();
  		Integer year = yearChoice.getValue();
  		Cinema cinema = cinemaChoice.getValue();
  		String report_type = report_typeChoice.getValue();
  		
  		// *MISSING CODE* //
  		
  		App.setRoot("ViewReportBoundary");
    }
    
    void CheckIfFilled() {
  		// if all choiceboxes were filled
  		if(report_typeChoice.getValue()!= null && monthChoice.getValue()!= null && 
  				yearChoice.getValue()!= null && cinemaChoice.getValue()!= null) {
  			viewReportBtn.setDisable(false);
  		}
  		else {
  			viewReportBtn.setDisable(true);
  		}
  	}
   

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		// update data
		UpdateCinemasData();
		
		// initialize month and year choice boxes
		for (int month = 1; month<=12; month++) {
        	monthChoice.getItems().add(month);
        }
    	for (int year = 0; year<=10; year++) {
        	yearChoice.getItems().add(2021+year);
        }
    	
    	// initialize cinema choice box
    	for (Cinema cinema:CinemaClient.CinemasData) {
        	cinemaChoice.getItems().add(cinema);
        }
    	
    	// initialize report type choice box
        report_typeChoice.getItems().addAll("Tickets Sales", "Packages and Online Shows Sales", "Refunds", "Complaints");
        
        // disable button
  		CheckIfFilled();
    	
    	//choice box listener
    	cinemaChoice.setOnAction((event) -> {
    	    CheckIfFilled();
    	});
    	report_typeChoice.setOnAction((event) -> {
    	    CheckIfFilled();
    	});
    	monthChoice.setOnAction((event) -> {
    	    CheckIfFilled();
    	});
    	yearChoice.setOnAction((event) -> {
    	    CheckIfFilled();
    	});
	}

}

