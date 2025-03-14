package it.polimi.ingsw.components;


/**
 * Superclass to express what all components have in common
 * SideType[0] corresponds to the front side, then rotate clockwise.
 * @author carlo
 */

public class Component {

    private SideType front;
    private SideType right;
    private SideType back;
    private SideType left;

    public Component(SideType[] sides){
        this.front = sides[0];
        this.right = sides[1];
        this.back = sides[2];
        this.left = sides[3];

    }

    /**
     * Rotates the component clockwise
     */

    public void rotate(){

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

    public String getComponentName(){
        return "Connector";
    }
}
