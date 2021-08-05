package org.example.Boundaries;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import org.example.App;
import org.example.OCSF.CinemaClient;
import org.example.OCSF.CinemaClientCLI;
import org.example.entities.*;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class AddComplaintBoundary extends Boundary implements Initializable, Serializable {

    @FXML
    private TextField ordersNumberTextField1;


    @FXML
    private TextField complaintContent;


    @FXML
    private Button SubmitBtn;
    @FXML
    private Label label= new Label();



    @FXML
    private Button GoBackToMainBtn;

    @FXML
    private TextField movieTitleTextField;

    @FXML
    void clickSubmitBtn(ActionEvent event) throws IOException {
        if(complaintContent.getText().isEmpty()|| ordersNumberTextField1.getText().isEmpty()|| movieTitleTextField.getText().isEmpty()){
            System.out.println("Empty!");
            label.setText(" Please fill All The Fields");


        }
        else if(isNumeric(ordersNumberTextField1.getText())==false){
            label.setText("Please fill a number for order");


        }
        else {
            label.setText("");

            Complaint c = new Complaint(complaintContent.getText(),Integer.parseInt(ordersNumberTextField1.getText()));
            try {
                AddComplaint(c);
                MessageBoundaryEmployee.displayInfo("Complaint sent. We will contact you in 24 Hours ");
                List<Object> l1 =new LinkedList<>();
                App.setParams(l1);
                App.setRoot("CustomerMain",params,stage);


            }catch (Exception e){
                MessageBoundaryEmployee.displayError("An error occured. Complaint  couldn't be added.");


            }

        }




    }

    public static boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c) == false) return false;
        }
        return true;
    }

    @FXML
    private Button refreshbtn;
    @FXML
    void clickrefreshBtn(ActionEvent event) {
        movieTitleTextField.setText("");
        ordersNumberTextField1.setText("");
        complaintContent.setText("");

    }


    @FXML
    void clickGoBackToMainBtn(ActionEvent event) throws IOException {
        List<Object> l = new LinkedList<>();
        App.setParams(l);
        App.setRoot("CustomerMain",null, stage);

    }
    public static Boolean ComplaintAdded = true;
    synchronized void AddComplaint(Complaint com) {
       ComplaintAdded = false;	// show isn't added yet
        // create message and send it to the server
        LinkedList<Object> message = new LinkedList<Object>();
        message.add("AddComplaint");
        message.add(com);
        synchronized(CinemaClient.ComplaintsDataLock)
        {
            CinemaClientCLI.sendMessage(message);

            // wait for Data to be changed
            while(!ComplaintAdded) {
                try {
                    CinemaClient.ComplaintsDataLock.wait();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        // update ShowData
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}