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
    private GameInformation gameInformation;
    private Player associatedPlayer;
    private AssemblyProtocol assemblyProtocol;
    private GameState currentState;
    private BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    private AtomicBoolean running;
    private CountDownLatch latch;
    private AtomicBoolean isfinished = new AtomicBoolean(false);
    private AtomicBoolean amIChoosing = new AtomicBoolean(false);
    private AtomicBoolean end = new AtomicBoolean(false);
    private Thread t;
    private AtomicBoolean blocked = new AtomicBoolean(false);


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

    public void setEnd(){
        end.set(true);

        if(blocked.get()) {
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(associatedPlayer).unblockUserInputGetterCall();
        }
    }

    public AssemblyProtocol getAssemblyProtocol() {
        return assemblyProtocol;
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
                            blocked.set(true);
                            String input = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(associatedPlayer).getPlayerString();
                            blocked.set(false);
                            inputQueue.offer(input);
                            try{
                                Thread.sleep(100);
                            }catch (InterruptedException e){

                                break;
                            }
                        } catch (PlayerDisconnectedException e) {
                            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(gameInformation, associatedPlayer);
                            String message = e.getMessage();
                            disconnected.set(true);
                            for (Player player : gameInformation.getPlayerList()) {
                                if (!player.equals(associatedPlayer)) {
                                    ClientMessenger.getGameMessenger(getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);
                                }
                            }
                        }
                    } else {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {

                            break;
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

            if (!amIChoosing.get()) {
                setState(new ChooseStartingPositionState(assemblyProtocol, associatedPlayer));
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
        }finally {

            latch.countDown();
        }
    }

}



