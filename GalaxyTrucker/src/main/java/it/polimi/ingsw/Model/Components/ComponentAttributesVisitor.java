package it.polimi.ingsw.Model.Components;

import java.util.List;

/**
 * Visits a component and returns the list of its attributes.
 */
public class ComponentAttributesVisitor implements ComponentVisitor<List<Object>, RuntimeException> {
    @Override
    public List<Object> visitAlienSupport(AlienSupport alienSupport) {
        return alienSupport.getAllIndexes();
    }

    @Override
    public List<Object> visitBattery(Battery battery) {
        return battery.getAllIndexes();
    }

    @Override
    public List<Object> visitCabin(Cabin cabin) {
        return cabin.getAllIndexes();
    }

    @Override
    public List<Object> visitCannon(Cannon cannon) {
        return cannon.getAllIndexes();
    }

    @Override
    public List<Object> visitComponent(Component component) {
        return component.getAllIndexes();
    }

    @Override
    public List<Object> visitEngine(Engine engine) {
        return engine.getAllIndexes();
    }

    @Override
    public List<Object> visitShield(Shield shield) {
        return shield.getAllIndexes();
    }


    @Override
    public List<Object> visitStorage(Storage storage) {
        return storage.getAllIndexes();
    }

}
