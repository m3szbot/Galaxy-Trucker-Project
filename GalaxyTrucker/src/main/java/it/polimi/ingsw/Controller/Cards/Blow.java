package it.polimi.ingsw.Controller.Cards;

import java.io.Serializable;

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

    public void rollDice() {

        //Real index
        roll = (int) ((Math.random() * 11) + 1);

    }
}
