package it.polimi.ingsw.Connection.ServerSide.socket;

import it.polimi.ingsw.Connection.ConnectionType;
import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.Server;
import it.polimi.ingsw.Controller.Game.GameState;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ClientSocketHandler extends Thread {


    private final int MAXTRIALS = 5;
    private SocketDataExchanger dataExchanger;
    private Server centralServer;
    private int clientGameCode;
    private Socket clientSocket;
    private String nickName;
    private Integer trials = 0;

    public ClientSocketHandler(Socket clientSocket, Server centralServer) throws IOException {

        this.clientSocket = clientSocket;
        ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
        ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        outputStream.flush();
        this.centralServer = centralServer;

        dataExchanger = new SocketDataExchanger(clientSocket, inputStream, outputStream);

        nickName = dataExchanger.getString();

    }

    private void changePlayerUsername() throws IOException {

        String message;

        while (centralServer.checkNickname(nickName)) {

            message = "nickname '" + nickName + "' has already been chosen, please enter a new one: ";
            dataExchanger.sendString(message);
            nickName = dataExchanger.getString();

        }

    }

    public void run() {

        try {

            if (centralServer.checkNickname(nickName)) {

                changePlayerUsername();

            }

            dataExchanger.sendString("nickname updated");
            dataExchanger.sendString(nickName);
            centralServer.addNickName(nickName);

            try {

                this.clientGameCode = Integer.parseInt(dataExchanger.getString());

            } catch (NumberFormatException e) {
                System.err.println("Error while receiving client game code");
            }

            if (clientGameCode != -1) {
                //tries to rejoin
                /*
                startRejoining(clientGameCode);

                 */
            } else {
                startLobby();
            }

        } catch (IOException e) {

            try {

                centralServer.removeNickName(nickName);

            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
            }

            if (trials == MAXTRIALS) {
                System.out.println("Client " + clientSocket.getInetAddress() + " (" + nickName + ")" +
                        " was kicked out because of too many input failures. The client probably had" +
                        " malicious intent");
            } else if (e instanceof SocketTimeoutException) {
                System.out.println("Player was kicked out because of inactivity");
            } else {
                System.out.println("Client " + clientSocket.getInetAddress() + " has disconnected while joining a game");
            }

            try {
                dataExchanger.closeResources();

            } catch (IOException e1) {
                System.err.println("Critical error while closing " + clientSocket.getInetAddress() + " resources");
            }

            if (centralServer.getLock().isLocked()) {
                centralServer.getLock().unlock();
            }
        }
    }

    private void startLobby() throws IOException {

        String message;

        while (true) {

            message = "Press 'Enter' key to enter in a game: ";

            dataExchanger.sendString(message);

            if (checkEnterKey()) {

                if (joinGame()) {

                    if (isEmpty()) {
                        //first player joining
                        makeFirstPlayerJoin();
                    } else {
                        //not first player joining
                        makeNonFirstPlayerJoin();
                    }

                    centralServer.getLock().unlock();

                    break;

                } else {
                    message = "Somebody is already joining a new game, please wait.";

                    dataExchanger.sendString(message);

                }

            } else {

                message = "The string you entered is invalid!";
                dataExchanger.sendString(message);
                trials++;
                checkTrials();

            }
        }

    }

    //TODO
    private void startRejoining(int gameCode) {


    }

    private boolean checkEnterKey() throws IOException {

        if (dataExchanger.getString().isEmpty()) {
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

    private void makeFirstPlayerJoin() throws IOException {

        int numberOfPlayers;
        GameType gameType;
        String message;
        String input;

        message = "You are the first player joining the game!";

        dataExchanger.sendString(message);

        message = "Enter the game type (TESTGAME/NORMALGAME): ";

        dataExchanger.sendString(message);

        while (true) {

            input = dataExchanger.getString();

            if (!input.equalsIgnoreCase("TESTGAME") && !input.equalsIgnoreCase("NORMALGAME")) {

                message = "The game type you entered is incorrect, please reenter it (TESTGAME/NORMALGAME): ";
                dataExchanger.sendString(message);
                trials++;
                checkTrials();

            } else {

                gameType = GameType.valueOf(input.toUpperCase());
                centralServer.getCurrentStartingGame().getGameInformation().setGameType(gameType);
                message = "Game type was set up correctly";
                dataExchanger.sendString(message);

                break;
            }

        }

        message = "Enter the number of players of the game (2-4): ";

        dataExchanger.sendString(message);

        while (true) {

            try {

                numberOfPlayers = Integer.parseInt(dataExchanger.getString());

            } catch (NumberFormatException e) {

                message = "You didn't enter a number! Please enter one: ";
                dataExchanger.sendString(message);
                trials++;
                checkTrials();
                continue;

            }

            if (numberOfPlayers < 2 || numberOfPlayers > 4) {

                message = "The number of players you entered is invalid, please enter a valid value (2-4): ";
                dataExchanger.sendString(message);
                trials++;
                checkTrials();
            } else {

                message = "Number of players was set up correctly";
                dataExchanger.sendString(message);
                break;
            }

        }

        //There are no repeated names as he is the first player.

        addPlayerToGame(true, numberOfPlayers, gameType);

    }

    private void makeNonFirstPlayerJoin() throws IOException {

        addPlayerToGame(false, 0, null);

    }

    private void checkTrials() throws IOException {
        if (trials == MAXTRIALS) {
            throw new IOException();
        }
    }

    private void addPlayerToGame(boolean isFirstPlayer, int numberOfPlayers, GameType gameType) throws IOException {
        String message;
        Player playerToAdd;

        playerToAdd = new Player(nickName, centralServer.getCurrentColor(), centralServer.getCurrentStartingGame().getGameInformation());

        if (isFirstPlayer) {
            centralServer.addPlayerToCurrentStartingGame(playerToAdd, gameType, numberOfPlayers);

        } else {
            centralServer.addPlayerToCurrentStartingGame(playerToAdd);
        }

        message = nickName + " joined the game!";
        notifyAllPlayers(message);
        ClientMessenger.getGameMessenger(centralServer.getCurrentGameCode()).addPlayer(playerToAdd, ConnectionType.SOCKET, dataExchanger);

        if (isFirstPlayer) {
            message = "You have successfully created the game (game code " + centralServer.getCurrentGameCode() + ")";

        } else {
            message = "You have joined the game of " + centralServer.getCurrentStartingGame().getCreator() + " (game code " + centralServer.getCurrentStartingGame().getGameCode() + ")";
        }
        dataExchanger.sendString(message);
        dataExchanger.sendString("added");
        dataExchanger.setSocketTimeOut(0);

        if (centralServer.getCurrentStartingGame().isFull()) {
            notifyAllPlayers("start");
            centralServer.startCurrentGame();
        } else {
            dataExchanger.sendString("Waiting for other players to join...");
        }

    }

    private void notifyAllPlayers(String message) throws IOException {

        ClientMessenger.getGameMessenger(centralServer.getCurrentGameCode()).sendShortCutMessageToAll(message);

    }

}
