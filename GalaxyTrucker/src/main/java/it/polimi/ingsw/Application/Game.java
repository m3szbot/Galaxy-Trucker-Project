package it.polimi.ingsw.Application;

import it.polimi.ingsw.Application.AssemblyPhase.AssemblyPhase;
import it.polimi.ingsw.Application.CorrectionPhase.CorrectionPhase;
import it.polimi.ingsw.Application.EvaluationPhase.EvaluationPhase;
import it.polimi.ingsw.Application.FlightPhase.FlightPhase;
import it.polimi.ingsw.Application.InitializationPhase.InitializationPhase;
import it.polimi.ingsw.Shipboard.Player;

public class Game implements Runnable {

    private GameState gameState = GameState.Starting;
    private GameInformation gameInformation;
    private final int gameCode;
    private InitializationPhase initializationPhase;
    private AssemblyPhase assemblyPhase;
    private CorrectionPhase correctionPhase;
    private FlightPhase flightPhase;
    private EvaluationPhase evaluationPhase;


    public Game(int gameCode) {
        this.gameCode = gameCode;
        gameInformation = new GameInformation();
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        gameInformation.setMaxNumberOfPlayers(numberOfPlayers);
    }

    public void addPlayer(Player player) {
        gameInformation.addPlayers(player);
    }

    public void changeGameState(GameState gameState){
        this.gameState = gameState;
    }


    public void run(){

        initializationPhase.start(gameInformation);
        assemblyPhase.start(gameInformation);
        correctionPhase.start(gameInformation);
        flightPhase.start(gameInformation);
        evaluationPhase.start(gameInformation);

    }

}
