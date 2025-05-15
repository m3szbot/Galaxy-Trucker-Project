package it.polimi.ingsw.Controller.AssemblyPhase;


import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Controller.Phase;
import it.polimi.ingsw.Model.AssemblyModel.AssemblyProtocol;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GamePhase;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * AssemblyGame is the main game controller that manages the game loop,
 * player input, and state transitions for the component assembly phase.
 *
 * @author Giacomo
 */
public class AssemblyPhase extends Phase {
    private GameState currentState;
    private AtomicBoolean running = new AtomicBoolean(true);
    private AtomicInteger end = new AtomicInteger(0);
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
    public void start(){

        gameInformation.setGamePhaseServerClient(GamePhase.Assembly);

        /*message = "Prova assurbanipal";
        for(Player player: gameInformation.getPlayerList()){
            ClientMessenger.getGameMessenger(getAssemblyProtocol().getGameCode()).sendPlayerMessage(player, message);;
        }*/



        /*
        for (int i = 0; i < gameInformation.getPlayerList().size(); i++) {
            int threadInt = i;
            new Thread(() -> {
                while (running.get() || end.get() < gameInformation.getMaxNumberOfPlayers()) {
                    Runnable task = new AssemblyThread(
                            gameInformation,
                            gameInformation.getPlayerList().get(threadInt),
                            assemblyProtocol,
                            running,
                            end
                    );
                    new Thread(task).start();  // ← qui lanci il thread che esegue il Runnable
                }
            }).start();
        }
        */

        ExecutorService executor = Executors.newFixedThreadPool(gameInformation.getPlayerList().size());
        for (Player player : gameInformation.getPlayerList()) {
            executor.submit(new AssemblyThread(gameInformation, player, assemblyProtocol, running, end));
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
                } catch (InterruptedException ignored) {
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        message = "Assembly phase has ended";
        for (Player player : gameInformation.getPlayerList()) {
            DataContainer dataContainer = ClientMessenger.getGameMessenger(getAssemblyProtocol().getGameCode()).getPlayerContainer(player);
            dataContainer.setMessage(message);
            dataContainer.setCommand("printMessage");
            ClientMessenger.getGameMessenger(getAssemblyProtocol().getGameCode()).sendPlayerData(player);
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