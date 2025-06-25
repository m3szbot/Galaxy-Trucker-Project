package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Model.GameInformation.GamePhase;
import it.polimi.ingsw.View.GeneralView;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

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

    private void loadPhaseGUI(String fxmlPath, GeneralView guiView) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane content = loader.load();
            GUIController controller = loader.getController();

            double baseW = 1920;
            double baseH = 1080;

            Group scaled = new Group(content);
            StackPane wrapper = new StackPane(scaled);
            StackPane.setAlignment(scaled, Pos.CENTER);


            AnchorPane.setTopAnchor(wrapper,    0.0);
            AnchorPane.setBottomAnchor(wrapper, 0.0);
            AnchorPane.setLeftAnchor(wrapper,   0.0);
            AnchorPane.setRightAnchor(wrapper,  0.0);

            rootPane.getChildren().setAll(wrapper);


            rootPane.setStyle(
                    "-fx-background-image: url('" + controller.getBackgroundImage() + "');" +
                            "-fx-background-size: cover;" +
                            "-fx-background-repeat: no-repeat;" +
                            "-fx-background-position: center center;"
            );


            ChangeListener<Number> listener = (obs, o, n) -> {
                double sX = wrapper.getWidth()  / baseW;
                double sY = wrapper.getHeight() / baseH;
                double s  = Math.min(sX, sY);
                scaled.setScaleX(s);
                scaled.setScaleY(s);
            };
            wrapper.widthProperty().addListener(listener);
            wrapper.heightProperty().addListener(listener);
            listener.changed(null, null, null);

            ((GUIView) guiView).setGuiController(controller);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
