package org.example.Boundaries;

import java.io.IOException;

import org.example.App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public abstract class EmployeeMainBoundary extends EmployeeBoundary{
	
	@FXML
    private Button backBtn;

    @FXML
    void clickBackBtn(ActionEvent event) throws IOException {
    	App.setRoot("TempBoundary", null, stage);
    }
	
}
