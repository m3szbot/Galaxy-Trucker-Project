package it.polimi.ingsw.shipboard;

import it.polimi.ingsw.components.Cannon;
import it.polimi.ingsw.components.Component;
import it.polimi.ingsw.components.Shield;

import java.util.List;

public class VisitorAdder implements Visitor<List<Object>> {
    @Override
    public List<Object> visit(Cannon cannon) {
        return cannon.getAllIndexes();
    }
    @Override
    public List<Object> visit(Shield shield) {
        return shield.getAllIndexes();
    }
    @Override
    public List<Object> visit(Component component){
        return component.getAllIndexes();
    }
}
