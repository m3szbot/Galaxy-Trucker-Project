package it.polimi.ingsw.Connection.ClientSide;

import it.polimi.ingsw.Connection.ClientSide.RMI.RMIGameHandler;
import it.polimi.ingsw.Connection.ClientSide.socket.SocketGameHandler;
import it.polimi.ingsw.Connection.ClientSide.utils.ClientInfo;
import it.polimi.ingsw.Connection.ClientSide.utils.ClientWelcomer;
import it.polimi.ingsw.Connection.ClientSide.utils.InputDaemon;
import it.polimi.ingsw.Connection.ConnectionType;

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

    public static void main(String[] args) {

        ClientWelcomer welcomer = new ClientWelcomer();
        ClientInfo clientInfo = new ClientInfo();
        InputDaemon inputDaemon = new InputDaemon(clientInfo.getUserInput());
        SocketGameHandler socketHandler = new SocketGameHandler(clientInfo);
        RMIGameHandler rmiHandler = new RMIGameHandler(clientInfo);

        welcomer.start(clientInfo);

        inputDaemon.setDaemon(true);
        inputDaemon.start();

        if(clientInfo.getConnectionType() == ConnectionType.SOCKET){

            socketHandler.start();

        }
        else{

            rmiHandler.start();

        }

        System.out.println("Disconnected");


    }

}
