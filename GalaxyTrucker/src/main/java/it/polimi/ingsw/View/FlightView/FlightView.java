package it.polimi.ingsw.View.FlightView;

import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.View.GeneralView;

public abstract class FlightView extends GeneralView {
    public void printFlightBoard() {

    }

    // TODO: remove
    public abstract void sendMessageToAll(String situationString);

    public abstract void sendMessageToPlayer(Player player, String message);

    public abstract boolean askPlayerGenericQuestion(Player player, String question);

    public abstract int[] askPlayerCoordinates(Player player, String question);

    public abstract int askPlayerValue(Player player, String question);


}
