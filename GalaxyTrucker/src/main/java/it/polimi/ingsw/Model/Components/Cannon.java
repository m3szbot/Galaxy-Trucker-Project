package it.polimi.ingsw.Model.Components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    }

    @JsonProperty("single")
    public void setSingle(boolean single) {
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

    //aggiunte

    @Override
    public float getFirePower() {
        if (single) {
            if (this.getFront().equals(SideType.Special)) {
                return 1;
            } else {
                return 0.5f;
            }
        } else {
            return 2;
        }
    }
}
