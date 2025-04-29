package it.polimi.ingsw.Controller.Game;

import it.polimi.ingsw.Controller.AssemblyPhase.AssemblyPhase;
import it.polimi.ingsw.Controller.CorrectionPhase.CorrectionPhase;
import it.polimi.ingsw.Controller.EvaluationPhase.EvaluationPhase;
import it.polimi.ingsw.Controller.FlightPhase.FlightPhase;
import it.polimi.ingsw.Controller.InitializationPhase.InitializationPhase;
import it.polimi.ingsw.Model.GameInformation.ConnectionType;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.GameInformation.ViewType;
import it.polimi.ingsw.Model.ShipBoard.Player;

public class Game implements Runnable {

    private GameState gameState = GameState.Starting;
    private GameInformation gameInformation;
    private final int gameCode;
    private InitializationPhase initializationPhase;
    private AssemblyPhase assemblyPhase;
    private CorrectionPhase correctionPhase;
    private FlightPhase flightPhase;
    private EvaluationPhase evaluationPhase;
    private int numberOfJoinedPlayers = 0;
    private String creator;


    public Game(int gameCode) {
        this.gameCode = gameCode;
        this.gameState = GameState.Empty;
        gameInformation = new GameInformation();
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

    public void addPlayer(Player player, ViewType viewType, ConnectionType connectionType, boolean first) {
        gameInformation.addPlayers(player);
        gameInformation.setPlayerViewType(player, viewType);
        gameInformation.setPlayerConnectionType(player, connectionType);
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


    public void run() {

        initializationPhase.start(gameInformation);
        assemblyPhase.start(gameInformation);
        correctionPhase.start(gameInformation);
        flightPhase.start(gameInformation);
        evaluationPhase.start(gameInformation);

    }

}
