package org.example.Boundaries;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.App;
import org.example.OCSF.CinemaClient;
import org.example.OCSF.CinemaClientCLI;
import org.example.entities.*;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class PaymentPackage extends  Boundary implements Initializable, Serializable {
    public static Boolean PackageAdded = true;

    @FXML // fx:id="refreshBtn"
    private Button payBtn1; // Value injected by FXMLLoader

    @FXML // fx:id="UpdateShowsTimesBtn"
    private Button BackBtn; // Value injected by FXMLLoader

    @FXML // fx:id="hoursChoice"
    private ChoiceBox<Integer> hoursChoice; // Value injected by FXMLLoader

    @FXML // fx:id="minsChoice"
    private ChoiceBox<Integer> minsChoice;

    @FXML
    private TextField IdText;

    @FXML
    private TextField CardText;

    @FXML
    private TextField CCV;
    @FXML
    private Label MessageText;
    @FXML
    private TextField Email;


    @FXML
    void clickpayBtn(ActionEvent event) throws IOException {

        MessageText.setText("");
        if (hoursChoice.getValue() == null || minsChoice.getValue() == null || Email.getText()==null||IdText.getText().isEmpty() || CardText.getText().isEmpty() || CCV.getText().isEmpty()) {
            MessageText.setText("Please fill All your details");

        } else {
            if (isNumeric(IdText.getText()) == true && isNumeric(CCV.getText()) == true && isNumeric(CardText.getText()) == true) {


//                int cinema_id, int hall_id,int seat_id, int show_id,LocalDateTime show_time,
//                double price, int customer_id
                String l = "https//www.cinema.com/"+IdText.getText();
                PackageOrder link= new PackageOrder(500,Integer.parseInt(IdText.getText()));

                try {
                    AddPackage(link);
                    MessageBoundaryEmployee.displayInfo("Package has been sent to you by Email ");
                    List<Object> l1 =new LinkedList<>();
                    App.setParams(l1);
                    String mail = Email.getText();
                    Customer p = new Customer("buy","050",Email.getText());
                    String Message = "ID is "+link.getID()+ " Link is: "+l1;
                    org.example.Controllers.MailController.sendMail(Message,mail,"Your Link");
                    Payment pay= new Payment(500,Integer.parseInt(IdText.getText()));
                    AddPayment(pay);

                    App.setRoot("CustomerMain",params,stage);
                }
                catch(Exception e) {
                    MessageBoundaryEmployee.displayError("An error occured. Show couldn't be added.");
                }


            } else {
                MessageText.setText("Please fill Numeric Values  For Id, CCV, and Card Number ");

            }
        }



    }


    @FXML
    void clickBackBtn(ActionEvent event) throws IOException {
        List<Object> l = new LinkedList<>();
        App.setParams(l);
        App.setRoot("CustomerMain", null, stage);

    }

    public static boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c) == false) return false;
        }
        return true;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // set-up the columns in the table
        for (int hour = 0; hour <= 12; hour++) {
            hoursChoice.getItems().add(hour);
        }
        for (int min = 2021; min <= 2030; min++) {
            minsChoice.getItems().add(min);
        }

        System.out.println("initializing done");
    }

    synchronized void AddPackage(PackageOrder link) {
        PackageAdded = false;	// show isn't added yet
        // create message and send it to the server
        LinkedList<Object> message = new LinkedList<Object>();
        message.add("AddPackage");
        message.add(link);
        synchronized(CinemaClient.PackageDataLock)
        {
            CinemaClientCLI.sendMessage(message);

            // wait for Data to be changed
            while(!PackageAdded) {
                try {
                    CinemaClient.PackageDataLock.wait();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        // update ShowData
        //org.example.Boundaries.Boundary.UpdateShowsData();
    }
    public  static Boolean PaymentAdded = true;
    synchronized void AddPayment(Payment pay) {
        PaymentAdded = false;	// show isn't added yet
        // create message and send it to the server
        LinkedList<Object> message = new LinkedList<Object>();
        message.add("MakePayment");
        message.add(pay);
        synchronized(CinemaClient.PaymentDataLock)
        {
            CinemaClientCLI.sendMessage(message);

            // wait for Data to be changed
            while(!PaymentAdded) {
                try {
                    CinemaClient.PaymentDataLock.wait();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        // update ShowData
    }

}
