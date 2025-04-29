package it.polimi.ingsw.Connection.ServerSide;

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

        try {
            serverSocket = new ServerSocket(5000);

        }catch (IOException e){
            //TODO
        }

        try{
            while(true) {

                clientSocket = serverSocket.accept();
                //setting a 1-minute timeout
                clientSocket.setSoTimeout(60000);

                new ClientSocketHandler(clientSocket, centralServer).start();
            }

        }catch (IOException e){
           //TODO
        }
    }
}
