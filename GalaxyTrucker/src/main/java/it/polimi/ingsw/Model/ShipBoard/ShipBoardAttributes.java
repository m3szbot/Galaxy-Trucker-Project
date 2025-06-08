package it.polimi.ingsw.Model.ShipBoard;

import it.polimi.ingsw.Model.Components.*;

import java.io.Serializable;

import static it.polimi.ingsw.Model.ShipBoard.ShipBoard.*;

/**
 * Class to manage the static attributes and dynamic inventory of a ShipBoard.
 *
 * @author Giacomo, Boti
 */
public class ShipBoardAttributes implements Serializable {
    // cannot have shipboard as attribute - circular reference
    // All attributes must be Serializable or transient

    // STATIC SHIP ATTRIBUTES
    // covered sides of the ship
    // [FRONT, RIGHT, BACK, LEFT]
    boolean[] coveredSides;
    // driving power of single engines
    private int singleEnginePower;
    // firepower of single cannons
    private float singleCannonPower;
    // DYNAMIC SHIP ATTRIBUTES, INVENTORY
    // available battery power
    private int remainingBatteries; // Ship's battery power
    // driving power of double engines (only available if battery is consumed!)
    private int doubleEnginePower;
    // number of forward facing double cannons
    private int numberForwardDoubleCannons;
    // number of lateral facing double cannons
    private int numberLateralDoubleCannons;
    // number of crew members
    private int crewMembers;
    // presence of a purple alien (only 1 permitted)
    private boolean purpleAlien;
    // presence of a brown alien (only 1 permitted)
    private boolean brownAlien;
    // number of goods
    // RED, YELLOW, GREEN, BLUE
    private int[] goods;
    // number of remaining red (RED) slots
    // (counted separately from blue slots)
    private int remainingRedSlots;
    // number of remaining blue (YELLOW, GREEN, BLUE) slots
    // (counted separately from red slots)
    private int remainingBlueSlots;
    // number of destroyed components
    private int destroyedComponents;
    // number of credits
    private int credits;

    /**
     * Constructor for ShipBoard.
     * Initializes the ship's structure and default values for all attributes.
     *
     * @author Giacomo, Boti
     */
    public ShipBoardAttributes(ShipBoard shipBoard) {
        coveredSides = new boolean[]{false, false, false, false};
        singleEnginePower = 0;
        singleCannonPower = 0;
        remainingBatteries = 0;
        doubleEnginePower = 0;
        numberForwardDoubleCannons = 0;
        numberLateralDoubleCannons = 0;
        crewMembers = 0;
        purpleAlien = false;
        brownAlien = false;
        goods = new int[]{0, 0, 0, 0};
        remainingRedSlots = 0;
        remainingBlueSlots = 0;
        destroyedComponents = 0;
        credits = 0;
    }

    public boolean[] getCoveredSides() {
        return coveredSides;
    }

    public int getSingleEnginePower() {
        return singleEnginePower;
    }

    public float getSingleCannonPower() {
        return singleCannonPower;
    }

    public int getRemainingBatteries() {
        return remainingBatteries;
    }

    public int getDoubleEnginePower() {
        return doubleEnginePower;
    }

    public int getNumberForwardDoubleCannons() {
        return numberForwardDoubleCannons;
    }

    public int getNumberLateralDoubleCannons() {
        return numberLateralDoubleCannons;
    }


    public int getCrewMembers() {
        return crewMembers;
    }

    public boolean getPurpleAlien() {
        return purpleAlien;
    }

    public boolean getBrownAlien() {
        return brownAlien;
    }

    /**
     * @return true if the given alien type is present, false otherwise (for human too).
     * @author Boti
     */
    public boolean getAlien(CrewType crewType) {
        if (crewType.equals(CrewType.Purple))
            return purpleAlien;
        if (crewType.equals(CrewType.Brown))
            return brownAlien;
        else
            return false;
    }

    public int[] getGoods() {
        return goods;
    }

    public int getRemainingRedSlots() {
        return remainingRedSlots;
    }

    public int getRemainingBlueSlots() {
        return remainingBlueSlots;
    }

    public int getDestroyedComponents() {
        return destroyedComponents;
    }

    public int getCredits() {
        return credits;
    }

    /**
     * Update every shipBoardAttribute attribute.
     * To use when shipBoard is fractured.
     * For normal updates use ComponentVisitor.
     */
    void updateShipBoardAttributes(ShipBoard shipBoard) throws NoHumanCrewLeftException {
        updateCoveredSides(shipBoard);
        updateCannons(shipBoard);
        updateEngines(shipBoard);
        updateRemainingBatteries(shipBoard);
        updateCabinsAlienSupports(shipBoard);
        updateGoods(shipBoard);
    }

    /**
     * Scan the shipboard for shields and update coveredSides.
     * Order: FRONT RIGHT BACK LEFT.
     *
     * @author Boti
     */
    void updateCoveredSides(ShipBoard shipBoard) {
        coveredSides = new boolean[]{false, false, false, false};
        for (int i = SB_FIRST_REAL_COL; i <= SB_COLS - SB_FIRST_REAL_COL; i++) {
            for (int j = SB_FIRST_REAL_ROW; j <= SB_ROWS - SB_FIRST_REAL_ROW; j++) {
                Component component = shipBoard.getComponentMatrix()[i][j];
                if (component instanceof Shield) {
                    // do OR on each covered side (add)
                    for (int k = 0; k < coveredSides.length; k++)
                        coveredSides[k] = coveredSides[k] || component.getCoveredSides()[k];
                }
            }
        }
    }

    /**
     * Scan the shipboard for cannons and update singleCannonPower, numberForwardDoubleCannons,
     * numberLateralDoubleCannons attributes.
     * Double cannons add cannonPower only if activated with batteries.
     * Does not account for aliens.
     *
     * @author Boti
     */
    void updateCannons(ShipBoard shipBoard) {
        singleCannonPower = 0;
        numberForwardDoubleCannons = 0;
        numberLateralDoubleCannons = 0;
        for (int i = SB_FIRST_REAL_COL; i <= SB_COLS - SB_FIRST_REAL_COL; i++) {
            for (int j = SB_FIRST_REAL_ROW; j <= SB_ROWS - SB_FIRST_REAL_ROW; j++) {
                Component component = shipBoard.getComponentMatrix()[i][j];
                if (component instanceof Cannon) {
                    if (component.isSingle())
                        singleCannonPower += component.getFirePower();
                    else {
                        // forward double cannon
                        if (component.getFront().equals(SideType.Special))
                            numberForwardDoubleCannons++;
                            // lateral double cannon
                        else
                            numberLateralDoubleCannons++;
                    }
                }
            }
        }
    }

    /**
     * Scan the shipboard for engines and update singleEnginePower and doubleEnginePower attributes.
     * Double engines add enginePower only if activated with batteries.
     * Does not account for aliens.
     *
     * @author Boti
     */
    void updateEngines(ShipBoard shipBoard) {
        singleEnginePower = 0;
        doubleEnginePower = 0;
        for (int i = SB_FIRST_REAL_COL; i <= SB_COLS - SB_FIRST_REAL_COL; i++) {
            for (int j = SB_FIRST_REAL_ROW; j <= SB_ROWS - SB_FIRST_REAL_ROW; j++) {
                Component component = shipBoard.getComponentMatrix()[i][j];
                if (component instanceof Engine) {
                    if (component.isSingle())
                        singleEnginePower += component.getDrivingPower();
                    else
                        doubleEnginePower += component.getDrivingPower();
                }
            }
        }
    }

    /**
     * Scan the shipboard for batteries and update remainingBatteries attributes.
     *
     * @author Boti
     */
    void updateRemainingBatteries(ShipBoard shipBoard) {
        remainingBatteries = 0;
        for (int i = SB_FIRST_REAL_COL; i <= SB_COLS - SB_FIRST_REAL_COL; i++) {
            for (int j = SB_FIRST_REAL_ROW; j <= SB_ROWS - SB_FIRST_REAL_ROW; j++) {
                Component component = shipBoard.getComponentMatrix()[i][j];
                if (component instanceof Battery) {
                    remainingBatteries += component.getBatteryPower();
                }
            }
        }
    }

    /**
     * Scans shipboard for Cabins and AlienSupports, and updates crewMembers, purpleAlien, brownAlien.
     * Checks for AlienSupports and removes aliens if no support is present.
     * 1 alien counts as 1 human.
     * If no human crew left, player is forced to give up.
     *
     * @throws NoHumanCrewLeftException if no human crew left and player forced to give up.
     * @author Boti
     */
    void updateCabinsAlienSupports(ShipBoard shipBoard) throws NoHumanCrewLeftException {
        // reset crew members, aliens
        crewMembers = 0;
        purpleAlien = false;
        brownAlien = false;
        // recount crew members, aliens
        for (int i = SB_FIRST_REAL_COL; i <= SB_COLS - SB_FIRST_REAL_COL; i++) {
            for (int j = SB_FIRST_REAL_ROW; j <= SB_ROWS - SB_FIRST_REAL_ROW; j++) {
                Component component = shipBoard.getComponentMatrix()[i][j];

                // if cabin with crew >0 (not empty)
                if ((component instanceof Cabin) && (component.getCrewMembers() > 0)) {
                    // check purple aliens
                    if (((Cabin) component).getCrewType().equals(CrewType.Purple)) {
                        // purple alien support present
                        if (shipBoard.checkForAlienSupport(i, j, CrewType.Purple))
                            purpleAlien = true;

                            // remove alien from cabin if no support is present
                        else
                            ((Cabin) component).setNumberOfCurrentInhabitants(0);

                        // check brown aliens
                    } else if (((Cabin) component).getCrewType().equals(CrewType.Brown)) {
                        // browns alien support present
                        if (shipBoard.checkForAlienSupport(i, j, CrewType.Brown))
                            brownAlien = true;

                            // remove alien from cabin if no support is present
                        else
                            ((Cabin) component).setNumberOfCurrentInhabitants(0);
                    }
                    // update crew count
                    crewMembers += component.getCrewMembers();
                }
            }
        }

        // no human crew left: forced to give up
        // throw exception
        if (getHumanCrewMembers() == 0) {
            throw new NoHumanCrewLeftException();
        }

    }

    /**
     * @return the number of human crew members on the shipboard.
     * @author Boti
     */
    public int getHumanCrewMembers() {
        return (crewMembers - (purpleAlien ? 1 : 0) - (brownAlien ? 1 : 0));
    }

    /**
     * Scan the shipboard for goods in storages and update goods, remainingRedSlots, remainingBlueSlots.
     * Order: RED YELLOW GREEN BLUE.
     * remainingRedSlots: only RED slots.
     * remainingBlueSlots: YELLOW, GREEN, BLUE slots.
     *
     * @author Boti
     */
    void updateGoods(ShipBoard shipBoard) {
        goods = new int[]{0, 0, 0, 0};
        remainingRedSlots = 0;
        remainingBlueSlots = 0;
        for (int i = SB_FIRST_REAL_COL; i <= SB_COLS - SB_FIRST_REAL_COL; i++) {
            for (int j = SB_FIRST_REAL_ROW; j <= SB_ROWS - SB_FIRST_REAL_ROW; j++) {
                Component component = shipBoard.getComponentMatrix()[i][j];
                if (component instanceof Storage) {
                    // update goods
                    int[] toAdd = ((Storage) component).getGoods();
                    for (int k = 0; k < goods.length; k++)
                        this.goods[k] += toAdd[k];

                    // update remaining slots
                    remainingRedSlots += component.getAvailableRedSlots();
                    remainingBlueSlots += component.getAvailableBlueSlots();
                }
            }
        }
    }

    /**
     * Adds the given number of destroyed components to the destroyed component counter.
     */
    void destroyComponents(int count) {
        if (count < 0)
            throw new IllegalArgumentException("Cannot destroy negative number of components");
        destroyedComponents += count;
    }

    public void addCredits(int credits) {
        if (credits < 0)
            throw new IllegalArgumentException("Cannot add negative credits.");
        this.credits += credits;
    }

    public void removeCredits(int credits) {
        if (credits < 0)
            throw new IllegalArgumentException("Cannot remove negative credits");
        this.credits -= credits;
        // cannot have negative credits
        if (this.credits < 0)
            this.credits = 0;
    }


    /**
     * Checks if the given side is protected by a shield (considering the remaining batteries).
     * Does not consume the necessary batteries.
     * Sides: 0 FRONT, 1 RIGHT, 2 BACK, 3 LEFT.
     *
     * @return True if protected, false if unprotected.
     */
    public boolean checkSideShieldProtected(int side) {
        if (side < 0 || side >= coveredSides.length)
            throw new IllegalArgumentException("Invalid side entered (0-3 is valid)");
        // no batteries remaining
        if (remainingBatteries <= 0)
            return false;
        // check shield coverage
        return coveredSides[side];
    }


}

