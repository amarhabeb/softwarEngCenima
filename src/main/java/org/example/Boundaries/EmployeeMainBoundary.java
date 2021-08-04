package org.example.Boundaries;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.example.App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public abstract class EmployeeMainBoundary extends EmployeeBoundary{
	
	@FXML
    private Button backBtn;

    @FXML
    void clickBackBtn(ActionEvent event) throws IOException {
    	MessageBoundary.displayInfo("You logged out from your account. You are now in customer mode.");
    	App.setRoot("CustomerMainBoundary", null, stage);
    }
	
}
