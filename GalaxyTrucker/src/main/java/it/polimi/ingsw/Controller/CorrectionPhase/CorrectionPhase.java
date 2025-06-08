package it.polimi.ingsw.Controller.CorrectionPhase;

import it.polimi.ingsw.Controller.Phase;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GamePhase;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * players correct their shipboards after assembly
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
        removeErroneousShipboardPlayers(gameInformation);

        // end of correction phase, advance to next phase
        System.out.println("Correction phase ended");
    }

    /**
     * Remove players with invalid shipboards and send message of it to all players.
     */
    private void removeErroneousShipboardPlayers(GameInformation gameInformation) {
        String message;

        // connected players
        for (int i = 0; i < gameInformation.getPlayerList().size(); i++) {
            Player player = gameInformation.getPlayerList().get(i);
            // remove erroneous shipboard
            if (player.getShipBoard().isErroneous()) {
                message = String.format("Player %s didn't correct his shipboard in time and got removed\n",
                        player.getNickName());
                forcePlayerToGiveUp(gameInformation, player, gameMessenger, message);
            }
        }

        // disconnected players
        for (int i = 0; i < gameInformation.getDisconnectedPlayerList().size(); i++) {
            Player player = gameInformation.getDisconnectedPlayerList().get(i);
            // remove erroneous shipboard
            if (player.getShipBoard().isErroneous()) {
                message = String.format("Player %s didn't correct his shipboard in time and got removed\n",
                        player.getNickName());
                forcePlayerToGiveUp(gameInformation, player, gameMessenger, message);
            }
        }

    }

}
