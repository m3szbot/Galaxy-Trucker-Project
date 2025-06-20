package it.polimi.ingsw.Controller.Game;

import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Controller.AssemblyPhase.AssemblyPhase;
import it.polimi.ingsw.Controller.CorrectionPhase.CorrectionPhase;
import it.polimi.ingsw.Controller.EvaluationPhase.EvaluationPhase;
import it.polimi.ingsw.Controller.FlightPhase.FlightPhase;
import it.polimi.ingsw.Controller.InitializationPhase.InitializationPhase;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.io.Serializable;

public class Game implements Runnable, Serializable {

    private final int gameCode;
    private GameState gameState;
    private GameInformation gameInformation;
    private InitializationPhase initializationPhase;
    private AssemblyPhase assemblyPhase;
    private CorrectionPhase correctionPhase;
    private FlightPhase flightPhase;
    private EvaluationPhase evaluationPhase;
    private int numberOfJoinedPlayers;
    private String creator;


    public Game(int gameCode) {

        this.gameCode = gameCode;
        this.gameState = GameState.Empty;
        gameInformation = new GameInformation();
        gameInformation.setGameCode(gameCode);
        numberOfJoinedPlayers = 0;


    }

    public void setUpPhases() {


        initializationPhase = new InitializationPhase(gameInformation);
        initializationPhase.start();
        assemblyPhase = new AssemblyPhase(gameInformation);
        correctionPhase = new CorrectionPhase(gameInformation);
        flightPhase = new FlightPhase(gameInformation);
        evaluationPhase = new EvaluationPhase(gameInformation);
    }

    public boolean isFull() {

        return (gameInformation.getMaxNumberOfPlayers() == numberOfJoinedPlayers);
    }


    public GameState getGameState() {
        return gameState;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        gameInformation.setMaxNumberOfPlayers(numberOfPlayers);
    }

    public int getGameCode() {
        return gameCode;
    }

    public void addPlayer(Player player, boolean first) {

        gameInformation.addPlayer(player);
        numberOfJoinedPlayers++;

        if (first) {
            creator = player.getNickName();
        }
    }

    public String getCreator() {
        return creator;
    }

    public void setGameType(GameType gameType) {
        gameInformation.setGameType(gameType);
    }

    public GameInformation getGameInformation() {
        return gameInformation;
    }

    public void changeGameState(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Start the different phases of the game.
     */
    public void run() {

        System.out.println("Starting assembly phase of game " + gameCode + "...");
        assemblyPhase.start();
        System.out.println("Starting correction phase of game " + gameCode + "...");
        correctionPhase.start();
        System.out.println("Starting flight phase of game " + gameCode + "...");
        flightPhase.start();
        System.out.println("Starting evaluation phase of game " + gameCode + "...");
        evaluationPhase.start();
        gameState = GameState.GameOver;
        ClientMessenger.endGame(gameCode);
        System.out.println("Game " + gameCode + " has ended");

    }
}
