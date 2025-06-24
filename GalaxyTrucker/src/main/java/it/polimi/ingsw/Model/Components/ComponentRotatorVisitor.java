package it.polimi.ingsw.Model.Components;

/**
 * Visitor class that rotates the components.
 *
 * @author Boti
 */
public class ComponentRotatorVisitor implements ComponentVisitor<Void, RuntimeException> {

    @Override
    public Void visitAlienSupport(AlienSupport alienSupport) throws RuntimeException {
        rotateSides(alienSupport);
        return null;
    }

    @Override
    public Void visitBattery(Battery battery) throws RuntimeException {
        rotateSides(battery);
        return null;
    }

    @Override
    public Void visitCabin(Cabin cabin) throws RuntimeException {
        rotateSides(cabin);
        return null;
    }

    @Override
    public Void visitCannon(Cannon cannon) throws RuntimeException {
        rotateSides(cannon);
        return null;
    }

    @Override
    public Void visitComponent(Component component) throws RuntimeException {
        rotateSides(component);
        return null;
    }

    @Override
    public Void visitEngine(Engine engine) throws RuntimeException {
        rotateSides(engine);
        return null;
    }

    @Override
    public Void visitShield(Shield shield) throws RuntimeException {
        rotateSides(shield);
        rotateCoveredSides(shield);
        return null;
    }

    @Override
    public Void visitStorage(Storage storage) throws RuntimeException {
        rotateSides(storage);
        return null;
    }

    private void rotateCoveredSides(Shield shield) {
        // shift coveredSides array to the right
        boolean[] sidesCopy = shield.getCoveredSides();
        boolean last = sidesCopy[sidesCopy.length - 1];

        // array indexed from 0 to length-1
        for (int i = sidesCopy.length - 1; i > 0; i--)
            sidesCopy[i] = sidesCopy[i - 1];

        sidesCopy[0] = last;

        shield.setCoveredSides(sidesCopy);
    }

    /**
     * Rotates the side of the passed component 90 degrees to the right.
     */
    private void rotateSides(Component component) {
        component.incrementRotations();
        // shift SideType array to the right
        SideType[] sidesCopy = component.getAllSides();
        SideType last = sidesCopy[sidesCopy.length - 1];

        // array indexed from 0 to length-1
        for (int i = sidesCopy.length - 1; i > 0; i--) {
            sidesCopy[i] = sidesCopy[i - 1];
        }
        sidesCopy[0] = last;

        component.setSides(sidesCopy);
    }
}
