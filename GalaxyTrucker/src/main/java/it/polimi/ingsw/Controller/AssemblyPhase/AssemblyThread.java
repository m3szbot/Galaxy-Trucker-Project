package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Connection.ServerSide.GameMessenger;
import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Controller.Game.Game;
import it.polimi.ingsw.Model.AssemblyModel.AssemblyProtocol;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class AssemblyThread implements Runnable {
    GameInformation gameInformation;
    Player associatedPlayer;
    AssemblyProtocol assemblyProtocol;
    private GameState currentState;
    private BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    AtomicBoolean running;
    AtomicInteger end;
    Boolean isfinished = false;
    Boolean amIChoosing = false;


    public AssemblyThread(GameInformation gameInformation, Player player, AssemblyProtocol assemblyProtocol, AtomicBoolean running, AtomicInteger end) {
        this.gameInformation = gameInformation;
        this.associatedPlayer = player;
        this.assemblyProtocol = assemblyProtocol;
        this.running = running;
        this.end = end;
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
        // For now, the initial state is set using only the first player.
        // Later, threads should be launched for all players.
        while (running.get() || end.get() < gameInformation.getMaxNumberOfPlayers()) {
            setState(new AssemblyState(assemblyProtocol, associatedPlayer));

            // Separate thread for reading user input from the console
            new Thread(() -> {
                //Scanner scanner = new Scanner(System.in);
                while (running.get() || end.get() != gameInformation.getPlayerList().size()) {
                    //String input = scanner.nextLine();
                    //System.out.println("prova");
                    try {
                        String input = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerString(associatedPlayer);
                        inputQueue.offer(input);
                    } catch (PlayerDisconnectedException e) {
                        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(gameInformation,associatedPlayer);
                        String message = e.getMessage();
                        for (Player player : gameInformation.getPlayerList()) {
                            DataContainer dataContainer = ClientMessenger.getGameMessenger(assemblyProtocol.getGameCode()).getPlayerContainer(player);
                            dataContainer.setMessage(message);
                            ClientMessenger.getGameMessenger(assemblyProtocol.getGameCode()).sendPlayerMessage(player, message);
                        }
                    }

                }
            }).start();

            new Thread(() -> {
                while (running.get() == false && isfinished == false) {
                    if (!amIChoosing) {
                        setState(new ChooseStartingPositionState(assemblyProtocol, associatedPlayer));
                    }
                }
            }).start();

            // Main non-blocking game loop
            while (running.get() || end.get() < gameInformation.getPlayerList().size()) {
                //System.out.println("prova2");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }

                // Handle user input if available
                String input = inputQueue.poll();
                if (input != null) {
                    currentState.handleInput(input, this);
                }
                // Update current game state (e.g., timers, state transitions)
                currentState.update(this);
            }

            String message = "Game Over";
            ClientMessenger.getGameMessenger(assemblyProtocol.getGameCode()).sendPlayerMessage(associatedPlayer, message);

        }
    }
}
