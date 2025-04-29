package it.polimi.ingsw.View.FlightView;

import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.Scanner;

public class FlightView {

    private GameInformation gameInformation;
    private Scanner in = new Scanner(System.in);

    public FlightView(GameInformation gameInformation) {
        this.gameInformation = gameInformation;
    }

    //methods are filled up only for testing purposes

    public void updateView() {
        //update GUI for all players
    }

    //Todo

    public void sendMessageToAll(String situationString) {
        /*
        Print a pop-up the players screen telling the actual situation
        about the resolve process of the card. For example, "Player x has
        decided to land on planet 1", or "Player x has been defeated by the
        pirates" and so on.
         */

        System.out.println(situationString);

    }

    //Todo

    public boolean askPlayerGenericQuestion(Player player, String question) {

        System.out.println(question);
        return in.nextBoolean();

    }

    //Todo

    public int[] askPlayerCoordinates(Player player, String question) {

        int[] result = new int[2];

        System.out.println(question);

        result[0] = in.nextInt();
        result[1] = in.nextInt();

        return result;
    }

    //Todo

    public int askPlayerValue(Player player, String question) {

        System.out.println(question);
        return in.nextInt();

    }

}
