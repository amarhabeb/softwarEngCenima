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

    public class BuyTicketBoundary extends Boundary implements Initializable, Serializable {

        @FXML // fx:id="refreshBtn"
        private Button refreshBtn; // Value injected by FXMLLoader

        @FXML // fx:id="UpdateShowsTimesBtn"
        private Button ChooseShowBtn; // Value injected by FXMLLoader



        @FXML
        private Button BackBtn;


        @FXML private TableView<Show> ShowsTable;
        @FXML private TableColumn<Show, String> movie_name;
        @FXML private TableColumn<Show, String> date;
        @FXML private TableColumn<Show, String> time;
        @FXML private TableColumn<Show, Integer> hall_number;
        @FXML private TableColumn<Show, Double> price;
        @FXML private TableColumn<Show, String> cinema;
        @FXML private ImageView Background;



        @FXML
        void clickRefreshBtn(ActionEvent event) {
            synchronized(CinemaClient.ShowsDataLock) {
                org.example.Boundaries.Boundary.UpdateShowsData();
                // set items in table
                ObservableList<Show> DataList = FXCollections.observableArrayList(CinemaClient.ShowsData);
                ShowsTable.setItems(DataList);
            }
        }

        @FXML
        void clickChooseShowBtn(ActionEvent event) throws IOException {
             int show_id =  ShowsTable.getSelectionModel().getSelectedItem().getID();
             //System.out.println(show_id);
            List<Object> params = new LinkedList<Object>();
            params.add(show_id);
            params.add(ShowsTable.getSelectionModel().getSelectedItem().getMovie());


            App.setRoot("ChooseSeat", params);
        }

        @FXML
        void clickBackBtn(ActionEvent event) throws IOException {
            App.setRoot("CustomerMain",null);

        }



        @Override
        public void initialize(URL url, ResourceBundle rb) {

            // set-up the columns in the table
            movie_name.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Show, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Show, String> show) {
                    //System.out.print(new SimpleStringProperty(show.getValue().getMovie().getName_en()));
                    return (new SimpleStringProperty(show.getValue().getMovie().getName_en()));
                }
            });
            date.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Show, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Show, String> show) {
                    return (new SimpleStringProperty(show.getValue().getDate().toString()));
                }
            });
            time.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Show, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Show, String> show) {
                    return (new SimpleStringProperty(show.getValue().getTime().toString()));
                }
            });
            hall_number.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Show, Integer>, ObservableValue<Integer>>() {
                public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Show, Integer> show) {
                    return (new SimpleIntegerProperty(show.getValue().getHall().getNumber()).asObject());
                }
            });
            price.setCellValueFactory(new PropertyValueFactory<Show, Double>("price"));
            cinema.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Show, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Show, String> show) {
                    return (new SimpleStringProperty(show.getValue().getHall().getCinema().getBranch_name()));
                }
            });

            synchronized(CinemaClient.ShowsDataLock) {
                org.example.Boundaries.Boundary.UpdateShowsData();
                // set items in table
                ObservableList<Show> DataList = FXCollections.observableArrayList(CinemaClient.ShowsData);
                ShowsTable.setItems(DataList);
               // System.out.println(DataList.get(0).getMovie().getName_en());
            }
            System.out.println("initializing done");
        }
    }
