package it.polimi.ingsw.Connection.ClientSide.utils;

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
    // default timeout value
    private static long timeOut = 1000;

    // used for input simulation
    private static AtomicBoolean testRunning = new AtomicBoolean(false);

    public static void unblockInput() {
        userInput.set("unblock");
    }

    public static void setTimeOut(long playerInputTimeOut) {
        timeOut = playerInputTimeOut;
    }

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
            // wait until input is taken
            while (testRunning.get() && userInput.get() != null) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
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
