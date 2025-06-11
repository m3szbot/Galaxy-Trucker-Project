package it.polimi.ingsw.Controller;

/**
 * Utility class to sleep threads.
 */
public final class Sleeper {
    // Utility class, cannot be instantiated
    private Sleeper() {
        throw new UnsupportedOperationException();
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
