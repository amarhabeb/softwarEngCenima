package org.example.Boundaries;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.App;
import org.example.Controllers.RegulationsController;
import org.example.OCSF.CinemaClient;
import org.example.OCSF.CinemaServer;
import org.example.entities.Hall;
import org.example.entities.Regulations;
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
    int seat_number=-1;

    @FXML
    private Button refreshBtn; // Value injected by FXMLLoader

    @FXML // fx:id="UpdateShowsTimesBtn"
    private Button ChooseSeatBtn; // Value injected by FXMLLoader


    @FXML
    private Button BackBtn;
    @FXML
    private AnchorPane pane;


    @FXML
    private ImageView Background;


    @FXML
    void clickRefreshBtn(ActionEvent event) {
        List<Regulations> r = null;
        synchronized (CinemaClient.RegulationsDataLock) {
            org.example.Boundaries.Boundary.UpdateRegulationsData();
            // set items in table
            r = CinemaClient.RegulationsData;
//            SeatTable.setItems(DataList);
            //System.out.println(DataList.get(0).getHall().getCinema().getBranch_name());
        }
        Regulations re = r.get(0);
        if (re.getStatus() == false) {
            System.out.print("Regulations is false");


//        Seat1 s = new Seat1(1,false);
//s.getSelected();

            List<Seat> Data = null;
            List<Seat1> Data1 = new LinkedList<>();
            int x = 0;
            int z = (int) App.getParams().get(0);
            List<Show> d = null;
            synchronized (CinemaClient.ShowsDataLock) {
                org.example.Boundaries.Boundary.UpdateShowsData();
                // set items in table
                d = CinemaClient.ShowsData;
//            SeatTable.setItems(DataList);
                //System.out.println(DataList.get(0).getHall().getCinema().getBranch_name());
            }
            System.out.println("Show is "+z);

            Hall h = null;
            for (int i = 0; i < d.size(); i++) {
                if (d.get(i).getID() == z) {

                    h = d.get(i).getHall();

                    //System.out.print(h.getID());

                }

            }
            System.out.println(" Hall is " + h.getNumber());
            synchronized (CinemaClient.SeatDataLock) {
                org.example.Boundaries.Boundary.UpdateSeatsData();
                // set items in table
                Data = CinemaClient.SeatData;
//            SeatTable.setItems(DataList);
                //System.out.println(DataList.get(0).getHall().getCinema().getBranch_name());
            }
            for (int i = 0; i < Data.size(); i++) {

                if (Data.get(i).getHall().getNumber() == h.getNumber()) {

                    Seat1 seat = new Seat1(Data.get(i).getNumber(), Data.get(i).isAvailable(),Data.get(i).getID());
                    Data1.add(seat);

                }


                //pane.getChildren().add(seat);


            }

            int i = 0;
            int j = 1;
            int count = 120;
            int y = 120;


            while (i < h.getMaxSeats() - 1) {
                Seat1 seat = Data1.get(i);
                Seat s = Data.get(i);
                while (s.getLine() == j) {
                    seat.setLayoutX(count);
                    count += seat.width() + 10;
                    seat.setLayoutY(y);
                    pane.getChildren().add(seat);
                    i += 1;
                    s = Data.get(i);
                    if (i < Data1.size()) {
                        seat = Data1.get(i);

                    } else {
                        break;
                    }
                }
                j += 1;
                count = 120;
                y += 30;


            }

            System.out.println("initializing done");
        }
        else {
            System.out.println("initializing done regulation is on ");
            List<Seat> Data = null;
            List<Seat1> Data1 = new LinkedList<>();
            int x = 0;
            int z = (int) App.getParams().get(0);
            List<Show> d = null;
            synchronized (CinemaClient.ShowsDataLock) {
                org.example.Boundaries.Boundary.UpdateShowsData();
                // set items in table
                d = CinemaClient.ShowsData;
//            SeatTable.setItems(DataList);
                //System.out.println(DataList.get(0).getHall().getCinema().getBranch_name());
            }
            Hall h = null;
            for (int i = 0; i < d.size(); i++) {
                if (d.get(i).getID() == z) {
                    h = d.get(i).getHall();
                    //System.out.print(h.getID());

                }

            }
            System.out.println(" Hall is " + h.getID());

        }
    }

    public Show load_shows(int show_id) {
        List<Show> shows;
        Show show = null;
        synchronized (CinemaClient.ShowsDataLock) {

            org.example.Boundaries.Boundary.UpdateShowsData();
            // set items in table
            shows = CinemaClient.ShowsData;
            // System.out.println(DataList.get(0).getMovie().getName_en());
        }
        for (int i = 0; i < shows.size(); i++) {
            if (shows.get(i).getID() == show_id) {
                //System.out.println(shows.get(i));
                show = shows.get(i);
            }
        }
        System.out.println(show);
        return show;

    }


    @FXML
    void clickChooseSeatBtn(ActionEvent event) throws IOException {
//        Show show =load_shows((int)App.getParams().get(0));

        Show sh = (Show)App.getParams().get(0);

            List<Object> p = new LinkedList<>();
//        p.add(show);
//        int id = SeatTable.getSelectionModel().getSelectedItem().getNumber();
//        p.add(id);
//        App.setParams(p);
            Seat1 s = new Seat1(1, false,1);
            s.getSelected();
            p.add(s.getSelected().size());
            for (int i = 0; i < s.getSelected().size(); i++) {
                p.add(s.getSelected().get(i));
            }
            p.add(sh);
            App.setParams(p);



        App.setRoot("Payment", null, stage);
        }


    @FXML
    void clickBackBtn(ActionEvent event) throws IOException {
        List<Object> p = new LinkedList<>();
        App.setParams(p);

        App.setRoot("CustomerMain", null, stage);

    }
    static class Seat1 extends Group {


        Color freeColor = Color.rgb(30, 250, 40);
        Color reservedColor = Color.rgb(170, 40, 40);
        Color nullColor = Color.rgb(255, 165, 0);


        BooleanProperty iamReserved = new SimpleBooleanProperty(false);
        int myNo;
        Boolean res;
        int id;
         public static List<Integer> selected = new LinkedList<>() ;

        public Seat1(int no, Boolean res,int id) {
            myNo = no;
            this.res=res;
            this.id=id;
            Circle pillow = new Circle(12);
            if(res==true){
                pillow.setFill(reservedColor);
            }
            else {
                pillow.setFill(freeColor);


            }
            pillow.setStrokeWidth(1);

            pillow.setStroke(Color.rgb(30, 40, 40));
            getChildren().add(pillow);

            Text lable = new Text("" + no);
            lable.setFont(lable.getFont().font(7));
            lable.setTextAlignment(TextAlignment.CENTER);
            lable.setTextOrigin(VPos.CENTER);
            lable.setLayoutX(-lable.getLayoutBounds().getWidth() / 2);
            getChildren().add(lable);

            iamReserved.addListener((e, o, n) -> {
                pillow.setFill(n ? nullColor : freeColor);

            });
            setOnMouseClicked(m -> {
                if(res==false) {
                    iamReserved.set(!iamReserved.get());
                    if(iamReserved.get()==true){
                        selected.add(no);
                        synchronized (CinemaClient.SeatDataLock) {
                            org.example.Boundaries.Boundary.BookSeat(this.id);
                            // set items in table
                            //r = CinemaClient.RegulationsData;
//            SeatTable.setItems(DataList);
                            //System.out.println(DataList.get(0).getHall().getCinema().getBranch_name());
                        }



                    }
                    else {

                        selected.remove(selected.indexOf(no));
                        synchronized (CinemaClient.SeatDataLock) {
                            org.example.Boundaries.Boundary.UnBookSeat(this.id);
                            // set items in table
                            //r = CinemaClient.RegulationsData;
//            SeatTable.setItems(DataList);
                            //System.out.println(DataList.get(0).getHall().getCinema().getBranch_name());
                        }
                    }
                    for(int i=0;i<getSelected().size();i++){
                        //System.out.println(" number selected is "+getSelected().get(i));
                    }


                }
                else {
                    MessageBoundaryEmployee.displayInfo("Seat is reserved by another customer please choose another Seat ");

                }
            });
        }

        static double width() {
            return 26;
        }

        static double height() {
            return 36;
        }
        public static List<Integer> getSelected() {
            return selected;
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<Regulations> r = null;
        synchronized (CinemaClient.RegulationsDataLock) {
            org.example.Boundaries.Boundary.UpdateRegulationsData();
            // set items in table
            r = CinemaClient.RegulationsData;
//            SeatTable.setItems(DataList);
            //System.out.println(DataList.get(0).getHall().getCinema().getBranch_name());
        }
        Regulations re = r.get(0);
        if (re.getStatus()==false) {
            System.out.print("Regulations is false");


//        Seat1 s = new Seat1(1,false);
//s.getSelected();

            List<Seat> Data = null;
            List<Seat1> Data1 = new LinkedList<>();
            Show s1=(Show)App.getParams().get(0);
            System.out.println("Show is "+s1.getMovie().getName_en());
            Hall h = s1.getHall();

            System.out.println(" Hall is " + h.getNumber());
            synchronized (CinemaClient.SeatDataLock) {
                org.example.Boundaries.Boundary.UpdateSeatsHallData(h.getID());
                // set items in table
                Data = CinemaClient.SeatData;
//            SeatTable.setItems(DataList);
                //System.out.println(DataList.get(0).getHall().getCinema().getBranch_name());
            }
            for (int i = 0; i < Data.size(); i++) {


                    Seat1 seat = new Seat1(Data.get(i).getNumber(), Data.get(i).isAvailable(),Data.get(i).getID());
                    Data1.add(seat);




                //pane.getChildren().add(seat);


            }

            int i = 0;
            int j = 1;
            int count = 120;
            int y = 120;


            while (i < h.getMaxSeats() - 1) {
                Seat1 seat = Data1.get(i);
                Seat s = Data.get(i);
                while (s.getLine() == j) {
                    seat.setLayoutX(count);
                    count += seat.width() + 10;
                    seat.setLayoutY(y);
                    pane.getChildren().add(seat);
                    i += 1;
                    if (i < Data1.size()) {
                        s = Data.get(i);
                        seat = Data1.get(i);

                    } else {
                        break;
                    }
                }
                j += 1;
                count = 120;
                y += 30;


            }

            System.out.println("initializing done");
        }
        else {
            System.out.println("initializing done regulation is on ");
            List<Seat> Data = null;
            List<Seat1> Data1 = new LinkedList<>();
            Show s1=(Show)App.getParams().get(0);
            System.out.println("Show is "+s1.getMovie().getName_en());
            Hall h = s1.getHall();

            System.out.println(" Hall is " + h.getNumber());
            synchronized (CinemaClient.SeatDataLock) {
                org.example.Boundaries.Boundary.UpdateSeatsHallData(h.getID());
                // set items in table
                Data = CinemaClient.SeatData;
//            SeatTable.setItems(DataList);
                //System.out.println(DataList.get(0).getHall().getCinema().getBranch_name());
            }
            System.out.println(" Hall is " + h.getID());
            Seat1 s = new Seat1(1,false,0);
            for(int i=0;i<Data.size();i++){
                if(Data.get(i).getAvailable()==true){
                    MessageBoundaryEmployee.displayInfo("Your seat number is  "+Data.get(i).getNumber()+ " Please Click Choose seat ");

                    s.selected.add(Data.get(i).getNumber());
                    break;


                }
            }

        }

    }



}

