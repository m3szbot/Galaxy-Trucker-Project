package it.polimi.ingsw.Connection.ClientSide;

import it.polimi.ingsw.Model.GameInformation.ConnectionType;

import java.io.IOException;

/**
 * Client class. The client lifecycle is composed of
 * three phases. The welcoming phase consists in asking
 * the client config information that are necessary for the
 * correct communication with the server during the next phases.
 * The joining phase consists in making the client join a game.
 * And the gaming phase consist in the client playing.
 *
 * @author carlo
 */

public class Client {

    public static void main(String[] args){

        ClientWelcomer welcomer = new ClientWelcomer();
        ClientJoiner joiner = new ClientJoiner();
        ClientGameHandler gamehandler = new ClientGameHandler();
        ClientInfo clientInfo = new ClientInfo();
        InputDaemon inputDaemon = new InputDaemon(clientInfo.getUserInput());

        welcomer.start(clientInfo);

        inputDaemon.setDaemon(true);
        inputDaemon.start();

        if(joiner.start(clientInfo) == 0){
            gamehandler.start(clientInfo);
        }

        try {

            //closing resources

            if(clientInfo.getConnectionType() == ConnectionType.SOCKET) {

                clientInfo.getDataExchanger().closeResources();
                clientInfo.getServerSocket().close();

            }
            System.out.println("Resources closed successfully");

        } catch (IOException e) {
           System.err.println("Critical error while closing server socket and streams");
        }

    }

}
