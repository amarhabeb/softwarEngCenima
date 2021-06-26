package org.example;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class AddShowBoundary {

    @FXML // fx:id="GoBackToMainBtn"
    private Button GoBackToMainBtn; // Value injected by FXMLLoader

    @FXML // fx:id="movieChoice"
    private ChoiceBox<String> movieChoice; // Value injected by FXMLLoader

    @FXML // fx:id="dayChoice"
    private ChoiceBox<Integer> dayChoice; // Value injected by FXMLLoader

    @FXML // fx:id="monthChoice"
    private ChoiceBox<Integer> monthChoice; // Value injected by FXMLLoader

    @FXML // fx:id="hoursChoice"
    private ChoiceBox<Integer> hoursChoice; // Value injected by FXMLLoader

    @FXML // fx:id="minsChoice"
    private ChoiceBox<Integer> minsChoice; // Value injected by FXMLLoader

    @FXML // fx:id="yearChoice"
    private ChoiceBox<Integer> yearChoice; // Value injected by FXMLLoader

    @FXML // fx:id="onlineChoice"
    private ChoiceBox<Boolean> onlineChoice; // Value injected by FXMLLoader

    @FXML // fx:id="priceTextField"
    private TextField priceTextField; // Value injected by FXMLLoader

    @FXML // fx:id="cinemaChoice"
    private ChoiceBox<Integer> cinemaChoice; // Value injected by FXMLLoader

    @FXML // fx:id="hallChoice"
    private ChoiceBox<Integer> hallChoice; // Value injected by FXMLLoader

    @FXML // fx:id="AddShowBtn"
    private Button AddShowBtn; // Value injected by FXMLLoader

    @FXML
    void clickAddShowBtn(ActionEvent event) throws IOException {
    	App.setRoot("ContentManagerMB");
    }

    @FXML
    void clickGoBackToMainBtn(ActionEvent event) {

    }

}
