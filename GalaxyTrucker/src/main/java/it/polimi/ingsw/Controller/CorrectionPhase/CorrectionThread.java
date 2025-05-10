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
        int values[];
        int col, row;
        // check if there are errors
        errors = player.getShipBoard().isErroneous();
        // correct errors
        while (errors) {
            playerView.printErrorsMessage(player.getShipBoard());
            values = playerView.promptForColumnRow();
            col = values[0];
            row = values[1];
            playerView.printComponentRemovalMessage(col, row);
            // no check for col, row value - if out of bounds, nothing happens
            // trigger automatically removes disconnected components - set to false
            player.getShipBoard().removeComponent(col, row, false);
            errors = player.getShipBoard().isErroneous();
        }
        // errors corrected
        playerView.printFinishedMessage();
        // end of thread
    }

}
