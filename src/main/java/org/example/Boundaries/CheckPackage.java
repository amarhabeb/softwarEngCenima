package org.example.Boundaries;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.App;
import org.example.OCSF.CinemaClient;
import org.example.OCSF.CinemaClientCLI;
import org.example.entities.Complaint;
import org.example.entities.Order;
import org.example.entities.PackageOrder;
import org.example.entities.Show;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class CheckPackage extends Boundary implements Initializable, Serializable {

    @FXML
    private TextField ordersNumberTextField1;




    @FXML
    private Button SubmitBtn;
    @FXML
    private Label label= new Label();



    @FXML
    private Button GoBackToMainBtn;



    @FXML
    void clickSubmitBtn(ActionEvent event) throws IOException {
        if( ordersNumberTextField1.getText().isEmpty()){
            System.out.println("Empty!");
            label.setText(" Please fill All The Fields");


        }
        else if(isNumeric(ordersNumberTextField1.getText())==false){
            label.setText("Please fill a number for order");


        }
        else {
            synchronized(CinemaClient.PackageDataLock) {
                org.example.Boundaries.Boundary.UpdatePackagesData();
                // set items in table
               List<PackageOrder> DataList = CinemaClient.PackageData;
               System.out.println(DataList.size());

               int id = Integer.parseInt(ordersNumberTextField1.getText());
               int flag=0;

                // System.out.println(DataList.get(0).getMovie().getName_en());
                for(int i=0;i<DataList.size();i++){
                    System.out.println("suc");

                    if(id == DataList.get(i).getID()){
                        System.out.println("suc");
                        label.setText("Your Balance is "+Integer.toString(DataList.get(i).getCounter()));
                        flag=1;


                    }

                }
                if(flag==0) {
                    label.setText("Please Enter a Package number ");


                }
            }


        }




    }

    public static boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c) == false) return false;
        }
        return true;
    }

    @FXML
    private Button refreshbtn;
    @FXML
    void clickrefreshBtn(ActionEvent event) {
        ordersNumberTextField1.setText("");
        label.setText("");


    }


    @FXML
    void clickGoBackToMainBtn(ActionEvent event) throws IOException {
        List<Object> l = new LinkedList<>();
        App.setParams(l);
        App.setRoot("CustomerMain",null, stage);

    }

        // update ShowData

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
