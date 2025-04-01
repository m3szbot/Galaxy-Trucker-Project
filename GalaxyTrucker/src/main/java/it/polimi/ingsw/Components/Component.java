package it.polimi.ingsw.Components;

import it.polimi.ingsw.Shipboard.Visitor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Superclass to express what all components have in common
 * SideType[0] corresponds to the front side, then rotate clockwise.
 *
 * @author carlo
 */

public class Component implements Visitable {

    private SideType front;
    private SideType right;
    private SideType back;
    private SideType left;

    public Component(SideType[] sides) {
        this.front = sides[0];
        this.right = sides[1];
        this.back = sides[2];
        this.left = sides[3];
    }

    /**
     * Rotates the component clockwise
     */

    public void rotate() {

        SideType temp1, temp2;

        temp1 = front;
        temp2 = right;

        front = left;
        right = temp1;

        temp1 = back;
        back = temp2;
        left = temp1;
    }

    public SideType getRight() {
        return right;
    }

    public SideType getLeft() {
        return left;
    }

    public SideType getFront() {
        return front;
    }

    public SideType getBack() {
        return back;
    }

    public String getComponentName() {
        return "Connector";
    }

    //Da qui in poi codice aggiunto da Giacomo per provare ad implementare il visitor pattern
    public int getDrivingPower() {
        return 0;
    }

    public float getFirePower() {
        return 0;
    }

    public int getCrewMembers() {
        return 0;
    }

    public int getBatteryPower() {
        return 0;
    }

    public int[] getCoveredSides() {
        return new int[]{0, 0, 0, 0};
    }

    public int getAvailableRedSlots() {
        return 0;
    }

    public int getAvailableBlueSlots() {
        return 0;
    }

    public boolean amIASupport() {
        return false;
    }

    public List<Object> getAllIndexes() {
        ArrayList<Object> indexes = new ArrayList<>();
        indexes.add(getDrivingPower());
        indexes.add(getFirePower());
        indexes.add(getCrewMembers());
        indexes.add(getBatteryPower());
        indexes.add(getCoveredSides());
        indexes.add(getAvailableRedSlots());
        indexes.add(getAvailableBlueSlots());
        indexes.add(amIASupport());
        return indexes;
    }


    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    ;

}
