package it.polimi.ingsw.Controller;

/**
 * Utility class for the user interface.
 *
 * @author Boti
 */

import it.polimi.ingsw.Connection.ServerSide.messengers.GameMessenger;

/**
 * Utility class to sleep threads.
 */
public final class Sleeper {
    // Utility class, cannot be instantiated
    private Sleeper() {
        throw new UnsupportedOperationException();
    }

    /**
     * Prints a countdown of count seconds.
     *
     * @param count
     * @param gameMessenger
     */
    public static void countDown(int count, GameMessenger gameMessenger) {
        for (int i = count; i > 0; i--) {
            gameMessenger.sendMessageToAll(String.valueOf(i));
            sleepXSeconds(1);
        }
    }

    /**
     * Method that sleeps the current thread for the given seconds.
     * Used to delay prints.
     */
    public static void sleepXSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            System.out.println("Thread was interrupted.");
        }
    }
}
