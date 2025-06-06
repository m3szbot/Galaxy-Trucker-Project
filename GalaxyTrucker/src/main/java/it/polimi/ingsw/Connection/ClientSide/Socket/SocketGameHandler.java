package it.polimi.ingsw.Connection.ClientSide.Socket;

import it.polimi.ingsw.Connection.ClientSide.utils.ClientInfo;

import java.io.IOException;

public class SocketGameHandler {

    private ClientJoiner joiner = new ClientJoiner();
    private GameStarter gamehandler = new GameStarter();
    private ClientInfo clientInfo;

    public SocketGameHandler(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }


    public void start() {

        if (joiner.start(clientInfo)) {

            gamehandler.start(clientInfo);

        }

        try {

            clientInfo.getDataExchanger().closeResources();
            System.out.println("Resources closed succesfully");

        } catch (IOException e) {
            System.err.println("Critical error while closing server socket and streams");

        }

    }
}
