package it.polimi.ingsw.Model.ShipBoard;

import it.polimi.ingsw.Model.Components.*;

/**
 * Visits a component and calls the appropriate ShipBoardAttribute update method(s).
 *
 * @author Boti
 */
public class SBAttributesUpdaterVisitor implements ComponentVisitor {
    ShipBoard shipBoard;

    public SBAttributesUpdaterVisitor(ShipBoard shipBoard) {
        this.shipBoard = shipBoard;
    }

    // Void is used to signal no returned type when using Generics
    // must return null

    @Override
    public Void visitAlienSupport(AlienSupport alienSupport) {
        shipBoard.getShipBoardAttributes().updateCabinsAlienSupports(shipBoard);
        return null;
    }

    @Override
    public Void visitBattery(Battery battery) {
        shipBoard.getShipBoardAttributes().updateRemainingBatteries(shipBoard);
        return null;
    }

    @Override
    public Void visitCabin(Cabin cabin) {
        shipBoard.getShipBoardAttributes().updateCabinsAlienSupports(shipBoard);
        return null;
    }

    @Override
    public Void visitCannon(Cannon cannon) {
        shipBoard.getShipBoardAttributes().updateCannons(shipBoard);
        return null;
    }

    @Override
    public Void visitComponent(Component component) {
        return null;
    }

    @Override
    public Void visitEngine(Engine engine) {
        shipBoard.getShipBoardAttributes().updateEngines(shipBoard);
        return null;
    }

    @Override
    public Void visitShield(Shield shield) {
        shipBoard.getShipBoardAttributes().updateCoveredSides(shipBoard);
        return null;
    }

    @Override
    public Void visitStorage(Storage storage) {
        shipBoard.getShipBoardAttributes().updateGoods(shipBoard);
        return null;
    }
}
