package it.polimi.ingsw.Controller.CorrectionPhase;

import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.View.CorrectionView.CorrectionView;

public class CorrectionThread extends Thread {
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
        errors = checkErrors(player);
        // correct errors
        while (errors) {
            playerView.printErrorsMessage(player.getShipBoard());
            col = playerView.promptForColumn();
            row = playerView.promptForRow();
            // no check for col, row value - if out of bounds, nothing happens
            player.getShipBoard().removeComponent(col, row, true);
            errors = checkErrors(player);
        }
        // errors corrected
        playerView.printFinishedMessage();
        // end of thread
    }

    /**
     * Checks if there are any incorrectly placed components (errors) in shipboard
     *
     * @param player
     * @return true if there are errors, false if shipboard is valid
     */
    private boolean checkErrors(Player player) {
        boolean errors = false;
        boolean[][] matr = player.getShipBoard().getMatrErrors();
        for (int i = 0; i < player.getShipBoard().getMatrixCols() && !errors; i++) {
            for (int j = 0; j < player.getShipBoard().getMatrixRows() && !errors; j++) {
                if (matr[i][j])
                    errors = true;
            }
        }
        return errors;
    }
}
