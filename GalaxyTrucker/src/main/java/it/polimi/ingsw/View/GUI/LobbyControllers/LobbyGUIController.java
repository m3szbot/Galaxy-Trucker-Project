package it.polimi.ingsw.View.GUI.LobbyControllers;

import it.polimi.ingsw.View.GUI.GUIController;
import it.polimi.ingsw.View.GUI.PlayerInputSetter;
import it.polimi.ingsw.View.GUI.utils.FXUtil;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

/**
 * Controller of the Lobby gui during gui phase
 *
 * @author carlo
 */

public class LobbyGUIController extends GUIController implements PlayerInputSetter {

    @FXML
    private TextArea logTextArea;
    @FXML
    private Button button;
    @FXML
    private TextField playerInputField;
    @FXML
    private AnchorPane root;
    @FXML
    private Group centerGroup;


    public void initialize(){

        button.setOnAction(actionEvent -> {
            setUserInput(playerInputField.getText());
            playerInputField.clear();}
        );

        root.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            playerInputField.requestFocus();
        });

        playerInputField.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                button.fire();
            }
        });

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

    public String getBackgroundImage(){
        return ( "/Polytechnic/Imgs/image_4.png");
    }

}
