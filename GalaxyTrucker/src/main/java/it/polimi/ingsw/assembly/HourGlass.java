package it.polimi.ingsw.assembly;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Represents an HourGlass timer that increments its state after a fixed duration.
 * The timer counts seconds up to a predefined life span, and once it reaches
 * the limit, it updates the internal state.
 *
 * @author Giacomo
 */
public class HourGlass {
    private int state; // Represents the current state of the hourglass
    private boolean finished; // Indicates whether the timer has completed
    private int life = 60; // Duration of the timer in seconds

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
    public void twist(){
        if(finished == true) { // Ensures the hourglass is not already running
            finished = false;
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

            Runnable task = new Runnable() {
                int elapsedTime = 0; // Tracks elapsed time

                @Override
                public void run() {
                    if (elapsedTime < life) {
                        System.out.println("Elapsed Time: " + elapsedTime + "s");
                        elapsedTime++;
                    } else {
                        System.out.println("Time's up!");
                        scheduler.shutdown(); // Stops the scheduler
                        updateState(); // Updates the state of the hourglass
                        finished = true; // Resets to finished state
                    }
                }
            };
            // Schedules the task to run every second
            scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
        }
        else{
            System.out.println("The HourGlass has been already twisted!");
        }
    }

    /**
     * Updates the state of the hourglass when the timer finishes.
     */
    private void updateState(){
        state++;
    }

    /**
     * Returns the current state of the hourglass.
     *
     * @return the state of the hourglass
     */
    public int getState() {
        return state;
    }
}
