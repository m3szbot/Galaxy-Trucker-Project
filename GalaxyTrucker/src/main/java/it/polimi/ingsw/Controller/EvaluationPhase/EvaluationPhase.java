package it.polimi.ingsw.Controller.EvaluationPhase;

import java.util.*;

import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.Startable;
import it.polimi.ingsw.Model.ScoreCounter.ScoreCounter;
import it.polimi.ingsw.Model.ShipBoard.Player;


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
        message = getLeaderboardMessage(gameInformation);
        evaluationView.printLeaderboardMessage(message);
        // suspend main thread so that players have time to read the leaderboard
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            System.out.println("Sleep was interrupted");
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

    /*
    creates message string containing players and their credits in descending order
     */
    private String getLeaderboardMessage(GameInformation gameInformation) {
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
