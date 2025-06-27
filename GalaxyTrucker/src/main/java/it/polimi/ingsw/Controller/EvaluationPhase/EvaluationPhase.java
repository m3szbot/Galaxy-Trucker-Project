package it.polimi.ingsw.Controller.EvaluationPhase;

import it.polimi.ingsw.Controller.Phase;
import it.polimi.ingsw.Controller.Sleeper;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GamePhase;
import it.polimi.ingsw.Model.ScoreCounter.ScoreCounter;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;


/**
 * Calculates final scores of players added to the flightBoard after flight phase ends.
 *
 * @author Boti
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
        setGamePhaseToClientServer(GamePhase.Evaluation);
        String message;
        // assign player credits to shipBoard
        assignPlayerCredits();

        message = getLeaderboardMessage();
        gameMessenger.sendMessageToAll(message);

        // suspend main thread so that players have time to read the leaderboard
        Sleeper.sleepXSeconds(30);

        // end of evaluationPhase
        // end of game
        gameMessenger.endGameToAll();
        System.out.println("Evaluation phase ended");
    }


    /**
     * Calculate and assign final points for each player. Only for players added to the flightBoard.
     */
    private void assignPlayerCredits() {
        ScoreCounter scoreCounter = new ScoreCounter(gameInformation.getGameType());

        // calculate player credits
        scoreCounter.calculatePlayerScores(gameInformation.getFlightBoard());

        // assign player credits to their shipboards (only players added to the flightBoard)
        // finishing players
        for (Player player : gameInformation.getFlightBoard().getPlayerOrderList())
            player.getShipBoard().getShipBoardAttributes().addCredits(scoreCounter.getPlayerScore(player));

        // eliminated players
        for (Player player : gameInformation.getFlightBoard().getEliminatedList())
            player.getShipBoard().getShipBoardAttributes().addCredits(scoreCounter.getPlayerScore(player));

        // gave up players
        for (Player player : gameInformation.getFlightBoard().getGaveUpList())
            player.getShipBoard().getShipBoardAttributes().addCredits(scoreCounter.getPlayerScore(player));
    }

    /**
     * Creates message string containing players and their credits in descending order.
     * Only for players added to the flightBoard (connected or disconnected).
     *
     * @return leaderboard string
     */
    private String getLeaderboardMessage() {
        ArrayList<Map.Entry<Player, Integer>> creditsList = new ArrayList<>();

        // extract player credits into creditsList (only players added to the fligthBoard)

        // connected players
        for (Player player : gameInformation.getFlightBoard().getPlayerOrderList()) {
            Map.Entry<Player, Integer> entry = new AbstractMap.SimpleEntry<>(player, player.getShipBoard().getShipBoardAttributes().getCredits());
            creditsList.add(entry);
        }

        // eliminated players
        for (Player player : gameInformation.getFlightBoard().getEliminatedList()) {
            Map.Entry<Player, Integer> entry = new AbstractMap.SimpleEntry<>(player, player.getShipBoard().getShipBoardAttributes().getCredits());
            creditsList.add(entry);
        }

        // gave up players
        for (Player player : gameInformation.getFlightBoard().getGaveUpList()) {
            Map.Entry<Player, Integer> entry = new AbstractMap.SimpleEntry<>(player, player.getShipBoard().getShipBoardAttributes().getCredits());
            creditsList.add(entry);
        }

        // sort players based on credits in descending order
        creditsList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        // no players on flightboard
        if (creditsList.isEmpty()) {
            return "No players on the flightBoard.\n";
        }

        // create message string
        StringBuilder result = new StringBuilder();
        int pos = 1;
        for (Map.Entry<Player, Integer> entry : creditsList) {
            // print position
            switch (pos) {
                case 1 -> result.append("1st: ");
                case 2 -> result.append("2nd: ");
                case 3 -> result.append("3rd: ");
                default -> result.append(String.format("%dth: ", pos));
            }

            // add (disconnected) for disconnected players
            if (gameInformation.getDisconnectedPlayerList().contains(entry.getKey()))
                result.append("(disconnected) ");

            // did not finish
            if (!gameInformation.getFlightBoard().getPlayerOrderList().contains(entry.getKey()))
                result.append("(DNF) ");

            // add player name and score
            result.append(String.format("%s - %d\n", entry.getKey().getColouredNickName(), entry.getValue()));
            pos++;
        }
        return result.toString();
    }
}
