package it.polimi.ingsw.View.GUI.AssemblyControllers;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

public abstract class ShowDeckPopUpController {
    @FXML
    private HBox imageContainer;

    public HBox getImageContainer() {
        return imageContainer;
    }
}
