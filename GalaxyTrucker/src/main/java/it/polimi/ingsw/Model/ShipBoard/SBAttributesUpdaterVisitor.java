package it.polimi.ingsw.Model.ShipBoard;

import it.polimi.ingsw.Model.Components.*;

/**
 * Visits a component and calls the appropriate ShipBoardAttribute update method(s).
 *
 * @author Boti
 */
public class SBAttributesUpdaterVisitor implements ComponentVisitor {
    ShipBoardAttributes shipBoardAttributes;

    public SBAttributesUpdaterVisitor(ShipBoardAttributes shipBoardAttributes) {
        this.shipBoardAttributes = shipBoardAttributes;
    }

    @Override
    public Object visit(AlienSupport alienSupport) {
        return null;
    }

    @Override
    public Object visit(Battery battery) {
        shipBoardAttributes.updateRemainingBatteries();
        return null;
    }

    @Override
    public Object visit(Cabin cabin) {
        shipBoardAttributes.updateCrewMembers();
        shipBoardAttributes.updateAliens();
        return null;
    }

    @Override
    public Object visit(Cannon cannon) {
        shipBoardAttributes.updateCannonPower();
        return null;
    }

    @Override
    public Object visit(Component component) {
        return null;
    }

    @Override
    public Object visit(Engine engine) {
        shipBoardAttributes.updateEnginePower();
        return null;
    }

    @Override
    public Object visit(Shield shield) {
        shipBoardAttributes.updateCoveredSides();
        return null;
    }

    @Override
    public Object visit(Storage storage) {
        shipBoardAttributes.updateGoods();
        return null;
    }
}
