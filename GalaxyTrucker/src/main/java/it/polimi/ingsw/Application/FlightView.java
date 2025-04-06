package it.polimi.ingsw.Application;

import it.polimi.ingsw.Shipboard.Player;

public class FlightView {

    private GameInformation gameInformation;

    public FlightView(GameInformation gameInformation){
        this.gameInformation = gameInformation;
    }

    public void updateView(){
       //update GUI for all players
    }

    //Todo

    public void sendMessageToAll(String situationString){
        /*
        Print a pop up the players screen telling the actual situation
        about the resolve process of the card. For example, "Player x has
        decided to land on planet 1", or "Player x has been defeated by the
        pirates" and so on.
         */
    }

    //Todo

    public boolean askPlayerGenericQuestion(Player player, String question){

    }

    //Todo

    public int[] askPlayerCoordinates(Player player, String question){

    }

    //Todo

    public int askPlayerValue(Player player, String question){


    }

}
