package it.polimi.ingsw.Connection.ClientSide;

import it.polimi.ingsw.Connection.ClientSide.RMI.RMIGameHandler;
import it.polimi.ingsw.Connection.ClientSide.socket.SocketGameHandler;
import it.polimi.ingsw.Connection.ClientSide.utils.ClientInfo;
import it.polimi.ingsw.Connection.ClientSide.utils.ClientInputManager;
import it.polimi.ingsw.Connection.ClientSide.utils.ClientWelcomer;
import it.polimi.ingsw.Connection.ClientSide.utils.InputDaemon;
import it.polimi.ingsw.Connection.ConnectionType;
import it.polimi.ingsw.Connection.ViewType;
import it.polimi.ingsw.View.GUI.GUILoader;
import javafx.application.Application;

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


        welcomer.start(clientInfo);

        if(clientInfo.getViewType() == ViewType.TUI) {

            InputDaemon inputDaemon = new InputDaemon();
            inputDaemon.setDaemon(true);
            inputDaemon.start();

        }
        else{
            Application.launch(GUILoader.class, args);
        }



        ClientInputManager.setTimeOut(60000);

        if(clientInfo.getConnectionType() == ConnectionType.SOCKET){
            SocketGameHandler socketHandler = new SocketGameHandler(clientInfo);

            socketHandler.start();

        }
        else{
            RMIGameHandler rmiHandler = new RMIGameHandler(clientInfo);

            rmiHandler.start();

        }

        System.out.println("Disconnected");

    }

}
