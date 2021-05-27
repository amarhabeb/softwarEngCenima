package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

import javafx.event.ActionEvent;


public class ContentManagerMB {
	
	// boundary for updating time
	UpdateTimeBoundary updateTimeB = null;
	
	@FXML // fx:id="refreshBtn"
    private Button refreshBtn; // Value injected by FXMLLoader

	@FXML // fx:id="UpdateShowsTimesBtn"
	private Button UpdateShowsTimesBtn; // Value injected by FXMLLoader
	
	@FXML
    void clickRefreshBtn(ActionEvent event) {

    }

	@FXML
	void clickUpdateShowsTimesBtn(ActionEvent event) throws IOException {
		 App.setRoot("UpdateTimeBoundary");
	}


}
