package it.polimi.ingsw.Connection.ClientSide.socket;

import it.polimi.ingsw.Connection.ClientSide.utils.ClientInputManager;
import it.polimi.ingsw.Connection.ClientSide.utils.ViewCommunicator;
import it.polimi.ingsw.Connection.ServerSide.socket.SocketDataExchanger;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Thread that sends messages to the server during the game
 *
 * @author carlo
 */

public class GameMessageSender implements Runnable {

    private SocketDataExchanger dataExchanger;
    private AtomicBoolean running;
    private ViewCommunicator viewCommunicator;

    public GameMessageSender(ViewCommunicator viewCommunicator, SocketDataExchanger dataExchanger, AtomicBoolean running) {

        this.dataExchanger = dataExchanger;
        this.running = running;
        this.viewCommunicator = viewCommunicator;

    }

    @Override
    public void run() {

        while (running.get()) {

            String input;

            try {

                input = ClientInputManager.getUserInput();

            } catch (TimeoutException e) {

                viewCommunicator.showData("Timeout reached, you are considered inactive, disconnection will soon happen", false);
                running.set(false);
                break;
            }

            try {

                dataExchanger.sendString(input);


            } catch (IOException e) {

                viewCommunicator.showData("Error while writing data to the server, you have been disconnected", true);
                running.set(false);
            }

            try{
                Thread.sleep(100);

            } catch (InterruptedException e) {
                viewCommunicator.showData("Sender thread was abnormally interrupted", true);
            }


        }

    }
}
