package org.example.Boundaries;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;


import org.example.OCSF.CinemaClient;
import org.example.OCSF.CinemaClientCLI;
import org.example.entities.Movie;
import org.example.entities.Show;

import javafx.fxml.Initializable;

@SuppressWarnings("serial")
public class AddMovieBoundary extends ContentManagerDisplayBoundary implements Initializable, Serializable{
	
	@FXML // fx:id="GoBackToMainBtn"
    private Button GoBackToMainBtn; // Value injected by FXMLLoader

    @FXML // fx:id="name_en_TextField"
    private TextField name_en_TextField; // Value injected by FXMLLoader

    @FXML // fx:id="AddMovieBtn"
    private Button AddMovieBtn; // Value injected by FXMLLoader

    @FXML // fx:id="name_heb_TextField"
    private TextField name_heb_TextField; // Value injected by FXMLLoader

    @FXML // fx:id="directorTextField"
    private TextField directorTextField; // Value injected by FXMLLoader

    @FXML // fx:id="cast_TextField"
    private TextField cast_TextField; // Value injected by FXMLLoader

    @FXML // fx:id="datePicker"
    private DatePicker datePicker; // Value injected by FXMLLoader

    @FXML // fx:id="summaryText"
    private TextArea summaryText; // Value injected by FXMLLoader

    @FXML // fx:id="available_online_CB"
    private CheckBox available_online_CB; // Value injected by FXMLLoader

    @FXML // fx:id="chooseAnImgBtn"
    private Button chooseAnImgBtn; // Value injected by FXMLLoader

    @FXML // fx:id="imageView"
    private ImageView imageView; // Value injected by FXMLLoader
    
    // for choosing image
    FileChooser fileChooser = new FileChooser();
    
    byte[] image_bytes;
    
    public static Boolean MovieAdded = true;	// holds if the movie is added yet
    // add movie in DataBase and brings the Movies from the DataBase and updates 
 	// the MoviesData local list
    synchronized void AddMovie(Movie movie) {
		MovieAdded = false;	// show isn't added yet
		// create message and send it to the server
    	LinkedList<Object> message = new LinkedList<Object>();
		message.add("AddMovie");
		message.add(movie);
		synchronized(CinemaClient.MoviesDataLock)
		{	
			CinemaClientCLI.sendMessage(message);
							
			// wait for Data to be changed
			while(!MovieAdded) {
				try {
					CinemaClient.MoviesDataLock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
		}
		// update MovieData
		UpdateMoviesData();
	}

    @FXML
    void clickAddMovieBtn(ActionEvent event) {
    	String name_en = name_en_TextField.getText();
    	String name_heb = name_heb_TextField.getText();
    	String director = directorTextField.getText();
    	String summary = summaryText.getText();
    	LocalDate launch_date = datePicker.getValue();
    	boolean availableOnline = available_online_CB.isSelected();
    	List<Show> shows = new LinkedList<Show>();
    	// Convert comma separated String to List
    	List<String> cast = Arrays.asList(cast_TextField.getText().split("\\s*,\\s*"));
    	
    	// create new chosen movie
    	Movie movie = new Movie(name_en, name_heb, director, cast, summary,
                launch_date,  "image_bytes", shows, availableOnline,null);
    	
    	try {
	  		// add new show object to database
	  		AddMovie(movie);
	  		MessageBoundaryEmployee.displayInfo("Movie successfully added.");
  		}catch(Exception e) {
  			MessageBoundaryEmployee.displayError("An error occured. Movie couldn't be added.");
  		}
		
		resetChoices();
    }
    
    void resetChoices() {
  		// reset choices
  		name_en_TextField.setText(null);
  		name_heb_TextField.setText(null);
  		directorTextField.setText(null);
  		cast_TextField.setText(null);
  		summaryText.setText(null);
    	datePicker.setValue(null);	
    	available_online_CB.setSelected(false);
    	imageView.setImage(null);
    	
    	AddMovieBtn.setDisable(true);
    }

    @FXML
    void clickChooseAnImgBtn(ActionEvent event) throws IOException {
    	File selectedFile = fileChooser.showOpenDialog(stage);
    	 if (selectedFile != null) {
    		 image_bytes = new byte[(int) selectedFile.length()];
    		 Image image = new Image(selectedFile.toURI().toString());
    		 imageView.setImage(image); 	// display selected image
    	 }
    	 CheckIfFilled();
    }
    
    
    void CheckIfFilled() {
  		// if all choiceboxes were filled
  		if(!name_heb_TextField.getText().trim().isEmpty() && datePicker.getValue()!= null &&
  				!directorTextField.getText().trim().isEmpty() && !cast_TextField.getText().trim().isEmpty() &&
  				!summaryText.getText().trim().isEmpty() &&
  				imageView.getImage()!=null && !name_en_TextField.getText().trim().isEmpty()) {
  			AddMovieBtn.setDisable(false);
  		}
  		else {
  			AddMovieBtn.setDisable(true);
  		}
  	}
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		// disable button
  		CheckIfFilled();
  		
  		image_bytes = null;
  		
  		fileChooser.setTitle("Choose Image");
  		fileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
 
  		
		name_en_TextField.textProperty().addListener((observable, oldValue, newValue) -> {
    	    CheckIfFilled();
    	});
		
		name_heb_TextField.textProperty().addListener((observable, oldValue, newValue) -> {
    	    CheckIfFilled();
    	});
		
		directorTextField.textProperty().addListener((observable, oldValue, newValue) -> {
    	    CheckIfFilled();
    	});
		
		cast_TextField.textProperty().addListener((observable, oldValue, newValue) -> {
    	    CheckIfFilled();
    	});
		
		summaryText.textProperty().addListener((observable, oldValue, newValue) -> {
			CheckIfFilled();
		});
		
		datePicker.setOnAction((event) -> {
    	    CheckIfFilled();
    	});
		
		
		
	}

}
