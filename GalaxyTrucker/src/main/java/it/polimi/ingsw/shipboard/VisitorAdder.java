package it.polimi.ingsw.shipboard;

import it.polimi.ingsw.components.Cannon;
import it.polimi.ingsw.components.Component;
import it.polimi.ingsw.components.Shield;
import javafx.concurrent.Worker;

public class VisitorAdder implements Visitor {
    @Override
    public void visit(Cannon cannon) {
        cannon.getFirePower();
    }
    @Override
    public void visit(Shield shield) {}
    @Override
    public void visit(Component component){}
}
