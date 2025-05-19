package it.polimi.ingsw.View;

import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Model.Components.*;
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
        // line 1
        System.out.printf("+---%d---+\n", componentSideTranslator(component.getFront()));
        //  line 2: component name
        System.out.printf("| %s |\n", component.getComponentName().substring(0, 5));
        // line 3-4: component specific prints
        // alien support
        if (component instanceof AlienSupport) {
            if (((AlienSupport) component).isPurple()) {
                System.out.printf("%d  Pur  %d\n",
                        componentSideTranslator(component.getLeft()), componentSideTranslator(component.getRight()));
            } else {
                System.out.printf("%d  Bro  %d\n",
                        componentSideTranslator(component.getLeft()), componentSideTranslator(component.getRight()));
            }
            System.out.printf("|       |\n");
        }
        // battery
        else if (component instanceof Battery) {
            System.out.printf("%d   %d   %d\n",
                    componentSideTranslator(component.getLeft()),
                    component.getBatteryPower(),
                    componentSideTranslator(component.getRight()));
            System.out.printf("|       |\n");
        }

        // line 5

        System.out.printf("%d       %d\n", componentSideTranslator(component.getLeft()), componentSideTranslator(component.getRight()));
        System.out.printf("|       |\n");


        // print Red or Blue storage + storage size
        if (component instanceof Storage) {
            if (((Storage) component).isRed()) {
                System.out.printf("| Red  |\n");
            } else {
                System.out.println("| Blu  |\n");
            }
        }
        // print Single or Double cannon
        else if (component instanceof Cannon) {
            if (((Cannon) component).isSingle()) {
                System.out.printf("|   x1   |\n");
            } else {
                System.out.printf("|   x2   |\n");
            }
        }
        //print Single or Double engine
        else if (component instanceof Engine) {
            if (((Engine) component).isSingle()) {
                System.out.printf("|   x1  |\n");
            } else {
                System.out.printf("|   x2  |\n");
            }
        } else {
            System.out.println("|       |");
        }
        System.out.printf("+---%d---+\n", componentSideTranslator(component.getBack()));
    }

    /**
     * Returns a number identifying the given side's type
     *
     * @return identifier number
     * @author Boti
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

    /**
     * Used to print the shipboard
     *
     * @param dataContainer
     */

    public void printShipboard(DataContainer dataContainer) {
        int indexRow = 3;
        int indexColumn = 2;

        ShipBoard shipBoard = dataContainer.getShipBoard();

        if (shipBoard == null) {
            throw new IllegalArgumentException("The given container does not contain a shipboard");
        }

        Component[][] shipStructure = shipBoard.getStructureMatrix();
        boolean[][] validPositions = shipBoard.getMatr();

        int rows = shipBoard.getMatrixRows();
        int cols = shipBoard.getMatrixCols();

        for (int i = indexRow; i < rows - 3; i++) {

            //Printing every line singularly, this way we can obtain a table form
            for (int line = 0; line < 5; line++) {

                for (int j = indexColumn; j < cols - 2; j++) {

                    String[] cellLines;
                    if (i == 3 && j != 2) {
                        cellLines = getIndexCell(j + 1);
                    } else if (j == 2 && i != 3) {
                        cellLines = getIndexCell(i + 1);
                    } else if (!validPositions[i][j]) {
                        cellLines = getInvalidCell();
                    } else if (shipStructure[i][j] == null) {
                        cellLines = getEmptyCell();
                    } else {
                        cellLines = getComponentLines(shipStructure[i][j]);
                    }

                    System.out.print(cellLines[line] + " ");
                }
                System.out.println();
            }
        }

    }

    private String[] getIndexCell(int index) {
        return new String[]{
                "         ",
                "         ",
                String.format("    %d    ", index),
                "         ",
                "         ",
        };
    }

    private String[] getInvalidCell() {
        return new String[]{
                "         ",
                "         ",
                "         ",
                "         ",
                "         "
        };
    }

    private String[] getEmptyCell() {
        return new String[]{
                "+---.---+",
                "|       |",
                ".  ...  .",
                "|       |",
                "+---.---+"
        };
    }

    private String[] getComponentLines(Component component) {
        return new String[]{
                String.format("+---%d---+", componentSideTranslator(component.getFront())),
                "|       |",
                String.format("%d  %s  %d",
                        componentSideTranslator(component.getLeft()),
                        component.getComponentName().substring(0, 3),
                        componentSideTranslator(component.getRight())),
                "|       |",
                String.format("+---%d---+", componentSideTranslator(component.getBack()))
        };
    }

    public void printCard(DataContainer dataContainer) {

        Card card = dataContainer.getCard();

        if (card == null) {
            throw new IllegalArgumentException("The given container does not contain a card");
        }

        printCard(card);

    }

    private void printCard(Card card) {

        String cardName = card.getCardName();
        String cardAttributes;

        System.out.printf("Current card:    %s\n", cardName);

        card.showCard();

    }


}
