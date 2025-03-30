package it.polimi.ingsw.Components;

/**
 * Class that represent a cannon
 *
 * @author carlo
 */

public class Cannon extends Component {

    private boolean single;

    public Cannon(SideType[] sides, boolean single) {

        super(sides);
        this.single = single;

    }

    /**
     * @return true if cannon is single.
     */

    public boolean isSingle() {
        return single;
    }

    @Override
    public String getComponentName() {
        return "Cannon";
    }
}
