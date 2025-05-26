package it.polimi.ingsw.Model.Components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class that represent the cabin component
 *
 * @author carlo
 */

public class Cabin extends Component {

    private CrewType crewType = CrewType.Human;
    private int numberOfCurrentInhabitants = 2;

    public Cabin() {
    }

    @JsonCreator
    public Cabin(@JsonProperty("sides") SideType[] sides) {
        super(sides);
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
    public <T> T accept(ComponentVisitor<T> componentVisitor) {
        System.out.println("Entered cabin visitor");
        return componentVisitor.visitCabin(this);
    }
}
