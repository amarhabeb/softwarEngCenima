package org.example.Boundaries;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MessageBoundary {
	static public void displayInfo(String dialouge) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText(dialouge);

		alert.showAndWait();
	}
	
}
