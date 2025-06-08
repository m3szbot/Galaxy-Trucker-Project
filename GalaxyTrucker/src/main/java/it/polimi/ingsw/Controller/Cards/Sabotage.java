package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Controller.FracturedShipBoardHandler;
import it.polimi.ingsw.Model.Components.Storage;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.FracturedShipBoardException;
import it.polimi.ingsw.Model.ShipBoard.NoHumanCrewLeftException;
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

    @Override

    public void resolve(GameInformation gameInformation) {

        Player smallestCrewPlayer = calculateSmallestCrew(gameInformation.getFlightBoard());
        String message;
        PlayerMessenger playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(smallestCrewPlayer);
        boolean isEliminated = false;

        try {
            if (destroyRandomComponent(smallestCrewPlayer, gameInformation)) {

                message = "Player " + smallestCrewPlayer.getNickName() + " was hit!";
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

            } else {

                message = "Player " + smallestCrewPlayer.getNickName() +
                        " was lucky enough to not get hit!";
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

            }
        } catch (NoHumanCrewLeftException e) {

            message = e.getMessage();
            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(smallestCrewPlayer);
            playerMessenger.printMessage(message);

            gameInformation.getFlightBoard().eliminatePlayer(smallestCrewPlayer);
            isEliminated = true;

        } catch (PlayerDisconnectedException e) {
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(gameInformation, smallestCrewPlayer);
        }

        if (isEliminated) {

            message = "Player " + smallestCrewPlayer.getNickName() + " has no crew members left to continue the voyage and was eliminated!\n";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

        }

        gameInformation.getFlightBoard().updateFlightBoard();
        for (Player player : gameInformation.getFlightBoard().getPlayerOrderList()) {
            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
            playerMessenger.printFlightBoard(gameInformation.getFlightBoard());
        }

    }

    public void showCard() {

        System.out.println("Card name: " + getCardName());
        System.out.println("Card level: " + getCardLevel());

    }

    /**
     * @param player target
     * @return true if the player was hit, false otherwise
     */

    private boolean destroyRandomComponent(Player player, GameInformation gameInformation) throws NoHumanCrewLeftException, PlayerDisconnectedException {

        String message;
        PlayerMessenger playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
        int i, x, y;

        for (i = 0; i < 3; i++) {

            x = (int) ((Math.random() * 11) + 1);
            y = (int) ((Math.random() * 11) + 1);

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.out.println("Error while sleeping");
            }

            if (player.getShipBoard().getRealComponent(x, y) != null) {

                try {
                    player.getShipBoard().removeComponent(x + 1, y + 1, true);

                    message = "A shot hit player " + player.getNickName() + " at coordinates [ " + (x + 1) + " , " + (y + 1) + " ]!";
                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);
                    return true;

                } catch (FracturedShipBoardException e) {
                    FracturedShipBoardHandler.handleFracture(playerMessenger, e);
                }

            }

            message = "Shot number " + (i + 1) + " missed player " + player.getNickName() + "!\n";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

        }

        return false;

    }
}
