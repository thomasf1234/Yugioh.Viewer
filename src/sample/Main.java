package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("resources/sample.fxml"));

        primaryStage.setTitle("Yu-Gi-Oh! Viewer");
        Scene scene = new Scene(root);

        scene.getStylesheets().add("resources/css/sample.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop(){

    }

    public static void main(String[] args) {
        launch(args);
    }
}

