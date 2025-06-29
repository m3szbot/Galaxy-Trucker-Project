package it.polimi.ingsw.Connection.ClientSide.utils;

import it.polimi.ingsw.Controller.Sleeper;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Class responsible for managing user input. It is
 *
 * @author carlo
 */

public final class ClientInputManager {

    private static AtomicReference<String> userInput = new AtomicReference<>(null);
    // milliseconds
    private static long timeOut;

    // used for input simulation
    private static AtomicBoolean testRunning = new AtomicBoolean(false);

    /**
     * Unblock the input by setting it manually
     */

    public static void unblockInput() {
        userInput.set("unblock");
    }

    /**
     * set up the timeout in milliseconds. During the lobby the timeout is 1 minute,
     * while during the game 5 minutes. A warning is sent when 1 minute is left.
     *
     * @param playerInputTimeOut
     */

    public static void setTimeOut(long playerInputTimeOut) {
        timeOut = playerInputTimeOut;
    }

    /**
     * Method which return the user input. It is a blocking method and can be unblocked if needed.
     *
     * @return the user input
     * @throws TimeoutException
     */

    public static String getUserInput() throws TimeoutException {
        long temp = timeOut;

        while (true) {
            if (userInput.get() != null) {

                return userInput.getAndSet(null);

            } else {

                try {
                    Thread.sleep(100);
                    temp -= 100;

                    if (temp == 60000) {
                        System.out.println("Warning: 1 minute left before you are being considered inactive, please enter an input");
                    }

                    if (temp < 0) {
                        throw new TimeoutException();
                    }

                } catch (InterruptedException e) {
                    System.err.println("ClientInputManager thread abnormally interrupted");
                }
            }
        }
    }

    /**
     * Sets the user input, which unblock the getUserInput method.
     *
     * @param input
     */

    public static void setUserInput(String input) {
        userInput.set(input);
    }

    /**
     * Separates the current input on newlines and sets the current line as the userInput until it is taken,
     * then it sets the next line.
     *
     * @param input separated with newlines.
     * @author Boti
     */
    public static void setTestInput(String input) {
        testRunning.set(true);
        String[] lines = input.split("\\r?\\n");

        for (String line : lines) {
            // avoid collisions
            Sleeper.sleepXSeconds(0.1);

            // wait until input is taken
            while (testRunning.get() && userInput.get() != null) {
                Sleeper.sleepXSeconds(0.1);
            }

            // input thread ended
            if (!testRunning.get()) {
                userInput.set(null);
                break;
            }

            // input thread running
            // set input when previous was taken
            userInput.set(line);
            System.out.printf("Simulated input: %s\n", line);
        }

        testRunning.set(false);
    }

    public static void endTestInput() {
        testRunning.set(false);
    }

    public static String getSimulatedInput() {
        return userInput.get();
    }

    public static boolean getTestRunning() {
        return testRunning.get();
    }

}
