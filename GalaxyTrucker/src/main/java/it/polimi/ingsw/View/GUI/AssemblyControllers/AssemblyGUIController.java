package it.polimi.ingsw.View.GUI.AssemblyControllers;

import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.View.GUI.GUIController;
import it.polimi.ingsw.View.GUI.PlayerInputSetter;
import it.polimi.ingsw.View.GUI.utils.FXUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * {@code AssemblyGUIController} is the JavaFX controller for the assembly phase GUI.
 * It manages user interaction, input dispatching, popup management for deck selection,
 * and GUI updates for the player's shipboard and selected components.
 */
public class AssemblyGUIController extends GUIController implements PlayerInputSetter {
    String input;
    private HBox deckContainer;

    // Declatation of FXML elements
    @FXML
            private TextArea console;
    @FXML
            private AnchorPane shipBoardPane;
    @FXML
            private TextField playerInputField;
    @FXML
            private Button button;
    @FXML
            private AnchorPane currentComponent;

    @FXML private AnchorPane flightBoardPane;
    @FXML private AnchorPane root;
    private GameType gameType;


    /**
     * Processes special user inputs such as "show".
     *
     * @param input the input text to process
     */
    private void processInput(String input) {
        input = input.trim().toLowerCase();
        if(input.equals("show") && gameType.equals(GameType.NORMALGAME)){
            FXUtil.runOnFXThread(this::openDeckChoicePopup);
        }
    }

    /**
     * Sets the current game type for this session.
     *
     * @param gameType the current {@link GameType}
     */
    public void setGameType(GameType gameType){
        this.gameType = gameType;
    }

    /**
     * Initializes the controller, wiring event handlers for the input field and button.
     * Called automatically after the FXML is loaded.
     */
    public void initialize(){

        button.setOnAction(actionEvent -> {
            String input = playerInputField.getText();
            playerInputField.clear();
            setUserInput(input);
            processInput(input);
        });

        root.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            playerInputField.requestFocus();
        });

        playerInputField.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                button.fire();
            }
        });

    }

    /**
     * Appends a message to the console in a thread-safe way.
     *
     * @param message the message to display
     */
    public void refreshConsole(String message){

        FXUtil.runOnFXThread(() -> console.appendText(message + "\n"));

    }

    /**
     * Replaces the current shipboard content with a new node.
     *
     * @param node the visual representation of the shipboard
     */
    public void refreshShipBoard(Node node){

        FXUtil.runOnFXThread(() -> {
            shipBoardPane.getChildren().clear();
            shipBoardPane.getChildren().add(node);
        });

    }

    /**
     * Replaces the current displayed component with a new one.
     *
     * @param node the node to be shown as current component
     */
    public void refreshComponent(Node node){

        FXUtil.runOnFXThread(() -> {
            currentComponent.getChildren().clear();
            currentComponent.getChildren().add(node);
        });

    }

    /**
     * Not used in this phase. Reserved for future use or compliance with interface.
     *
     * @param node unused
     */
    public void refreshFlightBoard(Node node){

        //useless in this phase

    }

    /**
     * Adds a card node to the deck container.
     *
     * @param node the card node to be displayed
     */
    public void refreshCard(Node node){

        FXUtil.runOnFXThread(() -> {
            deckContainer.getChildren().add(node);
        });

    }

    /**
     * Opens a popup that allows the player to choose a deck to consult.
     * When a deck is selected, it closes and opens the deck viewer.
     */
    public void openDeckChoicePopup(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AssemblyView/deckChoicePopUp.fxml"));

            AnchorPane popupRoot = loader.load();
            HBox container = (HBox) popupRoot.lookup("#imageContainer");

            Stage popupStage = new Stage();
            popupStage.setTitle("Deck choice");
            popupStage.setScene(new Scene(popupRoot));
            popupStage.initModality(Modality.APPLICATION_MODAL);


            for (int i = 1; i < 4; i++) {
                int index = i;
                Image image = new Image(getClass().getResource("/Polytechnic/cards/GT-cards_II_IT_0121.jpg").toExternalForm());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);
                imageView.setPreserveRatio(true);
                imageView.setCursor(Cursor.HAND);

                imageView.setOnMouseClicked(event -> {
                    setUserInput(String.valueOf(index));
                    popupStage.close();
                    FXUtil.runOnFXThread(() -> openShowDeckPopup());
                });

                container.getChildren().add(imageView);
            }

            popupStage.toFront();
            popupStage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Opens a popup to display the selected deck’s contents.
     * Allows the player to close it once finished consulting.
     */
    public void openShowDeckPopup(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AssemblyView/showDeckPopUp.fxml"));
            AnchorPane popupRoot = loader.load();

            ShowDeckPopUpController controller = loader.getController();
            deckContainer = controller.getImageContainer();

            Stage popupStage = new Stage();
            popupStage.setTitle("Show deck");
            popupStage.setScene(new Scene(popupRoot));
            popupStage.initModality(Modality.APPLICATION_MODAL);

            Button closeButton = (Button) popupRoot.lookup("#closeButton");
            closeButton.setOnAction(e -> {
                setUserInput("yes");
                popupStage.close();
                deckContainer.getChildren().clear();
            });
            popupStage.toFront();
            popupStage.show();
        }catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Returns the relative path to the background image used in the assembly view.
     *
     * @return the background image path
     */
    public String getBackgroundImage(){
        return("/Polytechnic/Imgs/image_5.png");
    }

}
