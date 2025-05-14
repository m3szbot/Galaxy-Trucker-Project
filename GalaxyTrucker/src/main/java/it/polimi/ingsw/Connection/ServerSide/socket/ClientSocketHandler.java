package it.polimi.ingsw.Connection.ServerSide.socket;

import it.polimi.ingsw.Connection.ClientSide.ClientInfo;
import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.Server;
import it.polimi.ingsw.Controller.Game.Game;
import it.polimi.ingsw.Controller.Game.GameState;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Handles the connected client with socket protocol during
 * the joining phase of the client's lifecycle
 *
 * @author carlo
 */

public class ClientSocketHandler extends Thread {

    private Socket clientSocket;
    private Server centralServer;
    private ObjectInputStream dataReceiver;
    private ObjectOutputStream dataSender;
    private Player playerToAdd = null;
    private String nickName;
    private int trials;
    private static final int MAXTRIALS = 5;


    public ClientSocketHandler(Socket clientSocket, Server centralServer) {
        this.clientSocket = clientSocket;
        this.centralServer = centralServer;
        this.trials = 0;

        try {
            dataReceiver = new ObjectInputStream(clientSocket.getInputStream());
            dataSender = new ObjectOutputStream(clientSocket.getOutputStream());
            dataSender.flush();


        } catch (IOException e) {
            System.err.println("Error while opening streams");
        }

    }

    @Override
    public void run() {

        ClientInfo clientInfo;


        try {
            clientInfo = (ClientInfo) dataReceiver.readObject();
            this.nickName = clientInfo.getNickname();

        } catch (IOException e) {
            System.err.println("Error while opening clientInfo");
            return;
        } catch (ClassNotFoundException e) {
            System.err.println("Class 'clientInfo' not found");
            return;
        }

        if(clientInfo.getGameCode() != -1){
           //tries to rejoin
           startRejoining(clientInfo.getGameCode());
        }
        else{
            startLobby();
        }
    }

    private void startLobby(){

        String message;

        while(true) {

            try {

                message = "Press 'enter' key to enter in a game: ";

                dataSender.writeUTF(message);
                dataSender.flush();

                if (checkEnterKey()) {

                    if(joinGame()){

                       if(isEmpty()){
                           //first player joining
                           makeFirstPlayerJoin();
                       }
                       else{
                           //not first player joining
                           makeNonFirstPlayerJoin();
                       }

                       centralServer.getLock().unlock();


                       break;

                    }
                    else{
                        message = "Somebody is already joining a new game, please wait.";

                        dataSender.writeUTF(message);
                        dataSender.flush();
                    }

                }
                else{

                    message = "The string you entered is invalid!";
                    dataSender.writeUTF(message);
                    dataSender.flush();
                    dataSender.writeUTF("increment trials");
                    dataSender.flush();
                    trials++;

                }

            }
            catch (SocketTimeoutException e){

                message = "The server kicked you out because of inactivity!";
                try {
                    dataSender.writeUTF(message);
                    dataSender.flush();
                    message = "terminate";
                    dataSender.writeUTF(message);
                    dataSender.flush();
                } catch (IOException ex) {
                    System.err.println("Error while sending data to the client");
                }

                System.out.println("Client + " + clientSocket.getInetAddress() + " was kicked out because of inactivity");

                try {
                    //closing resources
                    dataReceiver.close();
                    dataSender.close();
                    clientSocket.close();
                } catch (IOException e2) {
                    System.err.println("Critical error while closing client socket and streams");
                }

                if(centralServer.getLock().isLocked()){
                    centralServer.getLock().unlock();
                }

                break;

            }
            catch (IOException e1) {

                if(trials == MAXTRIALS){
                    System.out.println("Client " + clientSocket.getInetAddress() + " (" + nickName + ")" +
                            " was kicked out because of too many input failures. The client probably had" +
                            " malicious intent");
                }
                else {
                    System.err.println("Client " + clientSocket.getInetAddress() + " has disconnected while in lobby");
                }

                try {
                    //closing resources
                    dataSender.close();
                    dataReceiver.close();
                    clientSocket.close();
                } catch (IOException e2) {
                    System.err.println("Critical error while closing client socket and streams");
                }

                if(centralServer.getLock().isLocked()){
                    centralServer.getLock().unlock();
                }

                break;

            }


        }

    }

    private void startRejoining(int gameCode){

        Game game;
        String message;

        try {

           game = centralServer.getGame(gameCode);

        }catch (IndexOutOfBoundsException e1){

            try {

                message = "The game code you entered is invalid!";
                dataSender.writeUTF(message);
                message = "terminate";
                dataSender.writeUTF(message);
                return;

            }catch (IOException e2){
                System.err.println("Client " + clientSocket.getInetAddress() + "has disconnected!");
            }

        }

        //player rejoin game
        //TODO (BOTI)

    }

    private boolean checkEnterKey() throws IOException {

        if(dataReceiver.readUTF().isEmpty()){
            return true;
        }
        return false;
    }

    private boolean joinGame(){

       return centralServer.getLock().tryLock();

    }

    private boolean isEmpty(){
        return centralServer.getCurrentStartingGame().getGameState() == GameState.Empty;
    }

    private void makeFirstPlayerJoin() throws IOException{

        int numberOfPlayers;
        GameType gameType;
        String message;
        String input;

        message = "You are the first player joining the game!";


        dataSender.writeUTF(message);
        dataSender.flush();

        message = "Enter the game type (TESTGAME/NORMALGAME): ";

        dataSender.writeUTF(message);
        dataSender.flush();

        while (true) {


            input = dataReceiver.readUTF();


            input = input.toUpperCase();

            if (!input.equals("TESTGAME") && !input.equals("NORMALGAME")) {

                message = "The game type you entered is incorrect, please reenter it (TESTGAME/NORMALGAME): ";
                dataSender.writeUTF(message);
                dataSender.flush();
                dataSender.writeUTF("increment trials");
                dataSender.flush();
                trials++;

            } else {

                gameType = GameType.valueOf(input);
                centralServer.getCurrentStartingGame().getGameInformation().setGameType(gameType);
                message = "Game type was set up correctly";
                dataSender.writeUTF(message);
                dataSender.flush();

                break;
            }

        }

        message = "Enter the number of players of the game (2-4): ";

        dataSender.writeUTF(message);
        dataSender.flush();

        while (true) {

            try {

                numberOfPlayers = Integer.parseInt( dataReceiver.readUTF());

            }catch (NumberFormatException e){

                message = "You didn't enter a number! Please enter one: ";
                dataSender.writeUTF(message);
                dataSender.flush();
                continue;

            }

            if (numberOfPlayers < 2 || numberOfPlayers > 4) {

                message = "The number of players you entered is invalid, please enter a valid value (2-4): ";
                dataSender.writeUTF(message);
                dataSender.flush();
                dataSender.writeUTF("increment trials");
                dataSender.flush();
                trials++;
            } else {

                message = "Number of players was set up correctly";
                dataSender.writeUTF(message);
                dataSender.flush();
                break;
            }

        }

        //There are no repeated names as he is the first player.

        addPlayerToGame(true, numberOfPlayers, gameType);

    }

    private void makeNonFirstPlayerJoin() throws IOException{

        String message;
        String input;

        if (centralServer.getCurrentStartingGame().isNickNameRepeated(nickName)){

            while (true) {

                message = "You're nickname has already been chosen, please enter a new one: ";

                dataSender.writeUTF(message);
                dataSender.flush();

                input = dataReceiver.readUTF();


                if (!centralServer.getCurrentStartingGame().isNickNameRepeated(input)) {
                    message = "You're nickname is now " + input;
                    dataSender.writeUTF(message);
                    dataSender.flush();
                    nickName = input;
                    break;
                }

                dataSender.writeUTF("increment trials");
                dataSender.flush();
                trials++;

            }

        }

        addPlayerToGame(false, 0, null);

    }

    private void addPlayerToGame(boolean isFirstPlayer, int numberOfPlayers, GameType gameType) throws IOException{
        String message;

        playerToAdd = new Player(nickName, centralServer.getCurrentColor(), centralServer.getCurrentStartingGame().getGameInformation());

        if(isFirstPlayer){
            centralServer.addPlayerToCurrentStartingGame(playerToAdd, gameType, numberOfPlayers);
        }
        else {
            centralServer.addPlayerToCurrentStartingGame(playerToAdd);
        }
        message = nickName + " joined the game!";
        notifyAllPlayers(message);
        ClientMessenger.getGameMessenger(centralServer.getCurrentGameCode()).addPlayerSocket(playerToAdd, clientSocket, dataSender, dataReceiver);

        message = "You have joined the game of " + centralServer.getCurrentStartingGame().getCreator() + " (game code " + centralServer.getCurrentStartingGame().getGameCode() + ")";
        dataSender.writeUTF(message);
        dataSender.flush();
        dataSender.writeUTF("added");
        dataSender.flush();

        //nickname might be changed
        dataSender.writeUTF(nickName);
        dataSender.flush();

        if (centralServer.getCurrentStartingGame().isFull()) {
            notifyAllPlayers("start");
            centralServer.startCurrentGame();
        }

        dataSender.writeUTF("Waiting for other players to join...");
        dataSender.flush();

    }

    private void notifyAllPlayers(String message) throws IOException{

        for(ObjectOutputStream out: ClientMessenger.getGameMessenger(centralServer.getCurrentGameCode()).getPlayersOutputStreams()){
            out.writeUTF(message);
            out.flush();
        }

    }
}
