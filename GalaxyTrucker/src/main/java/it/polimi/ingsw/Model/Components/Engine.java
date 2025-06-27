package it.polimi.ingsw.Model.Components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

/**
 * Class that represents an Engine
 *
 * @author carlo
 * @author Ludo
 */

public class Engine extends Component {

    private boolean single;

    public Engine() {
    }

    @JsonIgnore
    @JsonCreator
    public Engine(@JsonProperty("sides") SideType[] sides, @JsonProperty("single") boolean single) {
        super(sides);
        this.single = single;

        // check special sides
        if (Arrays.stream(sides).filter(sideType -> sideType.equals(SideType.Special)).count() != 1)
            throw new IllegalArgumentException("Engines must have exactly 1 special side.");

        // rotate until engine is facing backwards
        while (this.getBack() != SideType.Special) {
            this.accept(new ComponentRotatorVisitor());
        }
    }

    @JsonCreator
    public Engine(@JsonProperty("imagePath") String imagePath, @JsonProperty("rotations") int rotations, @JsonProperty("sides") SideType[] sides, @JsonProperty("single") boolean single) {
        super(imagePath, rotations, sides);
        this.single = single;

        // check special sides
        if (Arrays.stream(sides).filter(sideType -> sideType.equals(SideType.Special)).count() != 1)
            throw new IllegalArgumentException("Engines must have exactly 1 special side.");

        // rotate until engine is facing backwards
        while (this.getBack() != SideType.Special) {
            this.accept(new ComponentRotatorVisitor());
        }
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

    /**
     * @return true if the engine is single
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
        return componentVisitor.visitEngine(this);
    }

}

