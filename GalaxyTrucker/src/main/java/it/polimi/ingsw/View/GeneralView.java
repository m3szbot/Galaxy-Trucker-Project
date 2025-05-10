package it.polimi.ingsw.View;

import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.Components.SideType;

/**
 * General view class defining the most basic methods reused by others classes.
 * public methods: no parameters!, model extracted from DataContainer
 * private methods: model passed as parameter, always invoked inside public print methods
 */
public abstract class GeneralView {
    public void printMessage(DataContainer dataContainer) {
        System.out.println(dataContainer.getMessage());
    }

    /**
     * Print component extracted from the passed dataContainer
     */
    public void printComponent(DataContainer dataContainer) {
        Component component = dataContainer.getComponent();
        if (component == null) {
            throw new IllegalArgumentException("The given container does not contain a component");
        }
        printComponent(component);
    }


    /**
     * Print the component given as parameter. Called by other public print methods
     */
    private void printComponent(Component component) {
        System.out.printf("""
                        +---%d---+
                        |       |
                        %d  %s    %d
                        |       |
                        +---%d---+""",
                componentSideTranslator(component.getFront()), componentSideTranslator(component.getLeft()),
                component.getComponentName().substring(0, 3),
                componentSideTranslator(component.getRight()), componentSideTranslator(component.getBack()));
    }

    /**
     * Returns a number identifying the given side's type
     *
     * @return identifier number
     */
    private int componentSideTranslator(SideType sideType) {
        if (sideType.equals(SideType.Smooth))
            return 0;
        else if (sideType.equals(SideType.Single))
            return 1;
        else if (sideType.equals(SideType.Double))
            return 2;
        else if (sideType.equals(SideType.Universal))
            return 3;
        else
            return 4;
    }

    public void printShipboard() {
    }

    public void printCard() {
    }

    private void printCard(Card card) {
    }


}
