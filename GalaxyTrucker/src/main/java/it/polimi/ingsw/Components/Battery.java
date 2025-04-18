package it.polimi.ingsw.Components;

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

    @JsonProperty("numberOfCurrentBatteries")
    public void setNumberOfCurrentBatteries(int numberOfCurrentBatteries) {
        this.numberOfCurrentBatteries = numberOfCurrentBatteries;
    }

    public int getNumberOfCurrentBatteries() {
        return numberOfCurrentBatteries;
    }

    public void removeBattery() {
        numberOfCurrentBatteries--;
    }

    public String getComponentName() {
        return "Battery";
    }

    @Override
    public int getBatteryPower() {
        return numberOfCurrentBatteries;
    }

}
