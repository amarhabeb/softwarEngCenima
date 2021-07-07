package org.example.Boundaries;

import java.time.Month;
import java.time.Year;
import java.util.LinkedList;
import java.util.List;

import org.example.OCSF.CinemaClient;
import org.example.OCSF.CinemaClientCLI;
import org.example.entities.Show;

public abstract class Boundary {
	
	// title of boundary
	String title = "";
	List<Object> params;
	
	// brings the Shows from the DataBase and updates the ShowsData local list
	synchronized void UpdateShowsData() {
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
	synchronized void UpdateMoviesData() {
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
	synchronized void UpdateHallsData() {
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
		  	
	// brings the Cinemas from the DataBase and updates the CinemasData local list
	synchronized void UpdateCinemasData() {
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

	synchronized void UpdateUpdatePriceRequestsData() {
		// add message to ClientInput so it could be sent to server
		CinemaClient.UpdatePriceRequestsDataUpdated = false;
		LinkedList<Object> message = new LinkedList<Object>();
		message.add("LoadUpdatePriceRequests");
		synchronized(CinemaClient.UpdatePriceRequestsDataLock)
		{	
			CinemaClientCLI.sendMessage(message);
										
			// wait for Data to be updated
			while(!CinemaClient.UpdatePriceRequestsDataUpdated) {
				try {
						CinemaClient.UpdatePriceRequestsDataLock.wait();
				} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				}
			}	
		}	
	}
	
	synchronized void UpdateTicketsData() {
		// add message to ClientInput so it could be sent to server
		CinemaClient.TicketsDataUpdated = false;
		LinkedList<Object> message = new LinkedList<Object>();
		message.add("LoadTickets");
		synchronized(CinemaClient.TicketsDataLock)
		{	
			CinemaClientCLI.sendMessage(message);
										
			// wait for Data to be updated
			while(!CinemaClient.TicketsDataUpdated) {
				try {
						CinemaClient.TicketsDataLock.wait();
				} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				}
			}	
		}	
	}
	
	synchronized void UpdateTicketsReportData(int cinema_id, Month month, Year year) {
		// add message to ClientInput so it could be sent to server
		CinemaClient.TicketsReportDataUpdated = false;
		LinkedList<Object> message = new LinkedList<Object>();
		message.add("LoadTicketsReport");
		message.add(cinema_id);
		message.add(month);
		message.add(year);
		synchronized(CinemaClient.TicketsReportDataLock)
		{	
			CinemaClientCLI.sendMessage(message);
										
			// wait for Data to be updated
			while(!CinemaClient.TicketsReportDataUpdated) {
				try {
						CinemaClient.TicketsReportDataLock.wait();
				} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				}
			}	
		}	
	}
	
	// return show given id
	public Show idToShow(int id) {
   	 	for(Show show:CinemaClient.ShowsData) {
   	 		if(show.getID()==id) {
   	 			return show;
   	 		}
   	 	}
   	 	return null;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Object> getParams() {
		return Params;
	}

	public void setParams(List<Object> params) {
		Params = params;
	}
		  	
}
