package it.polimi.ingsw.View.FlightView;

import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.ShipBoard.Player;

public class FlightViewTUI extends FlightView {

    public void printFlightBoard(DataContainer dataContainer) {

        FlightBoard flightBoard = dataContainer.getFlightBoard();
        Player player;
        String nickname;
        int diff, temp1, temp2;

        System.out.println("FlightBoard:\n");

        for (int i = flightBoard.getPlayerOrderList().size() - 1; i >= 0; i--) {

            //String used to print the nickName
            player = flightBoard.getPlayerOrderList().get(i);
            nickname = player.getNickName();

            // Temp1 holds the current player tile, temp2 holds the subsequent player tile
            temp1 = flightBoard.getPlayerTile(player);
            temp2 = 0;

            //If the current player is not the first, diff is used to print the number of tiles between the two players
            if (i != 0) {
                temp2 = flightBoard.getPlayerTile(flightBoard.getPlayerOrderList().get(i - 1));
            }
            diff = temp2 - temp1;

            if (i == 0) {
                System.out.printf("%s:%d is 1st\n\n", nickname, temp1);
            } else {
                System.out.printf("%s:%d ---%d---> ", nickname, temp1, diff);
            }
        }
    }
}
