package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Model.Components.Storage;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;

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

    public void showCard() {

        System.out.println("Card name: " + getCardName());
        System.out.println("Card level: " + getCardLevel());

    }

    @Override

    public void resolve(GameInformation gameInformation) {

        Player smallestCrewPlayer = calculateSmallestCrew(gameInformation.getFlightBoard());
        String message;
        DataContainer dataContainer;

        if (destroyRandomComponent(smallestCrewPlayer, gameInformation.getFlightBoard())) {

            message = "Player " + smallestCrewPlayer.getNickName() + " was hit!";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToALl(message);

        } else {

            message = "Player " + smallestCrewPlayer.getNickName() +
                    "was lucky enough to not get hit!";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToALl(message);

        }

        gameInformation.getFlightBoard().updateFlightBoard();
        for (Player player : gameInformation.getFlightBoard().getPlayerOrderList()) {
            dataContainer = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerContainer(player);
            dataContainer.setFlightBoard(gameInformation.getFlightBoard());
            dataContainer.setCommand("printFlightBoard");
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerData(player);
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
