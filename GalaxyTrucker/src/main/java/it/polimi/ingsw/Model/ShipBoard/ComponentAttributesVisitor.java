package it.polimi.ingsw.Model.ShipBoard;

import it.polimi.ingsw.Model.Components.*;

import java.util.List;

/**
 * Visits a component and returns the list of its attributes.
 */
public class ComponentAttributesVisitor implements ComponentVisitor<List<Object>> {
    @Override
    public List<Object> visit(Cannon cannon) {
        return cannon.getAllIndexes();
    }

    @Override
    public List<Object> visit(Shield shield) {
        return shield.getAllIndexes();
    }

    @Override
    public List<Object> visit(Component component) {
        return component.getAllIndexes();
    }

    @Override
    public List<Object> visit(AlienSupport alienSupport) {
        return alienSupport.getAllIndexes();
    }

    @Override
    public List<Object> visit(Battery battery) {
        return battery.getAllIndexes();
    }

    @Override
    public List<Object> visit(Cabin cabin) {
        return cabin.getAllIndexes();
    }

    @Override
    public List<Object> visit(Engine engine) {
        return engine.getAllIndexes();
    }

    @Override
    public List<Object> visit(Storage storage) {
        return storage.getAllIndexes();
    }

}
