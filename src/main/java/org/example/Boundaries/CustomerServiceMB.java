package org.example.Boundaries;

import java.io.Serializable;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.net.URL;
import java.util.ResourceBundle;

import org.example.OCSF.CinemaServer;

import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;


@SuppressWarnings("serial")
public class CustomerServiceMB extends EmployeeMainBoundary implements Initializable, Serializable{
	
	@FXML // fx:id="ViewComplaintsBtn"
    private Button ViewComplaintsBtn; // Value injected by FXMLLoader

    @FXML // fx:id="NoLimitations"
    private CheckBox NoLimitations; // Value injected by FXMLLoader

    @FXML // fx:id="Y_TextField"
    private TextField Y_TextField; // Value injected by FXMLLoader

    @FXML // fx:id="ApplyUpdatesBtn"
    private Button ApplyUpdatesBtn; // Value injected by FXMLLoader

    @FXML
    void clickApplyUpdatesBtn(ActionEvent event) {
    	// update current regs according to input
    	if(NoLimitations.isSelected()) {
    		CinemaServer.currentRegs.setStatus(false);
    	}else {
    		CinemaServer.currentRegs.setStatus(true);
    		CinemaServer.currentRegs.setY(Integer.valueOf(Y_TextField.getText()));
    	}
    	MessageBoundaryEmployee.displayInfo("Regulations updated.");
    	
    	// reset textfield and checkbox
    	if(CinemaServer.currentRegs.getStatus()) {
			Y_TextField.setText(Double.toString(CinemaServer.currentRegs.getY()));	// initialize the textfiled
		}
		else {
			NoLimitations.setSelected(true);	// check the checkbox
			Y_TextField.setDisable(true);	// disable textfield
		}
    }

    @FXML
    void clickNoLimitations(ActionEvent event) {
    	if(NoLimitations.isSelected()) {
    		Y_TextField.setDisable(true);	// disable textfield
		}
		else {
			Y_TextField.setDisable(false);	// enable textfield
			Y_TextField.setText(Double.toString(CinemaServer.currentRegs.getY()));	// initialize the textfiled
		}
    	CheckIfFilled();
		
    }

    @FXML
    void clickViewComplaintsBtn(ActionEvent event) {

    }
    
    void CheckIfFilled() {
  		// if all choiceboxes were filled
  		if(Y_TextField.getText()!= null || NoLimitations.isSelected()) {
  			ApplyUpdatesBtn.setDisable(false);
  		}
  		else {
  			ApplyUpdatesBtn.setDisable(true);
  		}
  	}
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		if(CinemaServer.currentRegs.getStatus()) {
			Y_TextField.setText(Double.toString(CinemaServer.currentRegs.getY()));	// initialize the textfiled
		}
		else {
			NoLimitations.setSelected(true);	// check the checkbox
			Y_TextField.setDisable(true);	// disable textfield
		}
		
		Y_TextField.textProperty().addListener((observable, oldValue, newValue) -> {
    		double val=0;
    		try {
    			val = Double.valueOf(newValue);
    		}catch (NumberFormatException e) {
				val = -1;	
			}finally {
				if(val<0) {	// invalid input
					MessageBoundaryEmployee.displayError("Value must be a non-negative number.");
					Y_TextField.setText(null);
	    		}
				CheckIfFilled();
			}
    	});
		
	}

}
