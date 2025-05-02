package it.polimi.ingsw.Controller.CorrectionPhase;

import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.View.CorrectionView.CorrectionView;

public class CorrectionThread implements Runnable {
    Player player;
    CorrectionView playerView;

    public CorrectionThread(Player player, CorrectionView playerView) {
        this.player = player;
        this.playerView = playerView;
    }

    /**
     * remove components until shipboard of player is valid
     */
    @Override
    public void run() {
        boolean errors;
        int col, row;
        // check if there are errors
        errors = player.getShipBoard().isErroneous();
        // correct errors
        while (errors) {
            playerView.printErrorsMessage(player.getShipBoard());
            col = playerView.promptForColumn();
            row = playerView.promptForRow();
            // no check for col, row value - if out of bounds, nothing happens
            player.getShipBoard().removeComponent(col, row, true);
            errors = player.getShipBoard().isErroneous();
        }
        // errors corrected
        playerView.printFinishedMessage();
        // end of thread
    }

}
