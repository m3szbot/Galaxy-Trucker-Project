package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Controller.ExceptionsHandler;
import it.polimi.ingsw.Controller.FlightPhase.PlayerFlightInputHandler;
import it.polimi.ingsw.Controller.Sleeper;
import it.polimi.ingsw.Model.FlightBoard.LappedPlayersException;
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
        this.imagePath = cardBuilder.getImagePath();

    }

    @Override

    public void resolve(GameInformation gameInformation) {

        Player smallestCrewPlayer = calculateSmallestCrew(gameInformation);
        String message;
        PlayerMessenger playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(smallestCrewPlayer);
        boolean isEliminated = false;

        try {
            if (destroyRandomComponent(smallestCrewPlayer, gameInformation)) {

                message = "Player " + smallestCrewPlayer.getColouredNickName() + " was hit!";
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

            } else {

                message = "Player " + smallestCrewPlayer.getColouredNickName() +
                        " was lucky enough to not get hit!";
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

            }
        } catch (NoHumanCrewLeftException e) {

            message = e.getMessage();
            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(smallestCrewPlayer);
            playerMessenger.printMessage(message);

            PlayerFlightInputHandler.removePlayer(smallestCrewPlayer);

            gameInformation.getFlightBoard().eliminatePlayer(smallestCrewPlayer);
            isEliminated = true;

        } catch (PlayerDisconnectedException e) {
            PlayerFlightInputHandler.removePlayer(smallestCrewPlayer);

            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(gameInformation, smallestCrewPlayer);
        }

        if (isEliminated) {

            message = "Player " + smallestCrewPlayer.getColouredNickName() + " has no crew members left to continue the voyage and was eliminated!\n";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

        }

        try {
            gameInformation.getFlightBoard().updateFlightBoard();

        } catch (LappedPlayersException e) {
            ExceptionsHandler.handleLappedPlayersException(ClientMessenger.getGameMessenger(gameInformation.getGameCode()), e);

            for (Player player1 : e.getPlayerList()) {
                PlayerFlightInputHandler.removePlayer(player1);
            }
        }


    }

    public void showCard() {

        System.out.println("Card name: " + getCardName());
        System.out.println("Card level: " + getCardLevel());
        System.out.println();

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

            Sleeper.sleepXSeconds(3);

            if (player.getShipBoard().getRealComponent(x, y) != null) {

                try {
                    player.getShipBoard().removeComponent(x + 1, y + 1, true);

                    message = "A shot hit player " + player.getColouredNickName() + " at coordinates [ " + (x + 1) + " , " + (y + 1) + " ]!";
                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);
                    return true;

                } catch (FracturedShipBoardException e) {
                    ExceptionsHandler.handleFracturedShipBoardException(playerMessenger, e);
                }

            }

            message = "Shot number " + (i + 1) + " missed player " + player.getColouredNickName() + "!\n";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

        }

        return false;

    }
}
