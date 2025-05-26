package it.polimi.ingsw.View.AssemblyView;

import it.polimi.ingsw.Model.Components.Component;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.util.concurrent.atomic.AtomicReference;

// TODO: implement
public abstract class AssemblyViewGUI extends AssemblyView {
    private transient AtomicReference<String> userInput;
    String input;

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
/*
    @Override
    public void public void printComponent(Component component) {
        String componentString = getCorrectComponentString(component);
        System.out.printf(componentString);
    }*/



}
