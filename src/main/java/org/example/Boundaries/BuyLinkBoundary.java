package org.example.Boundaries;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Callback;
import org.example.App;
import org.example.OCSF.CinemaClient;
import org.example.entities.Movie;

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
    private AnchorPane anchor;

    @FXML
    private Button BackBtn;
    @FXML
    Label label;

    @FXML
    private Button bakbtn;
    @FXML
    private TableView<String> tablesum;
    @FXML
    private TableColumn<String, String> sumcol;



    @FXML private TableView<Movie> MoviesTable;
    @FXML private TableColumn<Movie, String> english;
    @FXML private TableColumn<Movie, String> director;
    @FXML private TableColumn<Movie, Button> summary;
    @FXML private TableColumn<Movie, ListView<String>> cast;
    @FXML private TableColumn<Movie, String> hebrew;
    @FXML private TableColumn<Movie, ImageView> image;




    @FXML
    void clickRefreshBtn(ActionEvent event) {
        MoviesTable.setVisible(true);

        tablesum.setVisible(false);

    }
    @FXML
    void clickChooseMovieBtn(ActionEvent event) throws IOException {
        if(MoviesTable.getSelectionModel().getSelectedItem() == null ){
            label.setText("Please Choose Movie");
        }
        else {
            Movie m  =  MoviesTable.getSelectionModel().getSelectedItem();
            List<Object> l = new LinkedList<>();
            l.add(m);
            App.setParams(l);
            App.setRoot("PaymentLink", params, stage);

        }

         //Step 6



    }


    @FXML
    void clickBackBtn(ActionEvent event) throws IOException {
        List<Object> l = new LinkedList<>();
        App.setParams(l);
        App.setRoot("CustomerMain",null, stage);

    }
    @FXML
    void clickcak(ActionEvent event) {
        MoviesTable.setVisible(true);
        tablesum.setVisible(false);
        bakbtn.setVisible(false);


    }



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MoviesTable.setVisible(true);
        tablesum.setVisible(false);
        // set-up the columns in the table
        english.setStyle( "-fx-alignment: CENTER;");
        english.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Movie, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Movie, String> movie) {
                //System.out.print(new SimpleStringProperty(show.getValue().getMovie().getName_en()));
                return (new SimpleStringProperty(movie.getValue().getName_en()));
            }
        });
        hebrew.setStyle( "-fx-alignment: CENTER;");
        hebrew.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Movie, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Movie, String> movie) {
                //System.out.print(new SimpleStringProperty(show.getValue().getMovie().getName_en()));
                return (new SimpleStringProperty(movie.getValue().getName_heb()));
            }
        });
        director.setStyle( "-fx-alignment: CENTER;");
        director.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Movie, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Movie, String> movie) {
                return (new SimpleStringProperty(movie.getValue().getDirector()));
            }
        });
        cast.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Movie, ListView<String>>, ObservableValue<ListView<String>>>() {
            public ObservableValue<ListView<String>> call(TableColumn.CellDataFeatures<Movie, ListView<String>> movie) {
                ListView<String> l = new ListView<String>();
                l.setPrefHeight(120);
                l.setPrefWidth(180);
                List<String> l1 = movie.getValue().getCast();
                ObservableList<String> l5 =FXCollections.observableArrayList(l1);
                l.setItems(l5);
                return new SimpleObjectProperty<ListView<String>>(l);
            }
        });
        image.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Movie, ImageView>, ObservableValue<ImageView>>() {
            public ObservableValue<ImageView> call(TableColumn.CellDataFeatures<Movie, ImageView> movie) {
                ImageView m = new ImageView();
                m.setFitWidth(150);
                m.setFitHeight(150);
                //System.out.println(movie.getValue().getImage());
                m.setImage(new Image(movie.getValue().getImage()));

                return (new SimpleObjectProperty(m));
            }
        });
        tablesum.setVisible(false);
        summary.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Movie, Button>, ObservableValue<Button>>() {
            public ObservableValue<Button> call(TableColumn.CellDataFeatures<Movie, Button> movie) {
                Button t = new Button();
                t.setText("View Summary");
                t.setId(Integer.toString(movie.getValue().getID()));
                t.setMinWidth(30);
                t.setMinHeight(30);
                tablesum.setVisible(false);
                Font font = Font.font("Courier New", FontWeight.BOLD, 10);
                t.setFont(font);


                t.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        MoviesTable.setVisible(false);
                        tablesum.setVisible(true);
                        bakbtn.setVisible(true);
                          Movie mo=null;
                        synchronized(CinemaClient.MoviesDataLock) {
                            org.example.Boundaries.Boundary.UpdateMoviesData();
                            // set items in table
                            List<Movie> movies =CinemaClient.MoviesData;
                            List <Movie> mov = new LinkedList<>();
                            for(int i=0;i<movies.size();i++){
                                if(movies.get(i).isAvailableOnline()==true){
                                    mov.add(movies.get(i));
                                    if( movies .get(i).getID()==Integer.parseInt(t.getId())){
                                        mo =  movies.get(i);
                                    }
                                }
                            }

                            // System.out.println(DataList.get(0).getMovie().getName_en());
                        }
                        TableView table = new TableView();
                        table.setMinWidth(200);
                        table.setMinHeight(200);



                        sumcol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String, String>, ObservableValue<String>>() {
                            public ObservableValue<String> call(TableColumn.CellDataFeatures<String, String> movie) {
                                //System.out.print(new SimpleStringProperty("888888888888888"));
                                return (new SimpleStringProperty(movie.getValue()));
                            }
                        });
                       //ScrollBar verticalBar = (ScrollBar) tablesum.lookup(".scroll-bar:horizontal");


                        List<String> ll = new LinkedList<>();
                        ll.add(mo.getSummary());
                        ObservableList<String> DataList = FXCollections.observableArrayList(ll);
                       tablesum.setItems(DataList);
                        autoResizeColumns( tablesum );


                    }
                });
                return (new SimpleObjectProperty(t));
            }
        });
        List <Movie> mov = new LinkedList<>();
        synchronized(CinemaClient.MoviesDataLock) {
            org.example.Boundaries.Boundary.UpdateOnlineMoviesData();
            // set items in table
            List<Movie> movies =CinemaClient.MoviesData;
//            for(int i=0;i<movies.size();i++){
//                if(movies.get(i).isAvailableOnline()==true){
//                    mov.add(movies.get(i));
//                }
//            }
            ObservableList<Movie> DataList = FXCollections.observableArrayList(movies);
            MoviesTable.setItems(DataList);
//             System.out.println(DataList.get(0).getMovie().getName_en());
        }
//        tablesum.setVisible(false);
        //tablesum.managedProperty().bind(tablesum.visibleProperty());

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
