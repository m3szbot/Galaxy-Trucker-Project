
package it.polimi.ingsw.Shipboard;

import it.polimi.ingsw.Components.*;

public class ShipBoardAttributes {
    // Represents the structure of the ship
    private int drivingPower; // The ship's driving power
    private float firePower; // The ship's firepower
    private int crewMembers; // Number of crew members onboard
    private boolean purpleAlien; // Presence of a purple alien crew member
    private boolean brownAlien;  // Presence of a brown alien crew member
    private int batteryPower; // Ship's battery power
    private Integer[] coveredSides; // Indicates which sides of the ship are covered
    private int availableRedSlots; // Number of available red slots
    private int availableBlueSlots; // Number of available blue slots
    private int destroyedComponents; // Number of destroyed components
    private int credits;
    private int goods[];
    private int numberForwardDoubleCannons;
    private int numberNotForwardDoubleCannons;
    private int numberDoubleEngines;

    /**
     * Constructor for ShipBoard.
     * Initializes the ship's structure and default values for all attributes.
     *
     * @author Giacomo
     */
    public ShipBoardAttributes() {
        drivingPower = 0;
        firePower = 0;
        crewMembers = 0;
        purpleAlien = false;
        brownAlien = false;
        batteryPower = 0;
        coveredSides = new Integer[]{0, 0, 0, 0};
        availableRedSlots = 0;
        availableBlueSlots = 0;
        destroyedComponents = 0;
        credits = 0;
        goods = new int[]{0, 0, 0, 0};
        numberForwardDoubleCannons = 0;
        numberNotForwardDoubleCannons = 0;
        numberDoubleEngines = 0;
    }

    //metodo che si chiama solo una volta all'inizio e inizializza red slot, firepower, driving power e crew

    /**
     * Updates the firepower of the ship.
     *
     * @param value Amount to increase the firepower by.
     * @author Giacomo
     */
    public void updateFirePower(float value) {
        this.firePower = this.firePower + value;
    }

    /**
     * Updates the ship's driving power.
     *
     * @param value Amount to increase the driving power by.
     * @author Giacomo
     */
    public void updateDrivingPower(int value) {
        this.drivingPower = this.drivingPower + value;
    }

    /**
     * Updates the number of crew members onboard.
     *
     * @param value Number of crew members to add.
     * @author Giacomo
     */
    public void updateCrewMembers(int value) {
        this.crewMembers = this.crewMembers + value;
    }

    // 0 no tipo, 1 viola, 2 marrone

    /**
     * Updates the presence of an alien crew member.
     *
     * @param crewType The type of alien crew member (purple or brown).
     * @author Giacomo
     */
    public void updateAlien(CrewType crewType) {
        if (crewType == CrewType.Purple) {
            this.purpleAlien = true;
        } else if (crewType == CrewType.Brown) {
            this.brownAlien = true;
        }
    }

    /**
     * Updates the battery power of the ship.
     *
     * @param value Amount to decrease the battery power by.
     * @author Giacomo
     */
    public void updateBatteryPower(int value) {this.batteryPower = this.batteryPower - value;}

    /**
     * Updates the coverage status of a specific side of the ship.
     *
     * @param side  The side to update (index 0-3).
     *              The first Side (0) is front, the other indexes follow the clockwise.
     * @param cover True if the side is covered, false otherwise.
     * @author Giacomo
     */
    public void updateCoveredSides(int side, boolean cover, boolean type) {
        if(cover && type ){
            this.coveredSides[side]++;
        }
        else if(cover && !type ){
            this.coveredSides[side]--;
        }
    }

    // 1 red, 0 blue

    /**
     * Updates the number of available red or blue slots.
     *
     * @param type  1 for red slots, 0 for blue slots.
     * @param slots Number of slots to add.
     * @author Giacomo
     */
    public void updateAvailableSlots(int type, int slots) {
        if (type == 1) {
            this.availableRedSlots = this.availableRedSlots + slots;
        } else {
            this.availableBlueSlots = this.availableBlueSlots + slots;
        }
    }

    /**
     * Updates the count of destroyed components on the ship.
     *
     * @param components Number of components destroyed.
     * @author Giacomo
     */
    public void updateDestroyedComponents(int components) {
        this.destroyedComponents = this.destroyedComponents + components;
    }

    /**
     * Returns the current driving power of the ship.
     *
     * @return The driving power.
     * @author Giacomo
     */
    public int getDrivingPower() {
        return drivingPower;
    }

    /**
     * Returns the current firepower of the ship.
     *
     * @return The firepower.
     * @author Giacomo
     */
    public float getFirePower() {
        return firePower;
    }

    /**
     * Returns the number of crew members onboard.
     *
     * @return The crew member count.
     * @author Giacomo
     */
    public int getCrewMembers() {
        return crewMembers;
    }

    /**
     * Returns the type of alien crew member present on the ship.
     *
     * @return 1 if a purple alien is present, 2 if a brown alien is present, 0 if none.
     * @author Giacomo
     */
    public int getAlienType() {
        if (purpleAlien && brownAlien) {
            return 1;
        } else if (purpleAlien == true) {
            return 2;
        }
        else if(brownAlien == true){
            return 3;
        }
        return 0;
    }

    /**
     * Returns the current battery power of the ship.
     *
     * @return The battery power level.
     * @author Giacomo
     */
    public int getBatteryPower() {
        return batteryPower;
    }

    /**
     * Checks if a specific side of the ship is covered.
     *
     * @param side The side to check (index 0-3).
     * @return True if the side is covered, false otherwise.
     * @author Giacomo
     */
    public boolean checkSide(int side) {
        if (coveredSides[side] > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the number of available red slots.
     *
     * @return The number of available red slots.
     * @author Giacomo
     */
    public int getAvailableRedSlots() {
        return availableRedSlots;
    }

    /**
     * Returns the number of available blue slots.
     *
     * @return The number of available blue slots.
     * @author Giacomo
     */
    public int getAvailableBlueSlots() {
        return availableBlueSlots;
    }

    /**
     * Returns the number of destroyed components on the ship.
     *
     * @return The number of destroyed components.
     * @author Giacomo
     */
    public int getDestroyedComponents() {
        return destroyedComponents;
    }

/*IL SEGUENTE CONTROLLO SUGLI SLOT TOTALI DISPONIBILI VIENE SPOSTATO NEL CONTROLLORE, E VIENE CREATO UN METODO CHECKSLOTS IN SHIPSTRUCTURE PER CONTROLLARE QUESTA ROBA
    /**
     * Checks if the ship has enough slots available to store specified goods.
     * @param goods An array representing different types of goods.
     * @param x The x-coordinate where the goods are placed.
     * @param y The y-coordinate where the goods are placed.
     * @return True if there are enough slots available, false otherwise.
     * @author Giacomo

    public boolean checkSlots(int[] goods, int x, int y){
        boolean flag = true;
        if(goods[0] != 0){
            if(getAvailableRedSlots() < goods[0]){
                flag = false;
            }
        }
        if (goods[1] != 0 || goods[2] != 0 || goods[3] != 0){
            if(getAvailableBlueSlots() < goods[1]+goods[2]+goods[3]){
                flag = false;
            }
        }
        if(flag){
            shipStructure.addGoods(x, y, goods);
        }
        return flag;
    }
*/

    public int getCredits() {
        return credits;
    }

    public void updateCredits(int credits) {
        this.credits = this.credits + credits;
    }

    /**
     * @return Goods[4] array
     * @author Boti
     */
    public int[] getGoods() {
        return this.goods;
    }

    public int getNumberForwardDoubleCannons() {
        return numberForwardDoubleCannons;
    }

    public int getNumberNotForwardDoubleCannons() {
        return numberNotForwardDoubleCannons;
    }

    public int getNumberDoubleEngines() {
        return numberDoubleEngines;
    }

    public void updateNumberForwardDoubleCannons(int number) {
        this.numberForwardDoubleCannons = this.numberForwardDoubleCannons +  number;
    }
    public void updateNumberNotForwardDoubleCannons(int number) {
        this.numberNotForwardDoubleCannons = this.numberNotForwardDoubleCannons + number;
    }
    public void updateNumberDoubleEngines(int number) {
        this.numberDoubleEngines = this.numberDoubleEngines + number;
    }

}

