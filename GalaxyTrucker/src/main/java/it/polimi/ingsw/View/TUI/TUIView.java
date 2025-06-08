package it.polimi.ingsw.View.TUI;

import it.polimi.ingsw.Connection.ServerSide.socket.DataContainer;
import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.Components.ComponentStringGetterVisitor;
import it.polimi.ingsw.Model.Components.ComponentVisitor;
import it.polimi.ingsw.Model.Components.SideType;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;
import it.polimi.ingsw.View.GeneralView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implementations of all the methods of general view for the tui gamemode.
 */

public class TUIView extends GeneralView {
    // ANSI COLORS
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    // use: println(RED + "text" + RESET);


    public static final int COMPONENT_LINES = 5;
    // includes \n at the end of line
    public static final int COMPONENT_CHARACTERS_PER_LINE = 10;

    /**
     * Returns a number identifying the given side's type
     *
     * @return identifier number
     * @author Boti
     */
    public static int componentSideTranslator(SideType sideType) {
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
        ComponentVisitor<String> visitor = new ComponentStringGetterVisitor();
        String componentString = (String) component.accept(visitor);
        System.out.printf(componentString);
    }

    /**
     * Print a shipboard passed as parameter.
     */
    public void printShipboard(ShipBoard shipBoard) {
        // row and column to print indexes in
        // TODO adjust
        int indexRow = 3;
        int indexColumn = 2;
        // Print shipboard in:
        // col: 4-10
        // row: 5-9

        //  shipboard number of rows and cols
        int rows = shipBoard.getMatrixRows();
        int cols = shipBoard.getMatrixCols();

        Component[][] shipStructure = shipBoard.getComponentMatrix();
        boolean[][] validPositions = shipBoard.getValidityMatrix();
        List<String> cellLines;

        // cycle rows
        for (int i = indexRow; i < rows - indexRow; i++) {
            //Printing every line singularly, this way we can obtain a table form
            for (int line = 0; line < COMPONENT_LINES; line++) {

                // cycle columns
                for (int j = indexColumn; j < cols - indexColumn; j++) {
                    // print index row
                    if (i == indexRow && j != indexColumn) {
                        cellLines = getIndexCell(j + 1);
                    }
                    // print index column
                    else if (j == indexColumn && i != indexRow) {
                        cellLines = getIndexCell(i + 1);
                    }
                    // print invalid cells
                    else if (!validPositions[j][i]) {
                        cellLines = getInvalidCell();
                    }
                    // print empty cells
                    else if (shipStructure[j][i] == null) {
                        cellLines = getEmptyCell();
                    }
                    // print components
                    else {
                        cellLines = getComponentLines(shipStructure[j][i]);
                    }
                    System.out.print(cellLines.get(line) + " ");
                }
                System.out.println();
            }
        }
        // end of components
        System.out.println(String.format("Destroyed components: %d\n", shipBoard.getShipBoardAttributes().getDoubleEnginePower()));

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

    /**
     * Prints the whole shipboard for debugging.
     */
    public void printFullShipboard(ShipBoard shipBoard) {
        // row and column to print indexes in
        int indexRow = 0;
        int indexColumn = 0;
        // Print shipboard in:
        // col: 4-10
        // row: 5-9

        //  shipboard number of rows and cols
        int rows = shipBoard.getMatrixRows();
        int cols = shipBoard.getMatrixCols();

        Component[][] shipStructure = shipBoard.getComponentMatrix();
        boolean[][] validPositions = shipBoard.getValidityMatrix();
        List<String> cellLines;

        // cycle rows
        for (int i = indexRow; i < rows - indexRow; i++) {
            //Printing every line singularly, this way we can obtain a table form
            for (int line = 0; line < COMPONENT_LINES; line++) {

                // cycle columns
                for (int j = indexColumn; j < cols - indexColumn; j++) {
                    // print index row
                    if (i == indexRow && j != indexColumn) {
                        cellLines = getIndexCell(j + 1);
                    }
                    // print index column
                    else if (j == indexColumn && i != indexRow) {
                        cellLines = getIndexCell(i + 1);
                    }
                    // print invalid cells
                    else if (!validPositions[j][i]) {
                        cellLines = getInvalidCell();
                    }
                    // print empty cells
                    else if (shipStructure[j][i] == null) {
                        cellLines = getEmptyCell();
                    }
                    // print components
                    else {
                        cellLines = getComponentLines(shipStructure[j][i]);
                    }
                    System.out.print(cellLines.get(line) + " ");
                }
                System.out.println();
            }
        }

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
        ComponentVisitor visitor = new ComponentStringGetterVisitor();
        String componentString = (String) component.accept(visitor);
        // split rows based on newline \n
        String[] lines = componentString.split("\n");
        return new ArrayList<>(Arrays.asList(lines));
    }

    public void printComponent(DataContainer dataContainer) {
        if (dataContainer.getComponent() == null)
            throw new IllegalArgumentException("The DC does not contain a component");
        printComponent(dataContainer.getComponent());
    }

    public void printShipboard(DataContainer dataContainer) {
        if (dataContainer.getShipBoard() == null)
            throw new IllegalArgumentException("The DC does not contain a shipboard");
        printShipboard(dataContainer.getShipBoard());
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
