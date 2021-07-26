package org.example.Boundaries;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.ResourceBundle;

import org.example.App;
import org.example.OCSF.CinemaClient;
import org.example.OCSF.CinemaClientCLI;
import org.example.entities.Complaint;
import org.example.entities.Refund;
import org.example.entities.Show;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
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
    private Text idxText;
	
	@FXML
    private Button refreshBtn;
	
	public static Boolean RefundAdded = true;	// holds if the show is added yet
    // add refund to DataBase
    synchronized void AddRefund(Refund refund) {
		RefundAdded = false;	// show isn't added yet
		// create message and send it to the server
    	LinkedList<Object> message = new LinkedList<Object>();
		message.add("AddRefund");
		message.add(refund);
		synchronized(CinemaClient.RefundsDataLock)
		{	
			CinemaClientCLI.sendMessage(message);
							
			// wait for Data to be changed
			while(!RefundAdded) {
				try {
					CinemaClient.RefundsDataLock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
		}	
	}
    
    public static Boolean ComplaintDeleted = true;	// holds if the show is deleted yet
    // delete compalint in DataBase and brings the Complaints from the DataBase and updates 
 	// the CompalintsData local list
    synchronized void DeleteComplaint(int complaint_id) {
    	ComplaintDeleted = false;	// complaint isn't deleted yet
		// create message and send it to the server
    	LinkedList<Object> message = new LinkedList<Object>();
		message.add("DeleteComplaint");
		message.add(complaint_id);
		synchronized(CinemaClient.ComplaintsDataLock)
		{	
			CinemaClientCLI.sendMessage(message);
							
			// wait for Data to be changed
			while(!ComplaintDeleted) {
				try {
					CinemaClient.ComplaintsDataLock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
			// update ShowData
			UpdateComplaintsData();
		}	
	}
    
    void refresh() {
    	// update complaint data
    	synchronized(CinemaClient.ComplaintsDataLock) {
    		UpdateComplaintsData();
    		complaints = CinemaClient.ComplaintsData;
    	}		
    	if(complaints==null) {
    		loadEmpty();
    	}else {
    		loadComplaint(0);
    	}
    }

	@FXML
	void clickApplyBtn(ActionEvent event) {
		// assign amount
		double amount = 0;
		if (refundCB.isSelected()) {
			amount = Double.valueOf(refundTextField.getText());
		}
		// create a refund entity
		Complaint complaint = complaints.get(currentComplaintIdx);
		int complaint_id = complaint.getID();
		LocalDateTime date = LocalDateTime.now();
		int order_id = complaint.getOrder_id();
		Refund refund = new Refund(amount, order_id, complaint_id, date);
		// add refund to database and delete complaint
		AddRefund(refund);
		DeleteComplaint(complaint_id);
		// refresh page
		refresh();
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
	
	@FXML
    void clickRefreshBtn(ActionEvent event) {
		refresh();
    }
	
	private void loadEmpty() {
		// disable everything
		idxText.setText("0/0");
		prevBtn.setDisable(true);
		nextBtn.setDisable(true);
		dontRefundCB.setDisable(true);
		refundCB.setDisable(true);
		refundTextField.setText(null);
		refundTextField.setDisable(true);
		applyBtn.setDisable(true);
		complaintContent.setText("**THERE ARE NO COMPLAINTS AT THE MOMENT**");
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
		
		// initialize choice boxes, text, and textfield for refunding
		idxText.setText(Integer.toString(i+1)+"/"+Integer.toString(complaints.size()));
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
			loadEmpty();
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
