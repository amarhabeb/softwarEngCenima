package org.example.Boundaries;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.Serializable;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;


import org.example.OCSF.CinemaClient;
import org.example.OCSF.CinemaClientCLI;
import org.example.entities.Movie;
import org.example.entities.Show;

import javafx.fxml.Initializable;

@SuppressWarnings("serial")
public class DeleteMovieBoundary extends ContentManagerDisplayBoundary implements Initializable, Serializable {
	
	@FXML
    private TableView<Movie> MoviesTable;

	@FXML
    private TableColumn<Movie, String> name;

    @FXML
    private TableColumn<Movie, String> cast;

    @FXML
    private TableColumn<Movie, String> director;

    @FXML
    private TableColumn<Movie, String> launch_date;

    @FXML
    private TableColumn<Movie, String> online;

    @FXML
    private Button refreshBtn2;

    @FXML
    private Button GoBackToMainBtn;

    @FXML
    private Button DeleteMovieBtn;

    @FXML
    private Text selectedMovieEngText;

    @FXML
    private Text selectedMovieHebText;
        
    Movie selected_movie = null;
    List<Integer> shows_of_selected_movie = new LinkedList<Integer>();
    
    
    public static Boolean MovieDeleted = true;	// holds if the show is deleted yet
    // delete show in DataBase and brings the Shows from the DataBase and updates 
 	// the ShowsData local list
    synchronized void DeleteMovie(int movie_id) {
    	MovieDeleted = false;	// show isn't deleted yet
		// create message and send it to the server
    	LinkedList<Object> message = new LinkedList<Object>();
		message.add("DeleteMovie");
		message.add(movie_id);
		synchronized(CinemaClient.MoviesDataLock)
		{	
			CinemaClientCLI.sendMessage(message);
							
			// wait for Data to be changed
			while(!MovieDeleted) {
				try {
					CinemaClient.MoviesDataLock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
			// update ShowData
			UpdateMoviesData();
		}	
	}
    
    public static Boolean ShowDeleted = true;	// holds if the show is deleted yet
    // delete show in DataBase and brings the Shows from the DataBase and updates 
 	// the ShowsData local list
    synchronized void DeleteShow(int show_id) {
		// create message and send it to the server
    	LinkedList<Object> message = new LinkedList<Object>();
		message.add("DeleteShow");
		message.add(show_id);
		synchronized(CinemaClient.ShowsDataLock)
		{	
			ShowDeleted = false;	// show isn't deleted yet
			CinemaClientCLI.sendMessage(message);
							
			// wait for Data to be changed
			while(!ShowDeleted) {
				try {
					CinemaClient.ShowsDataLock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// update ShowData
			org.example.Boundaries.Boundary.UpdateShowsData();
		}	
	}


    @FXML
    void clickDeleteMovieBtn(ActionEvent event) {
    	int movie_id = selected_movie.getID();
    	
    	synchronized(CinemaClient.MoviesDataLock) {
	    	try {
	    		// delete movie entity
	        	DeleteMovie(movie_id);
	    		MessageBoundaryEmployee.displayInfo("Movie successfully deleted.");
	    	}catch(Exception e) {	// server threw exception while trying to delete show
	    		MessageBoundaryEmployee.displayError("An error occured. Movie couldn't be deleted.");
	    	}
			
	    	// set items in table
	    	ObservableList<Movie> DataList = FXCollections.observableArrayList(CinemaClient.MoviesData);
	    	MoviesTable.setItems(DataList);
    	}
    	
    	synchronized(CinemaClient.ShowsDataLock) {
    		UpdateShowsData();
    		for(Show show:CinemaClient.ShowsData) {
    			if(show.getMovie().getID()==movie_id) {
    				shows_of_selected_movie.add(show.getID());
    			}
    		}
    	
    		try {
    			for(Integer show_id:shows_of_selected_movie) {
    				// delete show entity
    				DeleteShow(show_id);
    				System.out.println("deleted");
    			}
    			MessageBoundaryEmployee.displayInfo("Shows of selected movie successfully deleted.");
	    	}catch(Exception e) {	// server threw exception while trying to delete show
	    		MessageBoundaryEmployee.displayError("An error occured. Shows of selected movie couldn't be deleted.");
	    	}
	    	
    	}
    }

    @FXML
    void clickRefreshBtn2(ActionEvent event) {
    	synchronized(CinemaClient.ShowsDataLock) {
	    	UpdateMoviesData();
	    	DeleteMovieBtn.setDisable(true);
	    	selected_movie = null;
	    	selectedMovieEngText.setText("*no show selected*");
	    	selectedMovieHebText.setText("");
			// set items in table
			ObservableList<Movie> DataList = FXCollections.observableArrayList(CinemaClient.MoviesData);
			MoviesTable.setItems(DataList);
			System.out.println("refreshed");
    	}
    }


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// set-up the columns in the table
		name.setCellValueFactory(new Callback<CellDataFeatures<Movie, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<Movie, String> movie) {
				return (new SimpleStringProperty(movie.getValue().getName_en()+"/"+movie.getValue().getName_heb()));
			}
		});
		director.setCellValueFactory(new Callback<CellDataFeatures<Movie, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<Movie, String> movie) {
				return (new SimpleStringProperty(movie.getValue().getDirector()));
			}
		});
		cast.setCellValueFactory(new Callback<CellDataFeatures<Movie, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<Movie, String> movie) {
				return (new SimpleStringProperty(String.join(",", movie.getValue().getCast())));
			}
		});
		launch_date.setCellValueFactory(new Callback<CellDataFeatures<Movie, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<Movie, String> movie) {
				return (new SimpleStringProperty(movie.getValue().getLanuch_date().toString()));
			}
		});
		online.setCellValueFactory(new Callback<CellDataFeatures<Movie, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<Movie, String> movie) {
				String output_str = "not available";
				if(movie.getValue().isAvailableOnline()) {
					output_str = "available";
				}
				return (new SimpleStringProperty(output_str));
			}
		});
		
		
		// disable button
		DeleteMovieBtn.setDisable(true);
				
		// get selected show from table
		MoviesTable.setOnMouseClicked((event) -> {
			selected_movie = MoviesTable.getSelectionModel().getSelectedItem();
			if (selected_movie != null) {
				selectedMovieEngText.setText(selected_movie.getName_en());
				selectedMovieHebText.setText(selected_movie.getName_heb());
				DeleteMovieBtn.setDisable(false);
			} else {
				selectedMovieEngText.setText("*no movie selected*");
				selectedMovieHebText.setText("");
			}
		});
				
		synchronized(CinemaClient.MoviesDataLock) {
			// update MovieData
			UpdateMoviesData();
			// set items in table
			ObservableList<Movie> DataList = FXCollections.observableArrayList(CinemaClient.MoviesData);
			MoviesTable.setItems(DataList);
		}
		
	}

}
