package it.polimi.ingsw.Connection.ServerSide.socket;

import it.polimi.ingsw.Connection.ServerSide.Server;

import java.io.IOException;
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

        try {
            while (true) {

                clientSocket = serverSocket.accept();
                System.out.println(clientSocket.getInetAddress() + " is connected through socket protocol");
                clientSocket.setSoTimeout(60000);

                new ClientSocketHandler(clientSocket, centralServer).start();
            }

        } catch (IOException e) {
            System.err.println("Error while accepting the client through socket");
        }
    }
}
