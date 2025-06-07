package it.polimi.ingsw.Model.ShipBoard;

/**
 * Class to store coordinates.
 *
 * @author Boti
 */

public class Coordinate {
    private int col, row;

    public Coordinate(int col, int row) {
        this.col = col;
        this.row = row;
    }

    public Coordinate(int[] coordinates) {
        this.col = coordinates[0];
        this.row = coordinates[1];
    }

    public int getCol() {
        return this.col;
    }

    public int getRow() {
        return this.row;
    }
}
