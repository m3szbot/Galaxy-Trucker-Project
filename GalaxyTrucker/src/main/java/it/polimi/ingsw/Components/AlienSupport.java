package it.polimi.ingsw.Components;
//not finished

/**
 * Class that represents an alien support component
 *
 * @author carlo
 */

public class AlienSupport extends Component {

    private boolean purple;

    public AlienSupport(SideType[] sides, boolean purple) {
        super(sides);
        this.purple = purple;
    }

    /**
     * @return boolean that is true if the alienSupport is for purple aliens
     */

    public boolean isPurple() {
        return purple;
    }

    @Override
    public String getComponentName() {
        return "AlienSupport";
    }
}
