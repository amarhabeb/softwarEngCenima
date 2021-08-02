package org.example.Boundaries;

import java.io.IOException;
import java.io.Serializable;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import org.example.App;
import org.example.OCSF.CinemaClient;
import org.example.OCSF.CinemaClientCLI;
import org.example.OCSF.CinemaServer;
import org.example.entities.Regulations;
import org.example.entities.Show;

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
    
    
    public static Boolean RegsActivated = true;	// holds if the show is added yet
    // add show in DataBase and brings the Shows from the DataBase and updates 
 	// the ShowsData local list
    synchronized void Activate(Integer Y) {
    	RegsActivated = false;	// show isn't added yet
		// create message and send it to the server
    	LinkedList<Object> message = new LinkedList<Object>();
		message.add("ActivateRegulations");
		message.add(Y);
		synchronized(CinemaClient.RegulationsDataLock)
		{	
			CinemaClientCLI.sendMessage(message);
							
			// wait for Data to be changed
			while(!RegsActivated) {
				try {
					CinemaClient.RegulationsDataLock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
		}
		// update ShowData
		org.example.Boundaries.Boundary.UpdateRegulationsData();
	}
    
    
    public static Boolean RegsDeactivated = true;	// holds if the show is added yet
    // add show in DataBase and brings the Shows from the DataBase and updates 
 	// the ShowsData local list
    synchronized void Deactivate() {
		RegsDeactivated = false;	// show isn't added yet
		// create message and send it to the server
    	LinkedList<Object> message = new LinkedList<Object>();
		message.add("DeActivateRegulations");
		synchronized(CinemaClient.RegulationsDataLock)
		{	
			CinemaClientCLI.sendMessage(message);
							
			// wait for Data to be changed
			while(!RegsDeactivated) {
				try {
					CinemaClient.RegulationsDataLock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
		}
		// update ShowData
		org.example.Boundaries.Boundary.UpdateRegulationsData();
	}

    @FXML
    void clickApplyUpdatesBtn(ActionEvent event) {
    	// update current regs according to input
    	if(NoLimitations.isSelected()) {
    		Deactivate();
    	}else {
    		Activate(Integer.valueOf(Y_TextField.getText()));
    	}
    	MessageBoundaryEmployee.displayInfo("Regulations updated.");
    	
    	// reset textfield and checkbox
    	synchronized(CinemaClient.RegulationsDataLock) {
    		UpdateRegulationsData();
    		Regulations regs = CinemaClient.RegulationsData.get(0);
    		if(regs.getStatus()) {
    			Y_TextField.setText(Double.toString(regs.getY()));	// initialize the textfiled
    		}
    		else {
    			NoLimitations.setSelected(true);	// check the checkbox
    			Y_TextField.setDisable(true);	// disable textfield
    		}
    	}
    }

    @FXML
    void clickNoLimitations(ActionEvent event) {
    	if(NoLimitations.isSelected()) {
    		Y_TextField.setDisable(true);	// disable textfield
		}
		else {
			synchronized(CinemaClient.RegulationsDataLock) {
				Y_TextField.setDisable(false);	// enable textfield
	    		UpdateRegulationsData();
	    		Regulations regs = CinemaClient.RegulationsData.get(0);
	    		Y_TextField.setText(Double.toString(regs.getY()));	// initialize the textfiled
			}
		}
    	CheckIfFilled();
		
    }

    @FXML
    void clickViewComplaintsBtn(ActionEvent event) throws IOException {
    	App.setRoot("ViewComplaintsBoundary", null, stage);
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
		
		// reset textfield and checkbox
    	synchronized(CinemaClient.RegulationsDataLock) {
    		UpdateRegulationsData();
    		Regulations regs = CinemaClient.RegulationsData.get(0);
    		if(regs.getStatus()) {
    			Y_TextField.setText(Double.toString(regs.getY()));	// initialize the textfiled
    		}
    		else {
    			NoLimitations.setSelected(true);	// check the checkbox
    			Y_TextField.setDisable(true);	// disable textfield
    		}
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
