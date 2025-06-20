package it.polimi.ingsw.Controller.CorrectionPhase;

import it.polimi.ingsw.Controller.Phase;
import it.polimi.ingsw.Controller.Sleeper;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GamePhase;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Players correct their shipboards after assembly.
 *
 * @author Boti
 */
public class CorrectionPhase extends Phase {
    // gameInformation, gameMessenger attributes inherited from Phase

    /**
     * Calls Phase constructor, sets inherited attributes gameInformation, gameMessenger.
     */
    public CorrectionPhase(GameInformation gameInformation) {
        super(gameInformation);
    }


    /**
     * Start correction phase.
     * Launches player threads and waits for their termination (player shipboard corrected),
     * or times out (player shipboard incorrect, player gets removed).
     */
    public void start() {
        setGamePhaseToClientServer(GamePhase.Correction);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(gameInformation.getPlayerList().size());
        // launch player threads
        for (Player player : gameInformation.getPlayerList()) {
            CorrectionThread playerThread = new CorrectionThread(gameInformation, player);
            scheduler.submit(playerThread);
        }

        // no new tasks should be added
        scheduler.shutdown();

        // timeout (force shutdown if someone still didn't finish)
        try {
            scheduler.awaitTermination(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            System.out.println("Error: correction phase ended abruptly.");
            e.printStackTrace();
        }
        scheduler.shutdownNow();

        // remove players with erroneous shipboards at the end of correction
        removeErroneousShipboardPlayers(gameInformation.getPlayerList());
        removeErroneousShipboardPlayers(gameInformation.getDisconnectedPlayerList());

        // end of correction phase, advance to next phase
        System.out.println("Correction phase ended");
    }

    /**
     * Remove players with invalid shipboards from the flightBoard and send messages to all players.
     * To call for connected and disconnected? players.
     */
    private void removeErroneousShipboardPlayers(List<Player> playerList) {
        // if playerList is empty, do nothing
        if (playerList.isEmpty())
            return;

        // find erroneous shipboard players
        for (int i = 0; i < playerList.size(); i++) {
            Player player = playerList.get(i);
            // remove erroneous shipboard
            if (player.getShipBoard().isErroneous()) {
                // print player messages
                gameMessenger.getPlayerMessenger(player).printMessage("\nYou didn't correct the errors in your shipboard and have been eliminated from the flight.");
                gameMessenger.getPlayerMessenger(player).printMessage("You are now spectating.");

                // notify all
                gameMessenger.sendMessageToAll(String.format("\n%s didn't correct the errors in his/her shipboard and have been eliminated from the flight.", player.getColouredNickName()));

                // remove player from flightboard if already added
                if (gameInformation.getFlightBoard().isInFlight(player))
                    gameInformation.getFlightBoard().eliminatePlayer(player);
            }
        }

        // all erroneous shipboard players removed
        Sleeper.sleepXSeconds(4);
    }

}
