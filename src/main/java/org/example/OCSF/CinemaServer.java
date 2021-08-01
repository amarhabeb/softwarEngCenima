package org.example.OCSF;

import org.example.Controllers.*;
import org.example.entities.*;
import org.example.init;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.*;

public class CinemaServer extends AbstractServer{
	
	public static Regulations currentRegs = null;	// this is the regulations of the cinema chain
	
	private static Session session;
	private static Thread activateThread;
	private static Thread packageMsgTHr;

	public static Object threadLock = new Object();





	private static SessionFactory getSessionFactory() throws HibernateException {
		Configuration configuration = new Configuration();

		// Add ALL of your entities here. You can also try adding a whole package.
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
		configuration.addAnnotatedClass(Manager.class);
		configuration.addAnnotatedClass(Movie.class);
		configuration.addAnnotatedClass(Order.class);
		configuration.addAnnotatedClass(PackageOrder.class);
		configuration.addAnnotatedClass(Payment.class);
		configuration.addAnnotatedClass(Person.class);
		configuration.addAnnotatedClass(Refund.class);
		configuration.addAnnotatedClass(Regulations.class);
		configuration.addAnnotatedClass(Seat.class);
		configuration.addAnnotatedClass(Ticket.class);
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
		synchronized (threadLock) {
			LinkedList<Object> message = (LinkedList<Object>) (msg);
			System.out.println("Message = " + message.get(0) + ", reached server");
			try {


				if (message.get(0).equals("ChangeShowTime")) {
					int show_id = (int) message.get(1);
					LocalTime newTime = (LocalTime) message.get(2);
					// change time of show in database
					session.clear();
					boolean success = ShowsController.updateTime(session, show_id, newTime);
					//session.refresh(Show.class);

					// reply to client
					LinkedList<Object> messageToClient = new LinkedList<Object>();
					messageToClient.add("ShowsTimeChanged");
					messageToClient.add(success);
					client.sendToClient(messageToClient);
				}


				if (message.get(0).equals("LoadShows")) {
					// load data
					try {
						session.clear();
						List<Show> Data = ShowsController.loadShows(session);

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

				if (message.get(0).equals("LoadComplaints")) {
					// load data
					try {
						session.clear();
						List<Complaint> Data = ComplaintsController.loadComplaints(session);

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

//			if(message.get(0).equals("LoadPeople")) {
//				// load data
//				try {
//					session.clear();
//					List<Person> Data = PeopleController.loadPeople(session);
//
//					// reply to client
//					LinkedList<Object> messageToClient = new LinkedList<Object>();
//					messageToClient.add("PeopleLoaded");
//					messageToClient.add(Data);
//					client.sendToClient(messageToClient);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}

				////////// MUST implement the refund here
				if (message.get(0).equals("MarkComplaintAsDone")) {
					int complaint_id = (int) message.get(1);
					// change complaine into done in database
					session.clear();
					boolean success = ComplaintsController.markComplaintAsDone(session, complaint_id);

					// reply to client
					LinkedList<Object> messageToClient = new LinkedList<Object>();
					messageToClient.add("ComplaintMarkedAsDone");
					messageToClient.add(success);
					client.sendToClient(messageToClient);
				}

				if (message.get(0).equals("AddComplaint")) {
					Complaint comp = (Complaint) message.get(1);
					// adding complaine into  database
					session.clear();
					boolean success = ComplaintsController.addComplaint(session, comp);
					// reply to client
					LinkedList<Object> messageToClient = new LinkedList<Object>();
					messageToClient.add("ComplaintAdded");
					messageToClient.add(success);
					client.sendToClient(messageToClient);
				}

				if (message.get(0).equals("LoadLinks")) {
					// load data
					try {
						session.clear();
						List<Link> Data = LinkController.loadLinks(session);

						// reply to client
						LinkedList<Object> messageToClient = new LinkedList<Object>();
						messageToClient.add("LinksLoaded");
						messageToClient.add(Data);
						client.sendToClient(messageToClient);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (message.get(0).equals("LoadCostumersLinks")) {
					// load data
					session.clear();
					int cost_id = (int) message.get(1);
					List<Link> Data = LinkController.loadCustomerLinks(session, cost_id);
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

				if (message.get(0).equals("LoadMovies")) {
					// load data
					try {
						session.clear();
						List<Movie> Data = MoviesController.loadMovies(session);

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

				if (message.get(0).equals("AddMovie")) {
					Movie newMovie = (Movie) message.get(1);
					// adding a movie into  database
					session.clear();
					boolean success = MoviesController.addMovie(session, newMovie);
					//session.refresh(Movie.class);
					if (!success) {
						throw new Exception("Movie  couldnt be added");
					}
					// reply to client
					LinkedList<Object> messageToClient = new LinkedList<Object>();
					messageToClient.add("MovieAdded");
					messageToClient.add(success);
					client.sendToClient(messageToClient);
				}

				if (message.get(0).equals("DeleteMovie")) {
					int movie_id = (int) message.get(1);
					// delete movie from database
					session.clear();
					boolean success = MoviesController.deleteMovie(session, movie_id);
					//session.refresh(Movie.class);
					if (!success) {
						throw new Exception("the Movie couldnt be deleted");
					}
					// reply to client
					LinkedList<Object> messageToClient = new LinkedList<Object>();
					messageToClient.add("MovieDeleted");
					messageToClient.add(success);
					client.sendToClient(messageToClient);
				}

				if (message.get(0).equals("LoadOrders")) {
					// load data
					try {
						session.clear();
						List<Order> Data = OrderController.loadOrders(session);

						// reply to client
						LinkedList<Object> messageToClient = new LinkedList<Object>();
						messageToClient.add("OrdersLoaded");
						messageToClient.add(Data);
						client.sendToClient(messageToClient);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (message.get(0).equals("LoadCutomersOrders")) {
					// load data
					int cost_id = (int) message.get(1);
					try {
						session.clear();
						List<Order> Data = OrderController.loadCutomersOrders(cost_id, session);

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

				if (message.get(0).equals("AddOrder")) {
					Order order = (Order) message.get(1);
					// adding a order into  database
					session.clear();
					boolean success = OrderController.addOrder(order, session);
					//session.refresh(Order.class);
					if (!success) {
						throw new Exception("Order  couldnt be added");
					}
					// reply to client
					LinkedList<Object> messageToClient = new LinkedList<Object>();
					messageToClient.add("OrderAdded");
					messageToClient.add(success);
					client.sendToClient(messageToClient);
				}

				if (message.get(0).equals("MakePayment")) {
					Payment payment = (Payment) message.get(1);
					session.clear();
					// putting payment details in database
					boolean success = PaymentController.makePayment(session, payment);
					//session.refresh(Payment.class);
					if (!success) {
						throw new Exception("Payment Not Success");
					}
					// reply to client
					LinkedList<Object> messageToClient = new LinkedList<Object>();
					messageToClient.add("PaymentSuccess");
					messageToClient.add(success);
					client.sendToClient(messageToClient);
				}

				if (message.get(0).equals("LoadRefunds")) {
					// load data
					try {
						session.clear();
						List<Refund> Data = RefundController.loadRefunds(session);

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

				if (message.get(0).equals("AddRefund")) {
					Refund refund = (Refund) message.get(1);
					// adding a refund into done in database
					session.clear();
					boolean success = RefundController.addRefund(session, refund);
					//session.refresh(Refund.class);
					if (!success) {
						throw new Exception("Refund  couldnt be added");
					}
					// reply to client
					LinkedList<Object> messageToClient = new LinkedList<Object>();
					messageToClient.add("RefundAdded");
					messageToClient.add(success);
					client.sendToClient(messageToClient);
				}

				if (message.get(0).equals("LoadReagulations")) {
					// load data
					try {
						session.clear();
						List<Regulations> Data = RegulationsController.loadReagulations(session);

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

				if (message.get(0).equals("ActivateRegulations")) {
					int Y = (int) message.get(1);
					// change status into true in database
					session.clear();
					boolean success = RegulationsController.activateRegulations(session, Y);
					List<Hall> hallsList=HallController.loadHalls(session);
					for(Hall h : hallsList){
						int capacity=h.getCapacity();
						if(1.2*Y< capacity){
							HallController.limitMaxSeats(session,h.getID(),Y);
						}
						else{
							if(0.8*Y<capacity){
								HallController.limitMaxSeats(session,h.getID(),(int) Math.round(0.8 * Y));
							}
							else{
								HallController.limitMaxSeats(session,h.getID(),(int) Math.round(0.5 * capacity));
							}
						}
					}
					//session.refresh(Regulation.class);
					if (!success) {
						throw new Exception("Regulation status couldnt be changed");
					}
					// reply to client
					LinkedList<Object> messageToClient = new LinkedList<Object>();
					messageToClient.add("RegulationStatusUpdated");
					messageToClient.add(success);
					client.sendToClient(messageToClient);
				}

				if (message.get(0).equals("DeActivateRegulations")) {
					// change status into false in database
					session.clear();
					boolean success = RegulationsController.deactivateRegulations(session);
					List<Hall> hallsList=HallController.loadHalls(session);
					for(Hall h : hallsList){
						HallController.resetMaxSeats(session,h.getID(),h.getCapacity());
					}
					//session.refresh(Regulation.class);
					if (!success) {
						throw new Exception("Regulation status couldnt be changed");
					}
					// reply to client
					LinkedList<Object> messageToClient = new LinkedList<Object>();
					messageToClient.add("RegulationStatusDeactivated");
					messageToClient.add(success);
					client.sendToClient(messageToClient);
				}

				if (message.get(0).equals("AddShow")) {
					Show show = (Show) message.get(1);
					session.clear();
					// adding show into  database
					boolean success = ShowsController.addShow(session, show);
					//session.refresh(Show.class);
					if (!success) {
						throw new Exception("Show couldnt be added");
					}
					// reply to client
					LinkedList<Object> messageToClient = new LinkedList<Object>();
					messageToClient.add("ShowAdded");
					messageToClient.add(success);
					client.sendToClient(messageToClient);
				}

				if (message.get(0).equals("DeleteShow")) {
					int show_id = (int) message.get(1);
					// delete Show from database
					session.clear();
					boolean success = ShowsController.deleteShow(session, show_id);
					//session.refresh(Show.class);

					// reply to client
					LinkedList<Object> messageToClient = new LinkedList<Object>();
					messageToClient.add("ShowDeleted");
					messageToClient.add(success);
					client.sendToClient(messageToClient);
				}

				if (message.get(0).equals("LoadTickets")) {
					// load data
					try {
						session.clear();
						List<Ticket> Data = TicketsController.loadTickets(session);

						// reply to client
						LinkedList<Object> messageToClient = new LinkedList<Object>();
						messageToClient.add("TicketsLoaded");
						messageToClient.add(Data);
						client.sendToClient(messageToClient);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (message.get(0).equals("LoadCustomersTickets")) {
					// load data
					int cost_id = (int) message.get(1);
					try {
						session.clear();
						List<Ticket> Data = TicketsController.loadCustomersTickets(session, cost_id);

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

				if (message.get(0).equals("AddTicket")) {
					Ticket newticket = (Ticket) message.get(1);
					// adding tickit into  database
					session.clear();
					boolean success = TicketsController.addTicket(session, newticket);

					//session.refresh(Ticket.class);
					if (!success) {
						throw new Exception("Ticket  couldnt be added");
					}
					// reply to client
					LinkedList<Object> messageToClient = new LinkedList<Object>();
					messageToClient.add("TicketAdded");
					messageToClient.add(success);
					client.sendToClient(messageToClient);
				}

				if (message.get(0).equals("LoadUpdatePriceRequests")) {
					// load data
					try {
						session.clear();
						List<UpdatePriceRequest> Data = UpdatePriceRequestController.loadRequests(session);

						// reply to client
						LinkedList<Object> messageToClient = new LinkedList<Object>();
						messageToClient.add("UpdatePriceRequestLoaded");
						messageToClient.add(Data);
						client.sendToClient(messageToClient);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (message.get(0).equals("ApproveRequest")) {
					session.clear();
					UpdatePriceRequest request = (UpdatePriceRequest) message.get(1);
					// changing price in database
					boolean success = UpdatePriceRequestController.approveRequest(session, request);
					//session.refresh(UpdatePriceRequest.class);
					if (!success) {
						throw new Exception("the price couldnt be changed");
					}
					// reply to client
					LinkedList<Object> messageToClient = new LinkedList<Object>();
					messageToClient.add("RequestsApproved");
					messageToClient.add(success);
					client.sendToClient(messageToClient);
				}

				if (message.get(0).equals("DeclineRequest")) {
					session.clear();
					int request_id = (int) message.get(1);
					// not changing price in database
					boolean success = UpdatePriceRequestController.declineRequest(session, request_id);
					//session.refresh(UpdatePriceRequest.class);
					if (!success) {
						throw new Exception("the request couldnt be declined");
					}
					// reply to client
					LinkedList<Object> messageToClient = new LinkedList<Object>();
					messageToClient.add("RequestDeclined");
					messageToClient.add(success);
					client.sendToClient(messageToClient);
				}

				if (message.get(0).equals("LoadCinemas")) {
					// load data
					try {
						session.clear();
						List<Cinema> Data = CinemaController.loadCinemas(session);

						// reply to client
						LinkedList<Object> messageToClient = new LinkedList<Object>();
						messageToClient.add("CinemasLoaded");
						messageToClient.add(Data);
						client.sendToClient(messageToClient);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (message.get(0).equals("LoadHalls")) {
					// load data
					try {
						session.clear();
						List<Hall> Data = HallController.loadHalls(session);

						// reply to client
						LinkedList<Object> messageToClient = new LinkedList<Object>();
						messageToClient.add("HallsLoaded");
						messageToClient.add(Data);
						client.sendToClient(messageToClient);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}


				if (message.get(0).equals("DeactivateAllComplaints24")) {
					session.clear();

					// deactivate All Complaints in database that had been more than 24 hours
					boolean success = ComplaintsController.deactivateAllComplaintsAfter24Hours(session);
					//session.refresh(Complaint.class);

					// reply to client
					LinkedList<Object> messageToClient = new LinkedList<Object>();
					messageToClient.add("ComplaintsDeactivated");
					messageToClient.add(success);
					client.sendToClient(messageToClient);
				}

				if (message.get(0).equals("LoadEmployees")) {
					session.clear();
					// load data
					try {
						List<Employee> Data = EmployeeController.loadEmployees(session);

						// reply to client
						LinkedList<Object> messageToClient = new LinkedList<Object>();
						messageToClient.add("EmployeesLoaded");
						messageToClient.add(Data);
						client.sendToClient(messageToClient);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}


				if (message.get(0).equals("LogIn")) {
					// load data

					try {
						session.clear();
						String username = (String) message.get(1);
						String password = (String) message.get(2);
						Employee Data = EmployeeController.logIn(session, username, password);
						LinkedList<Object> messageToClient = new LinkedList<Object>();
						if (Data != null) {
							messageToClient.add("LogInCompleted");
							messageToClient.add(Data);
						} else {
							messageToClient.add("Username or Password are wrong");
						}
						client.sendToClient(messageToClient);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (message.get(0).equals("AddLink")) {
					session.clear();
					Link newLink = (Link) message.get(1);
					// adding link into  database
					boolean success = LinkController.addLink(session, newLink);
					//session.refresh(Link.class);

					// reply to client
					LinkedList<Object> messageToClient = new LinkedList<Object>();
					messageToClient.add("LinkAdded");
					messageToClient.add(success);
					client.sendToClient(messageToClient);
				}


				if (message.get(0).equals("CancelLink")) {
					int link_id = (int) message.get(1);
					// load data
					try {
						session.clear();
						boolean Data = LinkController.cancelLink(session, link_id);
						LocalDateTime DT = LinkController.loadLinkTime(session, link_id);
						//if there is refund to be done
						if (ChronoUnit.HOURS.between(LocalDateTime.now(), DT) > 1) {
							double price = LinkController.loadLinkPrice(session, link_id);
							Refund refund = new Refund(price * 0.5, link_id, 0, LocalDateTime.now());
							RefundController.addRefund(session, refund);
						}

						// reply to client
						LinkedList<Object> messageToClient = new LinkedList<Object>();
						messageToClient.add("linkCanceled");
						messageToClient.add(Data);
						client.sendToClient(messageToClient);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (message.get(0).equals("CancelTicket")) {
					int ticket_id = (int) message.get(1);
					// load data
					try {
						session.clear();
						TicketsController.cancelTicket(session, ticket_id);
						LocalDateTime dt = TicketsController.loadTicketShowTime(session, ticket_id);
						double price = TicketsController.loadTicketPrice(session, ticket_id);
						double r = TicketsController.calcRefund(dt);
						//if refund is 50%
						if (r == 0.5) {
							price *= 0.5;
						}
						//if refund is not 0
						if (r != 0) {
							Refund refund = new Refund(price, ticket_id, 0, LocalDateTime.now());
							RefundController.addRefund(session, refund);
						}
						// reply to client
						LinkedList<Object> messageToClient = new LinkedList<Object>();
						messageToClient.add("linkCanceled");
						client.sendToClient(messageToClient);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (message.get(0).equals("SendMail")) {
					session.clear();
					String mesasge1 = (String) message.get(1);
					String mail = (String) message.get(2);
					String topic = (String) message.get(3);
					// load data
					MailController.sendMail(mesasge1, mail, topic);
					try {
						// reply to client
						LinkedList<Object> messageToClient = new LinkedList<Object>();
						messageToClient.add("mailSent");
						client.sendToClient(messageToClient);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (message.get(0).equals("LoadNewMovies")) {
					session.clear();
					// load data
					try {
						List<Movie> Data = MoviesController.loadNewMovies(session);

						// reply to client
						LinkedList<Object> messageToClient = new LinkedList<Object>();
						messageToClient.add("NewMoviesLoaded");
						messageToClient.add(Data);
						client.sendToClient(messageToClient);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (message.get(0).equals("LoadPackages")) {
					// load data
					try {
						session.clear();
						List<PackageOrder> Data = PackagesController.loadPackages(session);

						// reply to client
						LinkedList<Object> messageToClient = new LinkedList<Object>();
						messageToClient.add("PackagesLoaded");
						messageToClient.add(Data);
						client.sendToClient(messageToClient);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (message.get(0).equals("LoadCustomersPackages")) {
					// load data
					int cost_id = (int) message.get(1);
					try {
						session.clear();
						List<PackageOrder> Data = PackagesController.loadCustomersPackages(session, cost_id);

						// reply to client
						LinkedList<Object> messageToClient = new LinkedList<Object>();
						messageToClient.add("Costumer'sPackagesLoaded");
						messageToClient.add(Data);
						client.sendToClient(messageToClient);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (message.get(0).equals("AddPackage")) {
					session.clear();
					PackageOrder pcg = (PackageOrder) message.get(1);
					// adding package into  database
					boolean success = PackagesController.addPackage(session, pcg);
					//session.refresh(Package.class);

					// reply to client
					LinkedList<Object> messageToClient = new LinkedList<Object>();
					messageToClient.add("PackageAdded");
					messageToClient.add(success);
					client.sendToClient(messageToClient);
				}

				if (message.get(0).equals("LoadMovieShows")) {
					int movie_id = (int) message.get(1);
					// load data
					List<Show> Data = MoviesController.loadMovieShows(session, movie_id);
					try {
						// reply to client
						LinkedList<Object> messageToClient = new LinkedList<Object>();
						messageToClient.add("Movie'sShowsLoaded");
						messageToClient.add(Data);
						client.sendToClient(messageToClient);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (message.get(0).equals("LoadSeats")) {
					// load data
					try {
						session.clear();
						List<Seat> Data = SeatController.loadSeats(session);

						// reply to client
						LinkedList<Object> messageToClient = new LinkedList<Object>();
						messageToClient.add("SeatsLoaded");
						messageToClient.add(Data);
						client.sendToClient(messageToClient);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}


				if (message.get(0).equals("AddUpdatePriceRequest")) {
					session.clear();
					UpdatePriceRequest request = (UpdatePriceRequest) message.get(1);
					// adding request into  database
					boolean success = UpdatePriceRequestController.addRequest(session, request);
					//session.refresh(UpdatePriceRequest.class);

					// reply to client
					LinkedList<Object> messageToClient = new LinkedList<Object>();
					messageToClient.add("UpdatePriceRequestAdded");
					messageToClient.add(success);
					client.sendToClient(messageToClient);
				}

				if (message.get(0).equals("LoadTicketsReport")) {
					// load data
					try {
						session.clear();
						int cinema_id = (int) message.get(1);
						Integer month = (Integer) message.get(2);
						Integer year = (Integer) message.get(3);
						List<Ticket> Data = TicketsController.makeTicketsReportByMonth(session, cinema_id, month, year);

						// reply to client
						LinkedList<Object> messageToClient = new LinkedList<Object>();
						messageToClient.add("TicketsReportLoaded");
						messageToClient.add(Data);
						client.sendToClient(messageToClient);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (message.get(0).equals("LoadPackagesReport")) {
					// load data
					try {
						session.clear();
						Integer month = (Integer) message.get(1);
						Integer year = (Integer) message.get(2);
						List<PackageOrder> Data = PackagesController.makePackagesReportByMonth(session, month, year);

						// reply to client
						LinkedList<Object> messageToClient = new LinkedList<Object>();
						messageToClient.add("PackagesReportLoaded");
						messageToClient.add(Data);
						client.sendToClient(messageToClient);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (message.get(0).equals("LoadLinksReport")) {
					// load data
					try {
						session.clear();
						Integer month = (Integer) message.get(1);
						Integer year = (Integer) message.get(2);
						List<Link> Data = LinkController.makeLinksReportByMonth(session, month, year);

						// reply to client
						LinkedList<Object> messageToClient = new LinkedList<Object>();
						messageToClient.add("LinksReportLoaded");
						messageToClient.add(Data);
						client.sendToClient(messageToClient);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (message.get(0).equals("LoadRefundsReport")) {
					// load data
					try {
						session.clear();
						Integer month = (Integer) message.get(1);
						Integer year = (Integer) message.get(2);
						List<Refund> Data = RefundController.makeRefundsReportByMonth(session, month, year);

						// reply to client
						LinkedList<Object> messageToClient = new LinkedList<Object>();
						messageToClient.add("RefundsReportLoaded");
						messageToClient.add(Data);
						client.sendToClient(messageToClient);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (message.get(0).equals("LoadComplaintsReport")) {
					// load data
					try {
						session.clear();
						Integer month = (Integer) message.get(1);
						Integer year = (Integer) message.get(2);
						List<Complaint> Data = ComplaintsController.makeComplaintsReportByMonth(session, month, year);

						// reply to client
						LinkedList<Object> messageToClient = new LinkedList<Object>();
						messageToClient.add("ComplaintsReportLoaded");
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
			threadLock.notifyAll();
		}
    

    }

	protected static void activatingLoop() throws IOException {

		activateThread = new Thread(new Runnable() {

			@Override
			public void run() {
				List<Link> links;





					synchronized (threadLock) {
						try {

							LinkController.activateLinksWhenTimeCome(session);
							ComplaintsController.deactivateAllComplaintsAfter24Hours(session);


						} catch (Exception e) {
							e.printStackTrace();
						}
						threadLock.notifyAll();
					}

			}
		});
		activateThread.setPriority(Thread.MIN_PRIORITY);
		final ScheduledFuture<?> scheduler =  Executors.newScheduledThreadPool(1).scheduleAtFixedRate(activateThread,0,60, SECONDS);





	}

	protected static void sendNewMoviesToPackagesCostumers() throws IOException {

		packageMsgTHr = new Thread(new Runnable() {

			@Override
			public void run() {






				synchronized (threadLock) {
					try {

						MailController.sendNewMoviesMail(session);

					} catch (Exception e) {
						e.printStackTrace();
					}
					threadLock.notifyAll();
				}

			}
		});

		final ScheduledFuture<?> scheduler =  Executors.newScheduledThreadPool(1).scheduleAtFixedRate(packageMsgTHr,0,7, DAYS);





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

    
    private static void InitializeDataBase() throws Exception {
    	SessionFactory sessionFactory = getSessionFactory();
		session = sessionFactory.openSession();
		generateMovies(session);

		//intialize regulations
		Regulations regulations=new Regulations();
		RegulationsController.addRegulations(CinemaServer.session,regulations);

		//intialize employees
		ChainManager chainManager=new ChainManager("cersei lannister", "0534727563",
				"Lannister@gmail.com", "clannister","1212");
		EmployeeController.addEmployee(CinemaServer.session,chainManager);

		ContentManager contentManager=new ContentManager("Sirina Williams", "0533264563",
				"williams@gmail.com", "sWilliams","7yg2");
		EmployeeController.addEmployee(CinemaServer.session,contentManager);

		CustomerService customerService1=new CustomerService("Nina Dobrev", "0576514563",
				"Dobrev@gmail.com", "nDobrev","ch3i");
		EmployeeController.addEmployee(CinemaServer.session,customerService1);

		CustomerService customerService2=new CustomerService("Phill Adams", "0573299563",
				"Adams@gmail.com", "pAdams","hd72");
		EmployeeController.addEmployee(CinemaServer.session,customerService2);

		CustomerService customerService3=new CustomerService("Joe Jonas", "0547835611",
				"Jonas@gmail.com", "jJonas","8h2s");
		EmployeeController.addEmployee(CinemaServer.session,customerService3);

		Customer customer=new Customer("Bob", "050000000", "a@com");
		CustomerController.addCustomer(CinemaServer.session, customer);

		CinemaManager cinemaManager1=new CinemaManager("Anna Neeson", "0576773322",
				"Neeson@gmail.com", "aNeeson","ew92",1);
		EmployeeController.addEmployee(CinemaServer.session,cinemaManager1);

		CinemaManager cinemaManager2=new CinemaManager("Adam Levine", "0511199322",
				"Levine@gmail.com", "aLevine","so2o",2);
		EmployeeController.addEmployee(CinemaServer.session,cinemaManager2);

		/////// Testing ComplaintController

		Complaint complaint2=new Complaint("I paid but didn't receive confirmation!!",2);
		ComplaintsController.addComplaint(session,complaint2);
		Complaint complaint3=new Complaint("All hals are full there's no place for me",3);
		ComplaintsController.addComplaint(session,complaint3);
		ComplaintsController.markComplaintAsDone(session,1);

		/////// Testing TicketController
		Ticket ticket=new Ticket(2,2,23,8,
				LocalDateTime.of(2021,7,30,22,30),35,1);
		TicketsController.addTicket(session,ticket);
		Ticket ticket2=new Ticket(2,2,24,8,
				LocalDateTime.of(2021,7,30,21,00),60,4);
		TicketsController.addTicket(session,ticket2);
		Ticket ticket3=new Ticket(2,2,24,8,
				LocalDateTime.of(2021,7,30,21,00),60,2);
		TicketsController.addTicket(session,ticket3);
		TicketsController.cancelTicket(session,ticket2.getID());




		Customer cus1 = new Customer("Ali","0502700998", "aliaculielun@gmail.com");
		CustomerController.addCustomer(session,cus1);
		Customer cus2 = new Customer("Mosa","0502700998", "amar.habiballah@hotmail.com");
		CustomerController.addCustomer(session,cus2);
		Customer cus3 = new Customer("Ammar","0502700998", "amarha157@gmail.com");
		CustomerController.addCustomer(session,cus3);
		/////// Testing PackageControlle
		PackageOrder pack=new PackageOrder(150,cus1.getID());
		PackagesController.addPackage(session, pack);
		PackageOrder pack2=new PackageOrder(150,cus2.getID());
		PackagesController.addPackage(session, pack2);
		PackageOrder pack3=new PackageOrder(150,cus3.getID());
		PackagesController.addPackage(session, pack3);



		List<PackageOrder> cust_pacs=PackagesController.loadCustomersPackages(session,1);
		//System.out.println(cust_pacs.size());

		List<PackageOrder> pacs=PackagesController.loadPackages(session);
		//System.out.println(pacs.size());

		pack2.setCounter(12);
		int n=PackagesController.getNumberOfTicketsLeft(session,4);
		//System.out.println(n);

		/////// Testing LinkController
		Link link1=new Link("www.cinema.com",LocalDateTime.of(2021,7,31,18,0),
				LocalDateTime.of(2021,7,31,21,0), 11, 80,3);
		LinkController.addLink(session,link1);
		Link link2=new Link("www.cinema.com",LocalDateTime.of(2021,7,30,23,0),
				LocalDateTime.of(2021,7,31,1,30), 12, 80,3);
		LinkController.addLink(session,link2);
		Link link3=new Link("www.cinema.com",LocalDateTime.of(2021,7,31,2,45),
				LocalDateTime.of(2021,7,31,5,30), 11, 80,1);
		LinkController.addLink(session,link3);
		List<Link> links=LinkController.loadLinks(session);
		//System.out.println(links.size());
		List<Link> clinks=LinkController.loadCustomerLinks(session,3);
		//System.out.println(clinks.size());

		boolean Data = LinkController.cancelLink(session,link1.getID());
		LocalDateTime DT = LinkController.loadLinkTime(session, link1.getID());
		//if there is refund to be done
		if (ChronoUnit.HOURS.between(LocalDateTime.now(),DT) >1){
			double price=LinkController.loadLinkPrice(session,link1.getID());
			Refund refund=new Refund(price*0.5, link1.getID(), 0, LocalDateTime.now());
			RefundController.addRefund(session,refund);
		}


		/////// Testing MailController
		//MailController.sendMail("Testing our project","rayah.khatib.2@gmail.com","Test");

		/////// Testing UpdatePriceRequestController
//		UpdatePriceRequest req1=new UpdatePriceRequest(2,7,130);
//		UpdatePriceRequestController.addRequest(session,req1);
//		UpdatePriceRequest req2=new UpdatePriceRequest(2,5,20);
//		UpdatePriceRequestController.addRequest(session,req2);
//		UpdatePriceRequestController.approveRequest(session, req1);
//		UpdatePriceRequestController.declineRequest(session, req2.getID());

		/////// Testing NewMovies
		List<Movie> newMovies=MoviesController.loadNewMovies(session);
		for(Movie m:newMovies)
			System.out.println(m.getName_en());

		/////// Testing Payment
		Payment payment=new Payment(40,2);
		PaymentController.makePayment(session,payment);

		/////// Testing Cinema
//		List<Show> cinema_shows=CinemaController.loadCinemaShows(session,2);
//		System.out.println(cinema_shows.size());

		/////// Testing Regulations
		int Y=49;
		RegulationsController.activateRegulations(session, Y);
		List<Hall> hallsList=HallController.loadHalls(session);
		for(Hall h : hallsList){
			int capacity=h.getCapacity();
			if(1.2*Y< capacity){
				HallController.limitMaxSeats(session,h.getID(),Y);
			}
			else{
				if(0.8*Y<capacity){
					HallController.limitMaxSeats(session,h.getID(),(int) Math.round(0.8 * Y));
				}
				else{
					HallController.limitMaxSeats(session,h.getID(),(int) Math.round(0.5 * capacity));
				}
			}
		}

		RegulationsController.deactivateRegulations(session);
		List<Hall> hallsList2=HallController.loadHalls(session);
		for(Hall h : hallsList2){
			HallController.resetMaxSeats(session,h.getID(),h.getCapacity());
		}



	}



	private static void generateMovies(Session session) throws Exception {
    	try {
			List<Movie> moviesList = new LinkedList<Movie>();
			List<Show> emptyShowList = new LinkedList<Show>();

    		/*when we create the movie we give its empty list of shows
    		and when we create the shows list we set its in the movie
    		 */

			File im = new File("src/main/resources/org.example/Images/1.jpg");
			byte[] imFile = new byte[(int) im.length()];

			File im1 = new File("src/main/resources/org.example/Images/2.jpg");
			byte[] im1File = new byte[(int) im1.length()];

			File im2 = new File("src/main/resources/org.example/Images/3.jpg");
			byte[] im2File = new byte[(int) im2.length()];

			File im3 = new File("src/main/resources/org.example/Images/4.jpg");
			byte[] im3File = new byte[(int) im3.length()];

			File im4 = new File("src/main/resources/org.example/Images/5.jpg");
			byte[] im4File = new byte[(int) im4.length()];

			File im5 = new File("src/main/resources/org.example/Images/6.jpg");
			byte[] im5File = new byte[(int) im5.length()];

			File im6 = new File("src/main/resources/org.example/Images/7.jpg");
			byte[] im6File = new byte[(int) im6.length()];

			File im7 = new File("src/main/resources/org.example/Images/8.jpg");
			byte[] im7File = new byte[(int) im7.length()];

			File im8 = new File("src/main/resources/org.example/Images/9.jpg");
			byte[] im8File = new byte[(int) im8.length()];

			File im9 = new File("src/main/resources/org.example/Images/10.jpg");
			byte[] im9File = new byte[(int) im9.length()];

			File im10 = new File("src/main/resources/org.example/Images/11.jpg");
			byte[] im10File = new byte[(int) im10.length()];

			File im11= new File("src/main/resources/org.example/Images/12.jpg");
			byte[] im11File = new byte[(int) im11.length()];


			Movie HarryPotter7= new Movie ("Harry Potter 7", "הארי פוטר 7", "David Yates", init.HarryPotterCast(),"bla bla bla", LocalDate.parse("2021-07-31"),  imFile, emptyShowList,false);
			moviesList.add(HarryPotter7);
			Movie Joker=new Movie("Joker","גוקר","Todd Phillips",init.JokerCast(), init.JokerSummary(), LocalDate.parse("2021-08-07"), im1File, emptyShowList,false);
			moviesList.add(Joker);
			Movie TheAvengers=new Movie("The Avengers","הנוקמים","Kevin Feige",init.TheAvengersCast(), init.TheAvengersSummary(), LocalDate.parse("2021-05-18"), im3File, emptyShowList,false);
			moviesList.add(TheAvengers);
			Movie StarWars=new Movie("Star Wars","מלחמת הכוכבים","George Lucas",init.StarWarsCast(), init.StarWarsSummary(), LocalDate.parse("2021-06-19"), im2File, emptyShowList,false);
			moviesList.add(StarWars);
			Movie Inception=new Movie("Incepteion","התחלה","Emma Thomas",init.InceptionCast(), init.InceptionSummary(), LocalDate.parse("2021-02-13"), im4File, emptyShowList,false);
			moviesList.add(Inception);
			Movie TheDarKnight=new Movie("The Dark Knight","האביר האפל","Emma Thomas,Charles Roven,Christopher Nolan",init.TheDarkKnightCast(), init.TheDarkKnightSummary(), LocalDate.parse("2021-12-18"), im5File, emptyShowList,false);
			moviesList.add(TheDarKnight);
			Movie CaptainAmerica=new Movie("Captain America","קפטן אמריקה","Kevin Feige",init.CaptainAmericaCast(), init.CaptainAmericaSummary(), LocalDate.parse("2021-07-11"), im6File, emptyShowList,false);
			moviesList.add(CaptainAmerica);
			Movie Avatar=new Movie("Avatar","אווטאר","James Cameron,Jon Landau",init.AvatarCast(), init.AvatarSummary(), LocalDate.parse("2021-07-18"), im7File, emptyShowList,false);
			moviesList.add(Avatar);
			Movie Jaws=new Movie("Jaws","מלתעות","Steven Spielberg",init.JawsCast(), init.JawsSummary(), LocalDate.parse("2019-08-14"), im8File, emptyShowList,false);
			moviesList.add(Jaws);
			Movie Rocky=new Movie("Rocky","רוקי","John G. Avildsen",init.RockyCast(), init.RockySummary(), LocalDate.parse("2021-01-16"), im9File, emptyShowList,false);
			moviesList.add(Rocky);
			Movie Titanic=new Movie("Titanic","טיטניק","James Cameron",init.TitanicCast(), init.TitanicSummary(), LocalDate.parse("2021-02-18"), im10File, emptyShowList,true);
			moviesList.add(Titanic);
			Movie LordOfTheRings=new Movie("Lord Of The Rings","שר הטבעות","Peter Jackson",init.LordOfTheRingsCast(), init.LordOfTheRingsSummary(), LocalDate.parse("2021-06-18"), im11File, emptyShowList,true);
			moviesList.add(LordOfTheRings);

			Cinema cinema1 = new Cinema("לב המפרץ");
			List<Hall> cinemaHalls = new LinkedList<Hall>();
			List<Seat> tempseats = new LinkedList<Seat>();
			List<Seat> tempseats1 = new LinkedList<Seat>();
			List<Seat> tempseats2 = new LinkedList<Seat>();
			Hall cinemaHall = new Hall(1, 2*10,tempseats, cinema1);
			Hall cinemaHall1 = new Hall(2, 4*10,tempseats1, cinema1);
			Hall cinemaHall2 =new Hall(3, 6*10,tempseats2, cinema1);


			Show show1= new Show( LocalDateTime.of(2021,9,1, 21,0), 60,HarryPotter7,cinemaHall,cinema1);
			Show show112= new Show( LocalDateTime.of(2021,10,2, 21,0), 60,HarryPotter7,cinemaHall2,cinema1);
			Show show113= new Show( LocalDateTime.of(2021,11,2, 23,15), 60,HarryPotter7,cinemaHall1,cinema1);

			Show show2= new Show( LocalDateTime.of(2021,12,8, 22,30), 60, LordOfTheRings,cinemaHall1,cinema1);
			Show show22= new Show( LocalDateTime.of(2021,12,9, 20,30), 60, LordOfTheRings,cinemaHall2,cinema1);
			Show show23= new Show( LocalDateTime.of(2021,12,9, 22,45), 60, LordOfTheRings,cinemaHall,cinema1);

			Show show3= new Show( LocalDateTime.of(2021,11,4, 19,50), 60,Titanic,cinemaHall2,cinema1);
			Show show32= new Show( LocalDateTime.of(2021,12,1, 21,30), 60,Titanic,cinemaHall1,cinema1);
			Show show33= new Show( LocalDateTime.of(2021,12,2, 21,30), 60,Titanic,cinemaHall,cinema1);

			Show show4= new Show( LocalDateTime.of(2021,9,15, 18,30), 60,Joker,cinemaHall2,cinema1);
			Show show42= new Show( LocalDateTime.of(2021,9,14, 18,30), 60,Joker,cinemaHall1,cinema1);
			Show show43= new Show( LocalDateTime.of(2021,9,13, 18,30), 60,Joker,cinemaHall,cinema1);

			Show show5= new Show( LocalDateTime.of(2021,10,11, 20,45), 60,TheAvengers,cinemaHall2,cinema1);
			Show show52= new Show( LocalDateTime.of(2021,10,14, 20,45), 60,TheAvengers,cinemaHall1,cinema1);
			Show show53= new Show( LocalDateTime.of(2021,10,13, 20,45), 60,TheAvengers,cinemaHall,cinema1);

			Show show6= new Show( LocalDateTime.of(2021,9,15, 21,30), 60,StarWars,cinemaHall2,cinema1);
			Show show62= new Show( LocalDateTime.of(2021,9,16, 21,30), 60,StarWars,cinemaHall1,cinema1);
			Show show63= new Show( LocalDateTime.of(2021,9,18, 21,30), 60,StarWars,cinemaHall,cinema1);

			Show show7= new Show( LocalDateTime.of(2021,8,24, 19,0), 60,Inception,cinemaHall1,cinema1);
			Show show72= new Show( LocalDateTime.of(2021,8,25, 19,0), 60,Inception,cinemaHall1,cinema1);
			Show show73= new Show( LocalDateTime.of(2021,8,26, 19,0), 60,Inception,cinemaHall,cinema1);

			Show show8= new Show( LocalDateTime.of(2021,8,23, 19,0), 60,TheDarKnight,cinemaHall2,cinema1);
			Show show82= new Show( LocalDateTime.of(2021,8,24, 23,0), 60,TheDarKnight,cinemaHall1,cinema1);
			Show show83= new Show( LocalDateTime.of(2021,8,25, 19,0), 60,TheDarKnight,cinemaHall,cinema1);

			Show show9= new Show( LocalDateTime.of(2021,11,7, 18,45), 60,CaptainAmerica,cinemaHall2,cinema1);
			Show show92= new Show( LocalDateTime.of(2021,11,8, 18,45), 60,CaptainAmerica,cinemaHall1,cinema1);
			Show show93= new Show( LocalDateTime.of(2021,11,9, 18,45), 60,CaptainAmerica,cinemaHall,cinema1);

			Show show10= new Show( LocalDateTime.of(2021,10,5, 0,0), 60,Avatar,cinemaHall2,cinema1);
			Show show102= new Show( LocalDateTime.of(2021,10,6, 0,0), 60,Avatar,cinemaHall1,cinema1);
			Show show103= new Show( LocalDateTime.of(2021,10,7, 0,0), 60,Avatar,cinemaHall,cinema1);

			Show show11= new Show( LocalDateTime.of(2021,9,7, 21,30), 60,Jaws,cinemaHall2,cinema1);
			Show show11_2= new Show( LocalDateTime.of(2021,9,9, 21,30), 60,Jaws,cinemaHall1,cinema1);
			Show show11_3= new Show( LocalDateTime.of(2021,9,10, 21,30), 60,Jaws,cinemaHall,cinema1);

			Show show12= new Show( LocalDateTime.of(2021,8,5, 22,20), 60,Rocky,cinemaHall2,cinema1);
			Show show12_2= new Show( LocalDateTime.of(2021,8,6, 22,20), 60,Rocky,cinemaHall1,cinema1);
			Show show12_3= new Show( LocalDateTime.of(2021,8,7, 22,20), 60,Rocky,cinemaHall,cinema1);


			show1.setHall(cinemaHall);
			show43.setHall(cinemaHall);
			show53.setHall(cinemaHall);
			show63.setHall(cinemaHall);
			show73.setHall(cinemaHall);
			show83.setHall(cinemaHall);
			show93.setHall(cinemaHall);
			show103.setHall(cinemaHall);
			show11_3.setHall(cinemaHall);
			show12_3.setHall(cinemaHall);

			show2.setHall(cinemaHall1);
			show7.setHall(cinemaHall);
			show42.setHall(cinemaHall1);
			show52.setHall(cinemaHall1);
			show62.setHall(cinemaHall1);
			show72.setHall(cinemaHall1);
			show82.setHall(cinemaHall1);
			show92.setHall(cinemaHall1);
			show102.setHall(cinemaHall1);
			show11_2.setHall(cinemaHall1);
			show12_2.setHall(cinemaHall1);

			show3.setHall(cinemaHall2);
			show4.setHall(cinemaHall2);
			show5.setHall(cinemaHall2);
			show6.setHall(cinemaHall2);
			show8.setHall(cinemaHall2);
			show9.setHall(cinemaHall2);
			show10.setHall(cinemaHall2);
			show11.setHall(cinemaHall2);
			show12.setHall(cinemaHall2);

			List<Show> shows = new LinkedList<Show>();
			shows.add(show1);
			shows.add(show112);
			shows.add(show113);
			shows.add(show2);
			shows.add(show22);
			shows.add(show23);
			shows.add(show3);
			shows.add(show32);
			shows.add(show33);
			shows.add(show4);
			shows.add(show42);
			shows.add(show43);
			shows.add(show5);
			shows.add(show52);
			shows.add(show53);
			shows.add(show6);
			shows.add(show62);
			shows.add(show63);
			shows.add(show7);
			shows.add(show72);
			shows.add(show73);
			shows.add(show8);
			shows.add(show82);
			shows.add(show83);
			shows.add(show9);
			shows.add(show92);
			shows.add(show93);
			shows.add(show10);
			shows.add(show102);
			shows.add(show103);
			shows.add(show11);
			shows.add(show11_2);
			shows.add(show11_3);
			shows.add(show12);
			shows.add(show12_2);
			shows.add(show12_3);

			cinemaHalls.add(cinemaHall2);
			cinemaHalls.add(cinemaHall);
			cinemaHalls.add(cinemaHall1);
			cinema1.setMovies(moviesList);
			cinema1.setShows(shows);
			cinema1.setHalls(cinemaHalls);




			for (int k=1; k<=2*10; k++){
				Seat seat = new Seat(true, k, k/11+1,cinemaHall);
				tempseats.add(seat);
			}

			for (int k=1; k<=4*10; k++){
				Seat seat = new Seat(true, k, k/11 +1,cinemaHall1);
				tempseats1.add(seat);
			}

			for (int k=1; k<=6*10; k++){
				Seat seat = new Seat(true, k, k/11 +1,cinemaHall2);
				tempseats2.add(seat);
			}

			/*
			Cinema cinema2 = new Cinema("רמת גן");
			List<Hall> cinemaHalls2 = new LinkedList<Hall>();
			List<Seat> tempseats_2 = new LinkedList<Seat>();
			List<Seat> tempseats1_2 = new LinkedList<Seat>();
			List<Seat> tempseats2_2 = new LinkedList<Seat>();
			Hall cinemaHall_2 = new Hall(1, 2*10,tempseats_2, cinema2);
			Hall cinemaHall1_2 = new Hall(2, 4*10,tempseats1_2, cinema2);
			Hall cinemaHall2_2 = new Hall(3, 6*10,tempseats2_2, cinema2);

			Show chow1= new Show( LocalDateTime.of(2021,9,1, 21,0), 60,HarryPotter7,cinemaHall,cinema1);
			Show chow112= new Show( LocalDateTime.of(2021,10,2, 21,0), 60,HarryPotter7,cinemaHall2,cinema1);
			Show chow113= new Show( LocalDateTime.of(2021,11,2, 23,15), 60,HarryPotter7,cinemaHall1,cinema1);

			Show chow2= new Show( LocalDateTime.of(2021,12,8, 22,30), 60, LordOfTheRings,cinemaHall1,cinema1);
			Show chow22= new Show( LocalDateTime.of(2021,12,9, 20,30), 60, LordOfTheRings,cinemaHall2,cinema1);
			Show chow23= new Show( LocalDateTime.of(2021,12,9, 22,45), 60, LordOfTheRings,cinemaHall,cinema1);

			Show chow3= new Show( LocalDateTime.of(2021,11,4, 19,50), 60,Titanic,cinemaHall2,cinema1);
			Show chow32= new Show( LocalDateTime.of(2021,12,1, 21,30), 60,Titanic,cinemaHall1,cinema1);
			Show chow33= new Show( LocalDateTime.of(2021,12,2, 21,30), 60,Titanic,cinemaHall,cinema1);

			Show chow4= new Show( LocalDateTime.of(2021,9,15, 18,30), 60,Joker,cinemaHall2,cinema1);
			Show chow42= new Show( LocalDateTime.of(2021,9,14, 18,30), 60,Joker,cinemaHall1,cinema1);
			Show chow43= new Show( LocalDateTime.of(2021,9,13, 18,30), 60,Joker,cinemaHall,cinema1);

			Show chow5= new Show( LocalDateTime.of(2021,10,11, 20,45), 60,TheAvengers,cinemaHall2,cinema1);
			Show chow52= new Show( LocalDateTime.of(2021,10,14, 20,45), 60,TheAvengers,cinemaHall1,cinema1);
			Show chow53= new Show( LocalDateTime.of(2021,10,13, 20,45), 60,TheAvengers,cinemaHall,cinema1);

			Show chow6= new Show( LocalDateTime.of(2021,9,15, 21,30), 60,StarWars,cinemaHall2,cinema1);
			Show chow62= new Show( LocalDateTime.of(2021,9,16, 21,30), 60,StarWars,cinemaHall1,cinema1);
			Show chow63= new Show( LocalDateTime.of(2021,9,18, 21,30), 60,StarWars,cinemaHall,cinema1);

			Show chow7= new Show( LocalDateTime.of(2021,8,24, 19,0), 60,Inception,cinemaHall1,cinema1);
			Show chow72= new Show( LocalDateTime.of(2021,8,25, 19,0), 60,Inception,cinemaHall1,cinema1);
			Show chow73= new Show( LocalDateTime.of(2021,8,26, 19,0), 60,Inception,cinemaHall,cinema1);

			Show chow8= new Show( LocalDateTime.of(2021,8,23, 19,0), 60,TheDarKnight,cinemaHall2,cinema1);
			Show chow82= new Show( LocalDateTime.of(2021,8,24, 23,0), 60,TheDarKnight,cinemaHall1,cinema1);
			Show chow83= new Show( LocalDateTime.of(2021,8,25, 19,0), 60,TheDarKnight,cinemaHall,cinema1);

			Show chow9= new Show( LocalDateTime.of(2021,11,7, 18,45), 60,CaptainAmerica,cinemaHall2,cinema1);
			Show chow92= new Show( LocalDateTime.of(2021,11,8, 18,45), 60,CaptainAmerica,cinemaHall1,cinema1);
			Show chow93= new Show( LocalDateTime.of(2021,11,9, 18,45), 60,CaptainAmerica,cinemaHall,cinema1);

			Show chow10= new Show( LocalDateTime.of(2021,10,5, 0,0), 60,Avatar,cinemaHall2,cinema1);
			Show chow102= new Show( LocalDateTime.of(2021,10,6, 0,0), 60,Avatar,cinemaHall1,cinema1);
			Show chow103= new Show( LocalDateTime.of(2021,10,7, 0,0), 60,Avatar,cinemaHall,cinema1);

			Show chow11= new Show( LocalDateTime.of(2021,9,7, 21,30), 60,Jaws,cinemaHall2,cinema1);
			Show chow11_2= new Show( LocalDateTime.of(2021,9,9, 21,30), 60,Jaws,cinemaHall1,cinema1);
			Show chow11_3= new Show( LocalDateTime.of(2021,9,10, 21,30), 60,Jaws,cinemaHall,cinema1);

			Show chow12= new Show( LocalDateTime.of(2021,8,5, 22,20), 60,Rocky,cinemaHall2,cinema1);
			Show chow12_2= new Show( LocalDateTime.of(2021,8,6, 22,20), 60,Rocky,cinemaHall1,cinema1);
			Show chow12_3= new Show( LocalDateTime.of(2021,8,7, 22,20), 60,Rocky,cinemaHall,cinema1);


			chow1.setHall(cinemaHall_2);
			chow43.setHall(cinemaHall_2);
			chow53.setHall(cinemaHall_2);
			chow63.setHall(cinemaHall_2);
			chow73.setHall(cinemaHall_2);
			chow83.setHall(cinemaHall_2);
			chow93.setHall(cinemaHall_2);
			chow103.setHall(cinemaHall_2);
			chow11_3.setHall(cinemaHall_2);
			chow12_3.setHall(cinemaHall_2);

			chow2.setHall(cinemaHall1_2);
			chow7.setHall(cinemaHall1_2);
			chow42.setHall(cinemaHall1_2);
			chow52.setHall(cinemaHall1_2);
			chow62.setHall(cinemaHall1_2);
			chow72.setHall(cinemaHall1_2);
			chow82.setHall(cinemaHall1_2);
			chow92.setHall(cinemaHall1_2);
			chow102.setHall(cinemaHall1_2);
			chow11_2.setHall(cinemaHall1_2);
			chow12_2.setHall(cinemaHall1_2);

			chow3.setHall(cinemaHall2);
			chow4.setHall(cinemaHall2);
			chow5.setHall(cinemaHall2);
			chow6.setHall(cinemaHall2);
			chow8.setHall(cinemaHall2);
			chow9.setHall(cinemaHall2);
			chow10.setHall(cinemaHall2);
			chow11.setHall(cinemaHall2);
			chow12.setHall(cinemaHall2);

			List<Show> shows2 = new LinkedList<Show>();
			shows2.add(show1);
			shows2.add(show112);
			shows2.add(show113);
			shows2.add(show2);
			shows2.add(show22);
			shows2.add(show23);
			shows2.add(show3);
			shows2.add(show32);
			shows2.add(show33);
			shows2.add(show4);
			shows2.add(show42);
			shows2.add(show43);
			shows2.add(show5);
			shows2.add(show52);
			shows2.add(show53);
			shows2.add(show6);
			shows2.add(show62);
			shows2.add(show63);
			shows2.add(show7);
			shows2.add(show72);
			shows2.add(show73);
			shows2.add(show8);
			shows2.add(show82);
			shows2.add(show83);
			shows2.add(show9);
			shows2.add(show92);
			shows2.add(show93);
			shows2.add(show10);
			shows2.add(show102);
			shows2.add(show103);
			shows2.add(show11);
			shows2.add(show11_2);
			shows2.add(show11_3);
			shows2.add(show12);
			shows2.add(show12_2);
			shows2.add(show12_3);

			cinemaHalls2.add(cinemaHall2_2);
			cinemaHalls2.add(cinemaHall_2);
			cinemaHalls2.add(cinemaHall1_2);
			cinema2.setMovies(moviesList);
			cinema2.setShows(shows2);
			cinema2.setHalls(cinemaHalls2);
*/
			List<Show> HarryPotter7_shows = new LinkedList<Show>();
			HarryPotter7_shows.add(show1);
			HarryPotter7_shows.add(show112);
			HarryPotter7_shows.add(show113);
//			HarryPotter7_shows.add(chow1);
//			HarryPotter7_shows.add(chow112);
//			HarryPotter7_shows.add(chow113);
			HarryPotter7.setShows(HarryPotter7_shows);

			List<Show> LordOfTheRings_shows = new LinkedList<Show>();
			LordOfTheRings_shows.add(show2);
			LordOfTheRings_shows.add(show22);
			LordOfTheRings_shows.add(show23);
//			LordOfTheRings_shows.add(chow2);
//			LordOfTheRings_shows.add(chow22);
//			LordOfTheRings_shows.add(chow23);
			LordOfTheRings.setShows(LordOfTheRings_shows);

			List<Show> Titanic_shows = new LinkedList<Show>();
			Titanic_shows.add(show3);
			Titanic_shows.add(show32);
			Titanic_shows.add(show33);
//			Titanic_shows.add(chow3);
//			Titanic_shows.add(chow32);
//			Titanic_shows.add(chow33);
			Titanic.setShows(Titanic_shows);

			List<Show> Joker_shows = new LinkedList<Show>();
			Joker_shows.add(show4);
			Joker_shows.add(show42);
			Joker_shows.add(show43);
//			Joker_shows.add(chow4);
//			Joker_shows.add(chow42);
//			Joker_shows.add(chow43);
			Joker.setShows(Joker_shows);

			List<Show> TheAvengers_shows = new LinkedList<Show>();
			TheAvengers_shows.add(show5);
			TheAvengers_shows.add(show52);
			TheAvengers_shows.add(show53);
//			TheAvengers_shows.add(chow5);
//			TheAvengers_shows.add(chow52);
//			TheAvengers_shows.add(chow53);
			TheAvengers.setShows(TheAvengers_shows);

			List<Show> StarWars_shows = new LinkedList<Show>();
			StarWars_shows.add(show6);
			StarWars_shows.add(show62);
			StarWars_shows.add(show63);
//			StarWars_shows.add(chow6);
//			StarWars_shows.add(chow62);
//			StarWars_shows.add(chow63);
			StarWars.setShows(StarWars_shows);

			List<Show> Inception_shows = new LinkedList<Show>();
			Inception_shows.add(show7);
			Inception_shows.add(show72);
			Inception_shows.add(show73);
//			Inception_shows.add(chow7);
//			Inception_shows.add(chow72);
//			Inception_shows.add(chow73);
			Inception.setShows(Inception_shows);

			List<Show> TheDarKnight_shows = new LinkedList<Show>();
			TheDarKnight_shows.add(show8);
			TheDarKnight_shows.add(show82);
			TheDarKnight_shows.add(show83);
//			TheDarKnight_shows.add(chow8);
//			TheDarKnight_shows.add(chow82);
//			TheDarKnight_shows.add(chow83);
			TheDarKnight.setShows(TheDarKnight_shows);

			List<Show> CaptainAmerica_shows = new LinkedList<Show>();
			CaptainAmerica_shows.add(show9);
			CaptainAmerica_shows.add(show92);
			CaptainAmerica_shows.add(show93);
//			CaptainAmerica_shows.add(chow9);
//			CaptainAmerica_shows.add(chow92);
//			CaptainAmerica_shows.add(chow93);
			CaptainAmerica.setShows(CaptainAmerica_shows);

			List<Show> Avatar_shows = new LinkedList<Show>();
			Avatar_shows.add(show10);
			Avatar_shows.add(show102);
			Avatar_shows.add(show103);
//			Avatar_shows.add(chow10);
//			Avatar_shows.add(chow102);
//			Avatar_shows.add(chow103);
			Avatar.setShows(Avatar_shows);

			List<Show> Jaws_shows = new LinkedList<Show>();
			Jaws_shows.add(show11);
			Jaws_shows.add(show11_2);
			Jaws_shows.add(show11_3);
//			Jaws_shows.add(chow11);
//			Jaws_shows.add(chow11_2);
//			Jaws_shows.add(chow11_3);
			Jaws.setShows(Jaws_shows);

			List<Show> Rocky_shows = new LinkedList<Show>();
			Rocky_shows.add(show12);
			Rocky_shows.add(show12_2);
			Rocky_shows.add(show12_3);
//			Rocky_shows.add(chow12);
//			Rocky_shows.add(chow12_2);
//			Rocky_shows.add(chow12_3);
			Rocky.setShows(Rocky_shows);
/*
			for (int k=1; k<=2*10; k++){
				Seat seat = new Seat(true, k, k/11+1,cinemaHall_2);
				tempseats_2.add(seat);
			}

			for (int k=1; k<=4*10; k++){
				Seat seat = new Seat(true, k, k/11 +1,cinemaHall1_2);
				tempseats1_2.add(seat);
			}

			for (int k=1; k<=6*10; k++){
				Seat seat = new Seat(true, k, k/11 +1,cinemaHall2_2);
				tempseats2_2.add(seat);
			}
*/
			CinemaController.addCinema(session,cinema1);
			//CinemaController.addCinema(session,cinema2);




		} catch (Exception e) {
			e.printStackTrace();
		}
	}



		/***************************************/




	public static void main(String[] args) throws Exception {
		try {


			if (args.length != 1) {
				System.out.println("Required argument: <port>");
			} else {
				// initialize the DataBase
				InitializeDataBase();
				sendNewMoviesToPackagesCostumers();
				activatingLoop();
				CinemaServer server = new CinemaServer(Integer.parseInt(args[0]));
				server.listen();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
