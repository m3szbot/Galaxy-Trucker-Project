package it.polimi.ingsw.View.GUI.CorrectionControllers;

import it.polimi.ingsw.View.GUI.GUIController;
import it.polimi.ingsw.View.GUI.PlayerInputSetter;
import it.polimi.ingsw.View.GUI.utils.FXUtil;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class CorrectionGUIController extends GUIController implements PlayerInputSetter {
    @FXML
    AnchorPane shipBoardPane;

    @FXML
    TextArea console;

    @FXML
    TextField playerInputField;

    @FXML
    Button button;

    public void initialize(){

        button.setOnAction(actionEvent -> setUserInput(playerInputField.getText()));

    }

    public void refreshConsole(String message){
        FXUtil.runOnFXThread(() -> console.appendText(message + "\n"));
    }


    public void refreshShipBoard(Node node){

        FXUtil.runOnFXThread(() -> {
            shipBoardPane.getChildren().clear();
            shipBoardPane.getChildren().add(node);
        });

    }

    public void refreshCard(Node node){

        //useless in this phase

    }

    public void refreshComponent(Node node){

        //useless in this phase

    }

    public void refreshFlightBoard(Node node){

        //useless in this phase

    }
}

