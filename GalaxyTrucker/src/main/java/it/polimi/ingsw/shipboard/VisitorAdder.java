package it.polimi.ingsw.shipboard;

import it.polimi.ingsw.components.Cannon;
import it.polimi.ingsw.components.Component;
import it.polimi.ingsw.components.Shield;
import javafx.concurrent.Worker;

import java.util.List;

public class VisitorAdder implements Visitor<List<Integer>> {
    @Override
    public List<Integer> visit(Cannon cannon) {
        return cannon.getAllIndexes();
    }
    @Override
    public List<Integer> visit(Shield shield) {
        return shield.getAllIndexes();
    }
    @Override
    public List<Integer> visit(Component component){
        return component.getAllIndexes();
    }
}
