package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Connection.ClientSide.utils.ViewCommunicator;
import it.polimi.ingsw.Model.GameInformation.GamePhase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GUILoader extends Application {

    public static ViewCommunicator viewCommunicator;

    public static void setViewCommunicator(ViewCommunicator communicator) {
        viewCommunicator = communicator;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GeneralFXML.fxml"));
            Parent root = loader.load();

            GeneralGUIController controller = loader.getController();

            if(viewCommunicator == null){
                System.err.println("View communicator is not set!");
            }
            else{
                viewCommunicator.setGeneralGUIController(controller);
                viewCommunicator.setGamePhase(GamePhase.Lobby);
            }

            primaryStage.setTitle("Galaxy Trucker");
            primaryStage.setScene(new Scene(root));
            primaryStage.setFullScreen(true);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while loading gui");
        }
    }

}
