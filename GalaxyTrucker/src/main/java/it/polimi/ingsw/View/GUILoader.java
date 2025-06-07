package it.polimi.ingsw.View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GUILoader extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        try{

            Parent galaxyTruckerGui = FXMLLoader.load(getClass().getResource("/fxml/GeneralFXML.fxml"));
            primaryStage.setTitle("Galaxy Trucker");
            primaryStage.setScene(new Scene(galaxyTruckerGui));
            primaryStage.setFullScreen(true);
            primaryStage.show();


        }catch (IOException e){
           System.err.println("Error while loading gui");
        }

    }

}
