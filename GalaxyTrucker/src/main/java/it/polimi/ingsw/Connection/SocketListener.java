package it.polimi.ingsw.Connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketListener implements Runnable{

   ServerSocket serverSocket;
   Socket clientSocket;
   Server centralServer;

   public SocketListener(Server centralServer){
       this.centralServer = centralServer;
   }

    @Override
    public void run() {

        try{
            while(true) {

                serverSocket = new ServerSocket(5000);
                clientSocket = serverSocket.accept();

                new ClientHandler(clientSocket, centralServer).start();
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
