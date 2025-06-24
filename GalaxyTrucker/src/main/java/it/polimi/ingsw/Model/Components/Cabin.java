package it.polimi.ingsw.Model.Components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.Model.ShipBoard.Color;

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

    @JsonIgnore
    @JsonCreator
    public Cabin(@JsonProperty("sides") SideType[] sides, @JsonProperty("crewType") CrewType jsonCrewType, @JsonProperty("numberOfCurrentInhabitants") int jsonNumberOfCurrentInhabitants) {
        super(sides);
        this.crewType = jsonCrewType;
        this.numberOfCurrentInhabitants = jsonNumberOfCurrentInhabitants;
    }

    @JsonCreator
    public Cabin(@JsonProperty("imagePath") String imagePath, @JsonProperty("rotations") int rotations, @JsonProperty("sides") SideType[] sides, @JsonProperty("crewType") CrewType jsonCrewType, @JsonProperty("numberOfCurrentInhabitants") int jsonNumberOfCurrentInhabitants) {
        super(imagePath, rotations, sides);
        this.crewType = jsonCrewType;
        this.numberOfCurrentInhabitants = jsonNumberOfCurrentInhabitants;
    }

    public static Cabin getStarterCabin(Color color) {
        SideType[] universalSides = new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal};

        String imagePath;
        switch (color) {
            case RED -> {
                imagePath = "/Polytechnic/tiles/GT-new_tiles_16_for web52.jpg";
            }
            case YELLOW -> {
                imagePath = "/Polytechnic/tiles/GT-new_tiles_16_for web61.jpg";
            }
            case GREEN -> {
                imagePath = "/Polytechnic/tiles/GT-new_tiles_16_for web34.jpg";
            }
            case BLUE -> {
                imagePath = "/Polytechnic/tiles/GT-new_tiles_16_for web33.jpg";
            }
            default -> {
                imagePath = null;
            }
        }

        Cabin starter = new Cabin(imagePath, 0, universalSides, CrewType.Human, 2);
        return starter;
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
