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

    // Void is used to signal no returned type when using Generics
    // must return null

    @Override
    public Void visitAlienSupport(AlienSupport alienSupport) {
        return null;
    }

    @Override
    public Void visitBattery(Battery battery) {
        shipBoardAttributes.updateRemainingBatteries();
        return null;
    }

    @Override
    public Void visitCabin(Cabin cabin) {
        shipBoardAttributes.updateCrewMembers();
        shipBoardAttributes.updateAliens();
        return null;
    }

    @Override
    public Void visitCannon(Cannon cannon) {
        shipBoardAttributes.updateCannons();
        return null;
    }

    @Override
    public Void visitComponent(Component component) {
        return null;
    }

    @Override
    public Void visitEngine(Engine engine) {
        shipBoardAttributes.updateEngines();
        return null;
    }

    @Override
    public Void visitShield(Shield shield) {
        shipBoardAttributes.updateCoveredSides();
        return null;
    }

    @Override
    public Void visitStorage(Storage storage) {
        shipBoardAttributes.updateGoods();
        return null;
    }
}
