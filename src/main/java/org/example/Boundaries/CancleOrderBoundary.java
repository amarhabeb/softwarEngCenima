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
    private TextField movieTitleTextField;

    @FXML
    void clickSubmitBtn(ActionEvent event) {
        if(orderTypeChoiceBox.getSelectionModel().getSelectedItem().equals("Cancel Link")){


        }
        else  {

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
}
