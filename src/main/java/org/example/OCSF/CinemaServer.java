package org.example.OCSF;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
import java.time.LocalTime;

import org.example.Controllers.*;
import org.example.LinksController;
import org.example.entities.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import java.util.List;




public class CinemaServer extends AbstractServer{
	
	public static Regulations currentRegs = null;	// this is the regulations of the cinema chain
	
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


    @SuppressWarnings("unchecked")
	@Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
    	System.out.println("message reached server");
    	LinkedList<Object> message = (LinkedList<Object>)(msg);
    	try {
    		if(message.get(0).equals("ChangeShowTime")) {
    			int show_id = (int) message.get(1);
    			LocalTime newTime = (LocalTime) message.get(2);
    			// change time of show in database
    			boolean success = ShowsHandler.updateTime(session, show_id, newTime);
    			//session.refresh(Show.class);
    			if(!success) {
    				throw new Exception("Show's time couldnt be updated");
    			}
    			// reply to client	
				LinkedList<Object> messageToClient = new LinkedList<Object>();
				messageToClient.add("ShowsTimeChanged");
				client.sendToClient(messageToClient);
    		}
    		
    		if(message.get(0).equals("ChangePriceRequest")) {
    			int show_id = (int) message.get(1);
    			double newPrice = (double) message.get(2);
    			// change price of show in database
    			boolean success = ShowsHandler.updatePrice(session, show_id, newPrice);
    			//session.refresh(Show.class);
    			if(!success) {
    				throw new Exception("Show's price couldnt be updated");
    			}
    			// reply to client	
				LinkedList<Object> messageToClient = new LinkedList<Object>();
				messageToClient.add("ShowsPriceChanged");
				client.sendToClient(messageToClient);
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

			if(message.get(0).equals("LoadComplaints")) {
				// load data
				List<Complaint> Data = ComplaintsController.loadComplaints(session);
				try {
					// reply to client
					LinkedList<Object> messageToClient = new LinkedList<Object>();
					messageToClient.add("ComplaintesLoaded");
					messageToClient.add(Data);
					client.sendToClient(messageToClient);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if(message.get(0).equals("MarkComplaintAsDone")) {
				int complaint_id = (int) message.get(1);
				// change complaine into done in database
				boolean success = ComplaintsController.markComplaintAsDone(session,complaint_id );
				//session.refresh(Complaint.class);
				if(!success) {
					throw new Exception("Complaint's status couldnt be changed");
				}
				// reply to client
				LinkedList<Object> messageToClient = new LinkedList<Object>();
				messageToClient.add("MarkComplaintMarkedAsDone");
				client.sendToClient(messageToClient);
			}

			if(message.get(0).equals("AddComplaint")) {
				Complaint comp = (Complaint) message.get(1);
				// adding complaine into  database
				boolean success = ComplaintsController.addComplaint(session,comp );
				//session.refresh(Complaint.class);
				if(!success) {
					throw new Exception("Complaint  couldnt be added");
				}
				// reply to client
				LinkedList<Object> messageToClient = new LinkedList<Object>();
				messageToClient.add("ComplaintAdded");
				client.sendToClient(messageToClient);
			}

			if(message.get(0).equals("LoadLinks")) {
				// load data
				List<Link> Data = LinksController.loadLinks(session);
				try {
					// reply to client
					LinkedList<Object> messageToClient = new LinkedList<Object>();
					messageToClient.add("ShowsLinksLoaded");
					messageToClient.add(Data);
					client.sendToClient(messageToClient);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if(message.get(0).equals("LoadCostumersLinks")) {
				// load data
				int cost_id = (int)message.get(1);
				List<Link> Data = LinksController.loadCostumersLinks(cost_id,session);
				try {
					// reply to client
					LinkedList<Object> messageToClient = new LinkedList<Object>();
					messageToClient.add("Costumer'sLinksLoaded");
					messageToClient.add(Data);
					client.sendToClient(messageToClient);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if(message.get(0).equals("LoadMovies")) {
				// load data
				List<Movie> Data = MoviesHandler.loadMovies(session);
				try {
					// reply to client
					LinkedList<Object> messageToClient = new LinkedList<Object>();
					messageToClient.add("MoviesLoaded");
					messageToClient.add(Data);
					client.sendToClient(messageToClient);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if(message.get(0).equals("AddMovie")) {
				Movie newMovie = (Movie) message.get(1);
				// adding a movie into  database
				boolean success = MoviesHandler.addMovie(session,newMovie );
				//session.refresh(Movie.class);
				if(!success) {
					throw new Exception("Movie  couldnt be added");
				}
				// reply to client
				LinkedList<Object> messageToClient = new LinkedList<Object>();
				messageToClient.add("MovieAdded");
				client.sendToClient(messageToClient);
			}

			if(message.get(0).equals("DeleteMovie")) {
				int movie_id = (int) message.get(1);
				// delete movie from database
				boolean success = MoviesHandler.deleteMovie(session,movie_id );
				//session.refresh(Movie.class);
				if(!success) {
					throw new Exception("the Movie couldnt be deleted");
				}
				// reply to client
				LinkedList<Object> messageToClient = new LinkedList<Object>();
				messageToClient.add("MovieDeleted");
				client.sendToClient(messageToClient);
			}

			if(message.get(0).equals("LoadOrders")) {
				// load data
				List<Order> Data = OrderController.loadOrders(session);
				try {
					// reply to client
					LinkedList<Object> messageToClient = new LinkedList<Object>();
					messageToClient.add("OrderssLoaded");
					messageToClient.add(Data);
					client.sendToClient(messageToClient);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if(message.get(0).equals("LoadCutomersOrders")) {
				// load data
				int cost_id = (int)message.get(1);
				List<Order> Data = OrderController.loadCutomersOrders(cost_id,session);
				try {
					// reply to client
					LinkedList<Object> messageToClient = new LinkedList<Object>();
					messageToClient.add("Costumer'sOrdersLoaded");
					messageToClient.add(Data);
					client.sendToClient(messageToClient);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if(message.get(0).equals("AddOrder")) {
				Order order = (Order) message.get(1);
				// adding a order into  database
				boolean success = OrderController.addOrder(order,session );
				//session.refresh(Order.class);
				if(!success) {
					throw new Exception("Order  couldnt be added");
				}
				// reply to client
				LinkedList<Object> messageToClient = new LinkedList<Object>();
				messageToClient.add("OrderAdded");
				client.sendToClient(messageToClient);
			}

			if(message.get(0).equals("RemoveOrder")) {
				int order_id = (int) message.get(1);
				// delete movie from database
				boolean success = OrderController.removeOrder(order_id,session);
				//session.refresh(Movie.class);
				if(!success) {
					throw new Exception("the Order couldnt be deleted");
				}
				// reply to client
				LinkedList<Object> messageToClient = new LinkedList<Object>();
				messageToClient.add("OrderDeleted");
				client.sendToClient(messageToClient);
			}

			if(message.get(0).equals("MakePayment")) {
				Payment payment = (Payment) message.get(1);
				// putting payment details in database
				boolean success = PaymentController.makePayment(session, payment );
				//session.refresh(Payment.class);
				if(!success) {
					throw new Exception("Payment Not Success");
				}
				// reply to client
				LinkedList<Object> messageToClient = new LinkedList<Object>();
				messageToClient.add("PaymentSuccess");
				client.sendToClient(messageToClient);
			}

			if(message.get(0).equals("LoadRefunds")) {
				// load data
				List<Refund> Data = RefundController.loadRefunds(session);
				try {
					// reply to client
					LinkedList<Object> messageToClient = new LinkedList<Object>();
					messageToClient.add("RefundssLoaded");
					messageToClient.add(Data);
					client.sendToClient(messageToClient);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if(message.get(0).equals("AddRefund")) {
				Refund refund = (Refund) message.get(1);
				// adding a refund into done in database
				boolean success = RefundController.addRefund(session, refund );
				//session.refresh(Refund.class);
				if(!success) {
					throw new Exception("Refund  couldnt be added");
				}
				// reply to client
				LinkedList<Object> messageToClient = new LinkedList<Object>();
				messageToClient.add("RefundAdded");
				client.sendToClient(messageToClient);
			}

			if(message.get(0).equals("LoadReagulations")) {
				// load data
				List<Regulations> Data = RegulationsController.loadReagulations(session);
				try {
					// reply to client
					LinkedList<Object> messageToClient = new LinkedList<Object>();
					messageToClient.add("RegulationsLoaded");
					messageToClient.add(Data);
					client.sendToClient(messageToClient);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if(message.get(0).equals("ActivateRegulations")) {
				int Y = (int) message.get(1);
				// change status into true in database
				boolean success = RegulationsController.activateRegulations(session,Y );
				//session.refresh(Regulation.class);
				if(!success) {
					throw new Exception("Regulation status couldnt be changed");
				}
				// reply to client
				LinkedList<Object> messageToClient = new LinkedList<Object>();
				messageToClient.add("RegulationStatusUpdated");
				client.sendToClient(messageToClient);
			}

			if(message.get(0).equals("DeActivateRegulations")) {
				// change status into false in database
				boolean success = RegulationsController.deactivateRegulations(session );
				//session.refresh(Regulation.class);
				if(!success) {
					throw new Exception("Regulation status couldnt be changed");
				}
				// reply to client
				LinkedList<Object> messageToClient = new LinkedList<Object>();
				messageToClient.add("RegulationStatusUpdated");
				client.sendToClient(messageToClient);
			}

			if(message.get(0).equals("AddShow")) {
				Show show = (Show) message.get(1);
				// adding show into  database
				boolean success = ShowsHandler.addShow(session,show );
				//session.refresh(Show.class);
				if(!success) {
					throw new Exception("Show  couldnt be added");
				}
				// reply to client
				LinkedList<Object> messageToClient = new LinkedList<Object>();
				messageToClient.add("ShowAdded");
				client.sendToClient(messageToClient);
			}

			if(message.get(0).equals("DeleteShow")) {
				int show_id = (int) message.get(1);
				// delete Show from database
				boolean success = ShowsHandler.deleteShow(session,show_id );
				//session.refresh(Show.class);
				if(!success) {
					throw new Exception("the Show couldnt be deleted");
				}
				// reply to client
				LinkedList<Object> messageToClient = new LinkedList<Object>();
				messageToClient.add("ShowDeleted");
				client.sendToClient(messageToClient);
			}

			if(message.get(0).equals("LoadTickets")) {
				// load data
				List<Ticket> Data = TicketsController.loadTickets(session);
				try {
					// reply to client
					LinkedList<Object> messageToClient = new LinkedList<Object>();
					messageToClient.add("MoviesLoaded");
					messageToClient.add(Data);
					client.sendToClient(messageToClient);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if(message.get(0).equals("LoadCustomersTickets")) {
				// load data
				int cost_id = (int)message.get(1);
				List<Ticket> Data = TicketsController.loadCustomersTickets(session, cost_id);
				try {
					// reply to client
					LinkedList<Object> messageToClient = new LinkedList<Object>();
					messageToClient.add("Costumer'sTicketsLoaded");
					messageToClient.add(Data);
					client.sendToClient(messageToClient);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if(message.get(0).equals("AddTicket")) {
				Ticket newticket = (Ticket) message.get(1);
				// adding tickit into  database
				boolean success = TicketsController.addTicket(newticket,session );
				//session.refresh(Ticket.class);
				if(!success) {
					throw new Exception("Ticket  couldnt be added");
				}
				// reply to client
				LinkedList<Object> messageToClient = new LinkedList<Object>();
				messageToClient.add("TicketAdded");
				client.sendToClient(messageToClient);
			}

			if(message.get(0).equals("LoadUpdatePriceRequest")) {
				// load data
				List<UpdatePriceRequest> Data = UpdatePriceRequestController.loadRequest(session);
				try {
					// reply to client
					LinkedList<Object> messageToClient = new LinkedList<Object>();
					messageToClient.add("MoviesLoaded");
					messageToClient.add(Data);
					client.sendToClient(messageToClient);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if(message.get(0).equals("ApproveRequest")) {
				int request_id = (int) message.get(1);
				// changing price in database
				boolean success = UpdatePriceRequestController.approveRequest(session,request_id );
				//session.refresh(UpdatePriceRequest.class);
				if(!success) {
					throw new Exception("the price couldnt be changed");
				}
				// reply to client
				LinkedList<Object> messageToClient = new LinkedList<Object>();
				messageToClient.add("PriceChanged");
				client.sendToClient(messageToClient);
			}

			if(message.get(0).equals("DclineRequest")) {
				int request_id = (int) message.get(1);
				// not changing price in database
				boolean success = UpdatePriceRequestController.declineRequest(session,request_id );
				//session.refresh(UpdatePriceRequest.class);
				if(!success) {
					throw new Exception("the request couldnt be declined");
				}
				// reply to client
				LinkedList<Object> messageToClient = new LinkedList<Object>();
				messageToClient.add("RequestDeclined");
				client.sendToClient(messageToClient);
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
		
		// INITIALIZERS //
		
		//*MISSING CODE*//
		
		// Initialize halls
		for (int hall_number = 0; hall_number < 50; hall_number++) {
			Hall hall = new Hall(hall_number+1, random.nextInt(100)+75, random.nextInt(2), currentRegs);
			// add hall to database
			try {
				HallController.addHall(session, hall);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// Initialize shows
//		for (int i = 0; i < 30; i++) {
//			Show show = new Show(getRandom(dates), getRandom(times), random.nextBoolean(), getRandom(availability), 
//					round(50 + (100 - 50) * random.nextDouble(),2), 
//					// corresponding Hall, Movie and Cinema// 
//					);
//			// add show to database
//			try {
//				ShowsHandler.addShow(session, show);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
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
