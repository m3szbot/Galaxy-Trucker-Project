package it.polimi.ingsw.Application;

import it.polimi.ingsw.Application.InitializationPhase.InitializationPhase;

public class Game implements Runnable {

    private GameState gameState = GameState.Starting;
    private GameInformation gameInformation;
    private final int gameCode;
    private InitializationPhase initializationPhase;
    private


    public Game(int gameCode) {
        this.gameCode = gameCode;
        gameInformation = new GameInformation();
    }
    
    public void run(){

        initializationPhase.start(gameInformation);

    }

}
