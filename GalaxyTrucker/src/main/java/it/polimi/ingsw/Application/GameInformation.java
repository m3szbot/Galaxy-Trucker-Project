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
import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.*;

public class GameInformation {
    private List<Card> cardsList;
    private List<Component> componentList;
    private List<Player> playerList;
    private Bank bank;
    private FlightBoard flightBoard;
    private GameType gameType;
    private ViewType viewType;

    public List<Card> getCardsList() {
        return cardsList;
    }

    public List<Component> getComponentList() {
        return componentList;
    }

    public List<Player> getPlayerList() {
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

    /**
     * creates the complete list of cards based on the type of game
     * @param gameType
     */
    public void setUpCards(GameType gameType) throws IOException {

    }

    /**
     * creates components objects
     */
    public void setUpComponents(){}

    /**
     * creates playerList
     */
    public void setUpPlayers(){}

    /**
     * creates bank
     */
    public void setUpBank(){
        this.bank = new Bank(gameType);
    }

    /**
     * creates the flight board based on the game type
     */
    public void setUpFlightBoard(){
        this.flightBoard = new FlightBoard(gameType);
    }

    /**
     * asks the creator of the game which mode to play
     * @param gameType
     */
    public void setUpGameType(GameType gameType){
        this.gameType = askGameType();
    }

    /**
     * asks each player which view type they want to play with
     * @param viewType
     */
    public void setViewType(ViewType viewType){
        this.viewType = askViewType();
    }
}