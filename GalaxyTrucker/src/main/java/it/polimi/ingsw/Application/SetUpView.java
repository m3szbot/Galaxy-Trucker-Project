package it.polimi.ingsw.Application;

/**
 * set up view class
 *
 * @author Ludo
 */

import java.io.IOException;
import java.util.Scanner;


public class SetUpView {
    public int askMaxNumberOfPlayers() throws IOException {
        int temp = 0;
        System.out.println("How many players do you want the match to be played with?");
        temp = System.in.read();
        while (temp < 2 || temp > 4) {
            System.out.println("Please enter a number between 2 and 4:");
            temp = System.in.read();
        }
        return temp;
    }

    /**
     * asks the first player which game type he wants the new match to be
     *
     * @return GameType
     */
    public GameType askGameType() {
        Scanner scanner = new Scanner(System.in);
        String type;

        while (true) {
            System.out.println("What type of game do you want to play?");
            type = scanner.nextLine().toUpperCase();

            try {
                GameType.valueOf(type);
                System.out.println("You se" + type);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid game type");
            }
        }
        scanner.close();
        return GameType.valueOf(type);

    }

    /**
     * asks a player which view type he wants to play with
     *
     * @return ViewType
     */
    public ViewType askViewType() {
        Scanner scanner = new Scanner(System.in);
        String type;

        while (true) {
            System.out.println("What type of view do you want to play with? (CLI, GUI)");
            type = scanner.nextLine().toUpperCase();

            try {
                ViewType viewType = ViewType.valueOf(type);
                System.out.println("You selected " + viewType);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Please enter a valid view type.");
            }
        }
        scanner.close();
        return ViewType.valueOf(type);
    }
}
