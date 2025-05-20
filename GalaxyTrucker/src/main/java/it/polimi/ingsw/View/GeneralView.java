package it.polimi.ingsw.View;

import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Model.Components.*;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;

import java.util.ArrayList;
import java.util.List;

/**
 * General view class defining the most basic methods reused by others classes.
 * public methods: no parameters!, model extracted from DataContainer
 * private methods: model passed as parameter, always invoked inside public print methods
 */
public abstract class GeneralView {
    public static final int COMPONENT_LINES = 5;
    // includes \n at the end of line
    public static final int COMPONENT_CHARACTERS_PER_LINE = 10;


    public void printMessage(DataContainer dataContainer) {
        System.out.println(dataContainer.getMessage());
    }

    /**
     * Print component extracted from the passed dataContainer
     *
     * @author Boti
     */
    public void printComponent(DataContainer dataContainer) {
        Component component = dataContainer.getComponent();
        if (component == null) {
            throw new IllegalArgumentException("The given container does not contain a component");
        }
        String componentString = getCorrectComponentString(component);
        System.out.printf(componentString);
    }

    /**
     * Return correct string of component based on dynamic type.
     */
    private String getCorrectComponentString(Component component) {
        // select component specific method
        if (component instanceof AlienSupport) {
            return getComponentString((AlienSupport) component);
        } else if (component instanceof Battery) {
            return getComponentString((Battery) component);
        } else if (component instanceof Cabin) {
            return getComponentString((Cabin) component);
        } else if (component instanceof Cannon) {
            return getComponentString((Cannon) component);
        } else if (component instanceof Engine) {
            return getComponentString((Engine) component);
        } else if (component instanceof Shield) {
            return getComponentString((Shield) component);
        } else if (component instanceof Storage) {
            return getComponentString((Storage) component);
        }
        // default component string
        return getComponentString(component);
    }

    /**
     * Get string of AlienSupport
     */
    private String getComponentString(AlienSupport component) {
        return String.format("""
                        +---%d---+
                        | %s |
                        %d  %s  %d
                        |       |
                        +---%d---+
                        """,
                componentSideTranslator(component.getFront()),
                component.getComponentName().substring(0, 5),
                componentSideTranslator(component.getLeft()), component.isPurple() ? "Pur" : "Bro", componentSideTranslator(component.getRight()),
                componentSideTranslator(component.getBack()));
    }

    /**
     * Get string of Battery
     */
    private String getComponentString(Battery component) {
        return String.format("""
                        +---%d---+
                        | %s |
                        %d   %d   %d
                        |       |
                        +---%d---+
                        """,
                componentSideTranslator(component.getFront()),
                component.getComponentName().substring(0, 5),
                componentSideTranslator(component.getLeft()), component.getBatteryPower(), componentSideTranslator(component.getRight()),
                componentSideTranslator(component.getBack()));
    }

    /**
     * Get string of Cabin
     */
    private String getComponentString(Cabin component) {
        String crew = "   ";
        if (component.getCrewType() != null) {
            switch (component.getCrewType()) {
                case CrewType.Human:
                    crew = "Hum";
                case CrewType.Brown:
                    crew = "Bro";
                case CrewType.Purple:
                    crew = "Pur";
            }
        }
        return String.format("""
                        +---%d---+
                        | %s |
                        %d  %s  %d
                        |   %d   |
                        +---%d---+
                        """,
                componentSideTranslator(component.getFront()),
                component.getComponentName().substring(0, 5),
                componentSideTranslator(component.getLeft()), crew, componentSideTranslator(component.getRight()),
                component.getCrewMembers(),
                componentSideTranslator(component.getBack()));
    }

    /**
     * Get string of Cannon
     */
    private String getComponentString(Cannon component) {
        return String.format("""
                        +---%d---+
                        | %s |
                        %d   %d   %d
                        |       |
                        +---%d---+
                        """,
                componentSideTranslator(component.getFront()),
                component.getComponentName().substring(0, 5),
                componentSideTranslator(component.getLeft()), component.isSingle() ? 1 : 2, componentSideTranslator(component.getRight()),
                componentSideTranslator(component.getBack()));
    }

    /**
     * Get string of Engine
     */
    private String getComponentString(Engine component) {
        return String.format("""
                        +---%d---+
                        | %s |
                        %d   %d   %d
                        |       |
                        +---%d---+
                        """,
                componentSideTranslator(component.getFront()),
                component.getComponentName().substring(0, 5),
                componentSideTranslator(component.getLeft()), component.isSingle() ? 1 : 2, componentSideTranslator(component.getRight()),
                componentSideTranslator(component.getBack()));
    }

    /**
     * Get string of Shield
     */
    private String getComponentString(Shield component) {
        return String.format("""
                        +---%d---+
                        | %s |
                        %d       %d
                        |       |
                        +---%d---+
                        """,
                componentSideTranslator(component.getFront()),
                component.getComponentName().substring(0, 5),
                componentSideTranslator(component.getLeft()), componentSideTranslator(component.getRight()),
                componentSideTranslator(component.getBack()));
    }

    /**
     * Get string of Storage
     */
    private String getComponentString(Storage component) {
        int slots = component.getAvailableRedSlots() + component.getAvailableBlueSlots();
        return String.format("""
                        +---%d---+
                        | %s |
                        %d %s %d %d
                        |%d %d %d %d|
                        +---%d---+
                        """,
                componentSideTranslator(component.getFront()),
                component.getComponentName().substring(0, 5),
                componentSideTranslator(component.getLeft()), component.isRed() ? "Red" : "Blu", slots, componentSideTranslator(component.getRight()),
                component.getGoods()[0], component.getGoods()[1], component.getGoods()[2], component.getGoods()[3],
                componentSideTranslator(component.getBack()));
    }

    /**
     * Get string of basic component
     */
    private String getComponentString(Component component) {
        return String.format("""
                        +---%d---+
                        | %s |
                        %d       %d
                        |       |
                        +---%d---+
                        """,
                componentSideTranslator(component.getFront()),
                component.getComponentName().substring(0, 5),
                componentSideTranslator(component.getLeft()), componentSideTranslator(component.getRight()),
                componentSideTranslator(component.getBack()));
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
     * Prints the shipboard and its components.
     */
    public void printShipboard(DataContainer dataContainer) {
        // row and column to print indexes in
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
        List<String> cellLines;

        for (int i = indexRow; i < rows - 3; i++) {
            //Printing every line singularly, this way we can obtain a table form
            for (int line = 0; line < COMPONENT_LINES; line++) {

                for (int j = indexColumn; j < cols - 2; j++) {
                    // print index row
                    if (i == 3 && j != 2) {
                        cellLines = getIndexCell(j + 1);
                    }
                    // print index column
                    else if (j == 2 && i != 3) {
                        cellLines = getIndexCell(i + 1);
                    }
                    // print invalid cells
                    else if (!validPositions[i][j]) {
                        cellLines = getInvalidCell();
                    }
                    // print empty cells
                    else if (shipStructure[i][j] == null) {
                        cellLines = getEmptyCell();
                    }
                    // print components
                    else {
                        cellLines = getComponentLines(shipStructure[i][j]);
                    }
                    System.out.print(cellLines.get(line) + " ");
                }
                System.out.println();
            }
        }

    }

    private List<String> getIndexCell(int index) {
        return List.of(
                "         ",
                "         ",
                String.format("    %d    ", index),
                "         ",
                "         "
        );
    }

    private List<String> getInvalidCell() {
        return List.of(
                "         ",
                "         ",
                "         ",
                "         ",
                "         "
        );
    }

    private List<String> getEmptyCell() {
        return List.of(
                "+---.---+",
                "|       |",
                ".  ...  .",
                "|       |",
                "+---.---+"
        );
    }

    /**
     * Return String list of the 5 component lines, excluding the \n at the end of the lines.
     */
    private List<String> getComponentLines(Component component) {
        String componentString = getCorrectComponentString(component);
        List<String> returnList = new ArrayList<>();
        // construct return string array
        for (int i = 0; i < COMPONENT_LINES; i++) {
            // -1: exclude newline at end of line
            returnList.add(componentString.substring(i * COMPONENT_CHARACTERS_PER_LINE, (i + 1) * COMPONENT_CHARACTERS_PER_LINE - 1));
        }
        return returnList;
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
