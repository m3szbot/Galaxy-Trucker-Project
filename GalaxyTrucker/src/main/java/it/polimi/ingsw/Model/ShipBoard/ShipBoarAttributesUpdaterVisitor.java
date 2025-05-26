package it.polimi.ingsw.Model.ShipBoard;

import it.polimi.ingsw.Model.Components.*;

/**
 * Visits a component and calls the appropriate ShipBoardAttribute updater method.
 *
 * @author Boti
 */
public class ShipBoarAttributesUpdaterVisitor implements ComponentVisitor {

    @Override
    public Object visit(AlienSupport alienSupport) {
        return null;
    }

    @Override
    public Object visit(Battery battery) {
        return null;
    }

    @Override
    public Object visit(Cabin cabin) {
        return null;
    }

    @Override
    public Object visit(Cannon cannon) {
        return null;
    }

    @Override
    public Object visit(Component component) {
        return null;
    }

    @Override
    public Object visit(Engine engine) {
        return null;
    }

    @Override
    public Object visit(Shield shield) {
        return null;
    }

    @Override
    public Object visit(Storage storage) {
        return null;
    }
}
