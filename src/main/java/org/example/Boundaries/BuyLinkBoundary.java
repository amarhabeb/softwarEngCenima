package org.example.Boundaries;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import org.example.App;
import org.example.OCSF.CinemaClient;
import org.example.entities.Movie;
import org.example.entities.Show;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class BuyLinkBoundary extends Boundary implements Initializable, Serializable {

    @FXML // fx:id="refreshBtn"
    private Button refreshBtn; // Value injected by FXMLLoader

    @FXML // fx:id="UpdateShowsTimesBtn"
    private Button ChooseMovieBtn; // Value injected by FXMLLoader



    @FXML
    private Button BackBtn;
    @FXML
    Label label;


    @FXML private TableView<Movie> MoviesTable;
    @FXML private TableColumn<Movie, String> movie_name;
    @FXML private TableColumn<Movie, String> director;
    @FXML private TableColumn<Movie, String> summary;

    @FXML private ImageView Background;



    @FXML
    void clickRefreshBtn(ActionEvent event) {
        org.example.Boundaries.Boundary.UpdateMoviesData();
        // set items in table
        List<Movie> movies =CinemaClient.MoviesData;
        List <Movie> mov = new LinkedList<>();
        for(int i=0;i<movies.size();i++){
            if(movies.get(i).isAvailableOnline()==true){
                mov.add(movies.get(i));
            }
        }
    }


    @FXML
    void clickChooseMovieBtn(ActionEvent event) throws IOException {
        if(MoviesTable.getSelectionModel().getSelectedItem() == null ){
            label.setText("Please Choose Movie");
        }
        else {
            int movie_id = MoviesTable.getSelectionModel().getSelectedItem().getID();
            List<Object> l = new LinkedList<>();
            l.add(movie_id);
            App.setParams(l);
            App.setRoot("PaymentLink", params, stage);

        }
//        int show_id =  MoviesTable.getSelectionModel().getSelectedItem().getID();
//        //System.out.println(show_id);
//        List<Object> params = new LinkedList<Object>();
//        params.add(show_id);
//        params.add(MoviesTable.getSelectionModel().getSelectedItem().getMovie());
//
//        // Step 5
//        List<Object> l = App.getParams();
//        l.add(show_id);
//        App.setParams(l);
        // Step 6



        //App.setRoot("PaymentLink", params, stage);
    }


    @FXML
    void clickBackBtn(ActionEvent event) throws IOException {
        List<Object> l = new LinkedList<>();
        App.setParams(l);
        App.setRoot("CustomerMain",null, stage);

    }



    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // set-up the columns in the table
        movie_name.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Movie, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Movie, String> movie) {
                //System.out.print(new SimpleStringProperty(show.getValue().getMovie().getName_en()));
                return (new SimpleStringProperty(movie.getValue().getName_en()));
            }
        });
        director.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Movie, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Movie, String> movie) {
                return (new SimpleStringProperty(movie.getValue().getDirector()));
            }
        });
        summary.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Movie, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Movie, String> movie) {
                return (new SimpleStringProperty(movie.getValue().getSummary()));
            }
        });


        synchronized(CinemaClient.ShowsDataLock) {
            org.example.Boundaries.Boundary.UpdateMoviesData();
            // set items in table
            List<Movie> movies =CinemaClient.MoviesData;
            List <Movie> mov = new LinkedList<>();
            for(int i=0;i<movies.size();i++){
                if(movies.get(i).isAvailableOnline()==true){
                    mov.add(movies.get(i));
                }
            }
            ObservableList<Movie> DataList = FXCollections.observableArrayList(mov);
            MoviesTable.setItems(DataList);
            // System.out.println(DataList.get(0).getMovie().getName_en());
        }
        System.out.println("initializing done");
    }
}
