package it.polimi.ingsw.Components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class that represent the cabin component
 *
 * @author carlo
 */

public class Cabin extends Component {

    private CrewType crewType;
    private int numberOfCurrentInhabitant = 2;

    public Cabin() {
    }

    @JsonCreator
    public Cabin(@JsonProperty("sides") SideType[] sides) {
        super(sides);
    }

    /**
     * method to set the crew type. Has to be called at the end of the assembling phase.
     *
     * @param crewType represent the crew type
     */

    @JsonProperty("crewType")
    public void setCrewType(CrewType crewType) {
        this.crewType = crewType;

        if (crewType == CrewType.Purple || crewType == CrewType.Brown) {
            numberOfCurrentInhabitant = 1;
        }
    }

    public CrewType getCrewType() {
        return crewType;
    }

    //redundancy
    public int getNumberOfCurrentInhabitant() {
        return numberOfCurrentInhabitant;
    }

    public void removeInhabitant() {
        if (numberOfCurrentInhabitant > 0) {
            numberOfCurrentInhabitant--;
        }
    }

    public String getComponentName() {
        return "Cabin";
    }

    @Override
    public int getCrewMembers() {
        return numberOfCurrentInhabitant;
    }
}
