package it.polimi.ingsw.Controller.AssemblyPhase;

public class NotPermittedPlacementException extends Exception {
    public NotPermittedPlacementException() {
        super("Your are not allowed to place your component here");
    }
}
