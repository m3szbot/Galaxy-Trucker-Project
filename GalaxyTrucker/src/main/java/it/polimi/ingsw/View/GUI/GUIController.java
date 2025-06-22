package it.polimi.ingsw.View.GUI;

import javafx.scene.Node;

public abstract class GUIController {

    public abstract void refreshConsole(String message);


    public abstract void refreshShipBoard(Node node);

    public abstract void refreshCard(Node node);

    public abstract void refreshComponent(Node node);

    public abstract void refreshFlightBoard(Node node);

}
