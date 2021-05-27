package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

import javafx.event.ActionEvent;


public class UpdateTimeBoundary {
	@FXML // fx:id="refreshBtn2"
    private Button refreshBtn2; // Value injected by FXMLLoader

    @FXML
    void clickRefreshBtn2(ActionEvent event) {

    }
    
    @FXML // fx:id="GoBackToMainBtn"
    private Button GoBackToMainBtn; // Value injected by FXMLLoader

    @FXML
    void clickGoBackToMainBtn(ActionEvent event)  throws IOException {
    	App.setRoot("ContentManagerMB");
    }
    
    @FXML // fx:id="applyBtn"
    private Button ApplyBtn; // Value injected by FXMLLoader

    @FXML
    void clickApplyBtn(ActionEvent event) {
    	
    }

}

