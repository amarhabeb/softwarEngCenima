package org.example.Boundaries;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.example.App;
import org.example.OCSF.CinemaClient;
import org.example.entities.Cinema;
import org.example.entities.Complaint;
import org.example.entities.Link;
import org.example.entities.Ticket;
import org.example.entities.PackageOrder;
import org.example.entities.Refund;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;

import javafx.scene.control.Label;
import javafx.scene.text.Text;

@SuppressWarnings("serial")
public class ViewReportBoundary extends EmployeeBoundary implements Initializable, Serializable{
	
	@FXML // fx:id="title"
    private Label title; // Value injected by FXMLLoader

    @FXML // fx:id="GoBackToMainBtn"
    private Button GoBackToMainBtn; // Value injected by FXMLLoader
    
    @FXML
    private Button viewDetailsBtn;
    
    @FXML
    private LineChart<Number, Number> reports_chart;
    
    @FXML
    private Text bestSalesText;
    @FXML
    private Text worstSalesText;
    @FXML
    private Text totalText;
    @FXML
    private Text profitsText;
    @FXML
    private Text refundedText;
    
    // will hold the tickets of the chosen report if needed
    List<Ticket> tickets = null;
    // will hold the packages of the chosen report if needed
    List<PackageOrder> packages = null;
    // will hold the links of the chosen report if needed
    List<Link> links = null;
    // will hold the refunds of the chosen report if needed
    List<Refund> refunds = null;
    // will hold the complaints of the chosen report if needed
    List<Complaint> complaints = null;
    
    @FXML
    void clickGoBackToMainBtn(ActionEvent event) throws IOException {
    	App.setRoot("ChainManagerMB",null);
    }
    
    @FXML
    void clickViewDetailsBtn(ActionEvent event) {
    	
    }
    
    // given a data array, tis method will fill the chart with this data
    void fillChart(int[] data, XYChart.Series<Number, Number> series){
    	int idx = 1;
    	for(int val:data) {
    		series.getData().add(new XYChart.Data<>(idx, val));	// add (x,y) value to series
    		idx++;
    	}
    	// fill chart with series
    	reports_chart.getData().add(series);
    }
    
    public int getIndexOfLargest( int[] array ) {
      if ( array == null || array.length == 0 ) return -1; // null or empty

      int largest = 0;
      for ( int i = 1; i < array.length; i++ )
      {
          if ( array[i] > array[largest] ) largest = i;
      }
      return largest; // position of the first largest found
    }
    
    public int getIndexOfSmallest( int[] array ) {
      if ( array == null || array.length == 0 ) return -1; // null or empty

      int smallest = 0;
      for ( int i = 1; i < array.length; i++ )
      {
          if ( array[i] < array[smallest] ) smallest = i;
      }
      return smallest; // position of the first smallest found
    }
        
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		params=getParams();
		// get passsed parameters
		Integer month = (Integer) params.get(1);
  		Integer year = (Integer) params.get(2);
  		Cinema cinema = (Cinema) params.get(3);
  		String report_type = (String) params.get(0);
  		// set title according to chosen report type
  		title.setText(report_type);
  		
  	    
  		// if the report is of Tickets Sales type
  		if(report_type == "Tickets Sales") {
  			// data represented in chart
  	  		XYChart.Series<Number, Number> series = new XYChart.Series<>();
  	  	    series.setName("Tickets sold per day, " + cinema.toString() + ", "+ month.toString() + ", " + year.toString());
	  		// get needed tickets
	  		synchronized(CinemaClient.TicketsReportDataLock) {
		  		UpdateTicketsReportData(cinema.getID(), Month.of(month), Year.of(year));
    		  		tickets = CinemaClient.TicketsReportData;
	  		}

	  		// get number of days in the chosen month
	  		YearMonth yearMonthObject = YearMonth.of(year, month);
	  		int daysInMonth = yearMonthObject.lengthOfMonth();

	  		int[] salesInADay = new int[daysInMonth];	// array is initialized by default to zeros
	  		double profits = 0;
	  		for (Ticket ticket:tickets) {
	  			salesInADay[ticket.getOrderDate().getDayOfMonth()-1]++;
	  			profits+=ticket.getPrice();	// calculate how much money was paid for tickets
	  		}

	  		// fill chart
	  		fillChart(salesInADay, series);

	  		// find the day of max sales
	  		int max_sales_day = getIndexOfLargest(salesInADay)+1;
	  		bestSalesText.setText("Best tickets sales of the month were made on " + Integer.toString(max_sales_day) + "/" + Integer.toString(month));

	  		// find the day of min sales
	  		int min_sales_day = getIndexOfSmallest(salesInADay)+1;
	  		worstSalesText.setText("Worst tickets sales of the month were made on " + Integer.toString(min_sales_day) + "/" + Integer.toString(month));

	  		// find the total selled
	  		int total = tickets.size();
	  		totalText.setText("Total sold tickets in this month: " + Integer.toString(total));

	  		// find the profits
	  		profitsText.setText("Profits: " + Double.toString(profits) + "₪");

	  		refundedText.setText("");

  		}
  		

  		// if the report is of Packages and Online Shows Sales type
  		if(report_type == "Packages and Online Shows Sales") {
  			// data represented in chart
  	  		XYChart.Series<Number, Number> series_p = new XYChart.Series<>();
  	  	    series_p.setName("Packages sold per day, "+ month.toString() + ", " + year.toString());
  	  	    XYChart.Series<Number, Number> series_l = new XYChart.Series<>();
	  	    series_l.setName("Online shows sold per day, "+ month.toString() + ", " + year.toString());
	  		// get needed packages
	  		synchronized(CinemaClient.PackagesReportDataLock) {
		  		UpdatePackagesReportData(Month.of(month), Year.of(year));
		  		packages = CinemaClient.PackagesReportData;
	  		}
	  		// get needed packages
	  		synchronized(CinemaClient.LinksReportDataLock) {
		  		org.example.Boundaries.Boundary.UpdateLinksReportData(Month.of(month), Year.of(year));
		  		links = CinemaClient.LinksReportData;
	  		}

	  		// get number of days in the chosen month
	  		YearMonth yearMonthObject = YearMonth.of(year, month);
	  		int daysInMonth = yearMonthObject.lengthOfMonth();

	  		int[] salesInADay_packages = new int[daysInMonth];	// array is initialized by default to zeros
	  		int[] salesInADay_links = new int[daysInMonth];	// array is initialized by default to zeros
	  		double profits = 0;
	  		for (PackageOrder p:packages) {
	  			salesInADay_packages[p.getOrderDate().getDayOfMonth()-1]++;
	  			profits+=p.getPrice();	// calculate how much money was paid for packages
	  		}
	  		for (Link link:links) {
	  			salesInADay_links[link.getOrderDate().getDayOfMonth()-1]++;
	  			profits+=link.getPrice();	// calculate how much money was paid for links
	  		}

	  		// fill chart
	  		fillChart(salesInADay_packages, series_p);
	  		fillChart(salesInADay_links, series_l);

	  		// sum sales
	  		int[] salesInADay = new int[daysInMonth];
	  		for(int i=0; i<daysInMonth; i++) {
	  			salesInADay[i] = salesInADay_packages[i]+salesInADay_links[i];
	  		}

	  		// find the day of max sales
	  		int max_sales_day = getIndexOfLargest(salesInADay)+1;
	  		bestSalesText.setText("Best packages and online shows sales of the month were made on " + Integer.toString(max_sales_day) + "/" + Integer.toString(month));

	  		// find the day of min sales
	  		int min_sales_day = getIndexOfSmallest(salesInADay)+1;
	  		worstSalesText.setText("Worst packages and online shows sales of the month were made on " + Integer.toString(min_sales_day) + "/" + Integer.toString(month));

	  		// find the total selled
	  		int total = packages.size() + links.size();
	  		totalText.setText("Total sold packages and online shows in this month: " + Integer.toString(total));

	  		// find the profits
	  		profitsText.setText("Profits: " + Double.toString(profits) + "₪");

	  		refundedText.setText("");
  		}

  		// if the report is of Refunds type
  		if(report_type == "Refunds") {
  			// data represented in chart
  	  		XYChart.Series<Number, Number> series = new XYChart.Series<>();
  	  	    series.setName("Refunded money per day, " + month.toString() + ", " + year.toString());
	  		// get needed refunds
	  		synchronized(CinemaClient.RefundsReportDataLock) {
		  		org.example.Boundaries.Boundary.UpdateRefundsReportData(Month.of(month), Year.of(year));
		  		refunds = CinemaClient.RefundsReportData;
	  		}

	  		// get number of days in the chosen month
	  		YearMonth yearMonthObject = YearMonth.of(year, month);
	  		int daysInMonth = yearMonthObject.lengthOfMonth();

	  		int[] refundedInADay = new int[daysInMonth];	// array is initialized by default to zeros
	  		double amount_refunded = 0;
	  		for (Refund refund:refunds) {
	  			refundedInADay[refund.getDate().getDayOfMonth()-1]+=refund.getAmount();
	  			amount_refunded+=refund.getAmount();	// calculate how much money was paid back
	  		}

	  		// fill chart
	  		fillChart(refundedInADay, series);

	  		// find the day of max sales
	  		int max_refunded_day = getIndexOfLargest(refundedInADay)+1;
	  		bestSalesText.setText("Highest refunded amount of the month was made on " + Integer.toString(max_refunded_day) + "/" + Integer.toString(month));

	  		// find the day of min sales
	  		int min_refunded_day = getIndexOfSmallest(refundedInADay)+1;
	  		worstSalesText.setText("Lowest refunded amount of the month was made on " + Integer.toString(min_refunded_day) + "/" + Integer.toString(month));

	  		// find the total refunds
	  		int total = refunds.size();
	  		totalText.setText("Total refunds in this month: " + Integer.toString(total));

	  		profitsText.setText("");

	  		// find the refunded amount
	  		refundedText.setText("Total refunded money: " + Double.toString(amount_refunded) + "₪");
  		}


  		// if the report is of Refunds type
  		if(report_type == "Complaints") {
  			// data represented in chart
  	  		XYChart.Series<Number, Number> series = new XYChart.Series<>();
  	  	    series.setName("Complaints per day, " + month.toString() + ", " + year.toString());
	  		// get needed complaints
	  		synchronized(CinemaClient.ComplaintsReportDataLock) {
		  		org.example.Boundaries.Boundary.UpdateComplaintsReportData(Month.of(month), Year.of(year));
		  		complaints = CinemaClient.ComplaintsReportData;
	  		}

	  		// get number of days in the chosen month
	  		YearMonth yearMonthObject = YearMonth.of(year, month);
	  		int daysInMonth = yearMonthObject.lengthOfMonth();

	  		int[] complaintsInADay = new int[daysInMonth];	// array is initialized by default to zeros
	  		for (Complaint complaint:complaints) {
	  			complaintsInADay[complaint.getCreationDate().getDayOfMonth()-1]++;
	  		}

	  		// fill chart
	  		fillChart(complaintsInADay, series);

	  		// find the day of max complaints
	  		int max_complaints_day = getIndexOfLargest(complaintsInADay)+1;
	  		bestSalesText.setText("Highest complaints amount of the month was made on " + Integer.toString(max_complaints_day) + "/" + Integer.toString(month));

	  		// find the day of min sales
	  		int min_complaints_day = getIndexOfSmallest(complaintsInADay)+1;
	  		worstSalesText.setText("Lowest complaints amount of the month was made on " + Integer.toString(min_complaints_day) + "/" + Integer.toString(month));

	  		// find the total complaints
	  		int total = complaints.size();
	  		totalText.setText("Total complaints in this month: " + Integer.toString(total));

	  		profitsText.setText("");

	  		// find the refunded amount
	  		refundedText.setText("");
  		}
	}

}
