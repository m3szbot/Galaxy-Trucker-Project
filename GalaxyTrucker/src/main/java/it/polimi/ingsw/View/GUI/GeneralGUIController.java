package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Model.GameInformation.GamePhase;
import it.polimi.ingsw.View.GeneralView;
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

    /**
     * Set the gui interface for the phase passed as parameter. The method must
     * be used in the client side in the setGamePhase method
     * @param gamePhase
     */

    public void setPhaseGUI(GamePhase gamePhase, GeneralView guiView){

        switch (gamePhase){
            case Lobby -> {
                loadPhaseGUI("/fxml/LobbyView/Lobby.fxml", guiView);
            }
            case Assembly -> {

                loadPhaseGUI("/fxml/AssemblyView/AssemblyView.fxml", guiView);

            }
            case Correction -> {

                loadPhaseGUI("/fxml/CorrectionView/Correction.fxml", guiView);

            }
            case Flight -> {

                loadPhaseGUI("/fxml/FlightView/Flight.fxml", guiView);

            }
            case Evaluation -> {

                loadPhaseGUI("/fxml/EvaluationView/Evaluation.fxml", guiView);

            }
            default -> {
                return;
            }
        }

    }
    private void loadPhaseGUI(String fxmlPath, GeneralView guiView){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node node = loader.load();

            rootPane.getChildren().clear();

            rootPane.getChildren().add(node);

            //centering everything

            rootPane.widthProperty().addListener((obs, oldVal, newVal) -> {
                node.setLayoutX((newVal.doubleValue() - node.prefWidth(-1)) / 2);
            });
            rootPane.heightProperty().addListener((obs, oldVal, newVal) -> {
                node.setLayoutY((newVal.doubleValue() - node.prefHeight(-1)) / 2);
            });

            node.setLayoutX((rootPane.getWidth() - node.prefWidth(-1)) / 2);
            node.setLayoutY((rootPane.getHeight() - node.prefHeight(-1)) / 2);




            GUIController controller = loader.getController();
            ((GUIView)guiView).setGuiController(controller);

        } catch (IOException e) {
            System.err.println("Error while loading the fxml file at path: " + fxmlPath);
            e.printStackTrace();
        }
    }


}
