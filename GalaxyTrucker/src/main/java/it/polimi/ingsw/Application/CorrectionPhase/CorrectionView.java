package it.polimi.ingsw.Application.CorrectionPhase;

import java.util.Scanner;

public class CorrectionView {

    public void printFinishedMessage() {
        System.out.println("Your ship is correct, please wait for other players to finish...");
    }

    /*
    get column and return it
     */
    public int promptForColumn() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter column:");
        return scanner.nextInt();
    }

    /*
    get row and return it
     */
    public int promptForRow() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter row:");
        return scanner.nextInt();
    }

}
