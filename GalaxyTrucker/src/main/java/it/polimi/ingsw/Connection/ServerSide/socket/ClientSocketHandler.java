package it.polimi.ingsw.Connection.ServerSide.socket;

import it.polimi.ingsw.Connection.ClientSide.ClientInfo;
import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.Server;
import it.polimi.ingsw.Controller.Game.GameState;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.io.*;
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


    public ClientSocketHandler(Socket clientSocket, Server centralServer) {
        this.clientSocket = clientSocket;
        this.centralServer = centralServer;

        try {
            dataReceiver = new ObjectInputStream(clientSocket.getInputStream());
            dataSender = new ObjectOutputStream(clientSocket.getOutputStream());
            dataSender.flush();
            System.out.println("Output stream aperto e flushato");
            System.out.println(clientSocket.isConnected());


        } catch (IOException e) {
            System.err.println("Error while opening streams");
        }

    }

    @Override
    public void run() {

        String message;
        String input;
        ClientInfo clientInfo;

        try {
            clientInfo = (ClientInfo) dataReceiver.readObject();

        } catch (IOException e) {
            System.err.println("Error while opening clientInfo");
            return;
        } catch (ClassNotFoundException e) {
            System.err.println("Class 'clientInfo' not found");
            return;
        }

        while (true) {

            try {

                message = "Press 'enter' key to enter in a game: ";

                dataSender.writeObject(message);
                dataSender.flush();

                try {
                    if (((String)dataReceiver.readObject()).isEmpty()) {

                        if (centralServer.getLock().tryLock()) {

                            if (centralServer.getCurrentStartingGame().getGameState() == GameState.Empty) {
                                //first player

                                int numberOfPlayers;
                                GameType gameType;

                                message = "You are the first player joining the game!";


                                dataSender.writeObject(message);
                                dataSender.flush();

                                message = "Enter the game type (TESTGAME/NORMALGAME): ";

                                dataSender.writeObject(message);
                                dataSender.flush();

                                while (true) {


                                    try {
                                        input = (String)dataReceiver.readObject();
                                    } catch (ClassNotFoundException e) {
                                        input = null;
                                        System.err.println("Error: String class not found");
                                        return;
                                    }

                                    input = input.toUpperCase();

                                    if (!input.equals("TESTGAME") && !input.equals("NORMALGAME")) {

                                        message = "The game type you entered is incorrect, please reenter it (TESTGAME/NORMALGAME): ";
                                        dataSender.writeObject(message);
                                        dataSender.flush();
                                        dataSender.writeObject("increment trials");
                                        dataSender.flush();

                                    } else {

                                        gameType = GameType.valueOf(input);
                                        message = "Game type was set up correctly";
                                        dataSender.writeObject(message);
                                        dataSender.flush();

                                        break;
                                    }

                                }

                                message = "Enter the number of players of the game (2-4): ";

                                dataSender.writeObject(message);
                                dataSender.flush();

                                while (true) {

                                    numberOfPlayers = (Integer)dataReceiver.readObject();

                                    if (numberOfPlayers < 2 || numberOfPlayers > 4) {

                                        message = "The number of players you entered is invalid, please enter a valid value (2-4): ";
                                        dataSender.writeObject(message);
                                        dataSender.flush();
                                        dataSender.writeObject("increment trials");
                                        dataSender.flush();
                                    } else {

                                        message = "Number of players was set up correctly";
                                        dataSender.writeObject(message);
                                        dataSender.flush();
                                        break;
                                    }

                                }

                                //There are no repeated names as he is the first player.

                                playerToAdd = new Player(clientInfo.getNickname(), centralServer.getCurrentColor(), centralServer.getCurrentStartingGame().getGameInformation());
                                clientInfo.setGameCode(centralServer.getCurrentStartingGame().getGameCode());

                                centralServer.addPlayerToCurrentStartingGame(playerToAdd, gameType, numberOfPlayers);
                                ClientMessenger.getGameMessenger(centralServer.getCurrentGameCode()).addPlayerSocket(playerToAdd, clientSocket);

                                message = "You have been added to the game (game code " + centralServer.getCurrentStartingGame().getGameCode() + " )";
                                dataSender.writeObject(message);
                                dataSender.flush();
                                dataSender.writeObject("added");
                                dataSender.flush();
                                dataSender.writeObject(clientInfo);
                                dataSender.flush();
                                centralServer.getCurrentStartingGame().changeGameState(GameState.Starting);


                            } else {
                                //not first player

                                if (centralServer.getCurrentStartingGame().isNickNameRepeated(clientInfo.getNickname())) {

                                    while (true) {

                                        message = "You're nickname has already been chosen, please enter a new one: ";

                                        dataSender.writeObject(message);
                                        dataSender.flush();

                                        try {
                                            input = (String)dataReceiver.readObject();
                                        } catch (ClassNotFoundException e) {
                                            System.err.println("Error: String class not found");
                                            return;
                                        }

                                        if (!centralServer.getCurrentStartingGame().isNickNameRepeated(input)) {
                                            message = "You're nickname is now " + input;
                                            dataSender.writeObject(message);
                                            dataSender.flush();
                                            clientInfo.setNickname(input);
                                            break;
                                        }

                                        dataSender.writeObject("increment trials");
                                        dataSender.flush();

                                    }

                                }


                                playerToAdd = new Player(clientInfo.getNickname(), centralServer.getCurrentColor(), centralServer.getCurrentStartingGame().getGameInformation());
                                clientInfo.setGameCode(centralServer.getCurrentStartingGame().getGameCode());

                                centralServer.addPlayerToCurrentStartingGame(playerToAdd);
                                ClientMessenger.getGameMessenger(centralServer.getCurrentGameCode()).addPlayerSocket(playerToAdd, clientSocket);

                                message = "You have joined the game of " + centralServer.getCurrentStartingGame().getCreator() + " (game code " + centralServer.getCurrentStartingGame().getGameCode() + ")";
                                dataSender.writeObject(message);
                                dataSender.flush();
                                dataSender.writeObject("added");
                                dataSender.flush();
                                dataSender.writeObject(clientInfo);
                                dataSender.flush();


                                if (centralServer.getCurrentStartingGame().isFull()) {
                                    centralServer.startCurrentGame();
                                }

                            }

                            centralServer.getLock().unlock();
                            break;

                        } else {

                            message = "Somebody is already joining a new game, please wait.";

                            dataSender.writeObject(message);
                            dataSender.flush();

                        }

                    } else {

                        message = "The string you entered is invalid!";
                        dataSender.writeObject(message);
                        dataSender.flush();
                        dataSender.writeObject("increment trials");
                        dataSender.flush();

                    }
                } catch (ClassNotFoundException e) {

                    System.err.println("Error: String class not found");
                }


            } catch (SocketTimeoutException e) {

                message = "The server kicked you out because of inactivity!";
                try {
                    dataSender.writeObject(message);
                    dataSender.flush();
                } catch (IOException ex) {
                    System.err.println("Error while sending data to the client");
                }


            }catch (IOException e){

                System.err.println("Error while comunicating with " + clientSocket.getInetAddress() + " ,connection closed");
                try {
                    clientSocket.close();
                } catch (IOException ex) {
                    System.err.println("Error while closing the client socket");
                }

            }
            finally {

                try {

                    centralServer.getLock().unlock();

                }catch (IllegalMonitorStateException e){
                    //the client didn't have the lock
                }

            }
        }

    }
}
