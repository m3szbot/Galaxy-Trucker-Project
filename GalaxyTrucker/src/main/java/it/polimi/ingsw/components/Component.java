package it.polimi.ingsw.components;

import it.polimi.ingsw.shipboard.Visitor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Superclass to express what all components have in common
 * SideType[0] corresponds to the front side, then rotate clockwise.
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
    public int getDrivingPower(){
        return 0;
    }

    private int getFirePower(){
        return 0;
    }

    private int getCrewMembers(){
        return 0;
    }

    private int getBatteryPower(){
        return 0;
    }

    private boolean[] getCoveredSides(){
        return new boolean[]{false, false, false, false};
    }

    private int getAvailableRedSlots(){
        return 0;
    }

    private int getAvailableBlueSlots(){
        return 0;
    }

    public List<Integer> getAllIndexes(){
        ArrayList<Integer> indexes = new ArrayList<>();
        indexes.add(getDrivingPower());
        indexes.add(getFirePower());
        indexes.add(getCrewMembers());
        indexes.add(getBatteryPower());
        //mancano indici
        return indexes;
    }



    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    };

}
