package it.polimi.ingsw.Connection.ClientSide.RMI;

import it.polimi.ingsw.Connection.ClientSide.ClientInfo;
import it.polimi.ingsw.Connection.ConnectionType;
import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.Server;
import it.polimi.ingsw.Controller.Game.GameState;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.concurrent.atomic.AtomicReference;

public class JoinerUtil {

    private ClientInfo clientInfo;
    private int trials;
    private final int MAXTRIALS = 5;
    private Server centralServer;
    private AtomicReference<String> userInput;
    private VirtualClient virtualClient;

    public JoinerUtil(ClientInfo clientInfo, Server centralServer, VirtualClient virtualClient){
        this.clientInfo = clientInfo;
        this.centralServer = centralServer;
        this.virtualClient = virtualClient;
        this.trials = 0;
        this.userInput = clientInfo.getUserInput();
    }

    public void start() throws RemoteException {

        if(clientInfo.getGameCode() != -1){
            //joining an existing game
        }
        else{
            startLobby();
        }

    }


    public String getString(){

        while(userInput.get() == null);
        return userInput.getAndSet(null);

    }

    public void checkUsername(){

        String message;

        while(centralServer.checkNickname(clientInfo.getNickname())){

            message = "nickname '" + clientInfo.getNickname() + "' has already been chosen, please enter a new one: ";
            System.out.println(message);
            clientInfo.setNickname(getString());

        }

        centralServer.addNickName(clientInfo.getNickname());
    }


    private void startLobby() throws RemoteException{

        String message;

        checkUsername();

        while(true){

            message = "Press 'Enter' key to enter in a game: ";

            System.out.println(message);

            if(checkEnterKey()){

                if(joinGame(centralServer)){

                    if(isEmpty(centralServer)){
                        makeFirstPlayerJoin();
                    }
                    else{
                        makeNonFirstPlayerJoin();
                    }

                    centralServer.getLock().unlock();
                    break;

                }
                else{

                    message = "Somebody is already joining a new game, please wait.";
                    System.out.println(message);
                }
            }
            else{

                message = "The string you entered is invalid!";
                System.out.println(message);
                trials++;
                checkTrials();
            }

        }

    }

    private boolean checkEnterKey(){

        if(getString().isEmpty()){
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

        System.out.println(message);

        message = "Enter the game type (TESTGAME/NORMALGAME): ";

        System.out.println(message);

        while (true) {

            input = getString();

            if (!input.equalsIgnoreCase("TESTGAME") && !input.equalsIgnoreCase("NORMALGAME")) {

                message = "The game type you entered is incorrect, please reenter it (TESTGAME/NORMALGAME): ";
                System.out.println(message);
                trials++;
                checkTrials();

            } else {

                gameType = GameType.valueOf(input.toUpperCase());
                centralServer.getCurrentStartingGame().getGameInformation().setGameType(gameType);
                message = "Game type was set up correctly";
                System.out.println(message);

                break;
            }

        }

        message = "Enter the number of players of the game (2-4): ";

        System.out.println(message);

        while (true) {

            try {

                numberOfPlayers = Integer.parseInt(getString());

            } catch (NumberFormatException e) {

                message = "You didn't enter a number! Please enter one: ";
                System.out.println(message);
                trials++;
                checkTrials();
                continue;

            }

            if (numberOfPlayers < 2 || numberOfPlayers > 4) {

                message = "The number of players you entered is invalid, please enter a valid value (2-4): ";
                System.out.println(message);
                trials++;
                checkTrials();
            } else {

                message = "Number of players was set up correctly";
                System.out.println(message);
                break;
            }

        }

        //There are no repeated names as he is the first player.

        addPlayerToGame(true, numberOfPlayers, gameType);

    }

    private void addPlayerToGame(boolean isFirstPlayer, int numberOfPlayers, GameType gameType){

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
        ClientMessenger.getGameMessenger(centralServer.getCurrentGameCode()).addPlayer(playerToAdd, ConnectionType.RMI, virtualClient);

        if (isFirstPlayer) {
            message = "You have successfully created the game (game code " + centralServer.getCurrentGameCode() + ")";

        } else {
            message = "You have joined the game of " + centralServer.getCurrentStartingGame().getCreator() + " (game code " + centralServer.getCurrentStartingGame().getGameCode() + ")";
        }
        System.out.println(message);

        if (centralServer.getCurrentStartingGame().isFull()) {
            centralServer.startCurrentGame();
        } else {
            System.out.println("Waiting for other players to join...");
        }
    }

    private void notifyAllPlayers(String message){

        ClientMessenger.getGameMessenger(centralServer.getCurrentGameCode()).sendShortCutMessageToAll(message);

    }

    private void checkTrials() throws RemoteException{

        if(trials == MAXTRIALS){
            try {
                throw new RemoteException("Client " + InetAddress.getLocalHost().getHostAddress() + " (" + clientInfo.getNickname() + ")" +
                        " was kicked out because of too many input failures. The client probably had" +
                        " malicious intent");
            } catch (UnknownHostException e) {
                System.err.println("Unknown host: critical error");
            }
        }

    }

}
