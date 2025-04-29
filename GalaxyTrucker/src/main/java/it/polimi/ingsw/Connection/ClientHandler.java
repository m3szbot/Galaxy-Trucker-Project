package it.polimi.ingsw.Connection;

import it.polimi.ingsw.Application.Game;
import it.polimi.ingsw.Application.GameState;
import it.polimi.ingsw.Application.GameType;
import it.polimi.ingsw.Shipboard.Color;
import it.polimi.ingsw.Shipboard.Player;

import java.io.*;
import java.net.Socket;

/**
 * Handles the connected client with socket protocol
 *
 * @author carlo
 */

public class ClientHandler extends Thread{

    private Socket clientSocket;
    private Server centralServer;
    private DataInputStream dataReceiver;
    private DataOutputStream dataSender;
    private ObjectInputStream clientInfoReceiver;
    private ObjectOutputStream clientInfoSender;
    private static Color currentColor = Color.RED;


    public ClientHandler(Socket clientSocket, Server centralServer){
        this.clientSocket = clientSocket;
        this.centralServer = centralServer;

        try {

            dataReceiver = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
            dataSender = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
            clientInfoReceiver = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream()));
            clientInfoSender = new ObjectOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));

        } catch (IOException e) {
            //TODO
        }

    }

    private Color getNextColor(){
        switch (currentColor){
            case Color.RED -> {
                return Color.BLUE;
            }

            case Color.BLUE -> {
                return Color.GREEN;
            }

            case Color.GREEN -> {
                return Color.YELLOW;
            }
        }

        return null;
    }

    @Override
    public void run() {

        String message;
        String input;
        ClientInfo clientInfo = null;

        while(true){

            if(centralServer.getLock().tryLock()){

                try {
                    clientInfo = (ClientInfo) clientInfoReceiver.readObject();

                } catch (IOException e) {
                    //TODO
                } catch (ClassNotFoundException e) {
                    //TODO
                }

                if(centralServer.getCurrentGameState() == GameState.Empty){
                    //first player

                    int numberOfPlayers;
                    GameType gameType;

                    message = "You are the first player joining the game!";

                    try {

                        dataSender.writeChars(message);

                        message = "Enter the game type (TESTGAME/NORMALGAME): ";

                        dataSender.writeChars(message);

                        while (true) {

                            input = dataReceiver.readUTF();
                            input.toUpperCase();

                            if(!input.equals("TESTGAME") && !input.equals("NORMALGAME")){

                                message = "The game type you entered is incorrect, please reenter it (TESTGAME/NORMALGAME): ";
                                dataSender.writeChars(message);
                            }
                            else{

                                gameType = GameType.valueOf(input);
                                message = "Game type was set up correctly";
                                dataSender.writeChars(message);

                                break;
                            }

                        }

                        message = "Enter the number of players of the game (2-4): ";

                        dataSender.writeChars(message);

                        while(true){

                            numberOfPlayers = dataReceiver.readInt();

                            if(numberOfPlayers < 2 || numberOfPlayers > 4){

                                message = "The number of players you entered is invalid, please enter a valid value (2-4): ";
                                dataSender.writeChars(message);
                            }
                            else{

                                message = "Number of players was set up correctly";
                                dataSender.writeChars(message);
                                break;
                            }

                        }

                        Player player = new Player(clientInfo.getNickname(), currentColor, centralServer.getCurrentGameInformation());
                        clientInfo.setGameCode(centralServer.getGameCode());
                        clientInfoSender.writeObject(clientInfo);
                        centralServer.addPlayerToCurrentGame(player, clientInfo.getViewType(), clientInfo.getConnectionType(), gameType, numberOfPlayers);

                        message = "You have been added to the game!";
                        dataSender.writeChars(message);

                        currentColor = getNextColor();
                        centralServer.changeCurrentGameState(GameState.Starting);

                    }catch (IOException e){
                        //TODO
                    }

                }
                else{
                    //not first player

                    try {

                        Player player = new Player(clientInfo.getNickname(), currentColor, centralServer.getCurrentGameInformation());
                        clientInfo.setGameCode(centralServer.getGameCode());
                        clientInfoSender.writeObject(clientInfo);
                        centralServer.addPlayerToCurrentGame(player, clientInfo.getViewType(), clientInfo.getConnectionType());
                        currentColor = getNextColor();

                        message = "You have joined the game of " + centralServer.getCurrentGameCreator();
                        dataSender.writeChars(message);

                        if(centralServer.isCurrentGameFull()){
                            centralServer.startCurrentStartingGame();
                        }

                    }
                    catch (IOException e){
                        //TODO
                    }

                }

                centralServer.getLock().unlock();
                break;

            }
            else{
                System.out.println("Please wait: the game is going to start soon...");
                try {
                    this.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
}
