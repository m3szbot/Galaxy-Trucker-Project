package it.polimi.ingsw.Connection.ServerSide;

import it.polimi.ingsw.Application.*;
import it.polimi.ingsw.Shipboard.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Server {

    List<Game> games = new ArrayList<>();

    int gameCode = 0;
    Game currentStartingGame = new Game(gameCode);
    SocketListener socketListener = new SocketListener(this);
    RMIListener rmiListener;
    ReentrantLock lock;

    public void addGame(Game game){
        games.add(game);
    }

    public int getGameCode(){
        return gameCode;
    }

    public GameState getCurrentGameState(){
        return currentStartingGame.getGameState();
    }

    public ReentrantLock getLock(){
        return lock;
    }

    public boolean isCurrentGameFull(){
        return currentStartingGame.isFull();
    }

    public GameInformation getCurrentGameInformation(){
        return currentStartingGame.getGameInformation();
    }

    public String getCurrentGameCreator(){
        return currentStartingGame.getCreator();
    }

    public void changeCurrentGameState(GameState gameState){

        currentStartingGame.changeGameState(gameState);

    }

    //Method overloading, the second one is used to connect the first player.

    public void addPlayerToCurrentGame(Player player, ViewType viewType, ConnectionType connectionType){

        currentStartingGame.addPlayer(player, viewType, connectionType, false);

    }

    public void addPlayerToCurrentGame(Player player, ViewType viewType, ConnectionType connectionType, GameType gameType, int numberOfPlayers){

        currentStartingGame.setNumberOfPlayers(numberOfPlayers);
        currentStartingGame.setGameType(gameType);
        currentStartingGame.addPlayer(player, viewType, connectionType, true);
        currentStartingGame.changeGameState(GameState.Starting);

    }

    public void startCurrentGame(){
        currentStartingGame.run();
        gameCode++;
        addGame(currentStartingGame);
        //new game
        currentStartingGame = new Game(gameCode);
    }

    public void start(){

        socketListener.run();
        rmiListener.run();

    }

}
