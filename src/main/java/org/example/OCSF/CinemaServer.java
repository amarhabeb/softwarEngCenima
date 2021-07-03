package org.example.OCSF;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Random;
import java.time.LocalTime;

import org.example.Controllers.*;
import org.example.entities.*;
import org.example.init;
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
    			boolean success = ShowsController.updateTime(session, show_id, newTime);
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
    			boolean success = ShowsController.updatePrice(session, show_id, newPrice);
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
    			List<Show> Data = ShowsController.loadShows(session);
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
				List<Movie> Data = MoviesController.loadMovies(session);
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
				boolean success = MoviesController.addMovie(session,newMovie );
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
				boolean success = MoviesController.deleteMovie(session,movie_id );
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
				boolean success = ShowsController.addShow(session,show );
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
				boolean success = ShowsController.deleteShow(session,show_id );
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

    /********************************************/
    
    private static void InitializeDataBase(){
    	SessionFactory sessionFactory = getSessionFactory();
		session = sessionFactory.openSession();
		generateMovies(session);


	}



	private static void generateMovies(Session session) throws Exception {
    	try {
    		Cinema cinema1 = new Cinema();
    		List<Movie> moviesList = null;
    		List<Show> emptyShowList = null;
    		/*when we create the movie we give its empty list of shows
    		and when we create the shows list we set its in the movie
    		 */
			Movie HarryPotter7= new Movie ("Harry Potter 7", "הארי פוטר 7", "David Yates", init.HarryPotterCast(),init.HarryPotterSummary(),"18/07/2020",0,  image, emptyShowList);
			moviesList.add(HarryPotter7);
			Movie Joker=new Movie("Joker","גוקר","Todd Phillips",init.JokerCast(), init.JokerSummary(), "31/08/2019",0, image, emptyShowList);
			moviesList.add(Joker);
			Movie TheAvengers=new Movie("The Avengers","הנוקמים","Kevin Feige",init.TheAvengersCast(), init.TheAvengersSummary(), "14/08/2020",1, image, emptyShowList);
			moviesList.add(TheAvengers);
			Movie StarWars=new Movie("Star Wars","מלחמת הכוכבים","George Lucas",init.StarWarsCast(), init.StarWarsSummary(), "14/08/2020",1, image, emptyShowList);
			moviesList.add(StarWars);
			Movie Inception=new Movie("Incepteion","התחלה","Emma Thomas",init.InceptionCast(), init.InceptionSummary(), "14/08/2020",1, image, emptyShowList);
			moviesList.add(Inception);
			Movie TheDarKnight=new Movie("The Dark Knight","האביר האפל","Emma Thomas,Charles Roven,Christopher Nolan",init.TheDarkKnightCast(), init.TheDarkKnightSummary(), "14/08/2020",1, image, emptyShowList);
			moviesList.add(TheDarKnight);
			Movie CaptainAmerica=new Movie("Captain America","קפטן אמריקה","Kevin Feige",init.CaptainAmericaCast(), init.CaptainAmericaSummary(), "14/08/2020",1, image, emptyShowList);
			moviesList.add(CaptainAmerica);
			Movie Avatar=new Movie("Avatar","אווטאר","James Cameron,Jon Landau",init.AvatarCast(), init.AvatarSummary(), "14/08/2020",1, image, emptyShowList);
			moviesList.add(Avatar);
			Movie Jaws=new Movie("Jaws","מלתעות","Steven Spielberg",init.JawsCast(), init.JawsSummary(), "14/08/2020",1, image, emptyShowList);
			moviesList.add(Jaws);
			Movie Rocky=new Movie("Rocky","רוקי","John G. Avildsen",init.RockyCast(), init.RockySummary(), "14/08/2020",1, image, emptyShowList);
			moviesList.add(Rocky);
			Movie Titanic=new Movie("Titanic","טיטניק","James Cameron",init.TitanicCast(), init.TitanicSummary(), "14/08/2020",1, image, emptyShowList);
			moviesList.add(Titanic);
			Movie LordOfTheRings=new Movie("Lord Of The Rings","שר הטבעות","Peter Jackson",init.LordOfTheRingsCast(), init.LordOfTheRingsSummary(), "14/08/2020",1, image, emptyShowList);
			moviesList.add(LordOfTheRings);
			cinema1.setMovies(moviesList);


			List<Hall> cinemaHalls = null;
			for (int i=1; i<=5;i++){
				Hall cinemaHall = new Hall(i, 2*i*10, cinema1);
				cinemaHalls.add(cinemaHall);
				HallController.addHall(CinemaServer.session,cinemaHall);
			}
			int[] days = {1,2,3,4,5,6,7};
			int[] months = {6,6,6,6,6,6,6,};
			int[] years = {2021,2021,2021,2021,2021,2021,2021};

			int[] hours = {18,18,19,19,20,20,21,21,22,22};
			int[] minutes = {00,30,00,30,00,30,00,30,00,30,};


			String[] availability = {"AVAILABLE", "NOT_AVAILABLE"};





			for (int i=0; i<2; i++){
				for (int j=0; j<3; j++){
					Show show = new Show( LocalDateTime.of(years[i],months[i],days[i], hours[j],minutes[j]), (i+j)%2==0, availability[(i+j)%2], 60, HarryPotter7, cinemaHalls.get((i+j)%5));
					cinema1.addShow(show);
					HarryPotter7.addShow(show);
					ShowsController.addShow(CinemaServer.session,show);
				}
			}
			MoviesController.addMovie(CinemaServer.session,HarryPotter7);

			for (int i=0; i<2; i++){
				for (int j=0; j<3; j++){
					Show show = new Show( LocalDateTime.of(years[i],months[i],days[i], hours[j],minutes[j]), (i+j)%2==0, availability[(i+j)%2], 100, Joker, cinemaHalls.get((i+j)%5+1));
					cinema1.addShow(show);
					Joker.addShow(show);
					ShowsController.addShow(CinemaServer.session,show);
				}
			}
			MoviesController.addMovie(CinemaServer.session,Joker);


			for (int i=0; i<2; i++){
				for (int j=3; j<6; j++){
					Show show = new Show( LocalDateTime.of(years[i],months[i],days[i], hours[j],minutes[j]), (i+j)%2==0, availability[(i+j)%2], 90, TheAvengers, cinemaHalls.get((i+j)%5));
					cinema1.addShow(show);
					TheAvengers.addShow(show);
					ShowsController.addShow(CinemaServer.session,show);
				}
			}
			MoviesController.addMovie(CinemaServer.session,TheAvengers);


			for (int i=0; i<2; i++){
				for (int j=3; j<6; j++){
					Show show = new Show( LocalDateTime.of(years[i],months[i],days[i], hours[j],minutes[j]), (i+j)%2==0, availability[(i+j)%2], 70, StarWars, cinemaHalls.get((i+j)%5+1));
					cinema1.addShow(show);
					StarWars.addShow(show);
					ShowsController.addShow(CinemaServer.session,show);
				}
			}
			MoviesController.addMovie(CinemaServer.session,StarWars);

			for (int i=0; i<2; i++){
				for (int j=6; j<10; j++){
					Show show = new Show( LocalDateTime.of(years[i],months[i],days[i], hours[j],minutes[j]), (i+j)%2==0, availability[(i+j)%2], 120, Inception, cinemaHalls.get((i+j)%5));
					cinema1.addShow(show);
					Inception.addShow(show);
					ShowsController.addShow(CinemaServer.session,show);
				}
			}
			MoviesController.addMovie(CinemaServer.session,Inception);

			for (int i=0; i<2; i++){
				for (int j=6; j<10; j++){
					Show show = new Show( LocalDateTime.of(years[i],months[i],days[i], hours[j],minutes[j]), (i+j)%2==0, availability[(i+j)%2], 80, TheDarKnight, cinemaHalls.get((i+j)%5+1));
					cinema1.addShow(show);
					TheDarKnight.addShow(show);
					ShowsController.addShow(CinemaServer.session,show);
				}
			}
			MoviesController.addMovie(CinemaServer.session,TheDarKnight);

			for (int i=2; i<4; i++){
				for (int j=0; j<3; j++){
					Show show = new Show( LocalDateTime.of(years[i],months[i],days[i], hours[j],minutes[j]), (i+j)%2==0, availability[(i+j)%2], 90, CaptainAmerica, cinemaHalls.get((i+j)%5));
					cinema1.addShow(show);
					CaptainAmerica.addShow(show);
					ShowsController.addShow(CinemaServer.session,show);
				}
			}
			MoviesController.addMovie(CinemaServer.session,CaptainAmerica);

			for (int i=2; i<4; i++){
				for (int j=0; j<3; j++){
					Show show = new Show( LocalDateTime.of(years[i],months[i],days[i], hours[j],minutes[j]), (i+j)%2==0, availability[(i+j)%2], 95, Avatar, cinemaHalls.get((i+j)%5+1));
					cinema1.addShow(show);
					Avatar.addShow(show);
					ShowsController.addShow(CinemaServer.session,show);
				}
			}
			MoviesController.addMovie(CinemaServer.session,Avatar);

			for (int i=2; i<4; i++){
				for (int j=3; j<6; j++){
					Show show = new Show( LocalDateTime.of(years[i],months[i],days[i], hours[j],minutes[j]), (i+j)%2==0, availability[(i+j)%2], 45, Jaws, cinemaHalls.get((i+j)%5));
					cinema1.addShow(show);
					Jaws.addShow(show);
					ShowsController.addShow(CinemaServer.session,show);
				}
			}
			MoviesController.addMovie(CinemaServer.session,Jaws);

			for (int i=2; i<4; i++){
				for (int j=3; j<6; j++){
					Show show = new Show( LocalDateTime.of(years[i],months[i],days[i], hours[j],minutes[j]), (i+j)%2==0, availability[(i+j)%2], 90, Rocky, cinemaHalls.get((i+j)%5+1));
					cinema1.addShow(show);
					Rocky.addShow(show);
					ShowsController.addShow(CinemaServer.session,show);
				}
			}
			MoviesController.addMovie(CinemaServer.session,Rocky);

			for (int i=2; i<4; i++){
				for (int j=6; j<10; j++){
					Show show = new Show( LocalDateTime.of(years[i],months[i],days[i], hours[j],minutes[j]), (i+j)%2==0, availability[(i+j)%2], 40, Titanic, cinemaHalls.get((i+j)%5));
					cinema1.addShow(show);
					Titanic.addShow(show);
					ShowsController.addShow(CinemaServer.session,show);
				}
			}
			MoviesController.addMovie(CinemaServer.session,Titanic);

			for (int i=2; i<4; i++){
				for (int j=6; j<10; j++){
					Show show = new Show( LocalDateTime.of(years[i],months[i],days[i], hours[j],minutes[j]), (i+j)%2==0, availability[(i+j)%2], 80, LordOfTheRings, cinemaHalls.get((i+j)%5+1));
					cinema1.addShow(show);
					LordOfTheRings.addShow(show);
					ShowsController.addShow(CinemaServer.session,show);
				}
			}
			MoviesController.addMovie(CinemaServer.session,LordOfTheRings);
			CinemaController.addCinema(CinemaServer.session,cinema1);



		} catch (Exception e) {
			e.printStackTrace();
		}

	}



		/***************************************/




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
