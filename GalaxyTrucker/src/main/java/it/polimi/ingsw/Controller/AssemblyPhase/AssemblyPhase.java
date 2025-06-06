package it.polimi.ingsw.Controller.AssemblyPhase;


import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.GameMessenger;
import it.polimi.ingsw.Controller.Phase;
import it.polimi.ingsw.Model.AssemblyModel.AssemblyProtocol;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GamePhase;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * AssemblyGame is the main game controller that manages the game loop,
 * player input, and state transitions for the component assembly phase.
 *
 * @author Giacomo
 */
public class AssemblyPhase extends Phase {
    private AtomicBoolean running = new AtomicBoolean(true);
    private BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    private AssemblyProtocol assemblyProtocol;
    private String message;


    /**
     * Initializes the game with provided game information.
     */
    public AssemblyPhase(GameInformation gameInformation) {
        super(gameInformation);
        assemblyProtocol = new AssemblyProtocol(gameInformation);
    }

    public GameMessenger getGameMessenger() {
        return gameMessenger;
    }

    /**
     * Sets the current state of the game and triggers its enter logic.
     *
     * @param newState the new state to switch to
     */
    public void setState(GameState newState) {
        /*
        this.currentState = newState;
        currentState.enter(this, assemblyView);*/
    }

    /**
     * Returns the game information object containing all players.
     */
    public GameInformation getGameInformation() {
        return gameInformation;
    }

    /**
     * Starts the game, initializes the state, sets up user input thread,
     * and runs the main non-blocking game loop.
     */
    public void start() {

        setGamePhaseToClientServer(GamePhase.Assembly);

        CountDownLatch allPlayersReady = new CountDownLatch(gameInformation.getPlayerList().size());
        ExecutorService executor = Executors.newFixedThreadPool(gameInformation.getPlayerList().size());
        for (Player player : gameInformation.getPlayerList()) {
            executor.submit(new AssemblyThread(gameInformation, player, assemblyProtocol, running, allPlayersReady));
        }


        Thread t = new Thread(() -> {
            while (running.get()) {
                if (assemblyProtocol.getGameType().equals(GameType.NORMALGAME)) {
                    if (assemblyProtocol.getHourGlass().getState() == 3) {
                        setRunning(false);
                        break;
                    }
                } else {
                    if (assemblyProtocol.getHourGlass().getState() == 2) {
                        setRunning(false);
                        break;
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        t.start();

        try {
            allPlayersReady.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        executor.shutdownNow();
        t.interrupt();
        assemblyProtocol.getHourGlass().stopHourglass();

        message = "Assembly phase has ended";
        for (Player player : gameInformation.getPlayerList()) {
            ClientMessenger.getGameMessenger(getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);
        }

    }

    /**
     * Updates the running flag that controls the game loop.
     */
    public void setRunning(boolean value) {
        running.set(value);
    }

    /**
     * Returns the protocol managing component booking and placement.
     */
    public AssemblyProtocol getAssemblyProtocol() {
        return assemblyProtocol;
    }

}