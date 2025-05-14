package it.polimi.ingsw.Connection.ClientSide;

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

        welcomer.start(clientInfo);


        if(joiner.start(clientInfo) == 0){
            System.out.println("Thread concluso");
            gamehandler.start(clientInfo);
        }

        try {

            //closing resources

            clientInfo.getOutputStream().close();
            clientInfo.getInputStream().close();
            clientInfo.getServerSocket().close();

        } catch (IOException e) {
           System.err.println("Critical error while closing server socket and streams");
        }

    }

}
