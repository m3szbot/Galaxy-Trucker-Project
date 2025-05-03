package it.polimi.ingsw.Connection.ServerSide.RMI;

import it.polimi.ingsw.Connection.ClientSide.ClientInfo;
import it.polimi.ingsw.Connection.ServerSide.Server;
import it.polimi.ingsw.Controller.Game.GameState;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * remote class that implements the RMI joiner interface
 */

public class RMIJoinerImpl extends UnicastRemoteObject implements RMIJoiner{

    private Server centralServer;

    public RMIJoinerImpl(Server centralServer) throws RemoteException {
        this.centralServer = centralServer;
    }

    @Override
    public boolean joinGame() {

       if(centralServer.getLock().tryLock()){

           return true;
       }

       return false;

    }

    public void releaseLock(){
        centralServer.getLock().unlock();
    }

    @Override
    public boolean isFirstPlayer() {

        return (centralServer.getCurrentStartingGame().getGameState() == GameState.Empty);

    }

    @Override
    public boolean isNameRepeated(String nickname) {

        return centralServer.getCurrentStartingGame().isNickNameRepeated(nickname);
    }

    @Override
    public void addPlayer(ClientInfo clientInfo) {

        Player player = new Player(clientInfo.getNickname(), centralServer.getCurrentColor(), centralServer.getCurrentStartingGame().getGameInformation());
        clientInfo.setGameCode(centralServer.getCurrentStartingGame().getGameCode());
        centralServer.addPlayerToCurrentStartingGame(player, clientInfo.getViewType(), clientInfo.getConnectionType());
        centralServer.getCurrentStartingGame().getGameInformation().setPlayerSocketMap(player, null);

        if(centralServer.getCurrentStartingGame().isFull()){
            centralServer.startCurrentGame();
        }

    }

    public void addPlayer(ClientInfo clientInfo, GameType gameType, int numberOfPlayers){

        Player player = new Player(clientInfo.getNickname(), centralServer.getCurrentColor(), centralServer.getCurrentStartingGame().getGameInformation());
        clientInfo.setGameCode(centralServer.getCurrentStartingGame().getGameCode());
        centralServer.addPlayerToCurrentStartingGame(player, clientInfo.getViewType(), clientInfo.getConnectionType(), gameType, numberOfPlayers);
        centralServer.getCurrentStartingGame().getGameInformation().setPlayerSocketMap(player, null);

        centralServer.getCurrentStartingGame().changeGameState(GameState.Starting);
    }

    public int getGameCode(){
       return centralServer.getCurrentStartingGame().getGameCode();
    }

    public String getCurrentGameCreator(){
        return centralServer.getCurrentStartingGame().getCreator();
    }

}
