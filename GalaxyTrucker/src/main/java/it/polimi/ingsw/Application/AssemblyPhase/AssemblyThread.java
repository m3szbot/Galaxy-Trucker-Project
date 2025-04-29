package it.polimi.ingsw.Application.AssemblyPhase;

import it.polimi.ingsw.Application.Game;
import it.polimi.ingsw.Application.GameInformation;
import it.polimi.ingsw.Assembly.AssemblyProtocol;
import it.polimi.ingsw.Shipboard.Player;

import java.io.DataInputStream;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AssemblyThread implements Runnable {
    GameInformation gameInformation;
    Player associatedPlayer;
    AssemblyProtocol assemblyProtocol;
    AssemblyView assemblyView;
    private GameState currentState;
    private boolean running = true;
    private BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();

    public AssemblyThread(GameInformation gameInformation, Player player, AssemblyProtocol assemblyProtocol, AssemblyView assemblyView) {
        this.gameInformation = gameInformation;
        this.associatedPlayer = player;
        this.assemblyProtocol = assemblyProtocol;
        this.assemblyView = assemblyView;
    }

    /**
     * Sets the current state of the game and triggers its enter logic.
     *
     * @param newState the new state to switch to
     */
    public void setState(GameState newState) {
        this.currentState = newState;
        currentState.enter(this, assemblyView);
    }

    /**
     * Updates the running flag that controls the game loop.
     */
    public void setRunning(boolean value) {
        running = value;
    }

    public AssemblyView getAssemblyView() {
        return assemblyView;
    }

    public AssemblyProtocol getAssemblyProtocol() {
        return assemblyProtocol;
    }

    public GameInformation getGameInformation() {
        return gameInformation;
    }

    @Override
    public void run(){
        // For now, the initial state is set using only the first player.
        // Later, threads should be launched for all players.
        setState(new AssemblyState(assemblyView,assemblyProtocol, gameInformation.getPlayerList().getFirst()));

        // Separate thread for reading user input from the console
        new Thread(() -> {
            //Scanner scanner = new Scanner(System.in);
            while (running) {
                //String input = scanner.nextLine();
                String input = new DataInputStream(socket.getInputStream()).readUTF();
                inputQueue.offer(input);
            }
        }).start();

        // Main non-blocking game loop
        while (running) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {}

            // Handle user input if available
            String input = inputQueue.poll();
            if (input != null) {
                currentState.handleInput(input, this);
            }
            // Update current game state (e.g., timers, state transitions)
            currentState.update(this);
        }

        assemblyView.printGameOverMessage();
    }
}
