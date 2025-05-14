package it.polimi.ingsw.Connection.ClientSide;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Thread that sends messages to the server during the game
 *
 * @author carlo
 */

public class GameMessageSender implements Runnable {

    private ObjectOutputStream out;
    private AtomicBoolean running;

    public GameMessageSender(ObjectOutputStream out, AtomicBoolean running) {

        this.out = out;
        this.running = running;

    }

    @Override
    public void run() {

        Scanner reader = new Scanner(System.in);

        while (running.get()) {

            try {

                out.writeUTF(reader.nextLine());
                out.flush();

            } catch (IOException e) {

                System.err.println("Error while writing data to the server, you have been disconnected");
                running.set(false);
            }

        }

    }
}
