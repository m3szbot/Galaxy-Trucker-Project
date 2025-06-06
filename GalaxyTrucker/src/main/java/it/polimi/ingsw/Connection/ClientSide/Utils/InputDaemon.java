package it.polimi.ingsw.Connection.ClientSide.Utils;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Thread which reads from the user continually. It is a daemon
 * thread as it stops only when the program ends.
 *
 * @author carlo
 */

public class InputDaemon extends Thread {

    AtomicReference<String> userInput;

    public InputDaemon(AtomicReference<String> userInput) {
        this.userInput = userInput;
    }

    public void run() {

        Scanner scanner = new Scanner(System.in);

        while (true) {
            userInput.set(scanner.nextLine());
        }

    }
}
