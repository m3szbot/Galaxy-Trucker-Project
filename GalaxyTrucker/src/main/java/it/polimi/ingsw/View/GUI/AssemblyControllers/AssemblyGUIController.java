package it.polimi.ingsw.View.GUI.AssemblyControllers;

import it.polimi.ingsw.Connection.ClientSide.utils.ClientInputManager;
import it.polimi.ingsw.Controller.FlightPhase.Cards.Card;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.View.GUI.GUIController;
import it.polimi.ingsw.View.GUI.PlayerInputSetter;
import it.polimi.ingsw.View.GUI.utils.FXUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AssemblyGUIController extends GUIController implements PlayerInputSetter {
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

    @FXML private Button Tile1, Tile2, Tile3, Tile4;
    @FXML private Button Tile1T, Tile2T, Tile3T, Tile4T;

    @FXML private AnchorPane flightBoardPane;



    private void processInput(String input) {
        input = input.toLowerCase();
        if(input.equals("show deck")){
            openDeckChoicePopup();
        }
    }

    public void initialize(){

        button.setOnAction(actionEvent -> {
            String input = playerInputField.getText();
            setUserInput(input);
            processInput(input);
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

    public void refreshComponent(Node node){

        FXUtil.runOnFXThread(() -> {
            currentComponent.getChildren().clear();
            currentComponent.getChildren().add(node);
        });

    }

    public void refreshFlightBoard(Node node){

        //useless in this phase

    }

    public void refreshCard(Node node){

        FXUtil.runOnFXThread(() -> {
            deckContainer.getChildren().add(node);
        });

    }

    /*
    public void printUncovered(List<Component> components){
        openChoicePopup(components);
    }

    private void openChoicePopup(List<Component> components) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AssemblyView/componentChoicePopUp.fxml"));

            AnchorPane popupRoot = loader.load();

            ScrollPane scrollPane = (ScrollPane) popupRoot.lookup("#scrollPane");
            VBox container = (VBox) popupRoot.lookup("#dynamicContainer");

            Stage popupStage = new Stage();
            sendData("Choose your component");
            popupStage.setScene(new Scene(popupRoot));
            popupStage.initModality(Modality.APPLICATION_MODAL);

            //I dynamically add the components to the popup
            for (int i = 0; i < components.size(); i++) {
                int index = i;

                ImageView imageView = new ImageView(new Image(getClass().getResource(components.get(i).getImgAddress()).toExternalForm()));
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
                imageView.setPreserveRatio(true);

                VBox wrapper = new VBox(imageView);
                wrapper.setPadding(new Insets(10));
                wrapper.setStyle("-fx-cursor: hand;");
                wrapper.setOnMouseClicked(event -> {
                    sendData(String.valueOf(index));
                    popupStage.close();
                });

                container.getChildren().add(wrapper);

            }

            popupStage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
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


            for (int i = 0; i < 3; i++) {
                int index = i;
                Image image = new Image(getClass().getResource("/Polytechnic.cards.GT-cards_II_IT_0121.jpg").toExternalForm());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);
                imageView.setPreserveRatio(true);
                imageView.setCursor(Cursor.HAND);

                imageView.setOnMouseClicked(event -> {
                    setUserInput(String.valueOf(index));
                    popupStage.close();
                    openShowDeckPopup();
                });

                container.getChildren().add(imageView);
            }

            popupStage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

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
            popupStage.show();
        }catch (IOException ex) {
            ex.printStackTrace();
        }
    }
/*
    public void openPositionChoicePopUp(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AssemblyView/PositionChoicePopUp.fxml"));
        flightBoard.setImage(fly.getImgAddress()).toExternalForm();

        if(fly.getImgAddress().equals("src/main/resources/Polytechnic/cardboard/cardboard-5.png")){
            //TODO creation of starting position tiles
            Tile1.setOnMouseClicked(event -> {
                setUserInput("7");
            });
            Tile2.setOnMouseClicked(event -> {
                setUserInput("4");
            });
            Tile3.setOnMouseClicked(event -> {
                setUserInput("2");
            });
            Tile4.setOnMouseClicked(event -> {
                setUserInput("1");
            });

        }else{
            //TODO creation of starting position tiles in TestGame
            Tile1T.setOnMouseClicked(event -> {setUserInput("5");});
            Tile2T.setOnMouseClicked(event -> {setUserInput("3");});
            Tile3T.setOnMouseClicked(event -> {setUserInput("2");});
            Tile4T.setOnMouseClicked(event -> {setUserInput("1");});
        }
    }

*/
}
