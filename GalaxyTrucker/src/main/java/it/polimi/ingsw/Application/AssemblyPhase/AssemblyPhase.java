package it.polimi.ingsw.Application.AssemblyPhase;

import it.polimi.ingsw.Application.GameInformation;
import it.polimi.ingsw.Application.GameType;
import it.polimi.ingsw.Assembly.AssemblyProtocol;
import it.polimi.ingsw.Shipboard.Color;
import it.polimi.ingsw.Shipboard.Player;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * AssemblyGame is the main game controller that manages the game loop,
 * player input, and state transitions for the component assembly phase.
 *
 * @author Giacomo
 */
public class AssemblyPhase {
    private GameState currentState;
    private boolean running = true;
    private BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    private GameInformation gameInformation;
    private AssemblyProtocol assemblyProtocol;
    private AssemblyView assemblyView;


    /**
     * Initializes the game with provided game information.
     *
     */
    public AssemblyPhase() {
        assemblyProtocol = new AssemblyProtocol(gameInformation);
        assemblyView = new AssemblyView();
    }

    public AssemblyView getAssemblyView() {
        return assemblyView;
    }

    /**
     * Returns the protocol managing component booking and placement.
     */
    public AssemblyProtocol getAssemblyProtocol() {
        return assemblyProtocol;
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
     * Returns the game information object containing all players.
     */
    public GameInformation getGameInformation() {
        return gameInformation;
    }

    /**
     * Updates the running flag that controls the game loop.
     */
    public void setRunning(boolean value) {
        running = value;
    }

    /**
     * Starts the game, initializes the state, sets up user input thread,
     * and runs the main non-blocking game loop.
     */
    public void start(GameInformation gameInformation) {
        this.gameInformation = gameInformation;
        // For now, the initial state is set using only the first player.
        // Later, threads should be launched for all players.
        setState(new AssemblyState(assemblyView,assemblyProtocol, gameInformation.getPlayerList().getFirst()));

        // Separate thread for reading user input from the console
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (running) {
                String input = scanner.nextLine();
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
/*
      //main fatto a caso da gecky per fare test
    public static void main(String[] args) {
        AssemblyPhase ass;
        Player gecky;
        GameInformation gameInformation = new GameInformation();
        gameInformation.setGameType(GameType.NormalGame);
        gameInformation.setUpPlayers(gecky = new Player("Gecky", Color.RED, gameInformation), 1);
        try {gameInformation.setUpCards(GameType.NormalGame);} catch (Exception e){e.printStackTrace();}
        try  {gameInformation.setUpComponents();} catch (Exception e){e.printStackTrace();}
        ass = new AssemblyPhase(gameInformation);
        ass.start();
    }
*/
    /**
     * Temporary main method for standalone testing.
     * Should be moved into a class where GameInformation is initialized.
     */


    /*public static void main(String[] args) {
        new AssemblyGame().start();
    }*/
}