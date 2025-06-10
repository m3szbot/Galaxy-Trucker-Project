package it.polimi.ingsw.Model.Components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

/**
 * Class that represent a cannon
 *
 * @author carlo
 */

public class Cannon extends Component {

    private boolean single;

    public Cannon() {
    }

    @JsonCreator
    public Cannon(@JsonProperty("sides") SideType[] sides, @JsonProperty("single") boolean single) {
        super(sides);
        this.single = single;

        // check special sides
        if (Arrays.stream(sides).filter(sideType -> sideType.equals(SideType.Special)).count() != 1)
            throw new IllegalArgumentException("Cannons must have exactly 1 special side.");
    }

    @Override
    public String getComponentName() {
        return "Cannon";
    }

    @Override
    public float getFirePower() {
        int firePower = 1;
        if (!single)
            firePower = 2;
        if (this.getFront().equals(SideType.Special)) {
            return firePower;
        }
        return firePower * 0.5f;
    }

    /**
     * @return true if cannon is single.
     */

    @Override
    public boolean isSingle() {
        return single;
    }

    @JsonProperty("single")
    public void setSingle(boolean single) {
        this.single = single;
    }

    @Override
    public <T, E extends Exception> T accept(ComponentVisitor<T, E> componentVisitor) throws E {
        return componentVisitor.visitCannon(this);
    }

}
