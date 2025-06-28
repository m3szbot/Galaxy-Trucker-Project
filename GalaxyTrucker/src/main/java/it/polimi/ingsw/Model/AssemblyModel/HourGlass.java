package it.polimi.ingsw.Model.AssemblyModel;

import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Represents an HourGlass timer that increments its state after a fixed duration.
 * The timer counts seconds up to a predefined life span, and once it reaches
 * the limit, it updates the internal state.
 *
 * @author Giacomo
 */
public class HourGlass {
    private final int life = 60; // Duration of the timer in seconds

    private ScheduledExecutorService scheduler;

    // Represents the current state of the hourglass.
    // State == 3 is the final state - no more turns
    // updated when the hourglass finishes it's current cycle
    private int state;
    private boolean finished; // Indicates whether the timer has completed

    /**
     * Constructor initializes the HourGlass with a default state and finished status.
     */
    public HourGlass() {
        state = 0;
        finished = true;
    }

    /**
     * Starts the hourglass timer if it is not already running.
     * The timer runs for a predefined duration and updates the state when completed.
     */
    public synchronized void twist(AssemblyProtocol assemblyProtocol) {
        // Hourglass currently not running
        if (finished && state < 3) {
            finished = false;
            scheduler = Executors.newScheduledThreadPool(1);

            // runnable anonymous class
            Runnable task = new Runnable() {
                int elapsedTime = 0; // Tracks elapsed time

                @Override
                public void run() {
                    // hourglass running
                    if (elapsedTime < life) {
                        if (elapsedTime % 15 == 0) {
                            String message = String.format("Elapsed time: %ds (%d/3)", elapsedTime, state + 1);
                            ClientMessenger.getGameMessenger(assemblyProtocol.getGameCode()).sendMessageToAll(message);

                        }
                        elapsedTime++;
                    }
                    // hourglass finished current life
                    else {

                        String message = String.format("Time's up! (%d/3)", state + 1);
                        ClientMessenger.getGameMessenger(assemblyProtocol.getGameCode()).sendMessageToAll(message);
                        scheduler.shutdown(); // Stops the scheduler
                        finished = true; // Resets to finished state
                        incrementState(); // Updates the state of the hourglass
                    }
                }

            };
            // end of anonymous class

            // Schedules the task to run every second
            scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
        }
    }

    /**
     * Updates the state of the hourglass when the timer finishes.
     */
    private void incrementState() {
        state++;
    }

    /**
     * Returns the current state of the hourglass.
     *
     * @return the state of the hourglass
     */
    public synchronized int getState() {
        return state;
    }

    public synchronized void stopHourglass() {
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
    }

    public synchronized boolean isFinished() {
        return finished;
    }

    public ExecutorService getScheduler() {
        return scheduler;
    }
}
