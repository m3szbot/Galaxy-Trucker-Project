package it.polimi.ingsw.shipboard;
import it.polimi.ingsw.components.*;
import javafx.concurrent.Worker;

public interface Visitor<T> {
    T visit(Cannon cannon);
    T visit(Shield shield);
    T visit(Component component);
}