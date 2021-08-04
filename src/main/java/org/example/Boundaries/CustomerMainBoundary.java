package org.example.Boundaries;

import javafx.beans.property.SimpleIntegerProperty;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import javafx.util.Pair;

import org.example.App;
import org.example.OCSF.CinemaClient;
import org.example.OCSF.CinemaClientCLI;
import org.example.entities.Employee;
import org.example.entities.Show;
import org.example.entities.Ticket;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomerMainBoundary extends EmployeeMainBoundary implements Initializable, Serializable {

    @FXML // fx:id="refreshBtn"
    private Button showMoviesBtn; // Value injected by FXMLLoader

    @FXML // fx:id="UpdateShowsTimesBtn"
    private Button FillacomplaintBtn; // Value injected by FXMLLoader

    @FXML
    private Button BuyTicketBtn;

    @FXML
    private Button BuypackageBtn;

    @FXML
    private Button cardBtn;

    @FXML
    private Button LoginBtn;
    @FXML
    private Button checkpackage;
    @FXML
    private Button Cancelorder;
    @FXML
    void clickchecktBtn(ActionEvent event) throws IOException {
        App.setRoot("checkPackage",params,stage);

    }
    @FXML
    void clickcancelBtn(ActionEvent event) {

    }


    @FXML private TableView<Show> ShowsTable;
    @FXML private TableColumn<Show, String> movie_name;
    @FXML private TableColumn<Show, String> date;
    @FXML private TableColumn<Show, String> time;
    @FXML private TableColumn<Show, Integer> hall_number;
    @FXML private TableColumn<Show, Double> price;
    @FXML private TableColumn<Show, String> cinema;
    @FXML private ImageView Background;
    
    public static Boolean logInDone = false;	// holds if the show is added yet
    public static int success_status = 1;	// succeeded/failed/alreadyLoggedIn/UsernameOrPasswordWrong
    public static Employee user = null;	// will hold employee user that logged in
    // add show in DataBase and brings the Shows from the DataBase and updates 
 	// the ShowsData local list
    synchronized void logIn(String username, String password) {
    	logInDone = false;	// show isn't added yet
		// create message and send it to the server
    	LinkedList<Object> message = new LinkedList<Object>();
		message.add("LogIn");
		message.add(username);
		message.add(password);
		synchronized(CinemaClient.EmployeeDataLock)
		{	
			CinemaClientCLI.sendMessage(message);
							
			// wait for Data to be changed
			while(!logInDone) {
				try {
					CinemaClient.EmployeeDataLock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
		}
		// update ShowData
		org.example.Boundaries.Boundary.UpdateEmployeesData();
	}

    @FXML
    void clickLoginBtn(ActionEvent event) throws IOException {
    	MessageBoundaryEmployee msg_employee = new MessageBoundaryEmployee();
    	Optional<Pair<String, String>> result = msg_employee.displayLogIn();
    	if(result!=null) {
    		synchronized(CinemaClient.EmployeeDataLock) {
    			List<Object> params = new LinkedList<>();
    			logIn(result.get().getKey(), result.get().getValue());
    			System.out.println(success_status);
    			if(success_status==0) {
    				int user_role = user.getRole();
    				params.add(user);
    				if(user_role==-1) {
    					App.setRoot("ContentManagerMB", params, stage);
    				}else {
    					if(user_role==0) {
        					App.setRoot("CustomerServiceMB", params, stage);
        				}else {
        					if(user_role==1) {
            					App.setRoot("ChainManagerMB", params, stage);
            				}else {
            					if(user_role==2) {
                					App.setRoot("CinemaManagerMB", params, stage);
                				}
            				}
        				}
    				}
    			}else {
    				if(success_status==1) {
    					MessageBoundary.displayError("Login failed.");
    				}
    				else {
    					if(success_status==2) {
        					MessageBoundary.displayError("User already logged in!");
        				}else {
        					if(success_status==3) {
            					MessageBoundary.displayError("Wrong username or password.");
            				}
        				}
    				}
    			}
    		}
    	}
    }


    @FXML
    void clickshowMoviesBtn(ActionEvent event) throws IOException {
        App.setRoot("ShowMovies",params,stage);

    }

    @FXML
    void clickFillacomplaintBtn(ActionEvent event) throws IOException {
        App.setRoot("AddComplaint",null, stage);
    }

    @FXML
    void clickBuyTicketBtn(ActionEvent event) throws IOException {

        App.setRoot("BuyTicket",null, stage);

    }

    @FXML
    void clickcardBtn(ActionEvent event) throws IOException{
        App.setRoot("BuyLink",null, stage);
    }

    @FXML
    void clickBuypackageBtn(ActionEvent event) throws IOException {
        App.setRoot("PaymentPackage",null, stage);


    }




    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // set-up the columns in the table


        System.out.println("initializing done");
    }
}
