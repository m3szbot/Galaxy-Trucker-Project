package it.polimi.ingsw.Connection.ClientSide;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Thread that sends messages to the server during the game
 *
 * @author carlo
 */

public class GameMessageSender implements Runnable {

    private ObjectOutputStream out;
    private AtomicBoolean running;
    private AtomicReference<String> userInput;

    public GameMessageSender(ObjectOutputStream out, AtomicBoolean running, AtomicReference<String> userInput) {

        this.out = out;
        this.running = running;
        this.userInput = userInput;

    }

    @Override
    public void run() {

        while (running.get()) {

            if(userInput.get() != null) {

                try {

                    out.writeUTF(userInput.getAndSet(null));
                    out.flush();

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
