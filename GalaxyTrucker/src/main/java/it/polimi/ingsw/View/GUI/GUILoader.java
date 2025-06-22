package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Connection.ClientSide.utils.ViewCommunicator;
import it.polimi.ingsw.Connection.ServerSide.socket.SocketDataExchanger;
import it.polimi.ingsw.Model.GameInformation.GamePhase;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GUILoader extends Application {

    public static ViewCommunicator viewCommunicator;
    public static SocketDataExchanger socketDataExchanger;

    public static void setViewCommunicator(ViewCommunicator communicator) {
        viewCommunicator = communicator;
    }

    public static void setSocketDataExchanger(SocketDataExchanger dataExchanger){
        socketDataExchanger = dataExchanger;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/general/GeneralFXML.fxml"));
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

            primaryStage.setOnCloseRequest(event -> {

                socketDataExchanger.closeResources();
                System.out.println("Application closed");
                Platform.exit(); //stops the application thread
                System.exit(0); //forces all the threads to end
            });

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while loading gui");
        }
    }
}
