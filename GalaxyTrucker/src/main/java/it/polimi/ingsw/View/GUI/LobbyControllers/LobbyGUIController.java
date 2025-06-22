package it.polimi.ingsw.View.GUI.LobbyControllers;

import it.polimi.ingsw.View.GUI.GUIController;
import it.polimi.ingsw.View.GUI.PlayerInputSetter;
import it.polimi.ingsw.View.GUI.utils.FXUtil;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class LobbyGUIController extends GUIController implements PlayerInputSetter {

    @FXML
    private TextArea logTextArea;
    @FXML
    private Button button;
    @FXML
    private TextField playerInputField;

    public void initialize(){

        button.setOnAction(actionEvent -> setUserInput(playerInputField.getText()));

    }


    public void refreshConsole(String message){
        FXUtil.runOnFXThread(() -> logTextArea.appendText(message + "\n"));
    }

    @Override
    public void refreshCard(Node node) {
        //not used
    }

    @Override
    public void refreshComponent(Node node) {
       //not used
    }

    @Override
    public void refreshFlightBoard(Node node) {
       //not used
    }

    @Override
    public void refreshShipBoard(Node node) {
       //not used
    }

}
