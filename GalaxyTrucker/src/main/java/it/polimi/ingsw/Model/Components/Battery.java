package it.polimi.ingsw.Model.Components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class that represent a battery
 *
 * @author carlo
 */

public class Battery extends Component {

    private int numberOfCurrentBatteries;

    public Battery() {
    }

    @JsonCreator
    public Battery(@JsonProperty("sides") SideType[] sides, @JsonProperty("numberOfCurrentBatteries") int numberOfCurrentBatteries) {
        super(sides);
        this.numberOfCurrentBatteries = numberOfCurrentBatteries;
    }

    @JsonCreator
    public Battery(@JsonProperty("imagePath") String imagePath, @JsonProperty("rotations") int rotations, @JsonProperty("sides") SideType[] sides, @JsonProperty("numberOfCurrentBatteries") int numberOfCurrentBatteries) {
        super(imagePath, rotations, sides);
        this.numberOfCurrentBatteries = numberOfCurrentBatteries;
    }

    @JsonProperty("numberOfCurrentBatteries")
    public void setNumberOfCurrentBatteries(int numberOfCurrentBatteries) {
        this.numberOfCurrentBatteries = numberOfCurrentBatteries;
    }

    public void removeBattery() {
        if (numberOfCurrentBatteries > 0) {
            numberOfCurrentBatteries--;
        }
    }

    public String getComponentName() {
        return "Battery";
    }

    @Override
    public int getBatteryPower() {
        return numberOfCurrentBatteries;
    }

    @Override
    public <T, E extends Exception> T accept(ComponentVisitor<T, E> componentVisitor) throws E {
        return componentVisitor.visitBattery(this);
    }


}
