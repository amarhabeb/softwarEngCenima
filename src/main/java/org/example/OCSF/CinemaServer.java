package org.example.OCSF;
import org.example.entities.*;
import org.example.entities.Package;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import java.time.LocalDate;
import java.time.LocalTime;

import org.example.Controllers.HallController;
import org.example.Controllers.ShowsHandler;
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
		
		configuration.addAnnotatedClass(Show.class);
		configuration.addAnnotatedClass(ChainManager.class);
		configuration.addAnnotatedClass(Cinema.class);
		configuration.addAnnotatedClass(CinemaManager.class);
		configuration.addAnnotatedClass(Complaint.class);
		configuration.addAnnotatedClass(ContentManager.class);
		configuration.addAnnotatedClass(Customer.class);
		configuration.addAnnotatedClass(CustomerService.class);
		configuration.addAnnotatedClass(Employee.class);
		configuration.addAnnotatedClass(Hall.class);
		configuration.addAnnotatedClass(Link.class);
		configuration.addAnnotatedClass(LinkMessage.class);
		configuration.addAnnotatedClass(Manager.class);
		configuration.addAnnotatedClass(Message.class);
		configuration.addAnnotatedClass(Movie.class);
		configuration.addAnnotatedClass(NewMovieMessage.class);
		configuration.addAnnotatedClass(Order.class);
		configuration.addAnnotatedClass(Package.class);
		configuration.addAnnotatedClass(Payment.class);
		configuration.addAnnotatedClass(Person.class);
		configuration.addAnnotatedClass(Refund.class);
		configuration.addAnnotatedClass(Regulations.class);
		configuration.addAnnotatedClass(ReminderMessage.class);
		configuration.addAnnotatedClass(Seat.class);
		configuration.addAnnotatedClass(Ticket.class);
		configuration.addAnnotatedClass(TicketMessage.class);
		configuration.addAnnotatedClass(UpdatePriceRequest.class);

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
		/*for (int hall_number = 0; hall_number < 50; hall_number++) {
			List<Seat> seats;
			Cinema cinema;
			Hall hall = new Hall(hall_number+1, random.nextInt(100)+75, random.nextInt(2), Hall.setMaxSeats(currentRegs), seats, cinema);
			 
            session.save(hall);
            session.flush();*/
            
		
		
		
	/*Halls*/
		private static void generateHalls(int amount) throws Exception {
			Random random = new Random();
			List<Seat> seats;
			Cinema cinema;
			for (int hall_number = 1; hall_number < amount; hall_number++) {
			Hall hall = new Hall(hall_number+1, random.nextInt(100)+75, random.nextInt(2)/*, Hall.setMaxSeats(currentRegs)*/, seats, cinema);
				session.save(hall);
				session.flush();
			}
		
            try {
				Hall hall;
				HallController.addHall(session, hall);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		
		
		
		/*Shows*/
		private static void generateShows(int amount) throws Exception {
			Random random = new Random();
			Hall hall;
			Movie movie;
			for (int show = 0; show < amount; show++) {
			Show show1 = new Show(getRandom(dates), getRandom(times),  random.nextBoolean(), getRandom(availability), round(50 + (100 - 50)) * random.nextDouble(),movie,hall);
				session.save(show1);
				session.flush();
			}
		
            try {
				Show show;
				ShowsHandler.addShow(session, show);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		static String HarryPotterCast() {
			String Cast="Emma Watson,Richard Griffiths,Harry Melling,Daniel Radcliffe,Julie Walters,Bonnie Wright,Rupert Grint";
					return Cast;
					
		}
		static String HarryPotterSummary() {
			String summary="As Harry, Ron, and Hermione race against time and evil to destroy the Horcruxes, they uncover the existence of the three most powerful objects in the wizarding world: the Deathly Hallows.  ";
			return summary;
		}
		
		
	static	String JokerCast() {
			String Cast="Joaquin Phoenix,  Robert De Niro, Zazie Beetz , Frances Conroy, Penny Fleck, Murray Franklin,Sophie Dumond,Frances Conroy, Brett Cullen";
					return Cast;
					
		}
		static String JokerSummary() {
			String summary="In Gotham City, mentally troubled comedian Arthur Fleck is disregarded and mistreated by society. He then embarks on a downward spiral of revolution and bloody crime. This path brings him face-to-face with his alter-ego: the Joker.\n  ";
			return summary;
		}
		static String TheAvengersCast(){
			String Cast="Robert Downey Jr. , Chris Evans, Mark Ruffalo,Chris Hemsworth,Scarlett Johansson ,Jeremy Renner, Tom Hiddleston ,Clark Gregg";
		}
		
		static String TheAvengersSummary(){
			String Summary="Captain America, the Stark Enterprises created super soldier. Thor, the god of thunder, protector of Earth and his home planet of Asgard, and Loki's brother. Master assassins Hawkeye and Natasha Romanoff. Together they will become a team to take on an attack that will call them to become the greatest of all time";
		}

		static  String StarWarsCast(){
				String Cast="Mark Hamill, Harrison Ford, Carrie Fisher, Peter Cushing, Alec Guinness, David Prowse, James Earl Jones, Anthony Daniels";
			}
		static String StarWarsSummary(){
				String Summary="The movie revolves around a civil war taking place \"in a galaxy far far away.\" The Rebels are fighting against the nefarious Darth Vader and his Imperial forces from the Galactic Empire, a tyrannical army intent on destroying civilizations across the universe";
			
			}
		static  String InceptionCast(){
				String Cast="Leonardo DiCaprio ,Ken Watanabe, Joseph Gordon-Levitt ,Marion Cotillard ,Elliot Page,Tom Hardy,Cillian Murphy,Tom Berenger,Michael Caine";
			}
		static  String InceptionSummary(){
				String Summary="Cobb a unique con artist can enter anyone's dreams and extract the most valuable ideas and secrets of people with the help of inception (a dream sharing technology) when the state of mind is at its vulnerable best. Cobbs ability has made him the face of corporate ";
			}
		static  String TheDarkKnightCast(){
				String Cast= "Christian Bale,Michael Caine,Heath Ledger,Gary Oldman,Aaron Eckhart,Maggie Gyllenhaal,Morgan Freeman";

			}
		 static String TheDarkKnightSummary(){
				String Summary="A gang of criminals robs a Gotham City mob bank, murdering each other for a higher share until only the Joker remains; he escapes with the money. Batman, District Attorney Harvey Dent and Lieutenant Jim Gordon form an alliance to rid Gotham of organized crime ";
			}
		 
		 static  String CaptainAmericaCast(){
				String Cast= " Chris Evans,Tommy Lee Jones,Hugo Weaving,Sebastian Stan,Dominic Cooper,Neal McDonough,Derek Luke,Stanley Tucci";
						
			}
		 static  String CaptainAmericaSummary(){
				String Summary="Steve Rogers, a rejected military soldier, transforms into Captain America after taking a dose of a Super-Soldier serum. But being Captain America comes at a price as he attempts to take down a war monger and a terrorist organization. ";
			}
		 static  String AvatarCast(){
				String Cast= "Sam Worthington,Zoe Saldana,Stephen Lang,Michelle Rodriguez,Sigourney Weaver";
							
			}
		 static  String AvatarSummary(){
				String Summary="On the lush alien world of Pandora live the Na'vi, beings who appear primitive but are highly evolved. Because the planet's environment is poisonous, human/Na'vi hybrids, called Avatars, must link to human minds to allow for free movement on Pandora. Jake Sully, a paralyzed former Marine, becomes mobile again through one such Avatar and falls in love with a Na'vi woman. As a bond with her grows, he is drawn into a battle for the survival of her world. ";
			}
		 static  String JawsCast(){
				String Cast= "Roy Scheider,Robert Shaw,Richard Dreyfuss,Lorraine Gary,Murray Hamilton";
			
			}
		 static  String JawsSummary(){
				String Summary="The peaceful community of Amity island is being terrorized. There is something in the sea that is attacking swimmers. They can no longer enjoy the sea and the sun as they used to, and the spreading fear is affecting the numbers of tourists that are normally attracted to this island. After many attempts the great white shark won't go away and sheriff Brody, with friends Hooper and Quint decide to go after the shark and kill it. ";
			}
		 static  String RockyCast(){
				String Cast= "Sylvester Stallone,Talia Shire,Burt Young,Carl Weathers,Burgess Meredith";
			}
		 static  String RockySummary(){
				String Summary="Rocky Balboa, a small-time boxer from working-class Philadelphia, is arbitrarily chosen to take on the reigning world heavyweight champion, Apollo Creed, when the undefeated fighter's scheduled opponent is injured. While training with feisty former bantamweight contender Mickey Goldmill, Rocky tentatively begins a relationship with Adrian, the wallflower sister of his meat-packer pal Paulie.";
			}
		 static  String TitanicCast(){
				String Cast= "Leonardo DiCaprio,Kate Winslet,Billy Zane,Kathy Bates,Frances Fisher,Bernard Hill,Jonathan Hyde,Danny Nucci,David Warner,Bill Paxton";
			}
		 static  String TitanicSummary(){
				String Summary="Deep on the bottom of the sea, some 3,800 metres below the surface of the freezing Atlantic Ocean, lies the wreckage of a ship: the unmistakable carcass of the Titanic, once man's grandest mechanical achievement, now stripped of its former glory. Almost one long century later, intrigued by Titanic's hidden riches, the modern treasure hunter, Brock Lovett, and his well-equipped technical crew, find themselves in the middle of the ocean, digging for answers for the past three years; nevertheless, without any success. But, when centenarian Rose Calvert, one of the few survivors of the Titanic, learns about this ambitious crusade, she decides to unfold her incredible and utterly tragic story; one that intertwines the extraordinary journey of the exquisite, deep-blue Heart-of-the-Ocean diamond of King Louis XVI, with the unlikely romance of Rose and the young bohemian vagabond, Jack Dawson. Now, an emotional trip down memory lane awaits. Can the mistakes of the past teach a lesson in humility to paupers and royalty alike?";
			}
		 static  String LordOfTheRingsCast(){
				String Cast= "Sylvester Stallone,Talia Shire,Burt Young,Carl Weathers,Burgess Meredith";
			}
		 static String LordOfTheRingsSummary(){
				String Summary="A meek Hobbit from the Shire and eight companions set out on a journey to destroy the powerful One Ring and save Middle-earth from the Dark Lord Sauron. ";
			}
		private static void generateMovies(int amount) throws Exception {
			
		     Movie HarryPotter7= new Movie ("Harry Potter 7", "הארי פוטר 7", "David Yates",HarryPotterCast(),HarryPotterSummary(),"18/07/2020",0, String image, shows); 
             Movie Joker=new Movie("Joker","גוקר","Todd Phillips",JokerCast(), JokerSummary(), "31/08/2019",0,shows);
		     Movie TheAvengers=new Movie("The Avengers","הנוקמים","Kevin Feige",TheAvengersCast(), TheAvengersSummary(), "14/08/2020",1,shows);
		     Movie StarWars=new Movie("Star Wars","מלחמת הכוכבים","George Lucas",StarWarsCast(), StarWarsSummary(), "14/08/2020",1,shows);
		     Movie Inception=new Movie("Incepteion","התחלה","Emma Thomas",InceptionCast(), InceptionSummary(), "14/08/2020",1,shows);
		     Movie TheDarKnight=new Movie("The Dark Knight","האביר האפל","Emma Thomas,Charles Roven,Christopher Nolan",TheDarkNightCast(), TheDarkNightSummary(), "14/08/2020",1,shows);
		     Movie CaptainAmerica=new Movie("Captain America","קפטן אמריקה","Kevin Feige",CaptainAmericaCast(), CaptainAmericaSummary(), "14/08/2020",1,shows);
		     Movie Avatar=new Movie("Avatar","אווטאר","James Cameron,Jon Landau",AvatarCast(), AvatarSummary(), "14/08/2020",1,shows);
		     Movie Jaws=new Movie("Jaws","מלתעות","	Steven Spielberg",JawsCast(), JawsSummary(), "14/08/2020",1,shows);
		     Movie Rocky=new Movie("Rocky","רוקי","John G. Avildsen",RockyCast(), RockySummary(), "14/08/2020",1,shows);
		     Movie Titanic=new Movie("Titanic","טיטניק","James Cameron",TitanicCast(), TitanicSummary(), "14/08/2020",1,shows);
		     Movie LordOfTheRings=new Movie("Lord Of The Rings","שר הטבעות","Peter Jackson",LordOfTheRingsCast(), LordOfTheRingsSummary(), "14/08/2020",1,shows);

		     String[] movie_names_1= {"HarryPotter7","Joker","TheAvengers,StarWars,Inception,TheDarKnight,CaptainAmerica,Avatar,Jaws,Rocky,Titanic,LordOfTheRing"};
		     private List<Movie> movies = new ArrayList<Movie>();//A list that has all movies and thie attributes
		     movies.add(HarryPotter7); 
		     movies.add(Joker); 
		     movies.add(TheAvengers); 
		     movies.add(Inception); 
		     movies.add(TheDarKnight); 
		     movies.add(CaptainAmerica); 
		     movies.add(Avatar); 
		     movies.add(Jaws); 
		     movies.add(Rocky); 
		     movies.add(Titanic); 
		     movies.add(LordOfTheRings); 

			
			/*Hall hall;
			Movie movie;
			for (int show = 0; show < amount; show++) {
			Show show = new Show(getRandom(movienames), getRandom(times),  random.nextBoolean(), getRandom(availability), round(50 + (100 - 50)) * random.nextDouble(),movie,hall);
				session.save(show);
				session.flush();
			}
		
            try {
				ShowsHandler.addShow(session, hall);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
		
		// Initialize shows
	/*	for (int i = 0; i < 30; i++) {
			Show show = new Show(getRandom(dates), getRandom(times), random.nextBoolean(), getRandom(availability), 
					round(50 + (100 - 50) * random.nextDouble(),2), 
					// corresponding Hall, Movie and Cinema// 
					);
			// add show to database
			try {
				ShowsHandler.addShow(session, show);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	}

    
*/
	public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Required argument: <port>");
        } else {
        		 try {
        			 SessionFactory sessionFactory = getSessionFactory();
        			 session = sessionFactory.openSession();
        			 session.beginTransaction();

        			 generateHalls(50);
        			 generateShows(30);
        			 
        			 int index;
        			 Random rnd = new Random();
        			 
        			 for(Show show:loadShow()) {     /*one to many movies-shows*/
        				 List<Movie>AllMovies=loadMovies();
        				 index=rnd.nextInt(AllMovies.size());
        				 Movie movie =AllMovies.get(index);
        				 show.setMovie(movie);
        				 movie.getShows().add(show);
        			 }
        			 
        			 for(Show show:loadShow()) {      /*Hall-Show*/
        				 List<Hall>AllHalls=loadHalls();
        				 List<Show> AllShows=loadShows();
        				 index=rnd.nextInt(AllShows.size());
        				 Hall hall=AllHalls.get(index); //we choose one hall
        				 show.setHall(hall); //for each show we have one hall
        				 hall.getShows.add(show); //and for each hall we have many shows
       			 }
        			 for(Cinema cinema:loadCinemas()) /*each cinema has one cinema manager*/
        			 {
        				 List<CinemaManager>managers=loadCinemaManager();/*??*/
        				 List<Cinema>cinemas=loadCinemas();
        				 index = rnd.nextInt(managers.size());        	 
        				 CinemaManager manager= managers.get(index);
        				 cinema.setmanager(manager);
        				 manager.setCinema(cinema);
        				 
        			 }
        			 for(Ticket ticket:loadTicket()) {     /*Shows-tickets*/
        				 List<Show>Allshow=loadShows();
        				 index=rnd.nextInt(Allshow.size());
        				 Show show =Allshow.get(index);
        				 ticket.setShow(show);
        				 show.getTickets().add(ticket);
        			 }
        			 for(Seat seat:loadSeats()) {     /*Seats-Halls*/
        				 List<Hall>AllHall=loadHalls();
        				 index=rnd.nextInt(AllHall.size());
        				 Hall hall =AllHall.get(index);
        				 seat.setHall(hall);
        				 hall.getSeats().add(seat);
        			 }
        			 for(Link link:loadLinks()) {     /*Links-Shows*/
        				 List<Show>AllShow=loadShows();
        				 index=rnd.nextInt(AllShow.size());
        				 Show show =AllShow.get(index);
        				 link.setLink(link);
        				 show.getLinks().add(link);/*add link*/
        			 }
        			 
        			 for(Show show:loadShow()) {     /*cinema-shows*/
        				 List<Cinema>AllCinemas=loadCinema();
        				 index=rnd.nextInt(AllCinemas.size());
        				 Cinema cinema =AllCinemas.get(index);
        				 show.setCinema(cinema);
        				 cinema.getShows().add(show);
        			 }
        			 for(Cinema cinema:loadCinemas()) /*Cinema-Regulation*/
        			 {
        				 List<Regulations>AllReg=loadRegulations();/*??*/
        				 List<Cinema>cinemas=loadCinemas();
        				 index = rnd.nextInt(AllReg.size());        	 
        				 Regulations reg= AllReg.get(index);
        				 cinema.setRegulation(reg);
        				 reg.setCinema(cinema);
        				 
        			 }
        			 for(Complaint complaint:loadComplaints()) {     /*oCustomer-Complaint????*/
        				 List<Customer>AllCustomers=loadCustomers();
        				 index=rnd.nextInt(AllCustomers.size());
        				 Customer customer =AllCustomers.get(index);
        				 complaint.setCustomer(customer);
        				 customer.getComplaint().add(complaint);
        			 }

        			 session.getTransaction().commit(); // Save everything.

        		 } catch (Exception exception) {
        			 if (session != null) {
        				 session.getTransaction().rollback();
        			 }
        			 System.err.println("An error occured, changes have been rolled back.");
        			 exception.printStackTrace();
        		 } finally {
        			 session.close();
        		 }
        		}			InitializeDataBase();
            CinemaServer server = new CinemaServer(Integer.parseInt(args[0]));
            server.listen();
        
    }

}
