package org.example.Boundaries;

import java.time.Month;
import java.time.Year;
import java.util.LinkedList;
import java.util.List;

import org.example.OCSF.CinemaClient;
import org.example.OCSF.CinemaClientCLI;
import org.example.entities.Cinema;
import org.example.entities.Complaint;
import org.example.entities.Show;

public abstract class Boundary {
	
	// title of boundary
	String title = "";
	List<Object> params;
	
	// brings the Shows from the DataBase and updates the ShowsData local list
	static synchronized void UpdateShowsData() {
		// add message to ClientInput so it could be sent to server
		LinkedList<Object> message = new LinkedList<Object>();
		message.add("LoadShows");
		synchronized(CinemaClient.ShowsDataLock)
		{	
			CinemaClient.ShowsDataUpdated = false;
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
		LinkedList<Object> message = new LinkedList<Object>();
		message.add("LoadMovies");
		synchronized(CinemaClient.MoviesDataLock)
		 {	
			CinemaClient.MoviesDataUpdated = false;
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
		 LinkedList<Object> message = new LinkedList<Object>();
		 message.add("LoadHalls");
		 synchronized(CinemaClient.HallsDataLock)
		 {	
			 CinemaClient.HallsDataUpdated = false;
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
		LinkedList<Object> message = new LinkedList<Object>();
		message.add("LoadCinemas");
		synchronized(CinemaClient.CinemasDataLock)
		{	
			CinemaClient.CinemasDataUpdated = false;
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
		LinkedList<Object> message = new LinkedList<Object>();
		message.add("LoadUpdatePriceRequests");
		synchronized(CinemaClient.UpdatePriceRequestsDataLock)
		{	
			CinemaClient.UpdatePriceRequestsDataUpdated = false;
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
		LinkedList<Object> message = new LinkedList<Object>();
		message.add("LoadTickets");
		synchronized(CinemaClient.TicketsDataLock)
		{	
			CinemaClient.TicketsDataUpdated = false;
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
	
	synchronized void UpdateComplaintsData() {
		// add message to ClientInput so it could be sent to server
		LinkedList<Object> message = new LinkedList<Object>();
		message.add("LoadComplaints");
		synchronized(CinemaClient.ComplaintsDataLock)
		{	
			CinemaClient.ComplaintsDataUpdated = false;
			CinemaClientCLI.sendMessage(message);
										
			// wait for Data to be updated
			while(!CinemaClient.ComplaintsDataUpdated) {
				try {
						CinemaClient.ComplaintsDataLock.wait();
				} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				}
			}	
		}	
	}
	
	synchronized void UpdateTicketsReportData(int cinema_id, Month month, Year year) {
		// add message to ClientInput so it could be sent to server
		LinkedList<Object> message = new LinkedList<Object>();
		message.add("LoadTicketsReport");
		message.add(cinema_id);
		message.add(month);
		message.add(year);
		synchronized(CinemaClient.TicketsReportDataLock)
		{
			CinemaClient.TicketsReportDataUpdated = false;
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
	
	synchronized void UpdatePackagesReportData(Month month, Year year) {
		// add message to ClientInput so it could be sent to server
		LinkedList<Object> message = new LinkedList<Object>();
		message.add("LoadPackagesReport");
		message.add(month);
		message.add(year);
		synchronized(CinemaClient.PackagesReportDataLock)
		{
			CinemaClient.PackagesReportDataUpdated = false;
			CinemaClientCLI.sendMessage(message);

			// wait for Data to be updated
			while(!CinemaClient.PackagesReportDataUpdated) {
				try {
						CinemaClient.PackagesReportDataLock.wait();
				} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				}
			}
		}
	}
	
	synchronized void UpdateLinksReportData(Month month, Year year) {
		// add message to ClientInput so it could be sent to server
		LinkedList<Object> message = new LinkedList<Object>();
		message.add("LoadLinksReport");
		message.add(month);
		message.add(year);
		synchronized(CinemaClient.LinksReportDataLock)
		{
			CinemaClient.LinksReportDataUpdated = false;
			CinemaClientCLI.sendMessage(message);

			// wait for Data to be updated
			while(!CinemaClient.LinksReportDataUpdated) {
				try {
						CinemaClient.LinksReportDataLock.wait();
				} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				}
			}
		}
	}
	
	synchronized void UpdateRefundsReportData(Month month, Year year) {
		// add message to ClientInput so it could be sent to server
		LinkedList<Object> message = new LinkedList<Object>();
		message.add("LoadRefundsReport");
		message.add(month);
		message.add(year);
		synchronized(CinemaClient.RefundsReportDataLock)
		{
			CinemaClient.RefundsReportDataUpdated = false;
			CinemaClientCLI.sendMessage(message);

			// wait for Data to be updated
			while(!CinemaClient.RefundsReportDataUpdated) {
				try {
						CinemaClient.RefundsReportDataLock.wait();
				} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				}
			}
		}
	}

	synchronized void UpdateComplaintsReportData(Month month, Year year) {
		// add message to ClientInput so it could be sent to server
		LinkedList<Object> message = new LinkedList<Object>();
		message.add("LoadComplaintsReport");
		message.add(month);
		message.add(year);
		synchronized(CinemaClient.ComplaintsReportDataLock)
		{
			CinemaClient.ComplaintsReportDataUpdated = false;
			CinemaClientCLI.sendMessage(message);

			// wait for Data to be updated
			while(!CinemaClient.ComplaintsReportDataUpdated) {
				try {
						CinemaClient.ComplaintsReportDataLock.wait();
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
	
	// return cinema given id
	public Cinema idToCinema(int id) {
	   	 for(Cinema cinema:CinemaClient.CinemasData) {
	   	 	if(cinema.getID()==id) {
	   	 		return cinema;
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
		return params;
	}

	public void setParams(List<Object> params) {
		this.params = params;
	}
		  	
}
