package it.polimi.ingsw.Model.Components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class that represent the shield component
 *
 * @author carlo
 */

public class Shield extends Component {

    // front right back left
    private boolean[] coveredSides;

    public Shield() {
        coveredSides = new boolean[]{false, false, false, false};
    }

    @JsonCreator
    public Shield(@JsonProperty("sides") SideType[] sides, @JsonProperty("coveredSide1") int coveredSide1,
                  @JsonProperty("coveredSide2") int coveredSide2) {
        super(sides);
        coveredSides = new boolean[]{false, false, false, false};
        setCoveredSides(coveredSide1, coveredSide2);
    }

    /**
     * private method that sets the two private attributes
     */

    private void setCoveredSides(int coveredSide1, int coveredSide2) {
        coveredSides[coveredSide1] = true;
        coveredSides[coveredSide2] = true;
    }

    @Override
    public String getComponentName() {
        return "Shield";
    }

    @Override
    public boolean[] getCoveredSides() {
        return coveredSides;
    }

    @Override
    public <T> T accept(ComponentVisitor<T> componentVisitor) {
        return componentVisitor.visitShield(this);
    }


}
