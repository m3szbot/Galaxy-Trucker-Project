package it.polimi.ingsw.Application;

/**
 * main class of the model
 * @author Ludo
 */

import it.polimi.ingsw.components.*;
import it.polimi.ingsw.cards.*;
import it.polimi.ingsw.Bank.*;
import it.polimi.ingsw.shipboard.*;
import it.polimi.ingsw.FlightBoard.*;

public class GameInformation {
    private Card[] cardsList;
    private Component[] componentList;
    private Player[] playerList;
    private Bank bank;
    private FlightBoard flightBoard;
    private GameType gameType;
    private ViewType viewType;

    public Card[] getCardsList() {
        return cardsList;
    }

    public Component[] getComponentList() {
        return componentList;
    }

    public Player[] getPlayerList() {
        return playerList;
    }

    public Bank getBank() {
        return bank;
    }

    public FlightBoard getFlightBoard() {
        return flightBoard;
    }

    public GameType getGameType(){
        return gameType;
    }

    public ViewType getViewType(){
        return viewType;
    }

    public Card[] setUpCards(GameType gameType){
        if(gameType == gameType.TestGame){

        }else{

        }
    }

    public void setUpComponents(){}

    public  void setUpPlayers(){}

    public void setUpBank(){
        this.bank = new Bank(gameType);
    }

    public void setUpFlightBoard(GameType gameType, FlightBoard flightBoard){
        if(gameType == gameType.TestGame){

        }else{

        }
    }

    public void setUpGameType(GameType gameType){
        //chiama un metodo nella view per chiedere la tipologia di game
        this.gameType = askGameType();
    }

    public void setViewType(ViewType viewType){
        //chama un metodo nella view per chiedere il tipo di view
        this.viewType = askViewType();
    }
}