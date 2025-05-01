package it.polimi.ingsw.View.CorrectionView;

import it.polimi.ingsw.Model.ShipBoard.ShipBoard;

import java.util.InputMismatchException;
import java.util.Scanner;

public class CorrectionView {

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
     * Get column number from input and return it
     *
     * @return column number
     */
    public int promptForColumn() {
        int value;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter column:");
            try {
                value = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Please enter a number!");
            }
        }
        return value;
    }

    /**
     * Get row number from input and return it
     *
     * @return row number
     */
    public int promptForRow() {
        int value;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter row:");
            try {
                value = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Please enter a number!");
            }
        }
        return value;
    }

}
