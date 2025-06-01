package it.polimi.ingsw.Connection.ClientSide.socket;

import it.polimi.ingsw.Connection.ServerSide.socket.SocketDataExchanger;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Thread that sends messages to the server during the game
 *
 * @author carlo
 */

public class GameMessageSender implements Runnable {

    private SocketDataExchanger dataExchanger;
    private AtomicBoolean running;
    private AtomicReference<String> userInput;

    public GameMessageSender(SocketDataExchanger dataExchanger, AtomicBoolean running, AtomicReference<String> userInput) {

        this.dataExchanger = dataExchanger;
        this.running = running;
        this.userInput = userInput;

    }

    @Override
    public void run() {

        while (running.get()) {

            if(userInput.get() != null) {
                try {

                    dataExchanger.sendString(userInput.getAndSet(null));

                } catch (IOException e) {

                    System.err.println("Error while writing data to the server, you have been disconnected");
                    running.set(false);
                }

                try{
                    Thread.sleep(100);

                } catch (InterruptedException e) {
                    System.err.println("Sender thread was abnormally interrupted");
                }

            }

        }

    }
}
