package it.polimi.ingsw.View.SetUpView;

import it.polimi.ingsw.Connection.ViewType;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Color;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.View.GeneralView;

import java.io.IOException;

public abstract class SetUpView extends GeneralView {
    public abstract int askMaxNumberOfPlayers(Player player, String message) throws IOException;

    public abstract GameType askGameType(String message);

    public abstract ViewType askViewType(String message);

    public abstract String askNickName(String message);

    public abstract Color askColor();


}
