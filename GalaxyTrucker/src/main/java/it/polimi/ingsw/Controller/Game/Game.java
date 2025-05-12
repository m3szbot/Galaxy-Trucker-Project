package it.polimi.ingsw.Controller.Game;

import it.polimi.ingsw.Controller.AssemblyPhase.AssemblyPhase;
import it.polimi.ingsw.Controller.CorrectionPhase.CorrectionPhase;
import it.polimi.ingsw.Controller.EvaluationPhase.EvaluationPhase;
import it.polimi.ingsw.Controller.FlightPhase.FlightPhase;
import it.polimi.ingsw.Controller.InitializationPhase.InitializationPhase;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw.Controller.Game.ConnectionStatus.Connected;
import static it.polimi.ingsw.Controller.Game.ConnectionStatus.Disconnected;

public class Game implements Runnable {

    private GameState gameState;
    private GameInformation gameInformation;
    private final int gameCode;
    private InitializationPhase initializationPhase;
    private AssemblyPhase assemblyPhase;
    private CorrectionPhase correctionPhase;
    private FlightPhase flightPhase;
    private EvaluationPhase evaluationPhase;
    private int numberOfJoinedPlayers = 0;
    private String creator;
    private Map<Player, ConnectionStatus > playerConnectionStatusMap;


    public Game(int gameCode) {

        this.gameCode = gameCode;
        this.gameState = GameState.Empty;
        gameInformation = new GameInformation();
        gameInformation.setGameCode(gameCode);
        playerConnectionStatusMap = new HashMap<>();

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

    public int getGameCode(){
        return gameCode;
    }

    public void disconnectPlayer(Player player){
        playerConnectionStatusMap.put(player, Disconnected);
    }

    public void addPlayer(Player player, boolean first) {

        gameInformation.addPlayers(player);
        numberOfJoinedPlayers++;
        playerConnectionStatusMap.put(player, Connected);

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

    public boolean isNickNameRepeated(String nickname){

        for(Player player: gameInformation.getPlayerList()){

            if(nickname.equals(player.getNickName()))
                return true;

        }

        return false;
    }


    public void run() {

        initializationPhase.start(gameInformation);
        assemblyPhase.start(gameInformation);
        correctionPhase.start(gameInformation);
        flightPhase.start(gameInformation);
        evaluationPhase.start(gameInformation);

    }

}
