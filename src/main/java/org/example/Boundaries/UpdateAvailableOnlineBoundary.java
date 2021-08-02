package org.example.Boundaries;

import java.io.Serializable;
import java.net.URL;
import java.util.ResourceBundle;

import org.example.OCSF.CinemaClient;
import org.example.entities.Movie;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

@SuppressWarnings("serial")
public class UpdateAvailableOnlineBoundary extends ContentManagerDisplayBoundary implements Initializable, Serializable{
	
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
    private Button ApplyChangesBtn;

    @FXML
    private Text selectedMovieEngText;

    @FXML
    private Text selectedMovieHebText;

    @FXML
    private CheckBox onlineCB;

    @FXML
    void clickApplyChangesBtn(ActionEvent event) {
    	//*FILL CODE*//
    }
    
    Movie selected_movie = null;
    
    @FXML
    void clickRefreshBtn2(ActionEvent event) {
    	synchronized(CinemaClient.ShowsDataLock) {
	    	UpdateMoviesData();
	    	ApplyChangesBtn.setDisable(true);
	    	onlineCB.setDisable(true);
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
		
		onlineCB.setDisable(true);	// disabling checkbox
		ApplyChangesBtn.setDisable(true);	// disabling checkbox
		
		// get selected show from table
		MoviesTable.setOnMouseClicked((event) -> {
			selected_movie = MoviesTable.getSelectionModel().getSelectedItem();
			if (selected_movie != null) {
				selectedMovieEngText.setText(selected_movie.getName_en());
				selectedMovieHebText.setText(selected_movie.getName_heb());
				onlineCB.setDisable(false);
				onlineCB.setSelected(selected_movie.isAvailableOnline());
				ApplyChangesBtn.setDisable(true);	// disabling checkbox
			} else {
				selectedMovieEngText.setText("*no movie selected*");
				selectedMovieHebText.setText("");
			}
		});
		
		onlineCB.setOnAction((event) -> {
	    	if(onlineCB.isSelected() == selected_movie.isAvailableOnline()) {
	    		ApplyChangesBtn.setDisable(true);
	    	}else {
	    		ApplyChangesBtn.setDisable(false);
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
