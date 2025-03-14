package it.polimi.ingsw.components;

/**
 * Class that represent the cabin component
 * @author carlo
 */

public class Cabin extends Component{

    private CrewType crewType;
    private int numberOfCurrentInhabitant = 2;

    public Cabin(SideType[] sides){
        super(sides);
        this.numberOfCurrentInhabitant = numberOfCurrentInhabitant;
    }

    /**
     *method to set the crew type. Has to be called at the end of the assembling phase.
     * @param crewType represent the crew type
     */

    public void setCrewType(CrewType crewType) {
        this.crewType = crewType;

        if(crewType == CrewType.Purple || crewType == CrewType.Brown){
            numberOfCurrentInhabitant = 1;
        }
    }

    public int getNumberOfCurrentInhabitant() {
        return numberOfCurrentInhabitant;
    }

    public void removeInhabitant(){
        numberOfCurrentInhabitant--;
    }

    public String getComponentName(){
        return "Cabin";
    }
}
