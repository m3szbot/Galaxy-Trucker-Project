package it.polimi.ingsw.Application.EvaluationPhase;

import java.util.*;

import it.polimi.ingsw.Application.GameInformation;
import it.polimi.ingsw.Application.Startable;
import it.polimi.ingsw.ScoreCounter.ScoreCounter;
import it.polimi.ingsw.Shipboard.Player;


public class EvaluationPhase implements Startable {
    EvaluationView evaluationView;

    public EvaluationPhase() {
        evaluationView = new EvaluationView();
    }

    public void start(GameInformation gameInformation) {
        String message;
        // assign player credits to shipBoard
        assignPlayerCredits(gameInformation);

        // Show player credits
        // (no sorting - maps can't be sorted by value, hassle)
        message = getPlayerCreditsMessage(gameInformation);
        evaluationView.printPlayerCreditsMessage(message);
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

    /*
    creates message string containing players and their credits in descending order
     */
    private String getPlayerCreditsMessage(GameInformation gameInformation) {
        ArrayList<Map.Entry<Player, Integer>> creditsList = new ArrayList<>();
        // extract player credits into creditsList
        for (Player player : gameInformation.getPlayerList()) {
            Map.Entry<Player, Integer> entry = new AbstractMap.SimpleEntry<>(player, player.getShipBoard().getShipBoardAttributes().getCredits());
            creditsList.add(entry);
        }
        // sort players based on credits in descending order
        creditsList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
        // create message string
        StringBuilder result = new StringBuilder();
        for (Map.Entry<Player, Integer> entry : creditsList) {
            result.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        // remove ", " from the end
        if (result.isEmpty()) {
            result.setLength(result.length() - 2);
        }
        return result.toString();
    }

}
