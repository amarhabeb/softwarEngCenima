package org.example.OCSF;


import org.example.App;
import org.example.Boundaries.*;
import org.example.entities.*;
import org.hibernate.sql.Update;
import org.example.entities.Show;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;



public class CinemaClient extends AbstractClient {
	

	public static List<Show> ShowsData = new LinkedList<>();	// holds the shows data
	public static Boolean ShowsDataUpdated = false;	// holdS if the list ShowsData got updated yet
	public static Object ShowsDataLock = new Object();	// lock for accessing the ShowsData List

	public static List<Complaint> ComplaintsData = new LinkedList<>();
	public static Boolean ComplaintsDataUpdated = false;
	public static Object ComplaintsDataLock = new Object();

	public static List<Link> LinksData = new LinkedList<>();
	public static Boolean LinksDataUpdated = false;
	public static Object LinksDataLock = new Object();

	public static List<Movie> MoviesData = new LinkedList<>();
	public static Boolean MoviesDataUpdated = false;
	public static Object MoviesDataLock = new Object();

	public static List<Order> OrdersData = new LinkedList<>();
	public static Boolean OrdersDataUpdated = false;
	public static Object OrdersDataLock = new Object();

	public static List<Payment> PaymentData = new LinkedList<>();
	public static Boolean PaymentDataUpdated = false;
	public static Object PaymentDataLock = new Object();

	public static List<Refund> RefundData = new LinkedList<>();
	public static Boolean  RefundDataUpdated = false;
	public static Object  RefundDataLock = new Object();

	public static List<Regulations> RegulationsData = new LinkedList<>();
	public static Boolean RegulationsDataUpdated = false;
	public static Object  RegulationsDataLock = new Object();

   public static List<Cinema> CinemasData = new LinkedList<>();
	public static Boolean CinemasDataUpdated = false;
	public static Object CinemasDataLock = new Object();

	public static List<Ticket> TicketsData = new LinkedList<>();
	public static Boolean TicketsDataUpdated = false;
	public static Object TicketsDataLock = new Object();

	public static List<UpdatePriceRequest> UpdatePriceRequestsData = new LinkedList<>();
	public static Boolean UpdatePriceRequestsDataUpdated = false;
	public static Object UpdatePriceRequestsDataLock = new Object();

	public static List<Employee> EmployeeData = new LinkedList<>();
	public static Boolean EmployeeDataUpdated = false;
	public static Object EmployeeDataLock = new Object();

	public static List<Hall> HallsData = new LinkedList<>();
	public static Boolean HallsDataUpdated = false;
	public static Object HallsDataLock = new Object();

//	public static List<Message> MessageData = new LinkedList<>();
//	public static Boolean MessageDataUpdated = false;
//	public static Object MessageDataLock = new Object();

	public static List<PackageOrder> PackageData = new LinkedList<>();
	public static Boolean PackageDataUpdated = false;
	public static Object PackageDataLock = new Object();

	public static List<Seat>SeatData = new LinkedList<>();
	public static Boolean SeatDataUpdated = false;
	public static Object SeatDataLock = new Object();

	public static List<Ticket>TicketsReportData = new LinkedList<>();
	public static Boolean TicketsReportDataUpdated = false;
	public static Object TicketsReportDataLock = new Object();

	public static List<PackageOrder>PackagesReportData = new LinkedList<>();
	public static Boolean PackagesReportDataUpdated = false;
	public static Object PackagesReportDataLock = new Object();

	public static List<Link>LinksReportData = new LinkedList<>();
	public static Boolean LinksReportDataUpdated = false;
	public static Object LinksReportDataLock = new Object();

	public static List<Refund>RefundsReportData = new LinkedList<>();
	public static Boolean RefundsReportDataUpdated = false;
	public static Object RefundsReportDataLock = new Object();

	public static List<Complaint>ComplaintsReportData = new LinkedList<>();
	public static Boolean ComplaintsReportDataUpdated = false;
	public static Object ComplaintsReportDataLock = new Object();
	
	public static List<Person>PeopleData = new LinkedList<>();
	public static Boolean PeopleDataUpdated = false;
	public static Object PeopleDataLock = new Object();






//	public static List<TicketMessage>SeatData = new LinkedList<>();
//	public static Boolean SeatDataUpdated = false;
//	public static Object SeatDataLock = new Object();
//





	
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
		System.out.println("Message reply = " + message.get(0) + " ,reached client");

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
				UpdatePriceBoundary.ShowsPriceChanged = true;	// time is now changed
				ShowsDataUpdated = false;	// client's ShowsData is now not updated
				ShowsDataLock.notifyAll();
			}
		}
		if(message.get(0).equals("ShowsLoaded")) {
			synchronized(ShowsDataLock) {
				ShowsData = (List<Show>) message.get(1);
				ShowsDataUpdated = true;	// client's ShowsData is now not updated
				ShowsDataLock.notifyAll();
			}
			
		}
		if(message.get(0).equals("ComplaintesLoaded")) {
			synchronized(ComplaintsDataLock) {
				ComplaintsData = (List<Complaint>) message.get(1);
				ComplaintsDataUpdated = true;	// client's ShowsData is now not updated
				ComplaintsDataLock.notifyAll();
			}
		}
		if(message.get(0).equals("MarkComplaintAsDone")) {
			boolean success = (boolean)message.get(1);
			if(!success){
				throw new Exception("Controller failed");
			}
			synchronized(ComplaintsDataLock) {

				ComplaintsDataUpdated = false;	// client's ShowsData is now not updated
				ComplaintsDataLock.notifyAll();
			}
		}
//		if(message.get(0).equals("AddComplaint")) {
//			boolean success = (boolean)message.get(1);
//			if(!success){
//				throw new Exception("Controller failed");
//			}
//			synchronized(ComplaintsDataLock) {
//				AddComplaintBoundary.compalintsAdded = true;	// complaint added
//				ComplaintsDataUpdated = false;	//  Complaint added
//				ComplaintsDataLock.notifyAll();
//			}
//		}
//		if(message.get(0).equals("LinksLoaded")) {
//			boolean success = (boolean)message.get(1);
//			if(!success){
//				throw new Exception("Controller failed");
//			}
//			synchronized(LinksDataLock) {
//
//				LinksDataUpdated = true;	// client's ShowsData is now not updated
//				LinksDataLock.notifyAll();
//			}
//		}
//		if(message.get(0).equals("Costumer'sLinksLoaded")) {
//			boolean success = (boolean)message.get(1);
//			if(!success){
//				throw new Exception("Controller failed");
//			}
//			synchronized(LinksDataLock) {
//
//				LinksDataUpdated = true;	// client's ShowsData is now not updated
//				LinksDataLock.notifyAll();
//			}
//		}
		if(message.get(0).equals("MoviesLoaded")) {
			synchronized(MoviesDataLock) {
				MoviesData = (List<Movie>) message.get(1);
				MoviesDataUpdated = true;	// client's ShowsData is now not updated
				MoviesDataLock.notifyAll();
			}
		}
		if(message.get(0).equals("MovieAdded")) {
			boolean success = (boolean)message.get(1);
			if(!success){
				throw new Exception("Controller failed");
			}
			synchronized(MoviesDataLock) {
                AddMovieBoundary.MovieAdded = true;
				MoviesDataUpdated = false;	// client's ShowsData is now not updated
				MoviesDataLock.notifyAll();
			}
		}
		if(message.get(0).equals("MovieDeleted")) {
			boolean success = (boolean)message.get(1);
			if(!success){
				throw new Exception("Controller failed");
			}
			synchronized(MoviesDataLock) {
				DeleteMovieBoundary.MovieDeleted = true;
				MoviesDataUpdated = false;	// client's ShowsData is now not updated
				MoviesDataLock.notifyAll();
			}
		}
		if(message.get(0).equals("OrdersLoaded")) {
			synchronized(OrdersDataLock) {
				OrdersData = (List<Order>) message.get(1);
				OrdersDataUpdated = true;	// client's ShowsData is now not updated
				OrdersDataLock.notifyAll();
			}
		}

//		if(message.get(0).equals("Costumer'sOrdersLoaded")) {
//			boolean success = (boolean)message.get(1);
//			if(!success){
//				throw new Exception("Controller failed");
//			}
//			synchronized(OrdersDataLock) {
//				OrdersBoundary.loadCustomer=true;
//				OrdersDataUpdated = true;	// client's ShowsData is now not updated
//				OrdersDataLock.notifyAll();
//			}
//		}
//		if(message.get(0).equals("OrderAdded")) {
//			boolean success = (boolean)message.get(1);
//			if(!success){
//				throw new Exception("Controller failed");
//			}
//			synchronized(OrdersDataLock) {
//				OrdersBoundary.orderAdded=true;
//				OrdersDataUpdated = false;	// client's ShowsData is now not updated
//				OrdersDataLock.notifyAll();
//			}
//		}
//		if(message.get(0).equals("OrderDeleted")) {
//			boolean success = (boolean)message.get(1);
//			if(!success){
//				throw new Exception("Controller failed");
//			}
//			synchronized(OrdersDataLock) {
//				OrdersBoundary.orderDeleted=true;
//				OrdersDataUpdated = false;	// client's ShowsData is now not updated
//				OrdersDataLock.notifyAll();
//			}
//		}
//		if(message.get(0).equals("OrderDeleted")) {
//			boolean success = (boolean)message.get(1);
//			if(!success){
//				throw new Exception("Controller failed");
//			}
//			synchronized(OrdersDataLock) {
//				OrdersBoundary.orderDeleted=true;
//				OrdersDataUpdated = false;	// client's ShowsData is now not updated
//				OrdersDataLock.notifyAll();
//			}
//		}

		if(message.get(0).equals("PaymentSuccess")) {
			boolean success = (boolean)message.get(1);
			if(!success){
				throw new Exception("Controller failed");
			}
			synchronized(PaymentDataLock) {
				PaymentDataUpdated = false;	// client's ShowsData is now not updated
				PaymentDataLock.notifyAll();
			}
		}

		if(message.get(0).equals("RefundssLoaded")) {
			boolean success = (boolean)message.get(1);
			if(!success){
				throw new Exception("Controller failed");
			}
			synchronized( RefundDataLock) {
				RefundDataUpdated = true;	// client's ShowsData is now not updated
				RefundDataLock.notifyAll();
			}
		}
//		if(message.get(0).equals("RefundAdded")) {
//			boolean success = (boolean)message.get(1);
//			if(!success){
//				throw new Exception("Controller failed");
//			}
//			synchronized( RefundDataLock) {
//				RefundBoundary.RefundAdded  = true;
//				RefundDataUpdated = false;	// client's ShowsData is now not updated
//				RefundDataLock.notifyAll();
//			}
//		}
		if(message.get(0).equals("RegulationsLoaded")) {
			boolean success = (boolean)message.get(1);
			if(!success){
				throw new Exception("Controller failed");
			}
			synchronized( RegulationsDataLock) {
				RegulationsDataUpdated = true;	// client's ShowsData is now not updated
				RegulationsDataLock.notifyAll();
			}
		}
//		if(message.get(0).equals("RegulationStatusUpdated")) {
//			boolean success = (boolean)message.get(1);
//			if(!success){
//				throw new Exception("Controller failed");
//			}
//			synchronized( RegulationsDataLock) {
//				RegulationsUpdateBoundary.UpdateStatus=true;
//				RegulationsDataUpdated = false;	// client's ShowsData is now not updated
//				RegulationsDataLock.notifyAll();
//			}
//		}
//		if(message.get(0).equals("RegulationStatusDeactivated")) {
//			boolean success = (boolean)message.get(1);
//			if(!success){
//				throw new Exception("Controller failed");
//			}
//			synchronized( RegulationsDataLock) {
//				RegulationsUpdateBoundary.UpdateStatus=true;
//				RegulationsDataUpdated = false;	// client's ShowsData is now not updated
//				RegulationsDataLock.notifyAll();
//			}
//		}
		
		if(message.get(0).equals("ShowAdded")) {
			boolean success = (boolean)message.get(1);
			if(!success){
				throw new Exception("Controller failed");
			}
			synchronized(ShowsDataLock) {
				AddShowBoundary.ShowAdded=true;
				ShowsDataUpdated = false;	// client's ShowsData is now not updated
				ShowsDataLock.notifyAll();
			}
		}
		if(message.get(0).equals("ShowDeleted")) {
			boolean success = (boolean)message.get(1);
			if(!success){
				throw new Exception("Controller failed");
			}
			synchronized(ShowsDataLock) {
				DeleteShowBoundary.ShowDeleted=true;
				DeleteMovieBoundary.ShowDeleted=true;
				ShowsDataUpdated = false;	// client's ShowsData is now not updated
				ShowsDataLock.notifyAll();
			}
		}

		if(message.get(0).equals("TicketsLoaded")) {
			synchronized(TicketsDataLock) {
				TicketsData = (List<Ticket>) message.get(1);
				TicketsDataUpdated = true;	// client's ShowsData is now not updated
				TicketsDataLock.notifyAll();
			}
		}
//		if(message.get(0).equals("Costumer'sTicketsLoaded")) {
//			boolean success = (boolean)message.get(1);
//			if(!success){
//				throw new Exception("Controller failed");
//			}
//			synchronized( TicketsDataLock) {
//                TicketsBoundary.customerLoaded=true;
//				TicketsDataUpdated = true;	// client's ShowsData is now not updated
//				TicketsDataLock.notifyAll();
//			}
//		}
//		if(message.get(0).equals("TicketAdded")) {
//			boolean success = (boolean)message.get(1);
//			if(!success){
//				throw new Exception("Controller failed");
//			}
//			synchronized( TicketsDataLock) {
//				AddTicketsBoundary.ticketAdded=true;
//				TicketsDataUpdated = false;	// client's ShowsData is now not updated
//				TicketsDataLock.notifyAll();
//			}
//		}
		if(message.get(0).equals("UpdatePriceRequestLoaded")) {
			synchronized(UpdatePriceRequestsDataLock) {
				UpdatePriceRequestsData = (List<UpdatePriceRequest>) message.get(1);
				UpdatePriceRequestsDataUpdated = true;	// client's ShowsData is now not updated
				UpdatePriceRequestsDataLock.notifyAll();
			}
		}
		if(message.get(0).equals("RequestsApproved")) {
			boolean success = (boolean)message.get(1);
			if(!success){
				throw new Exception("Controller failed");
			}
			synchronized( UpdatePriceRequestsDataLock) {
				PriceUpdatingRequestsBoundary.RequestApproved=true;
				UpdatePriceRequestsDataUpdated = false;	// client's ShowsData is now not updated
				UpdatePriceRequestsDataLock.notifyAll();
			}
		}
//		if(message.get(0).equals("UpdatePriceChanged")) {
//			boolean success = (boolean)message.get(1);
//			if(!success){
//				throw new Exception("Controller failed");
//			}
//			synchronized( UpdatePriceRequestDataLock) {
//				UpdatePriceRequestBoundary.Approved=true;
//				UpdatePriceRequestDataUpdated = false;	// client's ShowsData is now not updated
//				UpdatePriceRequestDataLock.notifyAll();
//			}
//		}
		if(message.get(0).equals("RequestDeclined")) {
			boolean success = (boolean)message.get(1);
			if(!success){
				throw new Exception("Controller failed");
			}
			synchronized( UpdatePriceRequestsDataLock) {
				PriceUpdatingRequestsBoundary.RequestDeclined=true;
				UpdatePriceRequestsDataUpdated = false;	// client's ShowsData is now not updated
				UpdatePriceRequestsDataLock.notifyAll();
			}
		}
		if(message.get(0).equals("CinemasLoaded")) {
			synchronized(CinemasDataLock) {
				CinemasData = (List<Cinema>) message.get(1);
				CinemasDataUpdated = true;	// client's CinemasData is now not updated
				CinemasDataLock.notifyAll();
			}
		}
		if(message.get(0).equals("HallsLoaded")) {
			synchronized(HallsDataLock) {
				HallsData = (List<Hall>) message.get(1);
				HallsDataUpdated = true;	// client's HallsData is now not updated
				HallsDataLock.notifyAll();
			}
		}
//		if(message.get(0).equals("CinemaDeleted")) {
//			boolean success = (boolean)message.get(1);
//			if(!success){
//				throw new Exception("Controller failed");
//			}
//			synchronized( CinemasDataLock) {
//				deleteCinemaBoundary.deleted=true;
//				CinemasDataUpdated = false;	// client's ShowsData is now not updated
//				CinemasDataLock.notifyAll();
//			}
//		}
		if(message.get(0).equals("ComplaintsDeactivated")) {
			boolean success = (boolean)message.get(1);
			if(!success){
				throw new Exception("Controller failed");
			}
			synchronized( ComplaintsDataLock) {

				ComplaintsDataUpdated = false;	// client's ShowsData is now not updated
				ComplaintsDataLock.notifyAll();
			}
		}
		if(message.get(0).equals("EmployeesLoaded")) {
			boolean success = (boolean)message.get(1);
			if(!success){
				throw new Exception("Controller failed");
			}
			synchronized( EmployeeDataLock) {

				EmployeeDataUpdated = true;	// client's ShowsData is now not updated
				EmployeeDataLock.notifyAll();
			}
		}
//		if(message.get(0).equals("EmployeeAdded")) {
//			boolean success = (boolean)message.get(1);
//			if(!success){
//				throw new Exception("Controller failed");
//			}
//			synchronized( EmployeeDataLock) {
//                addEmplyeeBoundary.Added=true;
//				EmployeeDataUpdated = false;	// client's ShowsData is now not updated
//				EmployeeDataLock.notifyAll();
//			}
//		}
//		if(message.get(0).equals("EmployeeDeleted")) {
//			boolean success = (boolean)message.get(1);
//			if(!success){
//				throw new Exception("Controller failed");
//			}
//			synchronized( EmployeeDataLock) {
//				deleteEmplyeeBoundary.Deleted=true;
//				EmployeeDataUpdated = false;	// client's ShowsData is now not updated
//				EmployeeDataLock.notifyAll();
//			}
//		}
//		if(message.get(0).equals("LogInCompleted")) {
//			boolean success = (boolean)message.get(1);
//			if(!success){
//				throw new Exception("Controller failed");
//			}
//			synchronized( EmployeeDataLock) {
//				logInBoundary.logged=true;
//				EmployeeDataUpdated = true;	// client's ShowsData is now not updated
//				EmployeeDataLock.notifyAll();
//			}
//		}
		if(message.get(0).equals("HallDeleted")) {
			boolean success = (boolean)message.get(1);
			if(!success){
				throw new Exception("Controller failed");
			}
			synchronized( HallsDataLock) {
				HallsDataUpdated = false;	// client's ShowsData is now not updated
				HallsDataLock.notifyAll();
			}
		}
//		if(message.get(0).equals("LinkAdded")) {
//			boolean success = (boolean)message.get(1);
//			if(!success){
//				throw new Exception("Controller failed");
//			}
//			synchronized( LinksDataLock) {
//				AddLinkBoundary.Added=true;
//				LinksDataUpdated = false;	// client's ShowsData is now not updated
//				LinksDataLock.notifyAll();
//			}
//		}
		if(message.get(0).equals("LinkTimeLoaded")) {
			boolean success = (boolean)message.get(1);
			if(!success){
				throw new Exception("Controller failed");
			}
			synchronized( LinksDataLock) {
				LinksDataUpdated = true;	// client's ShowsData is now not updated
				LinksDataLock.notifyAll();
			}
		}
		if(message.get(0).equals("LinkPriceLoaded")) {
			boolean success = (boolean)message.get(1);
			if(!success){
				throw new Exception("Controller failed");
			}
			synchronized( LinksDataLock) {
				LinksDataUpdated = true;	// client's ShowsData is now not updated
				LinksDataLock.notifyAll();
			}
		}
		if(message.get(0).equals("LinkCanceled")) {
			boolean success = (boolean)message.get(1);
			if(!success){
				throw new Exception("Controller failed");
			}
			synchronized( LinksDataLock) {
				LinksDataUpdated = true;	// client's ShowsData is now not updated
				LinksDataLock.notifyAll();
			}
		}
		if(message.get(0).equals("MailSent")) {
			boolean success = (boolean)message.get(1);
			if(!success){
				throw new Exception("Controller failed");
			}
//			synchronized( LinksDataLock) {
//				LinksDataUpdated = true;	// client's ShowsData is now not updated
//				LinksDataLock.notifyAll();
//			}
		}

		if(message.get(0).equals("NewMoviesLoaded")) {
			boolean success = (boolean)message.get(1);
			if(!success){
				throw new Exception("Controller failed");
			}
			synchronized( MoviesDataLock) {

				MoviesDataUpdated = true;	// client's ShowsData is now not updated
				MoviesDataLock.notifyAll();
			}
		}
		if(message.get(0).equals("PackagesLoaded")) {
			boolean success = (boolean)message.get(1);
			if(!success){
				throw new Exception("Controller failed");
			}
			synchronized( PackageDataLock) {

				PackageDataUpdated = true;	// client's ShowsData is now not updated
				PackageDataLock.notifyAll();
			}
		}
		if(message.get(0).equals("Costumer'sPackagesLoaded")) {
			boolean success = (boolean)message.get(1);
			if(!success){
				throw new Exception("Controller failed");
			}
			synchronized( PackageDataLock) {

				PackageDataUpdated = true;	// client's ShowsData is now not updated
				PackageDataLock.notifyAll();
			}
		}
//		if(message.get(0).equals("PackageAdded")) {
//			boolean success = (boolean)message.get(1);
//			if(!success){
//				throw new Exception("Controller failed");
//			}
//			synchronized( PackageDataLock) {
//                addPackageBoundary.Added=true;
//				PackageDataUpdated = false;	// client's ShowsData is now not updated
//				PackageDataLock.notifyAll();
//			}
//		}
		if(message.get(0).equals("SeatsLoaded")) {
			boolean success = (boolean)message.get(1);
			if(!success){
				throw new Exception("Controller failed");
			}
			synchronized( SeatDataLock) {

				SeatDataUpdated = true;	// client's ShowsData is now not updated
				SeatDataLock.notifyAll();
			}
		}
//		if(message.get(0).equals("SeatAdded")) {
//			boolean success = (boolean)message.get(1);
//			if(!success){
//				throw new Exception("Controller failed");
//			}
//			synchronized( SeatDataLock) {
//                addSeatsBoundary.Added=true;
//				SeatDataUpdated = false;	// client's ShowsData is now not updated
//				SeatDataLock.notifyAll();
//			}
//		}
//		if(message.get(0).equals("SeatDeleted")) {
//			boolean success = (boolean)message.get(1);
//			if(!success){
//				throw new Exception("Controller failed");
//			}
//			synchronized( SeatDataLock) {
//				deleteSeatsBoundary.Deleted=true;
//				SeatDataUpdated = false;	// client's ShowsData is now not updated
//				SeatDataLock.notifyAll();
//			}
//		}
		if(message.get(0).equals("UpdatePriceRequestAdded")) {
			boolean success = (boolean)message.get(1);
			if(!success){
				throw new Exception("Controller failed");
			}
			synchronized( UpdatePriceRequestsDataLock) {
			//	addUpdatePriceRequestBoundary.Addedtrue;
				UpdatePriceRequestsDataUpdated = false;	// client's ShowsData is now not updated
				UpdatePriceRequestsDataLock.notifyAll();
			}
		}
		if(message.get(0).equals("UpdatePriceRequestDeleted")) {
			boolean success = (boolean)message.get(1);
			if(!success){
				throw new Exception("Controller failed");
			}
			synchronized( UpdatePriceRequestsDataLock) {
				//deleteUpdatePriceRequestBoundary.Deleted=true;
				UpdatePriceRequestsDataUpdated = false;	// client's ShowsData is now not updated
				UpdatePriceRequestsDataLock.notifyAll();
			}
		}
		if(message.get(0).equals("UpdatePriceRequestDeleted")) {
			boolean success = (boolean)message.get(1);
			if(!success){
				throw new Exception("Controller failed");
			}
			synchronized( UpdatePriceRequestsDataLock) {
			//	deleteUpdatePriceRequestBoundary.Deleted=true;
				UpdatePriceRequestsDataUpdated = false;	// client's ShowsData is now not updated
				UpdatePriceRequestsDataLock.notifyAll();
			}
		}
		
//		if(message.get(0).equals("PeopleLoaded")) {
//			synchronized(PeopleDataLock) {
//				PeopleData = (List<Person>) message.get(1);
//				PeopleDataUpdated = true;	// client's ShowsData is now not updated
//				PeopleDataLock.notifyAll();
//			}
//			
//		}
		
		if(message.get(0).equals("TicketsReportLoaded")) {
			synchronized(TicketsReportDataLock) {
				TicketsReportData = (List<Ticket>) message.get(1);
				TicketsReportDataUpdated = true;	// client's ShowsData is now not updated
				TicketsReportDataLock.notifyAll();
			}
		}
		
		if(message.get(0).equals("PackagesReportLoaded")) {
			synchronized(PackagesReportDataLock) {
				PackagesReportData = (List<PackageOrder>) message.get(1);
				PackagesReportDataUpdated = true;	// client's ShowsData is now not updated
				PackagesReportDataLock.notifyAll();
			}
		}
		
		if(message.get(0).equals("LinksReportLoaded")) {
			synchronized(LinksReportDataLock) {
				LinksReportData = (List<Link>) message.get(1);
				LinksReportDataUpdated = true;	// client's ShowsData is now not updated
				LinksReportDataLock.notifyAll();
			}
		}
		
		if(message.get(0).equals("RefundsReportLoaded")) {
			synchronized(RefundsReportDataLock) {
				RefundsReportData = (List<Refund>) message.get(1);
				RefundsReportDataUpdated = true;	// client's ShowsData is now not updated
				RefundsReportDataLock.notifyAll();
			}
		}
		
		if(message.get(0).equals("ComplaintsReportLoaded")) {
			synchronized(ComplaintsReportDataLock) {
				ComplaintsReportData = (List<Complaint>) message.get(1);
				ComplaintsReportDataUpdated = true;	// client's ShowsData is now not updated
				ComplaintsReportDataLock.notifyAll();
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

