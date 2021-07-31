package org.example.Boundaries;

import java.io.IOException;

import org.example.App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class TempBoundary extends Boundary{

    @FXML
    private Button employee;

    @FXML
    private Button contenManager;

    @FXML
    private Button chain;

    @FXML
    private Button cinema;

    @FXML
    private Button customer;

    @FXML
    void clickChain(ActionEvent event) throws IOException {
    	App.setRoot("ChainManagerMB", null, stage);
    }

    @FXML
    void clickCinema(ActionEvent event) throws IOException {
    	App.setRoot("CinemaManagerMB", null, stage);

    }

    @FXML
    void clickContentManager(ActionEvent event) throws IOException {
    	App.setRoot("ContentManagerMB", null, stage);

    }

    @FXML
    void clickCustomer(ActionEvent event) throws IOException {
    	App.setRoot("CustomerServiceMB", null, stage);

    }

    @FXML
    void clickEmployee(ActionEvent event) throws IOException {
    	App.setRoot("CustomerMainBoundary", null, stage);

    }

}

