package org.example.Boundaries;


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
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import org.example.App;
import org.example.OCSF.CinemaClient;
import org.example.OCSF.CinemaClientCLI;
import org.example.entities.Payment;
import org.example.entities.Show;
import org.example.entities.Ticket;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class PaymenyTicket extends Boundary implements Initializable, Serializable {
    public static Boolean ticketAdded = true;

    @FXML // fx:id="refreshBtn"
    private Button payBtn; // Value injected by FXMLLoader

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
                Show show = (Show) App.getParams().get(0);
                int seat_id = (int) App.getParams().get(1);
//                int cinema_id, int hall_id,int seat_id, int show_id,LocalDateTime show_time,
//                double price, int customer_id
                Ticket ticket = new Ticket(show.getCinema().getID(), show.getHall().getID(), seat_id, show.getID(), show.getDateTime(), show.getPrice(), Integer.parseInt(IdText.getText()));
                try {
                    AddTicket(ticket);
                    MessageBoundaryEmployee.displayInfo("Ticket has been sent to you by Email ");
                    List<Object> l =new LinkedList<>();
                    App.setParams(l);
                    String mail = Email.getText();
                    String Message = "Movie  is :"+show.getMovie().getName_en()+ " Seat is: "+ticket.getSeat_id()+ " Price is "+ticket.getPrice()+" Hall is  "+show.getHall().getNumber();
                    org.example.Controllers.MailController.sendMail(Message,mail,"Your Ticket");
                    Payment pay= new Payment(show.getPrice(),Integer.parseInt(IdText.getText()));
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
        for (int hour = 0; hour <= 23; hour++) {
            hoursChoice.getItems().add(hour);
        }
        for (int min = 0; min <= 59; min++) {
            minsChoice.getItems().add(min);
        }

        System.out.println("initializing done");
    }

    synchronized void AddTicket(Ticket ticket) {
        ticketAdded = false;	// show isn't added yet
        // create message and send it to the server
        LinkedList<Object> message = new LinkedList<Object>();
        message.add("AddTicket");
        message.add(ticket);
        synchronized(CinemaClient.TicketsDataLock)
        {
            CinemaClientCLI.sendMessage(message);

            // wait for Data to be changed
            while(!ticketAdded) {
                try {
                    CinemaClient.TicketsDataLock.wait();
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