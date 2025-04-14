package it.polimi.ingsw.Application.EvaluationPhase;

import java.util.*;

import it.polimi.ingsw.Application.GameInformation;
import it.polimi.ingsw.Application.Startable;
import it.polimi.ingsw.ScoreCounter.ScoreCounter;
import it.polimi.ingsw.Shipboard.Player;

import java.util.HashMap;

public class EvaluationPhase implements Startable {

    public EvaluationPhase() {
    }

    public void start(GameInformation gameInformation) {
        EvaluationView evaluationView = new EvaluationView();
        String message;

        // assign player credits to shipBoard
        assignPlayerCredits(gameInformation);

        // Show player credits
        // (no sorting - maps can't be sorted by value, hassle)
        message = getPlayerCreditsMessage(gameInformation);
        evaluationView.ShowPlayerCredits(message);

        // TODO ask another game
        message = "Do you want to play another game with the same players?[y/n]";
        if (evaluationView.AskAnotherGame(message)) {
            // TODO start another game
        }
        // end evaluationPhase
    }


    /**
     * Calculate final points for each player
     *
     * @param gameInformation game information
     */
    private void assignPlayerCredits(GameInformation gameInformation) {
        ScoreCounter scoreCounter = new ScoreCounter(gameInformation.getGameType(), gameInformation.getPlayerList(), gameInformation.getFlightBoard().getPlayerOrderList());
        for (Player player : gameInformation.getPlayerList()) {
            player.getShipBoard().getShipBoardAttributes().updateCredits(scoreCounter.getPlayerScore(player));
        }
    }

    private String getPlayerCreditsMessage(GameInformation gameInformation) {
        // extract player credits into creditsMap
        Map<Player, Integer> creditsMap = new HashMap<>();
        for (Player player : gameInformation.getPlayerList()) {
            creditsMap.put(player, player.getShipBoard().getShipBoardAttributes().getCredits());
        }
        StringBuilder result = new StringBuilder();
        for (Map.Entry<Player, Integer> entry : creditsMap.entrySet()) {
            result.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        // remove ", " from the end
        if (result.isEmpty()) {
            result.setLength(result.length() - 2);
        }
        return result.toString();
    }

}
