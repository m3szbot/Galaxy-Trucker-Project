package it.polimi.ingsw.Connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketListener implements Runnable{

   ServerSocket serverSocket;
   Socket clientSocket;

    @Override
    public void run() {

        try{
            while(true) {

                serverSocket = new ServerSocket(5000);
                clientSocket = serverSocket.accept();

                (new Thread(new ClientHandler(clientSocket))).start();
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
