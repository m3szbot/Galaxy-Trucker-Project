package it.polimi.ingsw.Controller.EvaluationPhase;

import it.polimi.ingsw.Controller.Phase;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GamePhase;
import it.polimi.ingsw.Model.ScoreCounter.ScoreCounter;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;


/**
 * calculates final scores of players after flight phase ends
 */
public class EvaluationPhase extends Phase {
    // gameInformation, gameMessenger attributes inherited from Phase

    /**
     * Calls Phase constructor, sets inherited attributes gameInformation, gameMessenger.
     */
    public EvaluationPhase(GameInformation gameInformation) {
        super(gameInformation);
    }

    /**
     * Start evaluationPhase. Calculates player scores and prints scoreboard for all players.
     */
    public void start() {
        setGamePhaseToAll(GamePhase.Evaluation);
        String message;
        // assign player credits to shipBoard
        assignPlayerCredits(gameInformation);

        message = getLeaderboardMessage(gameInformation);
        gameMessenger.sendMessageToALl(message);

        // suspend main thread so that players have time to read the leaderboard
        try {
            // 1000ms = 1s
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // end of evaluationPhase
        // end of game
        gameMessenger.endGame();
        System.out.println("Evaluation phase ended");
    }


    /**
     * Calculate and assign final points for each player.
     */
    private void assignPlayerCredits(GameInformation gameInformation) {
        ScoreCounter scoreCounter = new ScoreCounter(gameInformation.getGameType());
        // calculate player credits
        scoreCounter.calculatePlayerScores(gameInformation.getFlightBoard());
        // assign player credits to their shipboards
        for (Player player : gameInformation.getPlayerList()) {
            player.getShipBoard().getShipBoardAttributes().updateCredits(scoreCounter.getPlayerScore(player));
        }
        for (Player player : gameInformation.getDisconnectedPlayerList()) {
            player.getShipBoard().getShipBoardAttributes().updateCredits(scoreCounter.getPlayerScore(player));
        }
    }

    /**
     * creates message string containing players and their credits in descending order
     *
     * @return leaderboard string
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
            result.append(entry.getKey().getNickName()).append(": ").append(entry.getValue()).append("\n");
        }
        return result.toString();
    }
}
