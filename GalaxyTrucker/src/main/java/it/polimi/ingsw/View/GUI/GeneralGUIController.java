package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Model.GameInformation.GamePhase;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * The general controller class set the gui for each phase by appending the
 * relative fxml file to the General fxml file. The set is happening in the client
 * side.
 *
 * @author carlo
 */

public class GeneralGUIController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    public void initialize(){

        //the first phase is lobby phase

        loadPhaseGUI("/fxml/LobbyView/Lobby.fxml");

    }

    /**
     * Set the gui interface for the phase passed as parameter. The method must
     * be used in the client side in the setGamePhase method
     * @param gamePhase
     */

    public void setPhaseGUI(GamePhase gamePhase){

        switch (gamePhase){
            case Assembly -> {

                loadPhaseGUI("/fxml/AssemblyView/AssemblyView.fxml");

            }
            case Correction -> {

                loadPhaseGUI("/fxml/CorrectionView/CorrectionView.fxml");

            }
            case Flight -> {

                loadPhaseGUI("/fxml/FlightView/FlightView.fxml");

            }
            case Evaluation -> {

                loadPhaseGUI("/fxml/EvaluationView/EvaluationView.fxml");

            }
        }

    }

    private void loadPhaseGUI(String fxmlPath){

        try {

            Node node = FXMLLoader.load(getClass().getResource(fxmlPath));

            //removing the old node

            rootPane.getChildren().clear();

            //adding the new node
            rootPane.getChildren().add(node);

            //making the inserted node occupy the entire stage

            AnchorPane.setTopAnchor(node, 0.0);
            AnchorPane.setBottomAnchor(node, 0.0);
            AnchorPane.setLeftAnchor(node, 0.0);
            AnchorPane.setRightAnchor(node, 0.0);

        } catch (IOException e) {

            System.err.println("Error while loading the fxml file at path: " + fxmlPath);

        }
    }

}
