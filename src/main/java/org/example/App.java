package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

import org.example.Boundaries.*;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @SuppressWarnings("exports")
	@Override
    public void start(Stage stage) throws IOException {
    	stage.setTitle("Cinema");
        scene = new Scene(loadFXML("ContentManagerMB", null, stage), 800, 488);
        stage.setScene(scene);
        stage.show();
        System.out.println("Client's boundary displayed");
    }

    @SuppressWarnings("exports")
	public static void setRoot(String fxml, List<Object> params, Stage stage) throws IOException {
    	FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
    	Parent root = fxmlLoader.load();
    	Boundary boundary = fxmlLoader.<Boundary>getController();
    	boundary.setParams(params);
    	boundary.setStage(stage);
        scene.setRoot(root);
    }

    private static Parent loadFXML(String fxml, List<Object> params, Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Parent root = fxmlLoader.load();
    	Boundary boundary = fxmlLoader.<Boundary>getController();
    	boundary.setParams(params);
    	boundary.setStage(stage);
        return root;
    }

    public static void main(String[] args) {
        launch();
    }

}
