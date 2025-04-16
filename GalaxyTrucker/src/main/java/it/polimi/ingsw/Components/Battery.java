package it.polimi.ingsw.Components;

/**
 * Class that represent a battery
 *
 * @author carlo
 */

public class Battery extends Component {

    private int numberOfCurrentBatteries;

    public Battery(SideType[] sides, int numberOfCurrentBatteries) {
        super(sides);
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
    public int getBatteryPower(){
        return numberOfCurrentBatteries;
    }

}
