package it.polimi.ingsw.Controller.SetUpPhase;

/**
 * set up view class
 *
 * @author Ludo
 */

import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.GameInformation.ViewType;
import it.polimi.ingsw.Model.ShipBoard.Color;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.io.IOException;
import java.util.Scanner;


public class SetUpView {

    public int askMaxNumberOfPlayers(Player player, String message) throws IOException {
        int temp = 0;
        System.out.println(message);
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
    public GameType askGameType(String message) {
        Scanner scanner = new Scanner(System.in);
        String type;

        while (true) {
            System.out.println(message);
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
    public ViewType askViewType(String message) {
        Scanner scanner = new Scanner(System.in);
        String type;

        while (true) {
            System.out.println(message);
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

    public String askNickName(String message) throws IOException {
        System.out.println(message);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public Color askColor() throws IOException {
        Scanner scanner = new Scanner(System.in);
        String mycolor;

        while (true) {
            System.out.println("What color do you want to play as (RED, YELLOW, BLUE, GREEN?");
            mycolor = scanner.nextLine().toUpperCase();
            try {
                Color color = Color.valueOf(mycolor);
                System.out.println("You selected " + color);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Please enter a valid color.");
            }
        }
        scanner.close();
        return Color.valueOf(mycolor);
    }
}
