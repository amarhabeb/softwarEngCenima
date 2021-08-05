package org.example.Boundaries;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
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
public class ChainManagerMB extends EmployeeMainBoundary implements Initializable, Serializable{

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
    void clickViewPriceUpdatingRequestsBtn(ActionEvent event) throws IOException {
    	App.setRoot("PriceUpdatingRequestsBoundary",params, stage);
    }

    @FXML
    void clickViewReportBtn(ActionEvent event) throws IOException {
    	Integer month = monthChoice.getValue();
  		Integer year = yearChoice.getValue();
  		Cinema cinema = cinemaChoice.getValue();
  		String report_type = report_typeChoice.getValue();
  		
  		List<Object> params = new LinkedList<Object>();
  		params.add(report_type);
  		params.add(month);
  		params.add(year);
  		params.add(cinema);
  		params.add("ChainManagerMB");
  		
  		// pass selected paramaters to view boundary
    	params.add(employee);
  		App.setRoot("ViewReportBoundary",params, stage);
    }
    
    void CheckIfFilled() {
  		// if all choiceboxes were filled
  		if(report_typeChoice.getValue()!= null && monthChoice.getValue()!= null && 
  				yearChoice.getValue()!= null) {
  			if(report_typeChoice.getValue()=="Tickets Sales") {
  				if(cinemaChoice.getValue()!=null) {
  					viewReportBtn.setDisable(false);
  				}else {
  					viewReportBtn.setDisable(true);
  				}
  			}else {
  				viewReportBtn.setDisable(false);
  			}
  		}
  		else {
  			viewReportBtn.setDisable(true);
  		}
  	}
   

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		// initialize month and year choice boxes
		// all months available (1-12)
		for (int month = 1; month<=12; month++) {
        	monthChoice.getItems().add(month);
        }
		// avaialble years are the past 10 years
    	for (int year = 0; year<=10; year++) {
        	yearChoice.getItems().add(LocalDate.now().getYear()-year);
        }
    	
    	cinemaChoice.setDisable(true);
    	
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
    	    // initialize cinema choice box
    	    String type = report_typeChoice.getValue();
    	    if(type=="Tickets Sales") {
    	    	synchronized(CinemaClient.CinemasDataLock) {
	    	    	// update data
	    			org.example.Boundaries.Boundary.UpdateCinemasData();
	    	    	cinemaChoice.setDisable(false);
	    	    	cinemaChoice.getItems().clear();
	    	    	// initialize cinema choice box
	    	    	for (Cinema cinema:CinemaClient.CinemasData) {
	    	        	cinemaChoice.getItems().add(cinema);
	    	        }
    	    	}
    	    }
    	    else {
    	    	cinemaChoice.getItems().clear();
    	    	cinemaChoice.setDisable(true);
    	    }
    	});
    	monthChoice.setOnAction((event) -> {
    	    CheckIfFilled();
    	});
    	yearChoice.setOnAction((event) -> {
    	    CheckIfFilled();
    	});
	}

}

