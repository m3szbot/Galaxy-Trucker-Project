package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Model.Components.Storage;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.View.FlightView.FlightView;

/**
 * class that represent the card sabotage
 *
 * @author carlo
 */

public class Sabotage extends Card implements SmallestCrew {

    public Sabotage(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.getCardLevel();
        this.cardName = cardBuilder.getCardName();

    }

    @Override

    public void resolve(FlightBoard flightBoard, FlightView flightView) {

        Player smallestCrewPlayer = calculateSmallestCrew(flightBoard);
        String message;

        if (destroyRandomComponent(smallestCrewPlayer, flightBoard)) {

            message = "Player " + smallestCrewPlayer.getNickName() + " was hit!";
            flightView.sendMessageToAll(message);

        } else {

            message = "Player " + smallestCrewPlayer.getNickName() +
                    "was lucky enough to not get hit!";
            flightView.sendMessageToAll(message);
        }

    }

    /**
     * @param player target
     * @return true if the player was hit, false otherwise
     */

    private boolean destroyRandomComponent(Player player, FlightBoard flightBoard) {

        int i, x, y;

        for (i = 0; i < 3; i++) {


            x = (int) (Math.random() * 13);
            y = (int) (Math.random() * 13);

            if (player.getShipBoard().getComponent(x, y) != null) {

                if (player.getShipBoard().getComponent(x, y).getComponentName().equals("Storage")) {

                    int[] goodsToRemove = ((Storage) player.getShipBoard().getComponent(x, y)).getGoods();

                    flightBoard.addGoods(goodsToRemove);

                }

                player.getShipBoard().removeComponent(x + 1, y + 1, true);
                player.getShipBoard().getShipBoardAttributes().updateDestroyedComponents(1);
                return true;

            }

        }

        return false;

    }
}
