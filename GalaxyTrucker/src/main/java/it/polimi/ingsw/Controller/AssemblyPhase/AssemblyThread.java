package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Model.AssemblyModel.AssemblyProtocol;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class AssemblyThread implements Runnable {
    GameInformation gameInformation;
    Player associatedPlayer;
    AssemblyProtocol assemblyProtocol;
    private GameState currentState;
    private BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    AtomicBoolean running;
    CountDownLatch latch;
    AtomicBoolean isfinished = new AtomicBoolean(false);
    AtomicBoolean amIChoosing = new AtomicBoolean(false);
    AtomicBoolean end = new AtomicBoolean(false);
    Thread t;


    public AssemblyThread(GameInformation gameInformation, Player player, AssemblyProtocol assemblyProtocol, AtomicBoolean running, CountDownLatch latch) {
        this.gameInformation = gameInformation;
        this.associatedPlayer = player;
        this.assemblyProtocol = assemblyProtocol;
        this.running = running;
        this.latch = latch;
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

    /**
     * Updates the running flag that controls the game loop.
     */
    public void setRunning(boolean value) {
        running.set(value);
    }

    public AssemblyProtocol getAssemblyProtocol() {
        return assemblyProtocol;
    }

    public GameInformation getGameInformation() {
        return gameInformation;
    }

    @Override
    public void run() {
        try {
        setState(new AssemblyState(assemblyProtocol, associatedPlayer));
        assemblyProtocol.getHourGlass().twist(assemblyProtocol, gameInformation.getPlayerList());

        // Separate thread for reading user input from the console
         t = new Thread(() -> {
            AtomicBoolean disconnected = new AtomicBoolean(false);
            while (!end.get()) {
                if (!disconnected.get()) {
                    try {
                        String input = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(associatedPlayer).getPlayerString();
                        inputQueue.offer(input);
                    } catch (PlayerDisconnectedException e) {
                        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(gameInformation, associatedPlayer);
                        String message = e.getMessage();
                        disconnected.set(true);
                        for (Player player : gameInformation.getPlayerList()) {
                            if (!player.equals(associatedPlayer)) {ClientMessenger.getGameMessenger(getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);}
                        }
                    }
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {
                    }

                    if (ClientMessenger.getGameMessenger(gameInformation.getGameCode()).isPlayerConnected(associatedPlayer, gameInformation)) {
                        disconnected.set(false);
                        String message = "Welcome back! You have been reconnected.";
                        ClientMessenger.getGameMessenger(getAssemblyProtocol().getGameCode()).getPlayerMessenger(associatedPlayer).printMessage(message);

                    }
                }
            }
        });
        t.start();


        // Main non-blocking game loop
        while (running.get()) {
            //System.out.println("prova2");
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
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

        if (!amIChoosing.get()) {
            setState(new ChooseStartingPositionState(assemblyProtocol, associatedPlayer));
            while (!isfinished.get()) {
                String input = inputQueue.poll();
                if (input != null) {
                    currentState.handleInput(input, this);
                }
                currentState.update(this);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }

            }
        }
    } catch (Exception e) {
            e.printStackTrace();
        }finally {
            t.interrupt();
            latch.countDown();
        }
    }
}
