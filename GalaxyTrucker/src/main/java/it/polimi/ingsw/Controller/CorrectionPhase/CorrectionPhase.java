package it.polimi.ingsw.Controller.CorrectionPhase;

import it.polimi.ingsw.Controller.Game.Startable;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.View.CorrectionView.CorrectionView;

import java.util.HashMap;
import java.util.Map;

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
    // TODO state pattern
    public void start(GameInformation gameInformation) {
        for (Player player : gameInformation.getPlayerList()) {
            // thread variable is lost, but the thread itself is not
            CorrectionThread playerThread = new CorrectionThread(player, playerViewMap.get(player));
            playerThread.start();

        }
    }


}
