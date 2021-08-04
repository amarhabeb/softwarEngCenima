package org.example.Boundaries;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.App;
import org.example.OCSF.CinemaClient;
import org.example.entities.Cinema;
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
        private ChoiceBox<String> cinemas;



        @FXML
        private Button BackBtn;
        @FXML Label label;




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
            if (ShowsTable.getSelectionModel().getSelectedItem() == null) {
                label.setText("Please Choose Show");

            } else {
                label.setText("");

                int show_id = ShowsTable.getSelectionModel().getSelectedItem().getID();
                System.out.println("Selected Id  is "+ShowsTable.getSelectionModel().getSelectedItem().getID());
                System.out.println("Selected index is "+ShowsTable.getSelectionModel().getSelectedIndex());
                //System.out.println(show_id);
                List<Object> params = new LinkedList<Object>();
                params.add(show_id);
                params.add(ShowsTable.getSelectionModel().getSelectedItem().getMovie());

                // Step 5
                List<Object> l = App.getParams();
                l.add(show_id);
                App.setParams(l);
                // Step 6


                App.setRoot("ChooseSeat", params, stage);
            }
        }

            @FXML
            void clickBackBtn(ActionEvent event) throws IOException {
                List<Object> l = new LinkedList<>();
                App.setParams(l);
                App.setRoot("CustomerMain",null, stage);


            }









        @Override
        public void initialize(URL url, ResourceBundle rb) {
            ObservableList<Cinema> DataList1;

            synchronized(CinemaClient.CinemasDataLock) {
                org.example.Boundaries.Boundary.UpdateCinemasData();
                // set items in table
                 DataList1 = FXCollections.observableArrayList(CinemaClient.CinemasData);

            }




            for(int i =0;i<DataList1.size();i++){
                cinemas.getItems().add(DataList1.get(i).getBranch_name());

            }
            // if the item of the list is changed
            cinemas.getSelectionModel().selectedItemProperty().addListener((InvalidationListener) ov -> {
                ObservableList<Cinema> DataList;
                ObservableList<Show> DataList2;
               List<Show> DataList3 = new LinkedList<>();
                synchronized(CinemaClient.CinemasDataLock) {
                    org.example.Boundaries.Boundary.UpdateCinemasData();
                    // set items in table
                     DataList = FXCollections.observableArrayList(CinemaClient.CinemasData);

                }
                Cinema c = null;
                for(int i=0;i<DataList.size();i++){
                    //System.out.println(cinemas.getSelectionModel().getSelectedItem().toString());
                    if(DataList.get(i).getBranch_name().equals(cinemas.getSelectionModel().getSelectedItem().toString())){
                         //System.out.println("Enter");
                        c=DataList.get(i);
                    }
                }
                synchronized(CinemaClient.ShowsDataLock) {
                    org.example.Boundaries.Boundary.UpdateShowsData();
                    // set items in table
                   DataList2 = FXCollections.observableArrayList(CinemaClient.ShowsData);

                    // System.out.println(DataList.get(0).getMovie().getName_en());
                }
                for(int i=0;i<DataList2.size();i++){
                     //System.out.println(DataList2.get(i).getCinema().getID());
                    if(DataList2.get(i).getCinema().getID()==c.getID()){
                        DataList3.add(DataList2.get(i));


                    }

                }
                ObservableList<Show> DataList5 = FXCollections.observableArrayList(DataList3);
                ShowsTable.setVisible(true);
               ShowsTable.setItems( DataList5);



                // set the text for the label to the selected item
                System.out.print("changed");
            });




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

//            synchronized(CinemaClient.ShowsDataLock) {
//                org.example.Boundaries.Boundary.UpdateShowsData();
//                // set items in table
//                ObservableList<Show> DataList = FXCollections.observableArrayList(CinemaClient.ShowsData);
//                ShowsTable.setItems(DataList);
//               // System.out.println(DataList.get(0).getMovie().getName_en());
//            }
            System.out.println("initializing done");
        }
    }
