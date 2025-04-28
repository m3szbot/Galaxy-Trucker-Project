package it.polimi.ingsw.Connection;

import java.net.Socket;

public class ClientHandler implements Runnable{

    Socket clientSocket;

    public ClientHandler(Socket clientSocket){
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {

    }
}
