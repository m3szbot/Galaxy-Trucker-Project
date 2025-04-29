package it.polimi.ingsw.Application.CorrectionPhase;

import it.polimi.ingsw.Application.GameInformation;
import it.polimi.ingsw.Application.Startable;
import it.polimi.ingsw.Shipboard.Player;

public class CorrectionPhase implements Startable {
    CorrectionView correctionView;

    public CorrectionPhase() {
        correctionView = new CorrectionView();
    }

    /*
    remove components until player ship is valid
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
                checkErrors(player);
            }
            correctionView.printFinishedMessage();
        }
    }

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
