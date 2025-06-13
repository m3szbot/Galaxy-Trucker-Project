package it.polimi.ingsw.View.GUI.LobbyControllers;

import it.polimi.ingsw.Connection.ClientSide.utils.ClientInputManager;
import it.polimi.ingsw.Connection.ServerSide.socket.DataContainer;
import it.polimi.ingsw.View.GeneralView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class LobbyGUIController extends GeneralView {

    @FXML
    private TextArea logTextArea;
    @FXML
    private Button button;
    @FXML
    private TextField playerInputField;

    public void initialize(){

        button.setOnAction(actionEvent -> sendData(playerInputField.getText()));

    }

    public void printMessage(DataContainer dataContainer){
        logTextArea.appendText(dataContainer.getMessage() + "\n");
    }

    public void printMessage(String message){
        logTextArea.appendText(message);
    }

    public void sendData(String playerInput){

        ClientInputManager.setUserInput(playerInput);
    }

}
