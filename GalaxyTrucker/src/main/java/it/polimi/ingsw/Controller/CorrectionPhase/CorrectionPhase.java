package it.polimi.ingsw.Controller.CorrectionPhase;

import it.polimi.ingsw.Controller.Game.Startable;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.View.CorrectionView.CorrectionView;

public class CorrectionPhase implements Startable {
    CorrectionView correctionView;

    public CorrectionPhase() {
        correctionView = new CorrectionView();
    }

    /**
     * remove components until shipboard of player is valid
     */
    // TODO: create threads for players
    // TODO state pattern
    // TODO refactor into methods
    public void start(GameInformation gameInformation) {
        boolean errors;
        int col, row;
        for (Player player : gameInformation.getPlayerList()) {
            errors = checkErrors(player);
            // ask to correct errors
            while (errors) {
                col = correctionView.promptForColumn();
                row = correctionView.promptForRow();
                player.getShipBoard().removeComponent(col, row, true);
                errors = checkErrors(player);
            }
            correctionView.printFinishedMessage();
        }
    }

    /**
     * Checks if there are any incorrectly placed components in shipboard (errors)
     *
     * @param player
     * @return true if there are errors, false if shipboard is valid
     */
    private boolean checkErrors(Player player) {
        boolean errors = false;
        boolean[][] matr = player.getShipBoard().getMatrErrors();
        for (int i = 0; i < 12 && !errors; i++) {
            for (int j = 0; j < 12 && !errors; j++) {
                if (matr[i][j])
                    errors = true;
            }
        }
        return errors;
    }

}
