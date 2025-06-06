/*package it.polimi.ingsw.View.AssemblyView;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
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
public abstract class AssemblyViewGUI extends AssemblyView {
    private transient AtomicReference<String> userInput;
    String input;

    // Declatation of FXML elements
    @FXML
            private ImageView inUseComponent;
    @FXML
            private ImageView booked1;
    @FXML
            private ImageView booked2;



    AssemblyViewGUI(AtomicReference<String> userInput) {
        this.userInput = userInput;
    }

    public String getFXML(){
        return "/fxml/AssemblyView/AssemblyView.fxml";
    }

    @FXML
    public void drawInput(ActionEvent event){
        input = "draw";
        this.userInput.set(input);
    }
    @FXML
    public void placeInput(ActionEvent event){
        input = "place";
        this.userInput.set(input);
        //... logica rinoscimento id e place
    }

    @FXML
    public void rotateInput(ActionEvent event){
        input = "rotate";
        this.userInput.set(input);
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
        this.userInput.set(input);
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
            popupStage.setTitle("Choose your component");
            popupStage.setScene(new Scene(popupRoot));
            popupStage.initModality(Modality.APPLICATION_MODAL);

            //I dynamically add the components to the popup
            for (int i = 0; i < components.size(); i++) {
                final int index = i;

                ImageView imageView = new ImageView(new Image(getClass().getResource(components.get(i).getImgAddress()).toExternalForm()));
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
                imageView.setPreserveRatio(true);

                VBox wrapper = new VBox(imageView);
                wrapper.setPadding(new Insets(10));
                wrapper.setStyle("-fx-cursor: hand;");
                wrapper.setOnMouseClicked(event -> {
                    this.userInput.set(String.valueOf(index));
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
            popupStage.setTitle("Show deck");
            popupStage.setScene(new Scene(popupRoot));
            popupStage.initModality(Modality.APPLICATION_MODAL);


            for (int i = 0; i < 3; i++) {
                final int index = i;

                Image image = new Image(getClass().getResource("/Polytechnic.cards.GT-cards_II_IT_0121.jpg").toExternalForm());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);
                imageView.setPreserveRatio(true);
                imageView.setCursor(Cursor.HAND);

                imageView.setOnMouseClicked(event -> {
                    this.userInput.set(String.valueOf(index));
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
                userInput.set("yes");
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
        this.userInput.set(input);
    }

    @FXML
    public void placeBookedInput(ActionEvent event){
        input = "place booked";
        this.userInput.set(input);
        if(event.getSource() == booked1){
            input = String.valueOf(0);
            this.userInput.set(input);
        }else{
            input = String.valueOf(1);
            this.userInput.set(input);
        }
    }

    @FXML
    public void bookInput(ActionEvent event){
        input = "book";
        this.userInput.set(input);
    }

    @FXML
    public void endInput(ActionEvent event){
        input = "end";
        this.userInput.set(input);
    }

    public void positionChoicePopUp(){

    }

}
*/