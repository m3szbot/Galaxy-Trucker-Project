package it.polimi.ingsw.Connection.ClientSide.socket;

import it.polimi.ingsw.Connection.ClientSide.utils.ClientInfo;
import it.polimi.ingsw.Connection.ClientSide.utils.ClientInputManager;
import it.polimi.ingsw.Connection.ClientSide.utils.ViewCommunicator;
import it.polimi.ingsw.Connection.ServerSide.socket.SocketDataExchanger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Client joiner is responsible for the second phase of the client
 * lifecycle, i.e, making the client join a game. It returns 1, if the client
 * join the game correctly, 0 if he is kicked, -1 if he encountered a connection
 * issue.
 *
 * @author carlo
 */


public class ClientJoiner {

    /**
     *
     * @param clientInfo
     * @return true if the joining process terminated correctly, false otherwise
     */

    public boolean start(ClientInfo clientInfo){

        ViewCommunicator viewCommunicator = clientInfo.getViewCommunicator();
        SocketDataExchanger dataExchanger;
        AtomicBoolean terminatedFlag = new AtomicBoolean(false);
        AtomicBoolean errorFlag = new AtomicBoolean(false);

        try {

            dataExchanger = setUpConnection(clientInfo);
            clientInfo.setDataExchanger(dataExchanger);
            dataExchanger.sendString(clientInfo.getNickname());

        } catch (IOException e) {
            viewCommunicator.showData("An error was encountered while setting up the socket connection", true);
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
                    else if(message.equals("trialsEx")){

                        viewCommunicator.showData("You are trying to keep the server busy! Disconnection will happen soon.", false);
                        terminatedFlag.set(true);
                        errorFlag.set(true);
                        break;
                    }
                    else if (message.equals("start")) {
                        ClientInputManager.unblockInput();
                        break;
                    } else {

                        viewCommunicator.showData(message, false);
                    }

                }

            } catch (IOException e) {
                terminatedFlag.set(true);
                errorFlag.set(true);
                viewCommunicator.showData("An error was encountered while communicating with the server", true);
            }

        });

        Thread messageSender = new Thread(() -> {

            while (!terminatedFlag.get()) {

                try {

                    String userInput = ClientInputManager.getUserInput();

                    if(!userInput.equals("unblocked")) {

                        dataExchanger.sendString(userInput);
                    }

                } catch (Exception e) {
                    terminatedFlag.set(true);
                    errorFlag.set(true);

                    if(e instanceof IOException) {

                        viewCommunicator.showData("An error was encountered while sending data to the server", true);
                    }
                    else{

                        viewCommunicator.showData("Timeout reached, you are considered inactive, disconnection will soon happen", false);
                    }
                }
            }

        });

        messageReceiver.start();
        messageSender.start();

        try {

            messageReceiver.join();
            messageSender.join();
            ClientInputManager.setTimeOut(300000);

        } catch (InterruptedException e1) {
            viewCommunicator.showData("Error: message receiver or sender was interrupted abnormally", true);
            errorFlag.set(true);
        }

        return !(errorFlag.get());


    }

    private SocketDataExchanger setUpConnection(ClientInfo clientInfo) throws IOException{

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

}


