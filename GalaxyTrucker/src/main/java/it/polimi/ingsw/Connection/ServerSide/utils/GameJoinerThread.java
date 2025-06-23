package it.polimi.ingsw.Connection.ServerSide.utils;

import it.polimi.ingsw.Connection.ClientSide.RMI.ClientRemoteInterface;
import it.polimi.ingsw.Connection.ConnectionType;
import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.Server;
import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerLobbyMessenger;
import it.polimi.ingsw.Connection.ServerSide.socket.SocketDataExchanger;
import it.polimi.ingsw.Controller.Game.GameState;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class GameJoinerThread extends Thread {

    private String nickName;
    private int trials;
    private final int MAXTRIALS = 5;
    private PlayerLobbyMessenger playerLobbyMessenger;
    private Server centralServer;

    public GameJoinerThread(Server centralServer, String nickName, SocketDataExchanger socketDataExchanger) throws IOException {



        this.trials = 0;
        this.nickName = nickName;
        this.centralServer = centralServer;
        ClientMessenger.addPlayerInLobby(nickName, socketDataExchanger);
        this.playerLobbyMessenger = ClientMessenger.getPlayerLobbyMessenger(nickName);

    }

    public GameJoinerThread(Server centralServer, ClientRemoteInterface virtualClient, String nickName) {

        this.centralServer = centralServer;
        this.nickName = nickName;
        ClientMessenger.addPlayerInLobby(nickName, virtualClient);
        this.trials = 0;
        this.playerLobbyMessenger = ClientMessenger.getPlayerLobbyMessenger(nickName);

    }


    public void run() {

        centralServer.addNickName(nickName);

        try {

            startLobby();

        } catch (IOException e) {
            //too many wrong inputs

            try {

                centralServer.removeNickName(nickName);

            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
            }

            System.out.println("Player " + nickName +
                    " was kicked out because of too many input failures. The client probably had" +
                    " malicious intent");

            playerLobbyMessenger.cleanResources();
        } catch (TimeoutException e) {

            try {

                centralServer.removeNickName(nickName);

            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
            }

            System.out.println("Player " + nickName + " was kicked out because of inactivity");
            playerLobbyMessenger.cleanResources();

        } catch (PlayerDisconnectedException e) {

            try {

                centralServer.removeNickName(nickName);

            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
            }

            System.out.println("Player " + nickName + " disconnected!");
            playerLobbyMessenger.cleanResources();

        }
        finally {
            //releasing the lock
            if(centralServer.getLock().isHeldByCurrentThread()) {
                centralServer.getLock().unlock();
            }
        }
    }

    private boolean askToJoin() throws PlayerDisconnectedException, TimeoutException, IOException{

        if(centralServer.getCurrentStartingGame().getCreator() == null){
            //the game is not yet created
            return true;
        }

        playerLobbyMessenger.printMessage("The game currently starting is the following: ");
        playerLobbyMessenger.printMessage("Game type: " + centralServer.getCurrentStartingGame().getGameInformation().getGameType());
        playerLobbyMessenger.printMessage("Game creator: " + centralServer.getCurrentStartingGame().getCreator());
        playerLobbyMessenger.printMessage("Game code: " + centralServer.getCurrentGameCode() );

        playerLobbyMessenger.printMessage("\nWould you like to join it ?(yes/no): ");

        while(true) {

            String input = playerLobbyMessenger.getPlayerString();

            if (input.equals("yes")) {
                return true;
            } else if (input.equals("no")) {
                return false;
            } else {

                playerLobbyMessenger.printMessage("Please enter a correct answer!(yes/no): ");
                trials++;
                checkTrials();

            }

        }

    }

    private void startLobby() throws IOException, PlayerDisconnectedException, TimeoutException {

        String message;

        while (true) {

            message = "Press 'Enter' key to enter in a game: ";

            playerLobbyMessenger.printMessage(message);

            if (checkEnterKey()) {

                if (joinGame()) {

                    if(askToJoin()) {

                        if (isEmpty()) {
                            //first player joining
                            makeFirstPlayerJoin();
                        } else {
                            //not first player joining
                            makeNonFirstPlayerJoin();
                        }

                        centralServer.getLock().unlock();
                        break;

                    }


                } else {
                    message = "Somebody is already joining a new game, please wait.";

                    playerLobbyMessenger.printMessage(message);

                }

            } else {

                message = "The string you entered is invalid!";
                playerLobbyMessenger.printMessage(message);
                trials++;
                checkTrials();

            }
        }

    }

    //TODO
    private void startRejoining(int gameCode) {


    }

    private boolean checkEnterKey() throws PlayerDisconnectedException, TimeoutException {

        if (playerLobbyMessenger.getPlayerString().isEmpty()) {
            return true;
        }
        return false;
    }

    private boolean joinGame() {

        return centralServer.getLock().tryLock();

    }

    private boolean isEmpty() {
        return centralServer.getCurrentStartingGame().getGameState() == GameState.Empty;
    }

    private void makeFirstPlayerJoin() throws PlayerDisconnectedException, IOException, TimeoutException {

        int numberOfPlayers;
        GameType gameType;
        String message;
        String input;

        message = "You are the first player joining the game!";

        playerLobbyMessenger.printMessage(message);

        message = "Enter the game type (TESTGAME/NORMALGAME): ";

        playerLobbyMessenger.printMessage(message);

        while (true) {

            input = playerLobbyMessenger.getPlayerString();

            if (!input.equalsIgnoreCase("TESTGAME") && !input.equalsIgnoreCase("NORMALGAME")) {

                message = "The game type you entered is incorrect, please reenter it (TESTGAME/NORMALGAME): ";
                playerLobbyMessenger.printMessage(message);
                trials++;
                checkTrials();

            } else {

                gameType = GameType.valueOf(input.toUpperCase());
                centralServer.getCurrentStartingGame().getGameInformation().setGameType(gameType);
                message = "Game type was set up correctly";
                playerLobbyMessenger.printMessage(message);

                break;
            }

        }

        message = "Enter the number of players of the game (2-4): ";
        playerLobbyMessenger.printMessage(message);

        while (true) {

            try {

                numberOfPlayers = Integer.parseInt(playerLobbyMessenger.getPlayerString());

            } catch (NumberFormatException e) {

                message = "You didn't enter a number! Please enter one: ";
                playerLobbyMessenger.printMessage(message);
                trials++;
                checkTrials();
                continue;

            }

            if (numberOfPlayers < 2 || numberOfPlayers > 4) {

                message = "The number of players you entered is invalid, please enter a valid value (2-4): ";
                playerLobbyMessenger.printMessage(message);
                trials++;
                checkTrials();
            } else {

                message = "Number of players was set up correctly";
                playerLobbyMessenger.printMessage(message);
                break;
            }

        }

        //There are no repeated names as he is the first player.

        addPlayerToGame(true, numberOfPlayers, gameType);

    }

    private void makeNonFirstPlayerJoin() throws PlayerDisconnectedException {

        addPlayerToGame(false, 0, null);

    }

    private void checkTrials() throws IOException {
        if (trials == MAXTRIALS) {
            throw new IOException();
        }
    }

    private void addPlayerToGame(boolean isFirstPlayer, int numberOfPlayers, GameType gameType) throws PlayerDisconnectedException {
        String message;
        Player playerToAdd;

        playerToAdd = new Player(nickName, centralServer.getCurrentColor(), centralServer.getCurrentStartingGame().getGameInformation());

        if (isFirstPlayer) {
            centralServer.addPlayerToCurrentStartingGame(playerToAdd, gameType, numberOfPlayers);

        } else {
            centralServer.addPlayerToCurrentStartingGame(playerToAdd);
        }

        ClientMessenger.removePlayerLobbyMessenger(nickName);

        message = nickName + " joined the game!";

        notifyAllPlayers(message);

        if(playerLobbyMessenger.getConnectionType() == ConnectionType.SOCKET) {

            ClientMessenger.getGameMessenger(centralServer.getCurrentGameCode()).addPlayer(playerToAdd, playerLobbyMessenger.getDataExchanger());

        }else {

            ClientMessenger.getGameMessenger(centralServer.getCurrentGameCode()).addPlayer(playerToAdd, playerLobbyMessenger.getVirtualClient());
        }

        if (isFirstPlayer) {
            message = "You have successfully created the game (game code " + centralServer.getCurrentGameCode() + ")";

        } else {
            message = "You have joined the game of " + centralServer.getCurrentStartingGame().getCreator() + " (game code " + centralServer.getCurrentStartingGame().getGameCode() + ")";
        }

        playerLobbyMessenger.printMessage(message);
        playerLobbyMessenger.sendCommand("joined");
        playerLobbyMessenger.sendGameType(centralServer.getCurrentStartingGame().getGameInformation().getGameType());

        if (centralServer.getCurrentStartingGame().isFull()) {
            ClientMessenger.getGameMessenger(centralServer.getCurrentGameCode()).exitLobby();
            centralServer.startCurrentGame();
        } else {
            playerLobbyMessenger.printMessage("Waiting for other players to join...");
        }

    }

    private void notifyAllPlayers(String message) {

        ClientMessenger.getGameMessenger(centralServer.getCurrentGameCode()).sendMessageToAll(message);

    }
}
