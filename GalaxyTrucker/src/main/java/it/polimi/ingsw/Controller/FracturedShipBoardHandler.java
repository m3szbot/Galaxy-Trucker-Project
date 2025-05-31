package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.FracturedShipBoardException;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;

import java.util.List;

/**
 * Controller class to handle FracturedShipBoardException.
 */
public class FracturedShipBoardHandler extends Phase {
    Player player;
    List<ShipBoard> shipBoardsList;

    public FracturedShipBoardHandler(GameInformation gameInformation, FracturedShipBoardException exception) {
        super(gameInformation);
        this.player = exception.getPlayer();
        this.shipBoardsList = exception.getShipBoardsList();
    }

    @Override
    public void start() {

    }
}
