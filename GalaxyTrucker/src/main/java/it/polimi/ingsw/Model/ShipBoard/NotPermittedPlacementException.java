package it.polimi.ingsw.Model.ShipBoard;

public class NotPermittedPlacementException extends Exception {
    public NotPermittedPlacementException(String s) {
        super("You are not allowed to place your component here");
    }
}
