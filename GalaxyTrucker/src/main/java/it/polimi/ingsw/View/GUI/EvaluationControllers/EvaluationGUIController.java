package it.polimi.ingsw.View.GUI.EvaluationControllers;

import it.polimi.ingsw.View.GUI.GUIController;
import it.polimi.ingsw.View.GUI.PlayerInputSetter;
import it.polimi.ingsw.View.GUI.utils.FXUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

/**
 * Controller of the evaluation gui during evaluation phase.
 *
 * @author carlo
 */

public class EvaluationGUIController extends GUIController implements PlayerInputSetter {

    @FXML
    TextArea console;

    @FXML
    Button button;

    @FXML private AnchorPane root;


    public void initialize(){

        button.setOnAction(e -> {
            Platform.exit();
            System.exit(0);
        });

    }


    public void refreshConsole(String message){
        FXUtil.runOnFXThread(() -> console.appendText(message + "\n"));
    }


    public void refreshShipBoard(Node node){

        //not used

    }

    public void refreshCard(Node node){

        //not used

    }

    public void refreshComponent(Node node){

        //not used

    }

    public void refreshFlightBoard(Node node){

        //not used

    }

    public String getBackgroundImage() {
        return "/Polytechnic/Imgs/evaluation_background.png";
    }
}

