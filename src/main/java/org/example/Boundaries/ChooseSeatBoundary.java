package org.example.Boundaries;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.App;
import org.example.OCSF.CinemaClient;
import org.example.entities.Seat;
import org.example.entities.Show;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.LinkedList;
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
    @FXML private TableColumn<Seat, Integer> HallNumber;
    @FXML private TableColumn<Seat, Integer> SeatNumber;
    @FXML private ImageView Background;



    @FXML
    void clickRefreshBtn(ActionEvent event) {
        synchronized(CinemaClient.SeatDataLock) {
            org.example.Boundaries.Boundary.UpdateSeatsData();
            // set items in table
            ObservableList<Seat> DataList = FXCollections.observableArrayList(CinemaClient.SeatData);
            SeatTable.setItems(DataList);
        }
    }
    public Show load_shows(int show_id){
        List<Show> shows;
        Show show = null;
        synchronized(CinemaClient.ShowsDataLock) {

            org.example.Boundaries.Boundary.UpdateShowsData();
            // set items in table
             shows = CinemaClient.ShowsData;
            // System.out.println(DataList.get(0).getMovie().getName_en());
        }
        for(int i=0;i<shows.size();i++){
            if(shows.get(i).getID()==show_id){
                //System.out.println(shows.get(i));
                show=shows.get(i);
            }
        }
        System.out.println(show);
        return show ;

    }


    @FXML
    void clickChooseSeatBtn(ActionEvent event) throws IOException {
        Show show =load_shows((int)App.getParams().get(0));
        List<Object> p = new LinkedList<>();
        p.add(show);
        int id = SeatTable.getSelectionModel().getSelectedItem().getNumber();
        p.add(id);
        App.setParams(p);

        App.setRoot("Payment", null, stage);
    }

    @FXML
    void clickBackBtn(ActionEvent event) throws IOException {
        List<Object> p = new LinkedList<>();
        App.setParams(p);

        App.setRoot("BuyTicket",null, stage);

    }



    @Override
    public void initialize(URL url, ResourceBundle rb) {




        // set-up the columns in the table
        HallNumber.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Seat, Integer>, ObservableValue<Integer>>() {
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Seat, Integer> seat) {
                return (new SimpleIntegerProperty(seat.getValue().getHall().getNumber()).asObject());
            }
        });
        SeatNumber.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Seat, Integer>, ObservableValue<Integer>>() {
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Seat, Integer> seat) {
                return (new SimpleIntegerProperty(seat.getValue().getNumber()).asObject());
            }
        });
//
        synchronized(CinemaClient.SeatDataLock) {
            org.example.Boundaries.Boundary.UpdateSeatsData();
            // set items in table
            ObservableList<Seat>DataList = FXCollections.observableArrayList(CinemaClient.SeatData);
            SeatTable.setItems(DataList);
             System.out.println(DataList.get(0).getHall().getCinema().getBranch_name());
        }
        System.out.println("initializing done");
    }
}
