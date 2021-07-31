package org.example.Boundaries;

import javafx.beans.property.SimpleIntegerProperty;
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
import org.example.App;
import org.example.OCSF.CinemaClient;
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




    @FXML private TableView<Show> ShowsTable;
    @FXML private TableColumn<Show, String> movie_name;
    @FXML private TableColumn<Show, String> date;
    @FXML private TableColumn<Show, String> time;
    @FXML private TableColumn<Show, Integer> hall_number;
    @FXML private TableColumn<Show, Double> price;
    @FXML private TableColumn<Show, String> cinema;
    @FXML private ImageView Background;

    @FXML
    void clickLoginBtn(ActionEvent event) {

    }


    @FXML
    void clickshowMoviesBtn(ActionEvent event) throws IOException {
        App.setRoot("ShowMovies",params,stage);

    }

    @FXML
    void clickFillacomplaintBtn(ActionEvent event) throws IOException {
        App.setRoot("UpdateTimeBoundary",null, stage);
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

    }




    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // set-up the columns in the table

        System.out.println("initializing done");
    }
}
