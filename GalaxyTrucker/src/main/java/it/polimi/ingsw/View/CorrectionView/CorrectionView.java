package it.polimi.ingsw.View.CorrectionView;

import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;
import it.polimi.ingsw.View.GeneralView;

public abstract class CorrectionView extends GeneralView {
    public abstract void printErrorsMessage(ShipBoard shipBoard);

    public abstract void printFinishedMessage();

    public abstract int[] promptForColumnRow();

    public abstract void printComponentRemovalMessage(int col, int row);

    public abstract void printPlayerRemovalMessage(Player player);

}
