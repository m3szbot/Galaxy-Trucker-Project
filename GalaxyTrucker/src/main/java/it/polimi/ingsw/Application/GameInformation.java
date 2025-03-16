package it.polimi.ingsw.Application;

/**
 * main class of the model
 * @author Ludo
 */

//import packages containing cards, bank, flightboard, gametype and viewtype
import it.polimi.ingsw.components.Component;
import it.polimi.ingsw.shipboard.Player;

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

    public GameType getGameType() {
        return gameType;
    }

    public ViewType getViewType() {
        return viewType;
    }

    public void setUpCards(GameType gameType){
        //card creation
    }

    public void setUpComponents(Component[] componentList){
        //components creation
    }

    public  void setUpPlayers(Player[] playerList){
        //playerList creation
    }

    public void setUpBank(){
        //bank creation
    }

    public void setUpFlightBoard(GameType gameType){
        //flightboard creation
    }

    public void setUpGameType(GameType gameType){}

    public void setViewType(ViewType viewType){}
}
