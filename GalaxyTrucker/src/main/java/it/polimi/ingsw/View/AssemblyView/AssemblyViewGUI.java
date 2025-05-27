package it.polimi.ingsw.View.AssemblyView;

import it.polimi.ingsw.Model.Components.Component;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.concurrent.atomic.AtomicReference;

// TODO: implement
public abstract class AssemblyViewGUI extends AssemblyView {
    private transient AtomicReference<String> userInput;
    String input;
    @FXML
    private ImageView inUseComponent;

    AssemblyViewGUI(AtomicReference<String> userInput) {
        this.userInput = userInput;
    }

    public String getFXML(){
        return "/fxml/AssemblyView.fxml";
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



}
