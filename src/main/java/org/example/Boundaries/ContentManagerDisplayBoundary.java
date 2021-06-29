package org.example.Boundaries;

import java.util.LinkedList;

import org.example.OCSF.CinemaClient;
import org.example.OCSF.CinemaClientCLI;

class ContentManagerDisplayBoundary {
	
	// brings the Shows from the DataBase and updates the ShowsData local list
	void UpdateShowsData() {
		// add message to ClientInput so it could be sent to server
		CinemaClient.ShowsDataUpdated = false;
		LinkedList<Object> message = new LinkedList<Object>();
		message.add("LoadShows");
		synchronized(CinemaClient.ShowsDataLock)
		{	
			CinemaClientCLI.sendMessage(message);
								
			// wait for Data to be updated
			while(!CinemaClient.ShowsDataUpdated) {
				try {
						CinemaClient.ShowsDataLock.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}	
		}	
	}
		
	// brings the Movies from the DataBase and updates the MoviesData local list
	 void UpdateMoviesData() {
		 // add message to ClientInput so it could be sent to server
		 CinemaClient.MoviesDataUpdated = false;
		 LinkedList<Object> message = new LinkedList<Object>();
		 message.add("LoadMovies");
		 synchronized(CinemaClient.MoviesDataLock)
	 	 {	
	 		 CinemaClientCLI.sendMessage(message);
	 							
	 		 // wait for Data to be updated
	 		 while(!CinemaClient.MoviesDataUpdated) {
	 			 try {
	 					 CinemaClient.MoviesDataLock.wait();
	 			 	 } catch (InterruptedException e) {
	 					 // TODO Auto-generated catch block
	 					 e.printStackTrace();
	 				 }
	 		 }	
	 	 }	
	  }
	 	
	 // brings the Halls from the DataBase and updates the MoviesData local list
	  void UpdateHallsData() {
	  		// add message to ClientInput so it could be sent to server
	  		CinemaClient.HallsDataUpdated = false;
	  		LinkedList<Object> message = new LinkedList<Object>();
	  		message.add("LoadHalls");
	  		synchronized(CinemaClient.HallsDataLock)
	  		{	
	  			CinemaClientCLI.sendMessage(message);
	  							
	  			// wait for Data to be updated
	  			while(!CinemaClient.HallsDataUpdated) {
	  				try {
	  						CinemaClient.HallsDataLock.wait();
	  					} catch (InterruptedException e) {
	  						// TODO Auto-generated catch block
	  						e.printStackTrace();
	  					}
	  			}	
	  		}	
	  	}
	  	
	 // brings the Movies from the DataBase and updates the CinemasData local list
	  	void UpdateCinemasData() {
	  		// add message to ClientInput so it could be sent to server
	  		CinemaClient.CinemasDataUpdated = false;
	  		LinkedList<Object> message = new LinkedList<Object>();
	  		message.add("LoadCinemas");
	  		synchronized(CinemaClient.CinemasDataLock)
	  		{	
	  			CinemaClientCLI.sendMessage(message);
	  							
	  			// wait for Data to be updated
	  			while(!CinemaClient.CinemasDataUpdated) {
	  				try {
	  						CinemaClient.CinemasDataLock.wait();
	  					} catch (InterruptedException e) {
	  						// TODO Auto-generated catch block
	  						e.printStackTrace();
	  					}
	  			}	
	  		}	
	  	}
	
}
