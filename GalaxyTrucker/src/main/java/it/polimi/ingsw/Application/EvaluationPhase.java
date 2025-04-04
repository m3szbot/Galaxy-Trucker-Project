package it.polimi.ingsw.Application;

import it.polimi.ingsw.Bank.ScoreCounter;
import it.polimi.ingsw.Shipboard.Player;

public class EvaluationPhase {

    public EvaluationPhase() {
    }

    // TODO refactor into smaller methods
    public void start(GameInformation gameInformation, EvaluationView evaluationView) {

        // show final scores
        evaluationView.ShowFinalScores();

        // start another game?
        if (evaluationView.AskAnotherGame()) {
            // TODO start another game
        } else {
            // TODO terminate game
        }

    }

    /**
     * Calculate final points for each player
     *
     * @param gameInformation
     */
    private void assignPoints(GameInformation gameInformation) {
        ScoreCounter scoreCounter = new ScoreCounter(gameInformation.getGameType(), gameInformation.getPlayerList());
        for (Player player : gameInformation.getPlayerList()) {
            // TODO shipboard not done
            player.shipBoard.shipBoardAttributes.modifyCredits(scoreCounter.calculatePlayerScore(player));
        }
    }

}
