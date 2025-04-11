package it.polimi.ingsw.Application.EvaluationPhase;

import it.polimi.ingsw.Application.GameInformation;
import it.polimi.ingsw.Bank.ScoreCounter;
import it.polimi.ingsw.Shipboard.Player;

public class EvaluationPhase {

    public EvaluationPhase() {
    }

    // TODO refactor into smaller methods
    public void start(GameInformation gameInformation, EvaluationView evaluationView) {
        // assign player credits to shipBoard
        assignPlayerCredits(gameInformation);

        // TODO show player credits
        evaluationView.ShowFinalScores();

        // TODO ask another game
        if (evaluationView.AskAnotherGame()) {
            // TODO start another game
        } else {
            // TODO terminate game
        }

    }

    /**
     * Calculate final points for each player
     *
     * @param gameInformation game information
     */
    private void assignPlayerCredits(GameInformation gameInformation) {
        ScoreCounter scoreCounter = new ScoreCounter(gameInformation.getGameType(), gameInformation.getPlayerList(), gameInformation.getFlightBoard().getPlayerOrderList());
        for (Player player : gameInformation.getPlayerList()) {
            player.shipBoard.shipBoardAttributes.modifyCredits(scoreCounter.getPlayerScore(player));
        }
    }

}
