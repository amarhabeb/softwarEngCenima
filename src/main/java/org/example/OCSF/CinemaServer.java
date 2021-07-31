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
import java.time.Month;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;



public class CinemaServer extends AbstractServer{
	
	public static Regulations currentRegs = null;	// this is the regulations of the cinema chain
	
	private static Session session;
	private static Thread activateThread;
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
						Month month = (Month) message.get(2);
						Year year = (Year) message.get(3);
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
						Month month = (Month) message.get(1);
						Year year = (Year) message.get(2);
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
						Month month = (Month) message.get(1);
						Year year = (Year) message.get(2);
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
						Month month = (Month) message.get(1);
						Year year = (Year) message.get(2);
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
						Month month = (Month) message.get(1);
						Year year = (Year) message.get(2);
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

				while (true) {



					synchronized (threadLock) {
						try {

							LinkController.activateLinksWhenTimeCome(session);


						} catch (Exception e) {
							e.printStackTrace();
						}
						threadLock.notifyAll();
					}
				}
			}
		});

		activateThread.start();


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



		/////// Testing PackageController
		PackageOrder pack=new PackageOrder(150,2);
		PackagesController.addPackage(session, pack);
		PackageOrder pack2=new PackageOrder(150,1);
		PackagesController.addPackage(session, pack2);
		PackageOrder pack3=new PackageOrder(150,1);
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
		List<Show> cinema_shows=CinemaController.loadCinemaShows(session,2);
		System.out.println(cinema_shows.size());
	}



	private static void generateMovies(Session session) throws Exception {
    	try {
			Cinema cinema1 = new Cinema("Downtown Cinema");
			//Cinema cinema2 = new Cinema("Mall Cinema");
			List<Movie> moviesList = new LinkedList<Movie>();
			List<Show> emptyShowList = new LinkedList<Show>();
			int[] days = {6, 15, 17, 18,22,24,30};
			int[] months = {8,9,10,11,12};
			int[] years = {2021,2021,2021,2021,2021,2021,2021};

			int[] hours = {18,18,19,19,20,20,21,21,22,22};
			int[] minutes = {00,30,00,30,00,30,00,30,00,30,};


			String[] availability = {"AVAILABLE", "NOT_AVAILABLE"};

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


			Movie HarryPotter7= new Movie ("Harry Potter 7", "הארי פוטר 7", "David Yates", init.HarryPotterCast(),"bla bla bla", LocalDate.parse("2021-07-31"),false,  imFile, emptyShowList,false);
			moviesList.add(HarryPotter7);
			Movie Joker=new Movie("Joker","גוקר","Todd Phillips",init.JokerCast(), init.JokerSummary(), LocalDate.parse("2021-08-03"),false, im1File, emptyShowList,false);
			moviesList.add(Joker);
			Movie TheAvengers=new Movie("The Avengers","הנוקמים","Kevin Feige",init.TheAvengersCast(), init.TheAvengersSummary(), LocalDate.parse("2021-05-18"),true, im3File, emptyShowList,false);
			moviesList.add(TheAvengers);
			Movie StarWars=new Movie("Star Wars","מלחמת הכוכבים","George Lucas",init.StarWarsCast(), init.StarWarsSummary(), LocalDate.parse("2021-06-19"),true, im2File, emptyShowList,false);
			moviesList.add(StarWars);
			Movie Inception=new Movie("Incepteion","התחלה","Emma Thomas",init.InceptionCast(), init.InceptionSummary(), LocalDate.parse("2021-02-13"),true, im4File, emptyShowList,false);
			moviesList.add(Inception);
			Movie TheDarKnight=new Movie("The Dark Knight","האביר האפל","Emma Thomas,Charles Roven,Christopher Nolan",init.TheDarkKnightCast(), init.TheDarkKnightSummary(), LocalDate.parse("2021-12-18"),true, im5File, emptyShowList,false);
			moviesList.add(TheDarKnight);
			Movie CaptainAmerica=new Movie("Captain America","קפטן אמריקה","Kevin Feige",init.CaptainAmericaCast(), init.CaptainAmericaSummary(), LocalDate.parse("2021-08-11"),true, im6File, emptyShowList,false);
			moviesList.add(CaptainAmerica);
			Movie Avatar=new Movie("Avatar","אווטאר","James Cameron,Jon Landau",init.AvatarCast(), init.AvatarSummary(), LocalDate.parse("2021-07-18"),true, im7File, emptyShowList,false);
			moviesList.add(Avatar);
			Movie Jaws=new Movie("Jaws","מלתעות","Steven Spielberg",init.JawsCast(), init.JawsSummary(), LocalDate.parse("2019-08-14"),true, im8File, emptyShowList,false);
			moviesList.add(Jaws);
			Movie Rocky=new Movie("Rocky","רוקי","John G. Avildsen",init.RockyCast(), init.RockySummary(), LocalDate.parse("2021-01-16"),true, im9File, emptyShowList,false);
			moviesList.add(Rocky);
			Movie Titanic=new Movie("Titanic","טיטניק","James Cameron",init.TitanicCast(), init.TitanicSummary(), LocalDate.parse("2021-02-18"),true, im10File, emptyShowList,true);
			moviesList.add(Titanic);
			Movie LordOfTheRings=new Movie("Lord Of The Rings","שר הטבעות","Peter Jackson",init.LordOfTheRingsCast(), init.LordOfTheRingsSummary(), LocalDate.parse("2021-06-18"),true, im11File, emptyShowList,true);
			moviesList.add(LordOfTheRings);
			List<Hall> cinemaHalls = new LinkedList<Hall>();
			List<Show> shows = new LinkedList<Show>();
			List<Show> shows1 = new LinkedList<Show>();
			List<Show> shows2 = new LinkedList<Show>();
			List<Show> shows3 = new LinkedList<Show>();

			List<Seat> tempseats = new LinkedList<Seat>();
			List<Seat> tempseats1 = new LinkedList<Seat>();
			List<Seat> tempseats2 = new LinkedList<Seat>();
			//Hall cinemaHall = new Hall(1, 2*10,tempseats, cinema1,shows1);
			Hall cinemaHall = new Hall(1, 2*10,tempseats, cinema1);
			//Hall cinemaHall1 = new Hall(2, 4*10,tempseats1, cinema1,shows2);
			Hall cinemaHall1 = new Hall(2, 4*10,tempseats1, cinema1);

			List<Seat> tempSeats = new LinkedList<Seat>();
			//Hall cinemaHall2 =new Hall(2, 6*10,tempSeats, cinema1,shows);
			Hall cinemaHall2 =new Hall(3, 6*10,tempSeats, cinema1);
			Show show1= new Show( LocalDateTime.of(years[0],months[1],days[0], hours[0],minutes[1]), availability[(0)%2], 60,HarryPotter7,cinemaHall,cinema1);
			Show show112= new Show( LocalDateTime.of(years[3],months[2],days[0], hours[1],minutes[1]), availability[(0)%2], 60,HarryPotter7,cinemaHall2,cinema1);
			Show show113= new Show( LocalDateTime.of(years[1],months[3],days[0], hours[0],minutes[1]), availability[(0)%2], 60,HarryPotter7,cinemaHall1,cinema1);

			Show show2= new Show( LocalDateTime.of(years[1],months[4],days[1], hours[2],minutes[2]), availability[(2)%2], 60, LordOfTheRings,cinemaHall1,cinema1);
			Show show22= new Show( LocalDateTime.of(years[1],months[4],days[2], hours[2],minutes[2]), availability[(2)%2], 60, LordOfTheRings,cinemaHall2,cinema1);
			Show show23= new Show( LocalDateTime.of(years[1],months[4],days[3], hours[2],minutes[2]), availability[(2)%2], 60, LordOfTheRings,cinemaHall,cinema1);

			Show show3= new Show( LocalDateTime.of(years[2],months[3],days[2], hours[2],minutes[3]), availability[(2)%2], 60,Titanic,cinemaHall2,cinema1);
			Show show32= new Show( LocalDateTime.of(years[2],months[4],days[4], hours[2],minutes[3]), availability[(2)%2], 60,Titanic,cinemaHall1,cinema1);
			Show show33= new Show( LocalDateTime.of(years[2],months[4],days[1], hours[2],minutes[3]), availability[(2)%2], 60,Titanic,cinemaHall,cinema1);

			Show show4= new Show( LocalDateTime.of(years[3],months[1],days[3], hours[0],minutes[3]), availability[(2)%2], 60,Joker,cinemaHall2,cinema1);
			Show show5= new Show( LocalDateTime.of(years[4],months[2],days[4], hours[0],minutes[3]), availability[(2)%2], 60,TheAvengers,cinemaHall2,cinema1);
			Show show6= new Show( LocalDateTime.of(years[0],months[1],days[0], hours[1],minutes[3]), availability[(2)%2], 60,StarWars,cinemaHall2,cinema1);
			Show show7= new Show( LocalDateTime.of(years[1],months[0],days[1], hours[2],minutes[4]), availability[(0)%2], 60,Inception,cinemaHall1,cinema1);
			Show show8= new Show( LocalDateTime.of(years[2],months[4],days[2], hours[1],minutes[0]), availability[(1)%2], 60,TheDarKnight,cinemaHall1,cinema1);
			Show show9= new Show( LocalDateTime.of(years[3],months[3],days[3], hours[2],minutes[1]), availability[(2)%2], 60,CaptainAmerica,cinemaHall1,cinema1);
			Show show10= new Show( LocalDateTime.of(years[4],months[2],days[4], hours[1],minutes[0]), availability[(1)%2], 60,Avatar,cinemaHall,cinema1);
			Show show11= new Show( LocalDateTime.of(years[2],months[1],days[0], hours[2],minutes[0]), availability[(1)%2], 60,Jaws,cinemaHall,cinema1);
			Show show12= new Show( LocalDateTime.of(years[1],months[0],days[1], hours[0],minutes[0]), availability[(1)%2], 60,Rocky,cinemaHall,cinema1);

			show1.setHall(cinemaHall);
			show10.setHall(cinemaHall);
			show11.setHall(cinemaHall);
			show12.setHall(cinemaHall);

			show2.setHall(cinemaHall1);
			show7.setHall(cinemaHall);
			show8.setHall(cinemaHall1);
			show9.setHall(cinemaHall1);


			show3.setHall(cinemaHall2);
			show4.setHall(cinemaHall2);
			show5.setHall(cinemaHall2);
			show6.setHall(cinemaHall2);


			shows.add(show1);
			shows.add(show10);
			shows.add(show11);
			shows.add(show12);

			shows1.add(show2);
			shows1.add(show7);
			shows1.add(show8);
			shows1.add(show9);


			shows2.add(show3);
			shows2.add(show4);
			shows2.add(show5);
			shows2.add(show6);


			shows3.add(show1);
			shows3.add(show2);
			shows3.add(show3);
			shows3.add(show4);
			shows3.add(show5);
			shows3.add(show6);
			shows3.add(show7);
			shows3.add(show8);
			shows3.add(show9);
			shows3.add(show10);
			shows3.add(show11);
			shows3.add(show12);

			cinema1.setMovies(moviesList);
			cinemaHalls.add(cinemaHall2);
			cinemaHalls.add(cinemaHall);
			cinemaHalls.add(cinemaHall1);
			cinema1.setShows(shows3);

			List<Show> HarryPotter7_shows = new LinkedList<Show>();
			HarryPotter7_shows.add(show1);
			HarryPotter7_shows.add(show112);
			HarryPotter7_shows.add(show113);
			HarryPotter7.setShows(HarryPotter7_shows);


			List<Show> Titanic_shows = new LinkedList<Show>();
			Titanic_shows.add(show3);
			Titanic_shows.add(show32);
			Titanic_shows.add(show33);
			Titanic.setShows(Titanic_shows);


			List<Show> LordOfTheRings_shows = new LinkedList<Show>();
			LordOfTheRings_shows.add(show2);
			LordOfTheRings_shows.add(show22);
			LordOfTheRings_shows.add(show23);

			Joker.setShows(Collections.singletonList(show4));
			TheAvengers.setShows(Collections.singletonList(show5));
			StarWars.setShows(Collections.singletonList(show6));
			Inception.setShows(Collections.singletonList(show7));
			TheDarKnight.setShows(Collections.singletonList(show8));
			CaptainAmerica.setShows(Collections.singletonList(show9));
			Avatar.setShows(Collections.singletonList(show10));
			Jaws.setShows(Collections.singletonList(show11));
			Rocky.setShows(Collections.singletonList(show12));

			//cinemaHall.setShows(shows1);
			//cinemaHall1.setShows(shows2);
			//cinemaHall2.setShows(shows);
			cinemaHall.setSeats(tempseats);
			cinemaHall1.setSeats(tempseats1);
			cinemaHall2.setSeats(tempseats2);

			for (int k=1; k<=2*10; k++){
				Seat seat = new Seat(true, 1, 1/10 +1,cinemaHall);
				tempseats.add(seat);
			}
			for(int o=0;o<tempseats.size();o++){
				tempseats.get(o).setHall(cinemaHall);
			}



			for (int k=1; k<=2*2*10; k++){
				Seat seat = new Seat(true, 2%10, 2/10 +1,cinemaHall1);
				tempseats1.add(seat);
			}
			for(int o=0;o<tempseats1.size();o++){
				tempseats1.get(o).setHall(cinemaHall1);
			}

			for (int k=1; k<=2*2*2*10; k++){
				Seat seat = new Seat(true, 3%10, 3/10 +1,cinemaHall2);
				tempseats2.add(seat);
			}
			for(int o=0;o<tempseats2.size();o++){
				tempseats2.get(o).setHall(cinemaHall2);
			}

			ShowsController.addShow(CinemaServer.session,show1);



			cinemaHall1.setCinema(cinema1);
			cinemaHall.setCinema(cinema1);
			cinemaHall2.setCinema(cinema1);
			//cinemaHall.setShows(emptyShowList);
			//cinemaHall1.setShows(emptyShowList);
			//cinemaHall2.setShows(emptyShowList);
			//cinemaHall2.addShow(show1);
			cinema1.setHalls(cinemaHalls);
			CinemaController.addCinema(CinemaServer.session,cinema1);
			HallController.addHall(CinemaServer.session,cinemaHall);
			HallController.addHall(CinemaServer.session,cinemaHall1);
			HallController.addHall(CinemaServer.session,cinemaHall2);

			for(int i=0;i<tempseats.size();i++){
				SeatController.addSeat(CinemaServer.session,tempseats.get(i));
			}
			for(int i=0;i<tempseats1.size();i++){
				SeatController.addSeat(CinemaServer.session,tempseats1.get(i));
			}
			for(int i=0;i<tempseats2.size();i++){
				SeatController.addSeat(CinemaServer.session,tempseats2.get(i));
			}
			for(int i=0;i<moviesList.size();i++){
				MoviesController.addMovie(CinemaServer.session,moviesList.get(i));
			}







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
				activatingLoop();
				CinemaServer server = new CinemaServer(Integer.parseInt(args[0]));
				server.listen();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
