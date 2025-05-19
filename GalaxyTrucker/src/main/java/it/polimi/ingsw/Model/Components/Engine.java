package it.polimi.ingsw.Model.Components;

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

    /**
     * @return true if the engine is single
     */

    public boolean isSingle() {
        return single;
    }

    @JsonProperty("single")
    public void setSingle(boolean single) {
        this.single = single;
    }

    @Override
    public String getComponentName() {
        return "Engine";
    }

    @Override
    public int getDrivingPower() {
        if (!single)
            return 2;
        return 1;
    }
}

