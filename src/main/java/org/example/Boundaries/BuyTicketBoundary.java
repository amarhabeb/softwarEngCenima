package org.example.Boundaries;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
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
        static  List<Show> shows = new LinkedList<>();

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
        @FXML private TableColumn<Show, Button> details;
        @FXML private ImageView Background;








        @FXML
        void clickRefreshBtn(ActionEvent event) {
            synchronized(CinemaClient.ShowsDataLock) {


                ObservableList<Show> DataList5 = FXCollections.observableArrayList(this.shows);
                ShowsTable.getSelectionModel().clearSelection();
                ShowsTable.setVisible(true);
                ShowsTable.setItems( DataList5);            }
        }


        @FXML
        void clickChooseShowBtn(ActionEvent event) throws IOException {
            if (ShowsTable.getSelectionModel().getSelectedItem() == null) {
                label.setText("Please Choose Show");

            } else {
                label.setText("");

                Show show= ShowsTable.getSelectionModel().getSelectedItem();
                System.out.println("Selected Id  is "+ShowsTable.getSelectionModel().getSelectedItem().getID());
                System.out.println("Selected index is "+ShowsTable.getSelectionModel().getSelectedIndex());
                //System.out.println(show_id);
                List<Object> params = new LinkedList<Object>();
                params.add(show);
                //params.add(ShowsTable.getSelectionModel().getSelectedItem().getMovie());

                // Step 5
                List<Object> l = App.getParams();
                l.add(show);
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
            if(App.getShows().isEmpty()==false && (int)App.getParams().get(0)==-10){
                System.out.println("Enter -10");
                ObservableList<Show>ss= FXCollections.observableArrayList(App.getShows());
                ShowsTable.setItems(ss);
                ShowsTable.setVisible(true);
                List<Object> l = new LinkedList<>();
                List<Show> l1 = new LinkedList<>();

                App.setShows(l1);
                App.setParams(l);

            }
            else {


                synchronized (CinemaClient.CinemasDataLock) {
                    org.example.Boundaries.Boundary.UpdateCinemasData();
                    // set items in table
                    DataList1 = FXCollections.observableArrayList(CinemaClient.CinemasData);

                }
                System.out.println("cinemas size is  " + DataList1.size());


                for (int i = 0; i < DataList1.size(); i++) {
                    cinemas.getItems().add(DataList1.get(i).getBranch_name());

                }
                // if the item of the list is changed
                cinemas.getSelectionModel().selectedItemProperty().addListener((InvalidationListener) ov -> {
                    ObservableList<Cinema> DataList;
                    ObservableList<Show> DataList2;
                    List<Show> DataList3 = new LinkedList<>();
                    synchronized (CinemaClient.CinemasDataLock) {
                        org.example.Boundaries.Boundary.UpdateCinemasData();
                        // set items in table
                        DataList = FXCollections.observableArrayList(CinemaClient.CinemasData);

                    }
                    Cinema c = null;
                    for (int i = 0; i < DataList.size(); i++) {
                        //System.out.println(cinemas.getSelectionModel().getSelectedItem().toString());
                        if (DataList.get(i).getBranch_name().equals(cinemas.getSelectionModel().getSelectedItem().toString())) {
                            //System.out.println("Enter");
                            c = DataList.get(i);
                        }
                    }
                    System.out.println(c.getBranch_name());
                    synchronized (CinemaClient.ShowsDataLock) {
                        org.example.Boundaries.Boundary.UpdateCinemaShowsData(c.getID());
                        // set items in table
                        DataList2 = FXCollections.observableArrayList(CinemaClient.ShowsData);

                        // System.out.println(DataList.get(0).getMovie().getName_en());
                    }
//                for(int i=0;i<DataList2.size();i++){
//                     //System.out.println(DataList2.get(i).getCinema().getID());
//                    if(DataList2.get(i).getCinema().getID()==c.getID()){
//                        DataList3.add(DataList2.get(i));
//
//
//                    }
//
//                }
                    this.shows = DataList2;
                    App.setShows(DataList2);
                    ObservableList<Show> DataList5 = FXCollections.observableArrayList(DataList2);
                    ShowsTable.setVisible(true);
                    ShowsTable.setItems(DataList5);


                    // set the text for the label to the selected item
                    System.out.print("changed");
                });
            }




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
            details.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Show, Button>, ObservableValue<Button>>() {
                public ObservableValue<Button> call(TableColumn.CellDataFeatures<Show, Button> movie) {
                    Button t = new Button();
                    t.setText("View Details");
                    t.setId(Integer.toString(movie.getValue().getID()));
                    t.setMinWidth(30);
                    t.setMinHeight(30);
                    Font font = Font.font("Courier New", FontWeight.BOLD, 10);
                    t.setFont(font);


                    t.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            List<Object> l = new LinkedList<>();
                            l.add(-10);
                            l.add(Integer.parseInt(t.getId()));
                            App.setParams(l);

                            try {
                                App.setRoot("movieB",null, stage);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    });
                    return (new SimpleObjectProperty(t));
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
        public static void autoResizeColumns( TableView<?> table )
        {
            //Set the right policy
            table.setColumnResizePolicy( TableView.UNCONSTRAINED_RESIZE_POLICY);
            table.getColumns().stream().forEach( (column) ->
            {
                //Minimal width = columnheader
                Text t = new Text( column.getText() );
                double max = t.getLayoutBounds().getWidth();
                for ( int i = 0; i < table.getItems().size(); i++ )
                {
                    //cell must not be empty
                    if ( column.getCellData( i ) != null )
                    {
                        t = new Text( column.getCellData( i ).toString() );
                        double calcwidth = t.getLayoutBounds().getWidth();
                        //remember new max-width
                        if ( calcwidth > max )
                        {
                            max = calcwidth;
                        }
                    }
                }
                //set the new max-widht with some extra space
                column.setPrefWidth( max + 10.0d );
            } );
        }
    }
