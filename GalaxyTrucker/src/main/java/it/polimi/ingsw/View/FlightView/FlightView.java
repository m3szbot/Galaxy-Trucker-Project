package it.polimi.ingsw.View.FlightView;

import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.View.GeneralView;

public abstract class FlightView extends GeneralView {
    public void printFlightBoard(DataContainer dataContainer) {

        FlightBoard flightBoard = dataContainer.getFlightBoard();
        String player;
        int diff, temp1, temp2;

        System.out.println("FlightBoard:\n");

        for (int i = flightBoard.getPlayerOrderList().size() - 1; i >= 0; i--) {
            System.out.println("PlayerOrderList not empty");

            player = flightBoard.getPlayerOrderList().get(i).getNickName();
            System.out.println("player order list accessible");

            temp1 = flightBoard.getPlayerTile(flightBoard.getPlayerOrderList().get(i));
            temp2 = 0;
            if (i != 0) {
                temp2 = flightBoard.getPlayerTile(flightBoard.getPlayerOrderList().get(i - 1));
            }
            diff = temp2 - temp1;

            if (i == 0) {
                System.out.printf("%s is 1st\n", player);
            } else {
                System.out.printf("%s---%d--->", player, diff);
            }
        }
    }

    // TODO: remove
    public abstract void sendMessageToAll(String situationString);

    public abstract void sendMessageToPlayer(Player player, String message);

    public abstract boolean askPlayerGenericQuestion(Player player, String question);

    public abstract int[] askPlayerCoordinates(Player player, String question);

    public abstract int askPlayerValue(Player player, String question);


}
