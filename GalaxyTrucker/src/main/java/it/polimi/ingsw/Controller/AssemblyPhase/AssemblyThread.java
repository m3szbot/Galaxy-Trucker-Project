package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.GameMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Model.AssemblyModel.AssemblyProtocol;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * {@code AssemblyThread} manages the entire assembly phase for a single player,
 * including state transitions, user input handling, and communication.
 * Each player has their own thread during this phase.
 */
public class AssemblyThread implements Runnable {
    private final GameInformation gameInformation;
    private final Player associatedPlayer;
    private final AssemblyProtocol assemblyProtocol;
    // messengers
    private final GameMessenger gameMessenger;
    private final PlayerMessenger playerMessenger;
    // non final attributes
    private GameState currentState;
    private BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    private AtomicBoolean running;
    private CountDownLatch latch;
    private AtomicBoolean isfinished = new AtomicBoolean(false);
    private AtomicBoolean amIChoosing = new AtomicBoolean(false);
    private AtomicBoolean end = new AtomicBoolean(false);
    private Thread t;
    private AtomicBoolean blocked = new AtomicBoolean(false);
    private AtomicBoolean disconnected = new AtomicBoolean(false);

    /**
     * Constructs an {@code AssemblyThread} for a specific player.
     *
     * @param gameInformation     the game information shared across all players
     * @param player              the player associated with this thread
     * @param assemblyProtocol    the shared assembly protocol logic
     * @param running             a flag controlling the loop of the assembly phase
     * @param latch               a countdown latch used to coordinate thread termination
     */
    public AssemblyThread(GameInformation gameInformation, Player player, AssemblyProtocol assemblyProtocol, AtomicBoolean running, CountDownLatch latch) {
        this.gameInformation = gameInformation;
        this.associatedPlayer = player;
        this.assemblyProtocol = assemblyProtocol;
        this.running = running;
        this.latch = latch;
        this.gameMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode());
        this.playerMessenger = gameMessenger.getPlayerMessenger(player);
    }

    public GameInformation getGameInformation() {
        return gameInformation;
    }

    public Player getAssociatedPlayer() {
        return associatedPlayer;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public BlockingQueue<String> getInputQueue() {
        return inputQueue;
    }

    public AtomicBoolean getRunning() {
        return running;
    }

    /**
     * Updates the running flag that controls the game loop.
     */
    public void setRunning(boolean value) {
        running.set(value);
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public AtomicBoolean getAmIChoosing() {
        return amIChoosing;
    }

    public AtomicBoolean getEnd() {
        return end;
    }

    public AtomicBoolean getIsfinished() {
        return isfinished;
    }

    /**
     * Runs the assembly logic for the player:
     * - Initializes state
     * - Starts a separate input thread
     * - Enters a loop to manage state and input
     * - Handles final position choice
     * - Ensures cleanup on termination
     */
    @Override
    public void run() {
        try {
            setState(new AssemblyState(assemblyProtocol, playerMessenger, associatedPlayer));
            assemblyProtocol.getHourGlass().twist(assemblyProtocol);


            // Separate thread for reading user input from the console
            t = new Thread(() -> {
                while (!end.get() && !disconnected.get()) {
                    try {
                        blocked.set(true);
                        String input = playerMessenger.getPlayerString();
                        blocked.set(false);

                        inputQueue.offer(input);

                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {

                            break;
                        }
                    } catch (PlayerDisconnectedException e) {
                        gameMessenger.disconnectPlayer(associatedPlayer);
                        disconnected.set(true);
                    }
                }
            });
            t.start();


            // Main non-blocking game loop
            while (running.get() && !disconnected.get()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    break;
                }

                // Handle user input if available
                String input = inputQueue.poll();
                if (input != null) {
                    try {
                        currentState.handleInput(input, this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // Update current game state (e.g., timers, state transitions)
                try {
                    currentState.update(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (!amIChoosing.get() && !disconnected.get()) {
                setState(new ChooseStartingPositionState(assemblyProtocol, playerMessenger, associatedPlayer));
                while (!isfinished.get()) {
                    String input = inputQueue.poll();
                    if (input != null) {
                        try {
                            currentState.handleInput(input, this);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            currentState.update(this);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(!disconnected.get()){
                associatedPlayer.getShipBoard().getShipBoardAttributes().destroyComponents(assemblyProtocol.getPlayersBookedComponents().get(associatedPlayer).size());
                setEnd();
                if (blocked.get()) {
                    playerMessenger.unblockUserInputGetterCall();
                }
            }
            latch.countDown();
        }
    }

    /**
     * Sets the current state of the game and triggers its enter logic.
     *
     * @param newState the new state to switch to
     */
    public void setState(GameState newState) {
        this.currentState = newState;
        currentState.enter(this);
    }

    public void setEnd() {
        end.set(true);
    }

    public AssemblyProtocol getAssemblyProtocol() {
        return assemblyProtocol;
    }

}



