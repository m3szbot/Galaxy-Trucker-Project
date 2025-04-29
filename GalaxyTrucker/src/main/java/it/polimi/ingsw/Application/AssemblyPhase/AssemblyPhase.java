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
     * Returns the game information object containing all players.
     */
    public GameInformation getGameInformation() {
        return gameInformation;
    }


    /**
     * Starts the game, initializes the state, sets up user input thread,
     * and runs the main non-blocking game loop.
     */
    public void start(GameInformation gameInformation) {
        this.gameInformation = gameInformation;
        for(int i = 0; i < gameInformation.getPlayerList().size(); i++){
            int threadInt = i;
            new Thread(() -> {
                AssemblyThread assemblyThread = new AssemblyThread(gameInformation, gameInformation.getPlayerList().get(threadInt), assemblyProtocol, assemblyView);
            });
        }
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