package it.polimi.ingsw.Controller.CorrectionPhase;

import it.polimi.ingsw.Controller.Game.Startable;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.View.CorrectionView.CorrectionView;

import java.util.HashMap;
import java.util.Map;

public class CorrectionPhase implements Startable {
    Map<Player, CorrectionView> playerViewMap;
    Map<Player, CorrectionThread> playerThreadMap;

    public CorrectionPhase(GameInformation gameInformation) {
        // create player-specific views
        playerViewMap = new HashMap<>();
        for (Player player : gameInformation.getPlayerList()) {
            playerViewMap.put(player, new CorrectionView());
        }
        playerThreadMap = new HashMap<>();
    }

    /**
     * start player threads
     *
     * @param gameInformation
     */
    public void start(GameInformation gameInformation) {
        for (Player player : gameInformation.getPlayerList()) {
            CorrectionThread playerThread = new CorrectionThread(player, playerViewMap.get(player));
            playerThreadMap.put(player, playerThread);
            playerThread.start();

        }

        // wait for threads to finish
        // set common timeout?, remove timeout players?
        for (Map.Entry<Player, CorrectionThread> entry : playerThreadMap.entrySet()) {
            try {
                entry.getValue().join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // end of correction phase
    }


}
