package it.polimi.ingsw.Components;

/**
 * Class that represents an Engine
 *
 * @author carlo
 */

public class Engine extends Component {

    private boolean single;

    public Engine(SideType[] sides, boolean single) {
        super(sides);
        this.single = single;
        while (this.getBack() != SideType.Special) {
            this.rotate();
        }
    }

    /**
     * @return true if the engine is single
     */

    public boolean isSingle() {
        return single;
    }

    @Override
    public String getComponentName() {
        return "Engine";
    }
}
