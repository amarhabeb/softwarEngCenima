package org.example.Boundaries;

import java.io.IOException;

import org.example.App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

class ContentManagerDisplayBoundary extends EmployeeBoundary{
	@FXML
    void clickGoBackToMainBtn(ActionEvent event)  throws IOException {
    	App.setRoot("ContentManagerMB",null,stage);
    }
}
