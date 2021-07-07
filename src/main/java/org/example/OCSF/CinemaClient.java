package org.example.OCSF;


import org.example.App;
import org.example.Boundaries.*;
import org.example.entities.Cinema;
import org.example.entities.Complaint;
import org.example.entities.Show;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;



public class CinemaClient extends AbstractClient {
	

	public static List<Show> ShowsData = new LinkedList<>();	// holds the shows data
	public static Boolean ShowsDataUpdated = false;	// holdS if the list ShowsData got updated yet
	public static Object ShowsDataLock = new Object();	// lock for accessing the ShowsData List

	public static List<Show> ComplaintsData = new LinkedList<>();
	public static Boolean ComplaintsDataUpdated = false;
	public static Object ComplaintsDataLock = new Object();

	public static List<Show> LinksData = new LinkedList<>();
	public static Boolean LinksDataUpdated = false;
	public static Object LinksDataLock = new Object();

	public static List<Show> MoviessData = new LinkedList<>();
	public static Boolean MoviesDataUpdated = false;
	public static Object MoviesDataLock = new Object();

	public static List<Cinema> CinemaData = new LinkedList<>();
	public static Boolean CinemaDataUpdated = false;
	public static Object MoviesDataLock = new Object();



	
    private static final Logger LOGGER =
            Logger.getLogger(CinemaClient.class.getName());

    CinemaClientCLI cinemaClientCLI;
    public CinemaClient(String host, int port) {
        super(host, port);
        this.cinemaClientCLI = new CinemaClientCLI(this);
    }

    @Override
    protected void connectionEstablished() {
        // TODO Auto-generated method stub
        super.connectionEstablished();
        LOGGER.info("Connected to server.");
        
        try {
        	cinemaClientCLI.loop();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @Override
    protected void connectionClosed() {
        // TODO Auto-generated method stub
        super.connectionClosed();
        cinemaClientCLI.closeConnection();
    }



    @Override
    @SuppressWarnings("unchecked")
    protected void handleMessageFromServer(Object msg) throws Exception {
		LinkedList<Object> message = (LinkedList<Object>)(msg);

    	if(message.get(0).equals("ShowsTimeChanged")) {
    		boolean success = (boolean)message.get(1);
    		if(!success){
    			throw new Exception("Controller failed");
			}
    		synchronized(ShowsDataLock) {
	    		UpdateTimeBoundary.ShowsTimeChanged = true;	// time is now changed
	    		ShowsDataUpdated = false;	// client's ShowsData is now not updated
	    		ShowsDataLock.notifyAll();
    		}
	}

    	
    	if(message.get(0).equals("ShowsPriceChanged")) {
			boolean success = (boolean)message.get(1);
			if(!success){
				throw new Exception("Controller failed");
			}
    		synchronized(ShowsDataLock) {
	    		UpdatePriceBoundary.ShowsPriceChanged = true;	// Price is now changed
	    		ShowsDataUpdated = false;	// client's ShowsData is now not updated
	    		ShowsDataLock.notifyAll();
    		}
	}
    	

    	if(message.get(0).equals("ShowsLoaded")) {
    		// get second argument which is the updated data and assign it to the static variable
    		ShowsData = (List<Show>) message.get(1);
    		synchronized(ShowsDataLock) {
    			ShowsDataUpdated = true;	// client's ShowsData is now updated
    			ShowsDataLock.notifyAll();
    		}
    	}

		if(message.get(0).equals("ComplaintesLoaded")) {
			// get second argument which is the updated data and assign it to the static variable
			ComplaintsData = (List<Show>) message.get(1);
			synchronized(ComplaintsDataLock) {
				ComplaintsDataUpdated = true;	// client's ComplaintsData is now updated
				ComplaintsDataLock.notifyAll();
			}
		}

		if(message.get(0).equals("ComplaintMarkedAsDone")) {
			boolean success = (boolean)message.get(1);
			if(!success){
				throw new Exception("Controller failed");
			}
			synchronized(ComplaintsDataLock) {
				MarkComplaintAsDoneBoundary.ComplaintMarkedAsDone = true;	// Complaint is now  Marked As Done
				ComplaintsDataUpdated = false;	// client's ComplaintsData is now not updated
				ComplaintsDataLock.notifyAll();
			}
		}

		if(message.get(0).equals("ComplaintAdded")) {
			boolean success = (boolean)message.get(1);
			if(!success){
				throw new Exception("Controller failed");
			}
			synchronized(ComplaintsDataLock) {
				AddComplaintBoundary.ComplaintAdded = true;	// Complaint is now added
				ComplaintsDataUpdated = false;	// client's ComplaintsData is now not updated
				ComplaintsDataLock.notifyAll();
			}
		}

		if(message.get(0).equals("LinksLoaded")) {
			// get second argument which is the updated data and assign it to the static variable
			LinksData = (List<Show>) message.get(1);
			synchronized(LinksDataLock) {
				LinksDataUpdated = true;	// client's LinksData is now updated
				LinksDataLock.notifyAll();
			}
		}

		if(message.get(0).equals("Costumer'sLinksLoaded")) {
			// get second argument which is the updated data and assign it to the static variable
			LinksData = (List<Show>) message.get(1);
			synchronized(LinksDataLock) {
				LinksDataUpdated = true;	// client's LinksData is now updated
				LinksDataLock.notifyAll();
			}
		}

		if(message.get(0).equals("ShowsLoaded")) {
			// get second argument which is the updated data and assign it to the static variable
			ShowsData = (List<Show>) message.get(1);
			synchronized(ShowsDataLock) {
				ShowsDataUpdated = true;	// client's ShowsData is now updated
				ShowsDataLock.notifyAll();
			}
		}
	    




    	if(message.get(0).equals("ShowDeleted")) {
    		System.out.println("Message ShowsPriceChanged replied");
    		synchronized(ShowsDataLock) {
    			DeleteShowBoundary.ShowDeleted = true;	// Show is now deleted
	    		ShowsDataUpdated = false;	// client's ShowsData is now not updated
	    		ShowsDataLock.notifyAll();
    		}
    	}


    }

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Required arguments: <host> <port>");
        } else {
            String host = args[0];
            int port = Integer.parseInt(args[1]);
            CinemaClient cinemaClient = new CinemaClient(host, port);
            cinemaClient.openConnection();

            App.main(args);
        }
    }
}

