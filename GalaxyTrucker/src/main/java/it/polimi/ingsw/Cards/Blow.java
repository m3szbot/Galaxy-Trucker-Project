package it.polimi.ingsw.Cards;

/**
 * Class that represents a blow
 *
 * @author carlo
 */

public class Blow {

    int direction;
    boolean big;

    /*
    The direction is as follows: 0 front, 1 right, 2 back, 3 left.
     */

    public Blow(int direction, boolean big) {

        this.direction = direction;
        this.big = big;
    }

    public int getDirection() {
        return direction;
    }

    public boolean isBig() {
        return big;
    }
}
