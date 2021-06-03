package org.example;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import java.util.List;




public class CinemaServer extends AbstractServer{

	private static Session session;

	private static SessionFactory getSessionFactory() throws HibernateException {
		Configuration configuration = new Configuration();

		// Add ALL of your entities here. You can also try adding a whole package.
		configuration.addAnnotatedClass(Show.class);
		
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties())
				.build();

		return configuration.buildSessionFactory(serviceRegistry);
	}

    public CinemaServer(int port) {
        super(port);
    }



                    /********** implement************/
    @SuppressWarnings("unchecked")
	@Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
    	System.out.println("message reached server");
    	LinkedList<Object> message = (LinkedList<Object>)(msg);
    	try {
    		if(message.get(0).equals("ChangeShowTime")) {
    			int show_id = (int) message.get(1);
    			String newTime = (String) message.get(2);
    			// change time of show in database
    			boolean success = ShowsHandler.updateTime(session, show_id, newTime);
    			//session.refresh(Show.class);
    			if(!success) {
    				throw new Exception("Show's time couldnt be updated");
    			}
    			// reply to client	
				LinkedList<Object> messageToClient = new LinkedList<Object>();
				//messageToClient.add("ShowsTimeChanged");
    		}
    	
    		if(message.get(0).equals("LoadShows")) {
    			// load data
    			List<Show> Data = ShowsHandler.loadShows(session);
    			try {	
    				// reply to client	
    				LinkedList<Object> messageToClient = new LinkedList<Object>();
    				messageToClient.add("ShowsLoaded");
    				messageToClient.add(Data);
					client.sendToClient(messageToClient);
    			} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
    			}
    		}
    		
    	} catch (Exception exception) {
    		if (session != null) {
    			session.getTransaction().rollback();
    		}
    		System.err.println("An error occured, changes have been rolled back.");
    		exception.printStackTrace();
    	}
    }
    
    @Override
    protected void serverClosed() {
    	session.close();   // close session when server closes
    }

    @Override
    protected synchronized void clientDisconnected(ConnectionToClient client) {
        // TODO Auto-generated method stub

        System.out.println("Client Disconnected.");
        super.clientDisconnected(client);
    }


    @Override
    protected void clientConnected(ConnectionToClient client) {
        super.clientConnected(client);
        System.out.println("Client connected: " + client.getInetAddress());
    }
    
    // get random object from array
    public static String getRandom(String[] array) {	// for picking random element from an array of Strings
	    int rnd = new Random().nextInt(array.length);
	    return array[rnd];
	}
    
    // round double to #places
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    
    private static void InitializeDataBase(){
    	SessionFactory sessionFactory = getSessionFactory();
		session = sessionFactory.openSession();
		Random random = new Random();
		String[] dates = {"1/6/2021", "2/6/2021", "3/6/2021", "4/6/2021", "5/6/2021", "6/6/2021", "7/6/2021"};
		String[] times = {"18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30"};
		String[] movie_names = {"Harry Potter 7", "Joker", "The Avengers", "Star Wars", "Inception", "The Dark Night", "Captain America", 
				"Avatar", "Jaws", "Rocky", "Titanic", "Lord Of The Rings"};
		String[] availability = {"AVAILABLE", "NOT_AVAILABLE"};
		for (int i = 0; i < 30; i++) {
			Show show = new Show(getRandom(dates), getRandom(times), random.nextBoolean(), getRandom(availability), 
					round(50 + (100 - 50) * random.nextDouble(),2), getRandom(movie_names), random.nextInt(49)+1, random.nextInt(5));
			// add show to database
			try {
				ShowsHandler.addShow(session, show);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Required argument: <port>");
        } else {
        	// initialize the DataBase
			InitializeDataBase();
            CinemaServer server = new CinemaServer(Integer.parseInt(args[0]));
            server.listen();
        }
    }

}
