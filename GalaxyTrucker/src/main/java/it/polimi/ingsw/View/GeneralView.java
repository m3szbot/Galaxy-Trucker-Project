package it.polimi.ingsw.View;

import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.Components.SideType;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;

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

    public void printShipboard(DataContainer dataContainer) {

        ShipBoard shipBoard = dataContainer.getShipBoard();

        if(shipBoard == null){
            throw new IllegalArgumentException("The given container does not contain a shipboard");
        }

        Component[][] shipStructure = shipBoard.getStructureMatrix();
        boolean[][] validPositions = shipBoard.getMatr();

        for(int i = 0; i < shipBoard.getMatrixRows(); i++){

            for(int j = 0; j < shipBoard.getMatrixCols(); j++){

                if(!validPositions[i][j]){
                    //position is invalid
                    System.out.printf("""
                                    +---.---+
                                    |       |
                                    x  xxx    x
                                    |       |
                                    +---.---+
                                    """);

                }
                else{

                    if(shipStructure[i][j] == null){
                        //position is valid but no component is found
                        System.out.printf("""
                                        +---.---+
                                        |       |
                                        .  ...    .
                                        |       |
                                        +---.---+
                                        """);

                    }
                    else{
                        //position is valid and component if found
                        printComponent(shipStructure[i][j]);
                    }
                }


            }
        }



    }

    public void printCard() {
    }

    private void printCard(Card card) {
    }


}
