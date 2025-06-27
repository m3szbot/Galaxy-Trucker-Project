package it.polimi.ingsw.Model.ShipBoard;

import java.util.Objects;

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

    /**
     * Override hashCode to maintain hash function coherency, by using fields instead of reference to calculate
     * hash code of the object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(col, row);
    }

    /**
     * Compare coordinates instead of references during equals.
     *
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        // reference to same object
        if (this == obj)
            return true;
        // different type
        if (obj == null || getClass() != obj.getClass())
            return false;

        Coordinate tmp = (Coordinate) obj;
        // compare coordinate fields
        return (this.col == tmp.getCol() && this.row == tmp.getRow());
    }

    public int getCol() {
        return this.col;
    }

    public int getRow() {
        return this.row;
    }
}
