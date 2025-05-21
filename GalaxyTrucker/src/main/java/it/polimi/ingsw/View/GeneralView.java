package it.polimi.ingsw.View;

import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Model.Components.*;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;

import java.util.ArrayList;
import java.util.List;

/**
 * General view class defining the most basic methods reused by other view classes.
 * <p>
 * Each print method has 2 versions:
 * 1 takes DataContainer as parameter (and calls the other version),
 * 1 takes model as parameter.
 */
public class GeneralView implements ClientViewMethods {
    public static final int COMPONENT_LINES = 5;
    // includes \n at the end of line
    public static final int COMPONENT_CHARACTERS_PER_LINE = 10;


    public void printMessage(DataContainer dataContainer) {
        if (dataContainer.getMessage() == null)
            throw new IllegalArgumentException("The DC does not contain a message");
        printMessage(dataContainer.getMessage());
    }

    /**
     * Print a message passed as parameter.
     */
    public void printMessage(String message) {
        System.out.println(message);
    }

    /**
     * Print a component passed as parameter.
     */
    public void printComponent(Component component) {
        String componentString = getCorrectComponentString(component);
        System.out.printf(componentString);
    }

    /**
     * Print a shipboard passed as parameter.
     */
    public void printShipboard(ShipBoard shipBoard) {
        // row and column to print indexes in
        int indexRow = 3;
        int indexColumn = 2;
        //  shipboard number of rows and cols
        int rows = shipBoard.getMatrixRows();
        int cols = shipBoard.getMatrixCols();

        Component[][] shipStructure = shipBoard.getStructureMatrix();
        boolean[][] validPositions = shipBoard.getMatr();
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

    /**
     * Print a card passed as parameter.
     */
    public void printCard(Card card) {
        String cardName = card.getCardName();
        System.out.printf("Current card: %s\n", cardName);
        card.showCard();
    }

    /**
     * Print a flightboard passed as parameter.
     */
    public void printFlightBoard(FlightBoard flightBoard) {
        Player player;
        String nickname;
        int diff, temp1, temp2;

        System.out.println("FlightBoard:\n");

        for (int i = flightBoard.getPlayerOrderList().size() - 1; i >= 0; i--) {

            //String used to print the nickName
            player = flightBoard.getPlayerOrderList().get(i);
            nickname = player.getNickName();

            // Temp1 holds the current player tile, temp2 holds the subsequent player tile
            temp1 = flightBoard.getPlayerTile(player);
            temp2 = 0;

            //If the current player is not the first, diff is used to print the number of tiles between the two players
            if (i != 0) {
                temp2 = flightBoard.getPlayerTile(flightBoard.getPlayerOrderList().get(i - 1));
            }
            diff = temp2 - temp1;

            if (i == 0) {
                System.out.printf("%s:%d is 1st\n\n", nickname, temp1);
            } else {
                System.out.printf("%s:%d ---%d---> ", nickname, temp1, diff);
            }
        }
    }

    public void printComponent(DataContainer dataContainer) {
        if (dataContainer.getComponent() == null)
            throw new IllegalArgumentException("The DC does not contain a component");
        printComponent(dataContainer.getComponent());
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

    public void printShipboard(DataContainer dataContainer) {
        if (dataContainer.getShipBoard() == null)
            throw new IllegalArgumentException("The DC does not contain a shipboard");
        printShipboard(dataContainer.getShipBoard());
    }

    /**
     * Get string of and index cell.
     */
    private List<String> getIndexCell(int index) {
        return List.of(
                "         ",
                "         ",
                String.format("    %d    ", index),
                "         ",
                "         "
        );
    }

    /**
     * Get string of an invalid cell.
     */
    private List<String> getInvalidCell() {
        return List.of(
                "         ",
                "         ",
                "         ",
                "         ",
                "         "
        );
    }

    /**
     * Get string of an empty (valid) cell.
     */
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
     * Helper method of printShipboard.
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
        if (dataContainer.getCard() == null)
            throw new IllegalArgumentException("The DC does not contain a card");
        printCard(dataContainer.getCard());
    }

    public void printFlightBoard(DataContainer dataContainer) {
        if (dataContainer.getFlightBoard() == null)
            throw new IllegalArgumentException("The DC does not contain a flightboard");
        printFlightBoard(dataContainer.getFlightBoard());
    }


}
