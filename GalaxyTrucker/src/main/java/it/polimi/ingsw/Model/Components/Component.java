package it.polimi.ingsw.Model.Components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Superclass to express what all components have in common
 * SideType[0] corresponds to the front side, then rotate clockwise.
 *
 * @author carlo
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "name",
        defaultImpl = Component.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AlienSupport.class, name = "AlienSupport"),
        @JsonSubTypes.Type(value = Battery.class, name = "Battery"),
        @JsonSubTypes.Type(value = Cabin.class, name = "Cabin"),
        @JsonSubTypes.Type(value = Cannon.class, name = "Cannon"),
        @JsonSubTypes.Type(value = Component.class, name = "Component"),
        @JsonSubTypes.Type(value = Engine.class, name = "Engine"),
        @JsonSubTypes.Type(value = Shield.class, name = "Shield"),
        @JsonSubTypes.Type(value = Storage.class, name = "Storage"),
})

public class Component implements Visitable, Serializable {

    private SideType front;
    private SideType right;
    private SideType back;
    private SideType left;


    public Component() {
    }

    @JsonCreator
    public Component(@JsonProperty("sides") SideType[] sides) {
        this.front = sides[0];
        this.right = sides[1];
        this.back = sides[2];
        this.left = sides[3];
    }

    public void setSides(SideType[] sides) {
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

    /**
     * Return all possible component indexes/attributes:
     * 0: drivingPower
     * 1: firePower
     * 2: crewMembers
     * 3: batteryPower
     * 4: coveredSides
     * 5: availableRedSlots
     * 6: availableBlueSlots
     * 7: isSingle
     */
    public List<Object> getAllIndexes() {
        ArrayList<Object> indexes = new ArrayList<>();
        indexes.add(getDrivingPower()); //0
        indexes.add(getFirePower()); //1
        indexes.add(getCrewMembers()); //2
        indexes.add(getBatteryPower()); //3
        indexes.add(getCoveredSides()); //4
        indexes.add(getAvailableRedSlots()); //5
        indexes.add(getAvailableBlueSlots()); //6
        indexes.add(isSingle()); // 7
        return indexes;
    }

    // attributes getter for non visitor
    // must use explicit type casting
    // (ComponentType) component.get()
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

    public boolean[] getCoveredSides() {
        return new boolean[]{false, false, false, false};
    }

    public int getAvailableRedSlots() {
        return 0;
    }

    public int getAvailableBlueSlots() {
        return 0;
    }

    public boolean isSingle() {
        return true;
    }


    /**
     * Return drivingPower of component using visitor pattern.
     * (ComponentAttributesVisitor, SBAttributesUpdaterVisitor etc...)
     */
    public int getDrivingPower(ComponentAttributesVisitor visitor) {
        return (Integer) this.accept(visitor).get(0);
    }

    /**
     * Accept any kind of visitor implementing the ComponentVisitor interface.
     */
    @Override
    public <T> T accept(ComponentVisitor<T> componentVisitor) {
        return componentVisitor.visitComponent(this);
    }

    /**
     * Return firePower of component using visitor pattern.
     */
    public float getFirePower(ComponentAttributesVisitor visitor) {
        return (Float) this.accept(visitor).get(1);
    }

    /**
     * Return crewMembers of component using visitor pattern.
     */
    public int getCrewMembers(ComponentAttributesVisitor visitor) {
        return (Integer) this.accept(visitor).get(2);
    }

    /**
     * Return batteryPower of component using visitor pattern.
     */
    public int getBatteryPower(ComponentAttributesVisitor visitor) {
        return (Integer) this.accept(visitor).get(3);
    }


    /**
     * Return coveredSides of component using visitor pattern.
     */
    public boolean[] getCoveredSides(ComponentAttributesVisitor visitor) {
        return (boolean[]) this.accept(visitor).get(4);
    }

    /**
     * Return availableRedSlots of component using visitor pattern.
     */
    public int getAvailableRedSlots(ComponentAttributesVisitor visitor) {
        return (Integer) this.accept(visitor).get(5);
    }

    /**
     * Return availableBlueSlots of component using visitor pattern.
     */
    public int getAvailableBlueSlots(ComponentAttributesVisitor visitor) {
        return (Integer) this.accept(visitor).get(6);
    }

    /**
     * Return true if the component is single, false if double.
     */
    public boolean isSingle(ComponentAttributesVisitor visitor) {
        return (boolean) this.accept(visitor).get(7);
    }


}
