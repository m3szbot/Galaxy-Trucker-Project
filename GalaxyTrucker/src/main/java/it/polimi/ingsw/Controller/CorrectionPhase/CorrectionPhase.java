package it.polimi.ingsw.Controller.CorrectionPhase;

import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Connection.ServerSide.GameMessenger;
import it.polimi.ingsw.Controller.Game.Startable;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * players correct their shipboards after assembly
 */
public class CorrectionPhase implements Startable {
    final GameMessenger gameMessenger;

    public CorrectionPhase(GameInformation gameInformation) {
        this.gameMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode());
    }

    /**
     * Start correction phase.
     * Launches player threads and waits for their termination (player shipboard corrected),
     * or times out (player shipboard incorrect, player gets removed).
     *
     * @param gameInformation
     */
    public void start(GameInformation gameInformation) {
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
            scheduler.awaitTermination(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        scheduler.shutdownNow();

        removeErroneousShipboardPlayers(gameInformation);

        // end of correction phase, advance to next phase
        for (Player player : gameInformation.getPlayerList()) {
            advancePhase(player);
        }
    }

    /**
     * Remove players with invalid shipboards.
     */
    private void removeErroneousShipboardPlayers(GameInformation gameInformation) {
        // remove players who timed out with invalid shipboard
        ArrayList<Player> toRemove = new ArrayList<>();
        // find connected players to remove
        for (Player player : gameInformation.getPlayerList()) {
            if (player.getShipBoard().isErroneous()) {
                toRemove.add(player);
                for (Player target : gameInformation.getPlayerList()) {
                    sendRemovalMessage(target, player);
                }
            }
        }
        // find disconnected players to remove
        for (Player player : gameInformation.getDisconnectedPlayerList()) {
            if (player.getShipBoard().isErroneous()) {
                toRemove.add(player);
                for (Player target : gameInformation.getPlayerList()) {
                    sendRemovalMessage(target, player);
                }
            }
        }
        // actual removal of players (avoids conflicts)
        for (Player player : toRemove) {
            gameInformation.removePlayers(player);
        }
    }

    /**
     * Send advancePhase command to client.
     *
     * @param player
     */
    private void advancePhase(Player player) {
        DataContainer dataContainer = gameMessenger.getPlayerContainer(player);
        dataContainer.clearContainer();
        dataContainer.setCommand("advancePhase");
        gameMessenger.sendPlayerData(player);
    }

    /**
     * Send removed player message to the selected player.
     *
     * @param target        player to send the message to.
     * @param removedPlayer who got removed (L).
     */
    private void sendRemovalMessage(Player target, Player removedPlayer) {
        DataContainer dataContainer = gameMessenger.getPlayerContainer(target);
        dataContainer.clearContainer();
        dataContainer.setCommand("printMessage");
        dataContainer.setMessage(String.format
                ("Player %s didn't correct his shipboard in time and got removed\n", removedPlayer.getNickName()));
        gameMessenger.sendPlayerData(target);
    }

}
