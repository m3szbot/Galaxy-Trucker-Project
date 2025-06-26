package it.polimi.ingsw.Connection.ServerSide.socket;

import it.polimi.ingsw.Connection.ServerSide.Server;
import it.polimi.ingsw.Connection.ServerSide.utils.GameJoinerThread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Thread that listens for incoming clients. It starts a new
 * clientSocketHandler for each connected client
 *
 * @author carlo
 */

public class SocketListener implements Runnable {

    ServerSocket serverSocket;
    Socket clientSocket;
    Server centralServer;

    public SocketListener(Server centralServer) {
        this.centralServer = centralServer;
    }

    @Override
    public void run() {

        try {
            serverSocket = new ServerSocket(centralServer.getPort());
            System.out.println("Socket listener is activated and is listening...");

        } catch (IOException e) {
            System.err.println("Error while opening the server listening socket");
            return;
        }

        while (true) {

            try {

                clientSocket = serverSocket.accept();
                System.out.println(clientSocket.getInetAddress() + " is connected through socket protocol");

            } catch (IOException e) {
                System.err.println("Error while accepting client through socket protocol");
            }

            SocketDataExchanger socketDataExchanger;

            try {

                socketDataExchanger = setUpStreams(clientSocket);

            }catch (IOException e){
                System.err.println("Error while setting up streams");
                continue;
            }

            String nickname = getPlayerNickname(socketDataExchanger);

            try {

                if (centralServer.checkNickname(nickname)) {

                    try {
                        socketDataExchanger.sendString("A player connected to the server already has your nickname, please reconnect using a new one");
                    } catch (IOException e) {
                        System.out.println("Error while sending OK message to " + clientSocket.getInetAddress().getHostAddress().toString());
                    }

                } else {
                    socketDataExchanger.sendString("OK");

                    (new GameJoinerThread(centralServer, nickname, socketDataExchanger)).start();
                }

            }catch (IOException e){
                e.printStackTrace();
                System.err.println("Error while checking the client nickname");
            }

        }


    }

    /**
     * Set up the streams needed for communication and pack them into the object which is returned
     * @param clientSocket
     * @return SocketDataExchanger, an object encapsulating all the information needed for exchanging information
     * @throws IOException
     */

    private SocketDataExchanger setUpStreams(Socket clientSocket) throws IOException{


        ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
        ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        outputStream.flush();


        return new SocketDataExchanger(clientSocket, inputStream, outputStream);

    }

    /**
     *
     * @param socketDataExchanger
     * @return the player nickname
     */

    private String getPlayerNickname(SocketDataExchanger socketDataExchanger){

        try {

            return socketDataExchanger.getString();

        } catch (IOException e) {
            System.err.println("Error while getting " + clientSocket.getInetAddress().getHostAddress().toString() + " nickname");
        }
       return null;
    }
}
