package it.polimi.ingsw.Controller.Cards;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class that represents a blow
 *
 * @author carlo
 */

public class Blow implements Serializable {

    private int direction, roll;
    private boolean big;

    /*
    The direction is as follows: 0 front, 1 right, 2 back, 3 left.
     */

    public Blow(Integer direction, boolean big) {

        this.direction = direction;
        this.big = big;
    }

    public int getDirection() {
        return direction;
    }

    public int getRoll() {
        return roll;
    }

    public boolean isBig() {
        return big;
    }

    /**
     * Get a real index between 1 and 11 (included).
     * (Dice roll can have values 2-12).
     */
    public void rollDice() {

        // Real index: (2-12) - 1
        roll = ThreadLocalRandom.current().nextInt(1, 12);

    }
}
