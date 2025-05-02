package it.polimi.ingsw.Controller.CorrectionPhase;

import it.polimi.ingsw.Controller.Game.Startable;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.View.CorrectionView.CorrectionView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CorrectionPhase implements Startable {
    Map<Player, CorrectionView> playerViewMap;

    public CorrectionPhase(GameInformation gameInformation) {
        // create player-specific views
        playerViewMap = new HashMap<>();
        for (Player player : gameInformation.getPlayerList()) {
            playerViewMap.put(player, new CorrectionView());
        }
    }

    /**
     * start player threads
     *
     * @param gameInformation
     */
    public void start(GameInformation gameInformation) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(gameInformation.getPlayerList().size());
        // launch player threads
        for (Player player : gameInformation.getPlayerList()) {
            CorrectionThread playerThread = new CorrectionThread(player, playerViewMap.get(player));
            scheduler.submit(playerThread);
        }
        // no new tasks should be added
        scheduler.shutdown();
        // timeout (force shutdown if someone still didn't finish)
        try {
            scheduler.awaitTermination(2, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        scheduler.shutdownNow();

        // remove players with who timed out with invalid shipboard
        for (Player player : gameInformation.getPlayerList()) {
            if (player.getShipBoard().isErroneous()) {
                gameInformation.removePlayers(player);
            }
        }

        // end of correction phase
    }


}
