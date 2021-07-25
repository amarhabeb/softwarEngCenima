package org.example.Boundaries;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import org.example.App;
import org.example.OCSF.CinemaClient;
import org.example.entities.Complaint;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.fxml.Initializable;

@SuppressWarnings("serial")
public class ViewComplaintsBoundary  extends EmployeeBoundary implements Initializable, Serializable{
	
	LinkedList<Complaint> complaints = new LinkedList<Complaint>();
	int currentComplaintIdx = 0;
	
	@FXML
	private TextArea complaintContent;

	@FXML
	private Button prevBtn;

	@FXML
	private Button nextBtn;

	@FXML
	private Button applyBtn;

	@FXML
	private TextField refundTextField;

	@FXML
	private CheckBox refundCB;

	@FXML
	private CheckBox dontRefundCB;

	@FXML
	private Button GoBackToMainBtn;

	@FXML
	void clickApplyBtn(ActionEvent event) {
		
	}

	@FXML
	void clickDontRefundCB(ActionEvent event) {
		if (dontRefundCB.isSelected()) {
			refundCB.setSelected(false);
			refundTextField.setText(null);
			refundTextField.setDisable(true);
		}
		CheckIfFilled();
	};

	@FXML
	void clickGoBackToMainBtn(ActionEvent event) throws IOException {
		App.setRoot("CustomeServiceMB",null);
	}

	@FXML
	 void clickNextBtn(ActionEvent event) {
		loadComplaint(currentComplaintIdx+1);
	}

	@FXML
	void clickPrevBtn(ActionEvent event) {
		loadComplaint(currentComplaintIdx-1);
	}

	@FXML
	void clickRefundCB(ActionEvent event) {
		if (refundCB.isSelected()) {
			dontRefundCB.setSelected(false);
			refundTextField.setText(null);
			refundTextField.setDisable(false);
		}
		CheckIfFilled();
	}
	 
	private void loadComplaint(int i) {
		currentComplaintIdx = i;
		// disable+enable buttons
		if (i==0) {
			prevBtn.setDisable(true);
		}else {
			prevBtn.setDisable(false);
		}
		if (i==complaints.size()-1) {
			nextBtn.setDisable(true);
		}else {
			nextBtn.setDisable(false);
		}
		// set content in text area
		Complaint comp = complaints.get(i);
		String dateTime = comp.getCreationDate().toString();
		complaintContent.setText(comp.getText() + "\ncreated at: " + dateTime);
		
		// initialize choice boxes and text for refunding
		dontRefundCB.setSelected(true);
		refundCB.setSelected(false);
		refundTextField.setText(null);
		refundTextField.setDisable(true);
		
	}
	
	// for enabling or disabling the apply button
	public void CheckIfFilled(){
		if((refundCB.isSelected() && !dontRefundCB.isSelected() && refundTextField.getText()!=null) 
				|| (!refundCB.isSelected() && dontRefundCB.isSelected())) {
			applyBtn.setDisable(false);
		}else {
			applyBtn.setDisable(true);
		}
	}

	    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		// update complaint data
		synchronized(CinemaClient.ComplaintsDataLock) {
			UpdateComplaintsData();
			complaints = CinemaClient.ComplaintsData;
		}
		
		if(complaints==null) {
			// disable everything
			prevBtn.setDisable(true);
			nextBtn.setDisable(true);
			dontRefundCB.setDisable(true);
			refundCB.setDisable(true);
			refundTextField.setText(null);
			refundTextField.setDisable(true);
			applyBtn.setDisable(true);
			complaintContent.setText("**THERE ARE NO COMPLAINTS AT THE MOMENT**");
		}else {
			loadComplaint(0);
		}
		
		refundTextField.textProperty().addListener((observable, oldValue, newValue) -> {
    		double val=0;
    		try {
    			val = Double.valueOf(newValue);
    		}catch (NumberFormatException e) {
				val = -1;	
			}finally {
				if(val<0) {	// invalid input
					MessageBoundaryEmployee.displayError("Value must be a non-negative number.");
					refundTextField.setText(null);
	    		}
				CheckIfFilled();
			}
    	});
		
	}

}
