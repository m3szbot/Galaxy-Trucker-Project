package it.polimi.ingsw.Controller.Game;

import it.polimi.ingsw.Controller.AssemblyPhase.AssemblyPhase;
import it.polimi.ingsw.Controller.CorrectionPhase.CorrectionPhase;
import it.polimi.ingsw.Controller.EvaluationPhase.EvaluationPhase;
import it.polimi.ingsw.Controller.FlightPhase.FlightPhase;
import it.polimi.ingsw.Controller.InitializationPhase.InitializationPhase;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Player;

public class Game implements Runnable {

    private GameState gameState;
    private GameInformation gameInformation;
    private final int gameCode;
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

    public void setUpPhases(){

        initializationPhase = new InitializationPhase();
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

    public int getGameCode(){
        return gameCode;
    }



    public void addPlayer(Player player, boolean first) {

        gameInformation.addPlayers(player);
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
