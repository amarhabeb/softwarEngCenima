package org.example.Boundaries;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.example.App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

class ContentManagerDisplayBoundary extends EmployeeBoundary{
	@FXML
    void clickGoBackToMainBtn(ActionEvent event)  throws IOException {
		List<Object> params = new LinkedList<>();
    	params.add(employee);
    	App.setRoot("ContentManagerMB",params,stage);
    }
}
