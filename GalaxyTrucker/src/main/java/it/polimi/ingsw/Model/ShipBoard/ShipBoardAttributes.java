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
    ShipBoard shipBoard;
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
    // firepower of double cannons (only available if battery is consumed!)
    private float doubleCannonPower;
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
    private int remainingRedSlots;
    // number of remaining blue (YELLOW, GREEN, BLUE) slots
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
    public ShipBoardAttributes() {
        coveredSides = new boolean[]{false, false, false, false};
        singleEnginePower = 0;
        singleCannonPower = 0;
        remainingBatteries = 0;
        doubleEnginePower = 0;
        doubleCannonPower = 0;
        crewMembers = 0;
        purpleAlien = false;
        brownAlien = false;
        goods = new int[]{0, 0, 0, 0};
        remainingRedSlots = 0;
        remainingBlueSlots = 0;
        destroyedComponents = 0;
        credits = 0;
    }

    public ShipBoard getShipBoard() {
        return shipBoard;
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

    public float getDoubleCannonPower() {
        return doubleCannonPower;
    }

    public int getCrewMembers() {
        return crewMembers;
    }

    public boolean isPurpleAlien() {
        return purpleAlien;
    }

    public boolean isBrownAlien() {
        return brownAlien;
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
     * TODO
     * To reorganize into visitor based on component type?
     * Scan matrix, each component updates based on type.
     */
    void updateShipBoardAttributes() {
        updateCoveredSides();
        updateCannonPower();
        updateEnginePower();
        updateRemainingBatteries();
        updateCrewMembers();
        updateAliens();
        updateGoods();
    }

    /**
     * Scan the shipboard for shields and update coveredSides.
     * Order: FRONT RIGHT BACK LEFT
     *
     * @author Boti
     */
    void updateCoveredSides() {
        coveredSides = new boolean[]{false, false, false, false};
        for (int i = SB_FIRST_REAL_COL; i <= SB_COLS - SB_FIRST_REAL_COL; i++) {
            for (int j = SB_FIRST_REAL_ROW; j <= SB_ROWS - SB_FIRST_REAL_ROW; j++) {
                Component component = shipBoard.getComponentMatrix()[i][j];
                if (component instanceof Shield) {
                    coveredSides = component.getCoveredSides();
                }
            }
        }
    }

    /**
     * Scan the shipboard for cannons and update singleCannonPower and doubleCannonPower attributes.
     * Does not account for aliens.
     *
     * @author Boti
     */
    void updateCannonPower() {
        singleCannonPower = 0;
        doubleCannonPower = 0;
        for (int i = SB_FIRST_REAL_COL; i <= SB_COLS - SB_FIRST_REAL_COL; i++) {
            for (int j = SB_FIRST_REAL_ROW; j <= SB_ROWS - SB_FIRST_REAL_ROW; j++) {
                Component component = shipBoard.getComponentMatrix()[i][j];
                if (component instanceof Cannon) {
                    if (component.isSingle())
                        singleCannonPower += component.getFirePower();
                    else
                        doubleCannonPower += component.getFirePower();
                }
            }
        }
    }

    /**
     * Scan the shipboard for engines and update singleEnginePower and doubleEnginePower attributes.
     * Does not account for aliens.
     *
     * @author Boti
     */
    void updateEnginePower() {
        singleEnginePower = 0;
        doubleEnginePower = 0;
        for (int i = SB_FIRST_REAL_COL; i <= SB_COLS - SB_FIRST_REAL_COL; i++) {
            for (int j = SB_FIRST_REAL_ROW; j <= SB_ROWS - SB_FIRST_REAL_ROW; j++) {
                Component component = shipBoard.getComponentMatrix()[i][j];
                if (component instanceof Engine) {
                    if (component.isSingle())
                        singleCannonPower += component.getDrivingPower();
                    else
                        doubleCannonPower += component.getDrivingPower();
                }
            }
        }
    }

    /**
     * Scan the shipboard for batteries and update remainingBatteries attributes.
     *
     * @author Boti
     */
    void updateRemainingBatteries() {
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
     * Scan the shipboard for cabins and update crewMembers attributes.
     * 1 alien counts as 1 human.
     *
     * @author Boti
     */
    void updateCrewMembers() {
        crewMembers = 0;
        for (int i = SB_FIRST_REAL_COL; i <= SB_COLS - SB_FIRST_REAL_COL; i++) {
            for (int j = SB_FIRST_REAL_ROW; j <= SB_ROWS - SB_FIRST_REAL_ROW; j++) {
                Component component = shipBoard.getComponentMatrix()[i][j];
                if (component instanceof Cabin) {
                    crewMembers += component.getCrewMembers();
                }
            }
        }
    }

    /**
     * Scan the shipboard for purple and brown aliens and update purpleAlien, brownAlien,
     * singleCannonPower, singleEnginePower attributes.
     * Cannon power, engine power must be updated before calling.
     * <p>
     * 1 purple alien adds +2 cannon strength if cannon power is >0.
     * 1 brown alien adds +2 engine strength if engine power is >0.
     *
     * @author Boti
     */
    void updateAliens() {
        purpleAlien = false;
        brownAlien = false;
        for (int i = SB_FIRST_REAL_COL; i <= SB_COLS - SB_FIRST_REAL_COL; i++) {
            for (int j = SB_FIRST_REAL_ROW; j <= SB_ROWS - SB_FIRST_REAL_ROW; j++) {
                Component component = shipBoard.getComponentMatrix()[i][j];
                if (component instanceof Cabin) {
                    if (((Cabin) component).getCrewType().equals(CrewType.Purple)) {
                        purpleAlien = true;
                        if (singleCannonPower > 0)
                            singleCannonPower += 2;
                    } else if (((Cabin) component).getCrewType().equals(CrewType.Brown)) {
                        brownAlien = true;
                        if (singleEnginePower > 0)
                            singleEnginePower += 2;
                    }
                }
            }
        }
    }

    /**
     * Scan the shipboard for goods in storages and update goods, remainingRedSlots, remainingBlueSlots.
     * Order: RED YELLOW GREEN BLUE
     * remainingRedSlots: only RED slots
     * remainingBlueSlots: YELLOW, GREEN, BLUE slots
     *
     * @author Boti
     */
    void updateGoods() {
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
        destroyedComponents++;
    }


}

