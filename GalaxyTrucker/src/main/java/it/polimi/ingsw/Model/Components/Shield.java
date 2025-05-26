package it.polimi.ingsw.Model.Components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class that represent the shield component
 *
 * @author carlo
 */

public class Shield extends Component {

    private int coveredSide1, coveredSide2;

    public Shield() {
    }

    @JsonCreator
    public Shield(@JsonProperty("sides") SideType[] sides) {
        super(sides);
        setCoveredSides();
    }

    /**
     * private method that sets the two private attributes
     */

    private void setCoveredSides() {

        if (getFront() == SideType.Special) {
            coveredSide1 = 0;

            if (getRight() == SideType.Special) {
                coveredSide2 = 1;
            } else if (getBack() == SideType.Special) {
                coveredSide2 = 2;
            } else if (getLeft() == SideType.Special) {
                coveredSide2 = 3;
            }
        } else if (getRight() == SideType.Special) {
            coveredSide1 = 1;

            if (getBack() == SideType.Special) {
                coveredSide2 = 2;
            } else if (getLeft() == SideType.Special) {
                coveredSide2 = 3;
            }
        } else if (getBack() == SideType.Special) {
            coveredSide1 = 2;
            coveredSide2 = 3;
        }

    }

    public int getCoveredSide1() {
        return coveredSide1;
    }

    public int getCoveredSide2() {
        return coveredSide2;
    }

    @Override
    public String getComponentName() {
        return "Shield";
    }

    @Override
    public boolean[] getCoveredSides() {
        boolean[] sides = new boolean[4];
        int i;
        for (i = 0; i < sides.length; i++) {
            if (i == coveredSide1 || i == coveredSide2) {
                sides[i] = true;
            } else {
                sides[i] = false;
            }
        }
        return sides;
    }

    @Override
    public <T> T accept(ComponentVisitor<T> componentVisitor) {
        System.out.println("Entered shield visitor");
        return componentVisitor.visitShield(this);
    }


}
