package it.polimi.ingsw.View.GUI;

import javafx.scene.Node;

public abstract class GUIController {

    public abstract void showMessage(String message);


    public abstract void showShipBoard(Node node);

    public abstract void showCard(Node node);

    public abstract void showComponent(Node node);

    public abstract void showFlightBoard(Node node);

}
