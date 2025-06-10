package it.polimi.ingsw.Model.Components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class that represent the cabin component
 *
 * @author carlo
 */

public class Cabin extends Component {
    // default values
    private CrewType crewType = CrewType.Human;
    private int numberOfCurrentInhabitants = 0;

    public Cabin() {
    }

    @JsonCreator
    public Cabin(@JsonProperty("sides") SideType[] sides, @JsonProperty("crewType") CrewType jsonCrewType, @JsonProperty("numberOfCurrentInhabitants") int jsonNumberOfCurrentInhabitants) {
        super(sides);
        this.crewType = jsonCrewType;
        this.numberOfCurrentInhabitants = jsonNumberOfCurrentInhabitants;
    }

    public CrewType getCrewType() {
        return crewType;
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
            numberOfCurrentInhabitants = 1;
        }
    }

    public void setNumberOfCurrentInhabitants(int count) {
        this.numberOfCurrentInhabitants = count;
    }

    public void removeInhabitant() {
        if (numberOfCurrentInhabitants > 0) {
            numberOfCurrentInhabitants--;
        }
    }

    public String getComponentName() {
        return "Cabin";
    }

    @Override
    public int getCrewMembers() {
        return numberOfCurrentInhabitants;
    }

    @Override
    public <T, E extends Exception> T accept(ComponentVisitor<T, E> componentVisitor) throws E {
        return componentVisitor.visitCabin(this);
    }
}
