package it.polimi.ingsw.Model.Components;
//not finished

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class that represents an alien support component
 *
 * @author carlo
 */

public class AlienSupport extends Component {

    private boolean purple;

    public AlienSupport() {
    }

    @JsonCreator
    public AlienSupport(@JsonProperty("sides") SideType[] sides, @JsonProperty("purple") boolean purple) {
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
