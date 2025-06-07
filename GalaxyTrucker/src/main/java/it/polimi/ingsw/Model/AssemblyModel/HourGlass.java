package it.polimi.ingsw.Model.AssemblyModel;

import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.ArrayList;
import java.util.List;
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
    private int state; // Represents the current state of the hourglass
    private boolean finished; // Indicates whether the timer has completed
    private int life = 60; // Duration of the timer in seconds
    List<Player> listOfPlayers = new ArrayList<>();
    ScheduledExecutorService scheduler;

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
    public void twist(AssemblyProtocol assemblyProtocol, List<Player> players) {
        if (finished == true) { // Ensures the hourglass is not already running
            finished = false;
            scheduler = Executors.newScheduledThreadPool(1);

            Runnable task = new Runnable() {
                int elapsedTime = 0; // Tracks elapsed time

                @Override
                public void run() {
                    if (elapsedTime < life) {
                        if(elapsedTime % 15 == 0) {
                            String message = ("Elapsed Time: " + elapsedTime + "s");
                            ClientMessenger.getGameMessenger(assemblyProtocol.getGameCode()).sendMessageToAll(message);

                        }
                        elapsedTime++;
                    } else {
                        String message = "Time's up!";
                        //System.out.println("Time's up!");

                        ClientMessenger.getGameMessenger(assemblyProtocol.getGameCode()).sendMessageToAll(message);
                        scheduler.shutdown(); // Stops the scheduler
                        updateState(); // Updates the state of the hourglass
                        finished = true; // Resets to finished state
                    }
                }
            };
            // Schedules the task to run every second
            scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
        } else {
            //System.out.println("The HourGlass has been already twisted!");
        }
    }

    public void stopHourglass() {
        scheduler.shutdownNow();
    }

    /**
     * Updates the state of the hourglass when the timer finishes.
     */
    private void updateState() {
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

    public boolean isFinished() {
        return finished;
    }
}
