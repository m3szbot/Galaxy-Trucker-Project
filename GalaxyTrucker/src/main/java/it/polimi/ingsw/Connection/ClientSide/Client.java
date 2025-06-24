package it.polimi.ingsw.Connection.ClientSide;

import it.polimi.ingsw.Connection.ClientSide.RMI.RMIGameHandler;
import it.polimi.ingsw.Connection.ClientSide.socket.SocketGameHandler;
import it.polimi.ingsw.Connection.ClientSide.utils.ClientInfo;
import it.polimi.ingsw.Connection.ClientSide.utils.ClientInputManager;
import it.polimi.ingsw.Connection.ClientSide.utils.ClientWelcomer;
import it.polimi.ingsw.Connection.ClientSide.utils.InputDaemon;
import it.polimi.ingsw.Connection.ConnectionType;
import it.polimi.ingsw.Connection.ViewType;
import it.polimi.ingsw.Controller.Sleeper;
import it.polimi.ingsw.View.GUI.GUILoader;
import javafx.application.Application;

/**
 * Client class. The client first enters config information and then
 * enters the lobby of the game where he will be able to join the game.
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
            GUILoader.setViewCommunicator(clientInfo.getViewCommunicator());
            Thread GUIThread = new Thread(() -> Application.launch(GUILoader.class, args));
            GUIThread.setDaemon(false);
            GUIThread.start();

            //pause to let the GUI configure itself and open
            Sleeper.sleepXSeconds(3);

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
