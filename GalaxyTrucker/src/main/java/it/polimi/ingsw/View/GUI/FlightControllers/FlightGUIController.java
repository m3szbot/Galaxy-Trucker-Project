package it.polimi.ingsw.View.GUI.FlightControllers;

import it.polimi.ingsw.View.GUI.GUIController;
import it.polimi.ingsw.View.GUI.PlayerInputSetter;
import it.polimi.ingsw.View.GUI.utils.FXUtil;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

/**
 * Controller of the flight gui during flight phase
 *
 * @author carlo
 */

public class FlightGUIController extends GUIController implements PlayerInputSetter {

    @FXML
    AnchorPane shipBoardPane;

    @FXML
    AnchorPane flightBoardPane;

    @FXML
    AnchorPane cardPane;

    @FXML
    TextArea console;

    @FXML
    TextField playerInputField;

    @FXML
    Button button;

    @FXML
    private AnchorPane root;


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

        FXUtil.runOnFXThread(() -> console.appendText(message + "\n"));

    }


    public void refreshShipBoard(Node node){

        FXUtil.runOnFXThread(() -> {
            shipBoardPane.getChildren().clear();
            shipBoardPane.getChildren().add(node);
        });

    }

    public void refreshCard(Node node){

        FXUtil.runOnFXThread(() -> {
            cardPane.getChildren().clear();
            cardPane.getChildren().add(node);
        });

    }

    public void refreshComponent(Node node){

        //useless in this phase

    }

    public void refreshFlightBoard(Node node){

        FXUtil.runOnFXThread(() -> {
            flightBoardPane.getChildren().clear();
            flightBoardPane.getChildren().add(node);
        });

    }

    public String getBackgroundImage() {
        return "/Polytechnic/Imgs/image_9.png";
    }


}
