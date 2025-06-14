/*package it.polimi.ingsw.View.GUI.AssemblyControllers;

import it.polimi.ingsw.Connection.ClientSide.utils.ClientInputManager;
import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.View.GUI.GeneralGUIController;
import it.polimi.ingsw.View.GeneralView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
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

// TODO: implement
public abstract class AssemblyGUIController extends GUIController {
    private transient AtomicReference<String> userInput;
    String input;

    // Declatation of FXML elements
    @FXML
            private ImageView inUseComponent;
    @FXML
            private ImageView booked1;
    @FXML
            private ImageView booked2;
    @FXML
            private ImageView flightBoard;

    @FXML private Button Tile1, Tile2, Tile3, Tile4;
    @FXML private Button Tile1T, Tile2T, Tile3T, Tile4T;



    AssemblyViewGUI(AtomicReference<String> userInput) {
        this.userInput = userInput;
    }

    public String getFXML(){
        return "/fxml/AssemblyView/AssemblyView.fxml";
    }

    @FXML
    public void drawInput(ActionEvent event){
        input = "draw";
        sendData(input);
    }
    @FXML
    public void placeInput(ActionEvent event){
        input = "place";
        sendData(input);
        //... logica rinoscimento id e place
    }

    @FXML
    public void rotateInput(ActionEvent event){
        input = "rotate";
        sendData(input);
    }

   @Override
    public void printComponent(Component component) {
       // String imdAddress = component.getImgAddress();
      //  Image img = new Image(imdAddress);
       // inUseComponent.setImage(img);
    }

    @FXML
    public void choiceInput(ActionEvent event){
        input = "choice";
        sendData(input);
    }

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

    public void openDeckChoicePopup(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AssemblyView/deckChoicePopUp.fxml"));

            AnchorPane popupRoot = loader.load();
            HBox container = (HBox) popupRoot.lookup("#imageContainer");

            Stage popupStage = new Stage();
            sendData("Show deck");
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
                    sendData(String.valueOf(index));
                    popupStage.close();
                });

                container.getChildren().add(imageView);
            }

            popupStage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void openShowDeckPopup(List<Card> cards){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AssemblyView/showDeckPopUp.fxml"));
            AnchorPane popupRoot = loader.load();
            HBox container = (HBox) popupRoot.lookup("#imageContainer");

            Stage popupStage = new Stage();
            popupStage.setTitle("Show deck");
            popupStage.setScene(new Scene(popupRoot));
            popupStage.initModality(Modality.APPLICATION_MODAL);

            for (int i = 0; i < cards.size(); i++) {
                final int index = i;

                Image image = new Image(getClass().getResource(cards.get(i).getImgAddress()).toExternalForm());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);
                imageView.setPreserveRatio(true);
                imageView.setCursor(Cursor.HAND);

                container.getChildren().add(imageView);
            }

            Button closeButton = (Button) popupRoot.lookup("#closeButton");
            closeButton.setOnAction(e -> {
                sendData("yes");
                popupStage.close();
            });
            popupStage.show();
        }catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void showInput(ActionEvent event){
        input = "show";
        sendData(input);
    }

    @FXML
    public void placeBookedInput(ActionEvent event){
        input = "place booked";
        sendData(input);
        if(event.getSource() == booked1){
            input = String.valueOf(0);
            sendData(input);
        }else{
            input = String.valueOf(1);
            sendData(input);
        }
    }

    @FXML
    public void bookInput(ActionEvent event){
        input = "book";
        sendData(input);
    }

    @FXML
    public void endInput(ActionEvent event){
        input = "end";
        sendData(input);
    }

    public void positionChoicePopUp(FlightBoard fly){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AssemblyView/PositionChoicePopUp.fxml"));
        flightBoard.setImage(fly.getImgAddress()).toExternalForm();

        if(fly.getImgAddress().equals("src/main/resources/Polytechnic/cardboard/cardboard-5.png")){
            //TODO creation of starting position tiles
            Tile1.setOnMouseClicked(event -> {
                sendData("7");
            });
            Tile2.setOnMouseClicked(event -> {
                sendData("4");
            });
            Tile3.setOnMouseClicked(event -> {
                sendData("2");
            });
            Tile4.setOnMouseClicked(event -> {
                sendData("1");
            });
        }else{
            //TODO creation of starting position tiles in TestGame
            Tile1T.setOnMouseClicked(event -> {sendData("5");});
            Tile2T.setOnMouseClicked(event -> {sendData("3");});
            Tile3T.setOnMouseClicked(event -> {sendData("2");});
            Tile4T.setOnMouseClicked(event -> {sendData("1");});
        }

    }

    public void sendData(String playerInput){
        ClientInputManager.setUserInput(playerInput);
    }


}
*/