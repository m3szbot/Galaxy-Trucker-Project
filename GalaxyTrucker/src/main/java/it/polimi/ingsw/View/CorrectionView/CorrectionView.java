package it.polimi.ingsw.View.CorrectionView;

import java.util.InputMismatchException;
import java.util.Scanner;

public class CorrectionView {

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
