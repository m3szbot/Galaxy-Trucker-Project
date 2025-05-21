package it.polimi.ingsw.Connection.ClientSide;

import it.polimi.ingsw.Connection.ConnectionType;
import it.polimi.ingsw.Connection.ServerSide.socket.SocketDataExchanger;
import it.polimi.ingsw.Connection.ViewType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Client joiner is responsible for the second phase of the client
 * lifecycle, i.e, making the client join a game. It returns 1, if the client
 * join the game correctly, 0 if he is kicked, -1 if he encountered a connection
 * issue.
 *
 * @author carlo
 */


public class ClientJoiner {

    private AtomicReference<String> userInput;

    /**
     *
     * @param clientInfo
     * @return true if the joining process terminated correctly, false otherwise
     */

    public boolean start(ClientInfo clientInfo){

        userInput = clientInfo.getUserInput();

        if (clientInfo.getViewType() == ViewType.TUI) {

            return startTUI(clientInfo);


        }

        return false;
    }

    private boolean startTUI(ClientInfo clientInfo){

        SocketDataExchanger dataExchanger;
        AtomicBoolean terminatedFlag = new AtomicBoolean(false);
        AtomicBoolean errorFlag = new AtomicBoolean(false);

        try {

            dataExchanger = setUpConnection(clientInfo);
            clientInfo.setDataExchanger(dataExchanger);
            dataExchanger.sendString(clientInfo.getNickname());

        } catch (IOException e) {
            System.err.println("An error was encountered while setting up the socket connection");
            return false;
        }

        //One thread to receive feedback from the server

        Thread messageReceiver = new Thread(() -> {

            try {
                String message;

                while (true) {

                    message = dataExchanger.getString();

                    if (message.equals("added")) {
                        terminatedFlag.set(true);


                    }
                    else if(message.equals("nickname updated")){

                        clientInfo.setNickname(dataExchanger.getString());
                        dataExchanger.sendString(String.valueOf(clientInfo.getGameCode()));

                    }
                    else if (message.equals("start")) {
                        break;
                    } else {

                        System.out.println(message);
                    }

                }

            } catch (IOException e) {
                terminatedFlag.set(true);
                errorFlag.set(true);
                System.err.println("An error was encountered while communicating with the server");
            }

        });

        Thread messageSender = new Thread(() -> {

            while (!terminatedFlag.get()) {

                if (userInput.get() != null) {

                    try {

                        dataExchanger.sendString(userInput.getAndSet(null));

                    } catch (IOException e) {
                        terminatedFlag.set(true);
                        errorFlag.set(true);
                        System.err.println("An error was encountered while sending data to the server");
                    }
                }

                try {
                    Thread.sleep(100);

                } catch (InterruptedException e) {
                    terminatedFlag.set(true);
                    errorFlag.set(true);
                    System.err.println("Sender thread was abnormally interrupted");
                }

            }

        });

        messageReceiver.start();
        messageSender.start();

        try {

            messageReceiver.join();
            messageSender.join();

        } catch (InterruptedException e1) {
            System.err.println("Error: message receiver or sender was interrupted abnormally");
            errorFlag.set(true);
        }

        return !(errorFlag.get());

    }


    private SocketDataExchanger setUpConnection(ClientInfo clientInfo) throws IOException{


        if(clientInfo.getConnectionType() == ConnectionType.SOCKET){

            SocketDataExchanger dataExchanger;

            Socket clientSocket;
            ObjectInputStream inputStream;
            ObjectOutputStream outputStream;


            clientSocket = new Socket(clientInfo.getServerIp(), clientInfo.getServerPort());
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            outputStream.flush();
            inputStream = new ObjectInputStream(clientSocket.getInputStream());

           dataExchanger = new SocketDataExchanger(clientSocket, inputStream, outputStream);
           return dataExchanger;

        }
        else{

            //RMI
            return null;

        }

    }

}


