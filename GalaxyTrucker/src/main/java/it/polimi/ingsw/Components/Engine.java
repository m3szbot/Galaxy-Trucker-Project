package it.polimi.ingsw.Components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class that represents an Engine
 *
 * @author carlo
 */

public class Engine extends Component {

    private boolean single;

    public Engine() {
    }

    @JsonCreator
    public Engine(@JsonProperty("sides") SideType[] sides, @JsonProperty("single") boolean single) {
        super(sides);
        this.single = single;
        while (this.getBack() != SideType.Special) {
            this.rotate();
        }
    }

    @JsonProperty("single")
    public void setSingle(boolean single) {
        this.single = single;
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

    @Override
    public int getDrivingPower() {
        if (single) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public void rotate() {
        System.out.println("Cannot rotate an engine!!");
    }
}

