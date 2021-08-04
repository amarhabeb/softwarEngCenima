package org.example.Boundaries;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.example.App;
import org.example.OCSF.CinemaClient;
import org.example.OCSF.CinemaClientCLI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public abstract class EmployeeMainBoundary extends EmployeeBoundary{
	
	@FXML
    private Button backBtn;
	
	public static Boolean loggedOut = false;
	synchronized void logOut() {
		loggedOut = false;	// show isn't added yet
		// create message and send it to the server
    	LinkedList<Object> message = new LinkedList<Object>();
		message.add("LogOut");
		message.add(employee.getID());
		synchronized(CinemaClient.EmployeeDataLock)
		{	
			CinemaClientCLI.sendMessage(message);
							
			// wait for Data to be changed
			while(!loggedOut) {
				try {
					CinemaClient.EmployeeDataLock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
		}
		// update ShowData
		org.example.Boundaries.Boundary.UpdateEmployeesData();
	}

    @FXML
    void clickBackBtn(ActionEvent event) throws IOException {
    	logOut();
    	MessageBoundary.displayInfo("You logged out from your account. You are now in customer mode.");
    	App.setRoot("CustomerMain", null, stage);
    }
	
}
