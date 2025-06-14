package it.polimi.ingsw.View.GUI.LobbyControllers;

import it.polimi.ingsw.Connection.ClientSide.utils.ClientInputManager;
import it.polimi.ingsw.View.GUI.GUIController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class LobbyGUIController extends GUIController {

    @FXML
    private TextArea logTextArea;
    @FXML
    private Button button;
    @FXML
    private TextField playerInputField;

    public void initialize(){

        button.setOnAction(actionEvent -> sendData(playerInputField.getText()));

    }


    public void showMessage(String message){
        logTextArea.appendText(message);
    }

    @Override
    public void showCard(Node node) {
        //not used
    }

    @Override
    public void showComponent(Node node) {
       //not used
    }

    @Override
    public void showFlightBoard(Node node) {
       //not used
    }

    @Override
    public void showShipBoard(Node node) {
       //not used
    }

    public void sendData(String playerInput){

        ClientInputManager.setUserInput(playerInput);
    }

}
