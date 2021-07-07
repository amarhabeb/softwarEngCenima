package org.example.Boundaries;

import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.example.OCSF.CinemaClient;
import org.example.entities.Cinema;
import org.example.entities.Link;
import org.example.entities.Ticket;
import org.example.entities.Package;

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
    private LineChart<Integer, Integer> reports_chart;
    
    @FXML
    private Text bestSalesText;
    @FXML
    private Text worstSalesText;
    @FXML
    private Text totalText;
    @FXML
    private Text profitsText;
    
    // will hold the tickets of the chosen report
    List<Ticket> tickets = null;
    // will hold the tickets of the chosen report
    List<Package> packages = null;
    // will hold the tickets of the chosen report
    List<Link> links = null;
    
    @FXML
    void clickGoBackToMainBtn(ActionEvent event) {

    }
    
    // given a data array, tis method will fill the chart with this data
    void fillChart(int[] data, XYChart.Series<Integer, Integer> series){
    	int idx = 1;
    	for(int val:data) {
    		series.getData().add(new XYChart.Data<>(idx, val));	// add (x,y) value to series
    		idx++;
    	}
    	// fill chart with series
    	reports_chart.getData().add(series);
    }
    
    public int getIndexOfLargest( int[] array )
    {
      if ( array == null || array.length == 0 ) return -1; // null or empty

      int largest = 0;
      for ( int i = 1; i < array.length; i++ )
      {
          if ( array[i] > array[largest] ) largest = i;
      }
      return largest; // position of the first largest found
    }
    
    public int getIndexOfSmallest( int[] array )
    {
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
  	  		XYChart.Series<Integer, Integer> series = new XYChart.Series<>();
  	  	    series.setName("Tickets Sales, " + cinema.toString() + ", "+ month.toString() + ", " + year.toString());
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
	  			profits+=ticket.getPayment().getAmount();	// calculate how much money was paid for tickets
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
	  		totalText.setText("Total selled tickets in this month: " + Integer.toString(total));
	  		
	  		// find the profits
	  		profitsText.setText("Profits: " + Double.toString(profits) + "₪");
  		}
  		
  		
  		// if the report is of Packages and Online Shows Sales type
  		if(report_type == "Packages and Online Shows Sales") {
  			// data represented in chart
  	  		XYChart.Series<Integer, Integer> series_p = new XYChart.Series<>();
  	  	    series_p.setName("Packages Sales, "+ month.toString() + ", " + year.toString());
  	  	    XYChart.Series<Integer, Integer> series_l = new XYChart.Series<>();
	  	    series_l.setName("Online Shows, "+ month.toString() + ", " + year.toString());
	  		// get needed packages
	  		synchronized(CinemaClient.PackagesReportDataLock) {
		  		UpdatePackagesReportData(Month.of(month), Year.of(year));
		  		packages = CinemaClient.PackagesReportData;
	  		}
	  		// get needed packages
	  		synchronized(CinemaClient.LinksReportDataLock) {
		  		UpdateLinksReportData(Month.of(month), Year.of(year));
		  		links = CinemaClient.LinksReportData;
	  		}
	  		
	  		// get number of days in the chosen month
	  		YearMonth yearMonthObject = YearMonth.of(year, month);
	  		int daysInMonth = yearMonthObject.lengthOfMonth();
	  	
	  		int[] salesInADay_packages = new int[daysInMonth];	// array is initialized by default to zeros
	  		int[] salesInADay_links = new int[daysInMonth];	// array is initialized by default to zeros
	  		double profits = 0;
	  		for (Package p:packages) {
	  			salesInADay_packages[p.getOrderDate().getDayOfMonth()-1]++;
	  			profits+=p.getPayment().getAmount();	// calculate how much money was paid for tickets
	  		}
	  		for (Link link:links) {
	  			salesInADay_links[link.getOrderDate().getDayOfMonth()-1]++;
	  			profits+=link.getPayment().getAmount();	// calculate how much money was paid for tickets
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
	  		totalText.setText("Total selled packages and online shows in this month: " + Integer.toString(total));
	  		
	  		// find the profits
	  		profitsText.setText("Profits: " + Double.toString(profits) + "₪");
  		}
	}

}
