package it.polimi.ingsw.Model.Components;
//not finished

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class that represents an alien support component
 *
 * @author carlo
 * @author Ludo
 */

public class AlienSupport extends Component {

    private boolean purple;

    public AlienSupport() {
    }

    @JsonIgnore
    @JsonCreator
    public AlienSupport(@JsonProperty("sides") SideType[] sides, @JsonProperty("purple") boolean purple) {
        super(sides);
        this.purple = purple;
    }

    @JsonCreator
    public AlienSupport(@JsonProperty("imagePath") String imagePath, @JsonProperty("rotations") int rotations, @JsonProperty("sides") SideType[] sides, @JsonProperty("purple") boolean purple) {
        super(imagePath, rotations, sides);
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

    @Override
    public <T, E extends Exception> T accept(ComponentVisitor<T, E> componentVisitor) throws E {
        return componentVisitor.visitAlienSupport(this);
    }
}
