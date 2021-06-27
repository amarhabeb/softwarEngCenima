package org.example;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;



public class CinemaClient extends AbstractClient {
	
	// these static variables are shared upon all threads
	static List<Show> ShowsData = new LinkedList<>();	// holds the shows data
	static Boolean ShowsDataUpdated = false;	// holdS if the list ShowsData is updated to the last version
	static Object ShowsDataLock = new Object();	// lock for accessing the ShowsData List
	
	
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
    protected void handleMessageFromServer(Object msg) {
		LinkedList<Object> message = (LinkedList<Object>)(msg);
    	if(message.get(0).equals("ShowsTimeChanged")) {
    		System.out.println("Message ShowsTimeChanged replied");
    		synchronized(ShowsDataLock) {
	    		UpdateTimeBoundary.ShowsTimeChanged = true;	// time is now changed
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

