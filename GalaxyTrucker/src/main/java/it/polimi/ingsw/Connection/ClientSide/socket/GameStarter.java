package it.polimi.ingsw.Connection.ClientSide.socket;

import it.polimi.ingsw.Connection.ClientSide.utils.ClientInfo;
import it.polimi.ingsw.Connection.ServerSide.socket.SocketDataExchanger;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class that handles the client playing the game through socket
 * protocol. It encapsulates the 2 main threads which send and
 * receive information to/from the server.
 *
 * @author carlo
 */

public class GameStarter {

    ClientInfo clientInfo;

    /**
     * Starts the handler that handles the player communication with
     * the server during the game
     *
     * @param clientInfo
     */

    public void start(ClientInfo clientInfo) {

        this.clientInfo = clientInfo;

        SocketDataExchanger dataExchanger = clientInfo.getDataExchanger();

        AtomicBoolean running = new AtomicBoolean(true);

        Thread messageReceiver = new Thread(new GameMessageReceiver(clientInfo.getViewCommunicator(), dataExchanger, running));
        Thread messageSender = new Thread(new GameMessageSender(clientInfo.getViewCommunicator(), dataExchanger, running));

        messageReceiver.start();
        messageSender.start();

        try {

            messageReceiver.join();
            messageSender.join();

        } catch (InterruptedException e) {
            System.err.println("Receiver or sender thread was interrupted abnormally");

        }

    }

}
