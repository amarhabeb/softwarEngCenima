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
import org.example.entities.Seat;
import org.example.entities.Show;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ChooseSeatBoundary extends BuyTicketBoundary implements Initializable, Serializable {


    @FXML // fx:id="refreshBtn"
    private Button refreshBtn; // Value injected by FXMLLoader

    @FXML // fx:id="UpdateShowsTimesBtn"
    private Button ChooseSeatBtn; // Value injected by FXMLLoader



    @FXML
    private Button BackBtn;


    @FXML private TableView<Seat> SeatTable;
    @FXML private TableColumn<Seat, String> HallNumber;
    @FXML private TableColumn<Seat, String> SeatNumber;
    @FXML private ImageView Background;



    @FXML
    void clickRefreshBtn(ActionEvent event) {
//        synchronized(CinemaClient.ShowsDataLock) {
//            org.example.Boundaries.Boundary.UpdateShowsData();
//            // set items in table
//            ObservableList<Show> DataList = FXCollections.observableArrayList(CinemaClient.ShowsData);
//            ShowsTable.setItems(DataList);
//        }
    }
//    public Show load_shows(int show_id){
//        synchronized(CinemaClient.ShowsDataLock) {
//            org.example.Boundaries.Boundary.UpdateShowsData();
//            // set items in table
//            shows = CinemaClient.ShowsData;
//            // System.out.println(DataList.get(0).getMovie().getName_en());
//        }
//        for(int i=0;i<shows.size();i++){
//            if(shows.get(i).getID()==show_id){
//                show=shows.get(i);
//            }
//        }
//        System.out.println(show);
//        return show ;
//
//    }


    @FXML
    void clickChooseSeatBtn(ActionEvent event) throws IOException {
//         int show_id =ShowsTable.getSelectionModel().getSelectedItem().getID();
//        List<Object> l = new LinkedList<>();
//        l.add(show_id);
        App.setRoot("CustomerMain", null, stage);
    }

    @FXML
    void clickBackBtn(ActionEvent event) throws IOException {
        App.setRoot("CustomerMain",null, stage);

    }



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        params=getParams();
//        load_shows(show_id);
        System.out.println(params.size());
        for(int i=0;i<params.size();i++){
            System.out.println(params.get(i));
        }

       // int show_id =(int) l.get(0);
        List<Show> shows;
        Show show;
        //System.out.print(param.size());
//        // set-up the columns in the table
//        movie_name.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Show, String>, ObservableValue<String>>() {
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<Show, String> show) {
//                //System.out.print(new SimpleStringProperty(show.getValue().getMovie().getName_en()));
//                return (new SimpleStringProperty(show.getValue().getMovie().getName_en()));
//            }
//        });
//        date.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Show, String>, ObservableValue<String>>() {
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<Show, String> show) {
//                return (new SimpleStringProperty(show.getValue().getDate().toString()));
//            }
//        });
//        time.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Show, String>, ObservableValue<String>>() {
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<Show, String> show) {
//                return (new SimpleStringProperty(show.getValue().getTime().toString()));
//            }
//        });
//        hall_number.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Show, Integer>, ObservableValue<Integer>>() {
//            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Show, Integer> show) {
//                return (new SimpleIntegerProperty(show.getValue().getHall().getNumber()).asObject());
//            }
//        });
//        price.setCellValueFactory(new PropertyValueFactory<Show, Double>("price"));
//        cinema.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Show, String>, ObservableValue<String>>() {
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<Show, String> show) {
//                return (new SimpleStringProperty(show.getValue().getHall().getCinema().getBranch_name()));
//            }
//        });
//
//        synchronized(CinemaClient.ShowsDataLock) {
//            org.example.Boundaries.Boundary.UpdateShowsData();
//            // set items in table
//            ObservableList<Show> DataList = FXCollections.observableArrayList(CinemaClient.ShowsData);
//            ShowsTable.setItems(DataList);
//            // System.out.println(DataList.get(0).getMovie().getName_en());
//        }
        System.out.println("initializing done");
    }
}
