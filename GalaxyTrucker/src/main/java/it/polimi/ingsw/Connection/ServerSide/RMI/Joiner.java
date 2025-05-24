package it.polimi.ingsw.Connection.ServerSide.RMI;

import it.polimi.ingsw.Connection.ClientSide.RMI.ClientRemoteInterface;
import it.polimi.ingsw.Connection.ClientSide.utils.ClientInfo;
import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.Server;
import it.polimi.ingsw.Controller.Game.GameState;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.rmi.RemoteException;

public class Joiner {

    private ClientInfo clientInfo;
    private int trials;
    private final int MAXTRIALS = 5;
    private Server centralServer;
    private ClientRemoteInterface virtualClient;

    public Joiner(ClientInfo clientInfo, Server centralServer, ClientRemoteInterface virtualClient) throws RemoteException{
        this.clientInfo = clientInfo;
        this.centralServer = centralServer;
        virtualClient.setInputTimeOut(false);
        this.virtualClient = virtualClient;
        this.trials = 0;
    }

    public void start() throws RemoteException {

        if(clientInfo.getGameCode() != -1){
            //joining an existing game
        }
        else{
            startLobby();
        }

    }

    public void checkUsername() throws RemoteException{

        String message;

        while(centralServer.checkNickname(clientInfo.getNickname())){

            message = "nickname '" + clientInfo.getNickname() + "' has already been chosen, please enter a new one: ";
            virtualClient.printMessage(message);
            clientInfo.setNickname(virtualClient.getString());

        }

        centralServer.addNickName(clientInfo.getNickname());
    }


    private void startLobby() throws RemoteException{

        String message;

        checkUsername();

        while(true){

            message = "Press 'Enter' key to enter in a game: ";

            virtualClient.printMessage(message);

            if(checkEnterKey()){

                try {

                    if (joinGame(centralServer)) {

                        if (isEmpty(centralServer)) {
                            makeFirstPlayerJoin();
                        } else {
                            makeNonFirstPlayerJoin();
                        }

                        centralServer.getLock().unlock();
                        break;

                    }
                    else{

                        message = "Somebody is already joining a new game, please wait.";
                        virtualClient.printMessage(message);
                    }

                }catch (RemoteException e){
                    //need to release the lock
                    centralServer.getLock().unlock();

                    throw e;
                }

            }
            else{

                message = "The string you entered is invalid!";
                virtualClient.printMessage(message);
                trials++;
                checkTrials();
            }

        }

    }

    private boolean checkEnterKey() throws RemoteException{

        if(virtualClient.getString().isEmpty()){
            return true;
        }
        return false;

    }


    private boolean joinGame(Server centralServer) {

        return centralServer.getLock().tryLock();

    }


    private boolean isEmpty(Server centralServer) {
        return centralServer.getCurrentStartingGame().getGameState() == GameState.Empty;

    }

    private void makeNonFirstPlayerJoin() throws RemoteException{
        addPlayerToGame(false, 0, null);
    }

    private void makeFirstPlayerJoin() throws RemoteException{

        int numberOfPlayers;
        GameType gameType;
        String message;
        String input;

        message = "You are the first player joining the game!";

        virtualClient.printMessage(message);

        message = "Enter the game type (TESTGAME/NORMALGAME): ";

        virtualClient.printMessage(message);

        while (true) {

            input = virtualClient.getString();

            if (!input.equalsIgnoreCase("TESTGAME") && !input.equalsIgnoreCase("NORMALGAME")) {

                message = "The game type you entered is incorrect, please reenter it (TESTGAME/NORMALGAME): ";
                virtualClient.printMessage(message);
                trials++;
                checkTrials();

            } else {

                gameType = GameType.valueOf(input.toUpperCase());
                centralServer.getCurrentStartingGame().getGameInformation().setGameType(gameType);
                message = "Game type was set up correctly";
                virtualClient.printMessage(message);

                break;
            }

        }

        message = "Enter the number of players of the game (2-4): ";

        virtualClient.printMessage(message);

        while (true) {

            try {

                numberOfPlayers = Integer.parseInt(virtualClient.getString());

            } catch (NumberFormatException e) {

                message = "You didn't enter a number! Please enter one: ";
                virtualClient.printMessage(message);
                trials++;
                checkTrials();
                continue;

            }

            if (numberOfPlayers < 2 || numberOfPlayers > 4) {

                message = "The number of players you entered is invalid, please enter a valid value (2-4): ";
                virtualClient.printMessage(message);
                trials++;
                checkTrials();
            } else {

                message = "Number of players was set up correctly";
                virtualClient.printMessage(message);
                break;
            }

        }

        //There are no repeated names as he is the first player.

        addPlayerToGame(true, numberOfPlayers, gameType);

    }

    private void addPlayerToGame(boolean isFirstPlayer, int numberOfPlayers, GameType gameType) throws RemoteException{

        String message;
        Player playerToAdd;

        playerToAdd = new Player(clientInfo.getNickname(), centralServer.getCurrentColor(), centralServer.getCurrentStartingGame().getGameInformation());

        if (isFirstPlayer) {

            centralServer.addPlayerToCurrentStartingGame(playerToAdd, gameType, numberOfPlayers);

        } else {
            centralServer.addPlayerToCurrentStartingGame(playerToAdd);
        }

        message = clientInfo.getNickname() + " joined the game!";
        notifyAllPlayers(message);
        ClientMessenger.getGameMessenger(centralServer.getCurrentGameCode()).addPlayer(playerToAdd, virtualClient);

        if (isFirstPlayer) {
            message = "You have successfully created the game (game code " + centralServer.getCurrentGameCode() + ")";

        } else {
            message = "You have joined the game of " + centralServer.getCurrentStartingGame().getCreator() + " (game code " + centralServer.getCurrentStartingGame().getGameCode() + ")";
        }
        virtualClient.printMessage(message);
        virtualClient.setInputTimeOut(true);
        virtualClient.setInGame(true);

        if (centralServer.getCurrentStartingGame().isFull()) {
            centralServer.startCurrentGame();
        } else {
            virtualClient.printMessage("Waiting for other players to join...");
        }
    }

    private void notifyAllPlayers(String message){

        ClientMessenger.getGameMessenger(centralServer.getCurrentGameCode()).sendShortCutMessageToAll(message);

    }

    private void checkTrials() throws RemoteException{

        if(trials == MAXTRIALS){
            virtualClient.printMessage("You are trying to keep the server busy! Disconnection will happen soon.");
            throw new RemoteException("Player " + clientInfo.getNickname() + " (" + clientInfo.getNickname() + ")" +
                    " was kicked out because of too many input failures. The client probably had" +
                    " malicious intent");
        }


    }

}
