package it.polimi.ingsw.Connection.ClientSide.View.CorrectionView;

import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;

import java.util.InputMismatchException;
import java.util.Scanner;

public class CorrectionViewTUI extends CorrectionView {

    /**
     * Print error message and error indexes
     */
    public void printErrorsMessage(ShipBoard shipBoard) {
        System.out.println("There are errors in your ship, please correct them!");
        for (int i = 0; i < shipBoard.getMatrixCols(); i++) {
            for (int j = 0; j < shipBoard.getMatrixCols(); j++) {
                if (shipBoard.getMatrErrors()[i][j]) {
                    System.out.printf("Error in: %d %d\n", i + 1, j + 1);
                }
            }
        }
    }

    /**
     * Print finish message if shipboard is valid
     */
    public void printFinishedMessage() {
        System.out.println("Your ship is correct, please wait for other players to finish...");
    }

    /**
     * Get column and row number from input and return it
     *
     * @return column, row number
     */
    public int[] promptForColumnRow() {
        Scanner scanner = new Scanner(System.in);
        int col, row;
        while (true) {
            System.out.println("Enter column:");
            try {
                col = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Please enter a number!");
            }
        }
        while (true) {
            System.out.println("Enter row:");
            try {
                row = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Please enter a number!");
            }
        }
        return new int[]{col, row};
    }

    /**
     * Print columnm, row of component selected to be removed
     *
     * @param col
     * @param row
     */
    public void printComponentRemovalMessage(int col, int row) {
        System.out.printf("Removing component %d %d\n", col, row);
    }

    /**
     * Print timed out player
     *
     * @param player
     */
    public void printPlayerRemovalMessage(Player player) {
        System.out.printf("Player %s timed out and got removed\n", player.getNickName());
    }

}
