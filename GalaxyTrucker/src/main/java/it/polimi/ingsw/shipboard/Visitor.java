package it.polimi.ingsw.shipboard;
import it.polimi.ingsw.components.*;
import javafx.concurrent.Worker;

public interface Visitor {
    void visit(Cannon cannon);
    void visit(Shield shield);
    void visit(Component component);
}