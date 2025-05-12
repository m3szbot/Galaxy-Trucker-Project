package it.polimi.ingsw.Connection.ClientSide;

import it.polimi.ingsw.Connection.ServerSide.RMI.RMIJoiner;
import it.polimi.ingsw.Model.GameInformation.ConnectionType;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.GameInformation.ViewType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *  Client joiner is responsible for the second phase of the client
 *  lifecycle, i.e, making the client join a game. It returns 1, if the client
 *  join the game correctly, 0 if he is kicked, -1 if he encountered a connection
 *  issue.
 *
 * @author carlo
 */


public class ClientJoiner {

    public int start(ClientInfo clientInfo){

        if (clientInfo.getViewType() == ViewType.CLI) {

            return startCLI(clientInfo);


        } else {
            //GUI
        }

        return 0; //if no returns have been activated, the player is automatically kicked.
    }

    private boolean checkTrials(int trials){

        if(trials > 5){
            System.out.println("You entered too much wrong information and therefore are slowing" +
                    "the server, you have been kicked out");
            return true;
        }
        return false;
    }

    private int startCLI(ClientInfo clientInfo){

        if(clientInfo.getConnectionType() == ConnectionType.Socket) {

            return startSCK(clientInfo);

        }
        else{

            return startRMI(clientInfo);

        }
    }

    private int startSCK(ClientInfo clientInfo){

        Socket socket;
        DataInputStream dataReceiver;
        DataOutputStream dataSender;
        ObjectOutputStream objectSender;
        Scanner scanner;
        AtomicBoolean terminatedFlag = new AtomicBoolean(false);
        AtomicBoolean isKicked = new AtomicBoolean(false);
        AtomicBoolean connectionLost = new AtomicBoolean(false);

        try {
            socket = new Socket(clientInfo.getServerIp(), clientInfo.getServerPort());
            clientInfo.setServerSocket(socket);
            dataReceiver = new DataInputStream(socket.getInputStream());
            dataSender = new DataOutputStream(socket.getOutputStream());
            objectSender = new ObjectOutputStream(socket.getOutputStream());
            scanner = new Scanner(System.in);

            objectSender.writeObject(clientInfo);

        } catch (IOException e) {
            System.err.println("Error while connecting to the server");
            return -1;
        }
        //One thread to receive feedback from the server

        Thread messageReceiver = new Thread(() -> {

            try{
                int trials = 0;

                while(true){

                    if(checkTrials(trials)){
                        terminatedFlag.set(true);
                        isKicked.set(true);
                        break;
                    }

                    String message = dataReceiver.readUTF();

                    if(message.equals("added")){
                        terminatedFlag.set(true);

                        break;
                    }
                    else if(message.equals("increment trials")){
                        trials++;
                    }
                    else if(message.equals("The server kicked you out because of inactivity!")){

                        System.out.println(message);
                        terminatedFlag.set(true);
                        isKicked.set(true);
                        break;
                    }
                    else {

                        System.out.println(message);
                    }

                }

            }catch (IOException e){
                connectionLost.set(true);
                terminatedFlag.set(true);
                System.err.println("An error was encountered while receiving data from the server");
            }

        });

        //One thread to send message to the server

        Thread messageSender = new Thread(() -> {

            while(!terminatedFlag.get()) {

                try {
                    dataSender.writeUTF(scanner.nextLine());
                } catch (IOException e) {
                    connectionLost.set(true);
                    terminatedFlag.set(true);
                    System.err.println("An error was encountered while sending data to the server");
                }

            }

        });

        messageReceiver.start();
        messageSender.start();

        try {

            messageReceiver.join();
            //the sender is interrupted when the receiver is terminated, i.e,
            //when a connection problem occurred, or when he is kicked, or when he joined a game
            messageSender.interrupt();

        } catch (InterruptedException e) {
            System.err.println("Error: message receiver was interrupted abnormally");
        }

        if(isKicked.get()){
            return 0;
        }
        else if(connectionLost.get()){
            return -1;
        }
        else{
            return 1;
        }
    }

    private int startRMI(ClientInfo clientInfo){

        AtomicBoolean isKicked = new AtomicBoolean(false);
        RMIJoiner joiner;
        int trials = 0;

        try {

            joiner = (RMIJoiner) Naming.lookup("rmi://localhost/Joiner");

        }catch (NotBoundException | RemoteException | MalformedURLException e){
            System.err.println("Error while connecting to the host registry");
            return -1;
        }

        Scanner scanner = new Scanner(System.in);
        String input;

        Thread timer = new Thread(() -> {
            try {
                Thread.sleep(60000);
                isKicked.set(true);
            } catch (InterruptedException e) {
                //player answer before the time is finished
            }
        });


        while(true){

            timer.start();
            System.out.println("Press 'enter' key to enter in a game: ");

            if(checkTrials(trials)){
                return 0;
            }

            if(scanner.nextLine().isEmpty()){

                if(isKicked.get()){

                    System.out.println("The server kicked you out because of inactivity!");

                    return 0;
                }

                timer.interrupt();
                timer.start();

                if(joiner.joinGame()){

                    if(joiner.isFirstPlayer()){

                        int numberOfPlayers;
                        GameType gameType;

                        System.out.println("You are the first player joining the game!");
                        System.out.println("Enter the game type (TESTGAME/NORMALGAME): ");

                        while (true) {

                            input = scanner.nextLine();

                            if(isKicked.get()){

                                System.out.println("The server kicked you out because of inactivity!");
                                joiner.releaseLock();

                                return 0;
                            }

                            timer.interrupt();
                            timer.start();

                            input = input.toUpperCase();

                            if (!input.equals("TESTGAME") && !input.equals("NORMALGAME")) {

                                System.out.println("The game type you entered is incorrect, please reenter it (TESTGAME/NORMALGAME): ");
                                trials++;

                            } else {

                                gameType = GameType.valueOf(input);
                                System.out.println("Game type was set up correctly");

                                break;
                            }

                            if(checkTrials(trials)){
                                return 0;
                            }

                        }

                        System.out.println("Enter the number of players of the game (2-4): ");

                        while (true) {

                            numberOfPlayers = scanner.nextInt();

                            if(isKicked.get()){

                                System.out.println("The server kicked you out because of inactivity!");
                                joiner.releaseLock();
                                return 0;
                            }

                            timer.interrupt();
                            timer.start();

                            if (numberOfPlayers < 2 || numberOfPlayers > 4) {

                                System.out.println("The number of players you entered is invalid, please enter a valid value (2-4): ");
                                trials++;

                            } else {

                                System.out.println("Number of players was set up correctly");
                                break;
                            }

                            if(checkTrials(trials)){
                                return 0;
                            }

                        }

                        joiner.addPlayer(clientInfo, gameType, numberOfPlayers);
                        System.out.println("You have been added to the game (game code " + joiner.getGameCode() + ")");
                        joiner.releaseLock();
                        return 1;

                    }
                    //the player is not the first one
                    if(joiner.isNameRepeated(clientInfo.getNickname())){

                        while (true) {

                            System.out.println("You're nickname has already been chosen, please enter a new one: ");

                            input = scanner.nextLine();

                            if(isKicked.get()){

                                System.out.println("The server kicked you out because of inactivity!");
                                joiner.releaseLock();
                                return 0;
                            }

                            timer.interrupt();
                            timer.start();

                            if (!joiner.isNameRepeated(input)) {

                                System.out.println( "You're nickname is now " + input);
                                clientInfo.setNickname(input);
                                break;
                            }

                            trials++;
                            if(checkTrials(trials)){
                                return 0;
                            }

                        }

                    }

                    joiner.addPlayer(clientInfo);
                    System.out.println("You have joined the game of " + joiner.getCurrentGameCreator() + "(game code " + joiner.getGameCode() + ")");
                    joiner.releaseLock();
                    return 1;

                }
                else{

                    System.out.println("Somebody is already joining the game, please wait.");
                    timer.interrupt();

                }

            }
            else{

                if(isKicked.get()){

                    System.out.println("The server kicked you out because of inactivity!");

                    return 0;
                }
                trials++;

                System.out.println("The string you entered is invalid!");

                timer.interrupt();

            }

        }

    }

}


