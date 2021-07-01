package org.example.Boundaries;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class MessageBoundary {
	static public void displayInfo(String dialouge) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText(null);
		alert.setContentText(dialouge);

		alert.showAndWait();
	}
	
	static public void displayWarning(String dialouge) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning");
		alert.setHeaderText(null);
		alert.setContentText(dialouge);

		alert.showAndWait();
	}
	
	static public void displayError(String dialouge) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(dialouge);

		alert.showAndWait();
	}
	
}
