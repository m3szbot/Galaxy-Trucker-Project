package it.polimi.ingsw.View.GUI.LobbyControllers;

import it.polimi.ingsw.Connection.ClientSide.utils.ClientInputManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class LobbyGUIController {

    @FXML
    private TextArea logTextArea;
    @FXML
    private Button button;
    @FXML
    private TextField playerInputField;

    public void initialize(){

        button.setOnAction(actionEvent -> sendData(playerInputField.getText()));

    }

    public void showData(String message, boolean isError){
        if (isError) {
            logTextArea.appendText("[ERROR] " + message + "\n");
        } else {
            logTextArea.appendText(message + "\n");
        }
    }

    public void sendData(String playerInput){

        ClientInputManager.setUserInput(playerInput);
    }

}
