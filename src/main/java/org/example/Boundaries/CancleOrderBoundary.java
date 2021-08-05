package org.example.Boundaries;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Callback;
import org.example.App;
import org.example.OCSF.CinemaClient;
import org.example.OCSF.CinemaClientCLI;
import org.example.entities.Movie;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class CancleOrderBoundary extends Boundary implements Initializable, Serializable {

    @FXML
    private TextField ordersNumberTextField1;

    @FXML
    private Label movieTitleTxt;

    @FXML
    private Label OrdersNumberTxt1;

    @FXML
    private ChoiceBox<String> orderTypeChoiceBox;

    @FXML
    private Button SubmitBtn;

    @FXML
    private Button GoBackToMainBtn;

    @FXML
    private TextField customerTitleTextField;

    @FXML
    private Label label;
    
    public static Boolean linkCancelled;
    public static Boolean ticketCancelled;
    
    synchronized void cancelLink(int order_id) {	
		// create message and send it to the server
    	LinkedList<Object> message = new LinkedList<Object>();
		message.add("CancelLink");
		message.add(order_id);
		synchronized(CinemaClient.LinksDataLock)
		{	
			linkCancelled = false;
			CinemaClientCLI.sendMessage(message);
							
			// wait for Data to be changed
			while(!linkCancelled) {
				try {
					CinemaClient.LinksDataLock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
			// update Data
			org.example.Boundaries.Boundary.UpdateOrdersData();
		}	
	}
    
    synchronized void cancelTicket(int order_id) {	
		// create message and send it to the server
    	LinkedList<Object> message = new LinkedList<Object>();
		message.add("CancelTicket");
		message.add(order_id);
		synchronized(CinemaClient.TicketsDataLock)
		{	
			ticketCancelled = false;
			CinemaClientCLI.sendMessage(message);
							
			// wait for Data to be changed
			while(!ticketCancelled) {
				try {
					CinemaClient.TicketsDataLock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
			// update Data
			org.example.Boundaries.Boundary.UpdateOrdersData();
		}	
	}
    	
    

    @FXML
    void clickSubmitBtn(ActionEvent event) {
        if(orderTypeChoiceBox.getSelectionModel().getSelectedItem().equals("Cancel Link")){
            String order_id = ordersNumberTextField1.getText();
            if(!isNumeric(order_id)){
                label.setText("Invalid input. Please Insert a number for order number.");
            }
            else {
            	try {
            		synchronized(CinemaClient.OrdersDataLock) {
            			cancelLink(Integer.parseInt(order_id));
            			MessageBoundary.displayInfo("Order successfully cancelled. You've been refunded.");
            		}
            	}catch(Exception e) {
            		MessageBoundary.displayError("Order cancelling failed.");
            	}
            	
            }

        }
        else  {
            String order_id = ordersNumberTextField1.getText();

            if(!isNumeric(order_id)){
                label.setText("Invalid input. Please Insert a number for order number.");
            }else {
            	try {
	            	synchronized(CinemaClient.OrdersDataLock) {
	            		cancelTicket(Integer.parseInt(order_id));
	            		MessageBoundary.displayInfo("Order successfully cancelled. You've been refunded.");
	            	}
            	}catch(Exception e) {
            		MessageBoundary.displayError("Order cancelling failed.");
            	}
            }

            
        }

    }

    @FXML
    void clickGoBackToMainBtn(ActionEvent event) throws IOException {
        List<Object> l = new LinkedList<>();
        App.setParams(l);
        App.setRoot("CustomerMain",null, stage);

    }




    @Override
    public void initialize(URL url, ResourceBundle rb) {
        orderTypeChoiceBox.getItems().add("Cancel Link");
        orderTypeChoiceBox.getItems().add("Cancel Ticket");




    }
    
    public static boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c) == false) return false;
        }
        return true;
    }
    
}
