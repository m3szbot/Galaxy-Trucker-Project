package it.polimi.ingsw.Connection.ServerSide;

import it.polimi.ingsw.Connection.ServerSide.RMI.RMICommunicatorImpl;
import it.polimi.ingsw.Controller.Game.GameState;
import it.polimi.ingsw.Model.GameInformation.ConnectionType;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class JoinerThread extends Thread{


    private DataExchanger dataExchanger;
    private Server centralServer;
    private int clientGameCode;
    private Socket clientSocket;
    private String nickName;
    private Integer trials = 0;
    private final int MAXTRIALS = 5;

    public JoinerThread(Socket clientSocket, ConnectionType connectionType, Server centralServer){

        try {

            this.clientSocket = clientSocket;
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            outputStream.flush();
            this.centralServer = centralServer;

            this.nickName = inputStream.readUTF();

            while(centralServer.checkNickname(nickName)){

                outputStream.writeUTF("You're nickname has already been chosen, please enter a new one: ");
                outputStream.flush();
                this.nickName = inputStream.readUTF();

            }

            outputStream.writeUTF("nicknameSet");
            outputStream.flush();
            outputStream.writeUTF(this.nickName);
            outputStream.flush();
            centralServer.addNickName(nickName);

            dataExchanger = new DataExchanger(clientSocket, outputStream, inputStream, connectionType);


        }catch (IOException e){
            System.err.println("Error while opening streams");
        }

    }

    public JoinerThread(RMICommunicatorImpl rmiCommunicator, ConnectionType connectionType, String nickName, Server centralServer){

        dataExchanger = new DataExchanger(rmiCommunicator, nickName, centralServer.getCurrentGameCode(), connectionType);
        this.nickName = nickName;
        dataExchanger.setTrials(trials);
        this.centralServer = centralServer;
        centralServer.addNickName(nickName);

    }

    public void run() {

        try {

            try {

                this.clientGameCode = Integer.parseInt(dataExchanger.receiveMessage(true));

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

        }
        catch (IOException e) {

            if (trials == MAXTRIALS) {
                System.out.println("Client " + clientSocket.getInetAddress() + " (" + nickName + ")" +
                        " was kicked out because of too many input failures. The client probably had" +
                        " malicious intent");
            } else {
                System.err.println("Client " + clientSocket.getInetAddress() + " has disconnected while in lobby");
            }

            try {
                //closing resources
                dataExchanger.closeResources();
                if(clientSocket != null){
                    clientSocket.close();
                }

            } catch (IOException e1) {
                System.err.println("Critical error while closing resources");
            }

            if (centralServer.getLock().isLocked()) {
                centralServer.getLock().unlock();
            }
        }
    }

    private void startLobby()throws IOException {

        String message;

        while (true) {

            message = "Press 'Enter' key to enter in a game: ";

            dataExchanger.sendMessage(message, true);

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

                    dataExchanger.sendMessage(message, true);

                }

            } else {

                message = "The string you entered is invalid!";
                dataExchanger.sendMessage(message, true);
                dataExchanger.sendMessage("increment trials", true);
                trials++;

            }
        }

    }
    /*

    private void startRejoining(int gameCode) {

        Game game;
        String message;

        try {

            game = centralServer.getGame(gameCode);

        } catch (IndexOutOfBoundsException e1) {

            try {

                message = "The game code you entered is invalid!";
                dataSender.writeUTF(message);
                message = "terminate";
                dataSender.writeUTF(message);
                return;

            } catch (IOException e2) {
                System.err.println("Client " + clientSocket.getInetAddress() + " has disconnected!");
            }

        }

        //player rejoin game
        //TODO (BOTI)

    }

     */

    private boolean checkEnterKey() throws IOException {

        if (dataExchanger.receiveMessage(true).isEmpty()) {
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

        dataExchanger.sendMessage(message, true);

        message = "Enter the game type (TESTGAME/NORMALGAME): ";

        dataExchanger.sendMessage(message, true);

        while (true) {

            input = dataExchanger.receiveMessage(true);

            if (!input.equalsIgnoreCase("TESTGAME") && !input.equalsIgnoreCase("NORMALGAME")) {

                message = "The game type you entered is incorrect, please reenter it (TESTGAME/NORMALGAME): ";
                dataExchanger.sendMessage(message, true);
                dataExchanger.sendMessage("increment trials", true);
                trials++;

            } else {

                gameType = GameType.valueOf(input.toUpperCase());
                centralServer.getCurrentStartingGame().getGameInformation().setGameType(gameType);
                message = "Game type was set up correctly";
                dataExchanger.sendMessage(message, true);

                break;
            }

        }

        message = "Enter the number of players of the game (2-4): ";

        dataExchanger.sendMessage(message, true);

        while (true) {

            try {

                numberOfPlayers = Integer.parseInt(dataExchanger.receiveMessage(true));

            } catch (NumberFormatException e) {

                message = "You didn't enter a number! Please enter one: ";
                dataExchanger.sendMessage(message, true);
                continue;

            }

            if (numberOfPlayers < 2 || numberOfPlayers > 4) {

                message = "The number of players you entered is invalid, please enter a valid value (2-4): ";
                dataExchanger.sendMessage(message, true);
                dataExchanger.sendMessage("increment trials", true);
                trials++;
            } else {

                message = "Number of players was set up correctly";
                dataExchanger.sendMessage(message, true);
                break;
            }

        }

        //There are no repeated names as he is the first player.

        addPlayerToGame(true, numberOfPlayers, gameType);

    }

    private void makeNonFirstPlayerJoin() throws IOException {

        addPlayerToGame(false, 0, null);

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
        ClientMessenger.getGameMessenger(centralServer.getCurrentGameCode()).addPlayer(playerToAdd, dataExchanger);

        if (isFirstPlayer) {
            message = "You have successfully created the game (game code " + centralServer.getCurrentGameCode() + ")";

        } else {
            message = "You have joined the game of " + centralServer.getCurrentStartingGame().getCreator() + " (game code " + centralServer.getCurrentStartingGame().getGameCode() + ")";
        }
        dataExchanger.sendMessage(message, true);
        dataExchanger.sendMessage("added", true);

        if (centralServer.getCurrentStartingGame().isFull()) {
            notifyAllPlayers("start");
            centralServer.startCurrentGame();
        }

        dataExchanger.sendMessage("Waiting for other players to join...", true);

    }

    private void notifyAllPlayers(String message){

        ClientMessenger.getGameMessenger(centralServer.getCurrentGameCode()).sendMessageToAll(message);

    }


}
