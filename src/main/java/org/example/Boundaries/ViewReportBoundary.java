package org.example.Boundaries;

import java.io.Serializable;
import java.net.URL;
import java.util.ResourceBundle;

import org.example.entities.Cinema;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

@SuppressWarnings("serial")
public class ViewReportBoundary extends EmployeeMainBoundary implements Initializable, Serializable{
	
	@FXML // fx:id="title"
    private Label title; // Value injected by FXMLLoader

    @FXML // fx:id="GoBackToMainBtn"
    private Button GoBackToMainBtn; // Value injected by FXMLLoader

    @FXML
    void clickGoBackToMainBtn(ActionEvent event) {

    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		// get passsed parameters
		Integer month = (Integer) params.get(1);
  		Integer year = (Integer) params.get(2);
  		Cinema cinema = (Cinema) params.get(3);
  		String report_type = (String) params.get(0);
  		
  		// set title according to chosen report type
  		title.setText(report_type);
	}

}
