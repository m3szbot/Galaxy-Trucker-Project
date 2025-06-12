package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Controller.ExceptionsHandler;
import it.polimi.ingsw.Controller.FlightPhase.IndexChecker;
import it.polimi.ingsw.Controller.FlightPhase.PlayerFlightInputHandler;
import it.polimi.ingsw.Model.FlightBoard.LappedPlayersException;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;

/**
 * class that represent the card openspace
 *
 * @author carlo
 */

public class OpenSpace extends Card implements Movable, EnginePowerChoice {

    public OpenSpace(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.getCardLevel();
        this.cardName = cardBuilder.getCardName();

    }

    @Override

    public void resolve(GameInformation gameInformation) {

        PlayerMessenger playerMessenger;
        Player player;
        int enginePowerChosen, playerSingleEnginePower, playerDoubleEngineNumber, playerRemainingBatteries;
        gameInformation.getFlightBoard().getPlayerOrderList();

        for (int i = 0; i < gameInformation.getFlightBoard().getPlayerOrderList().size(); i++) {

            //Checks the validity of the current index (precaution for disconnection)
            i = IndexChecker.checkIndex(gameInformation, i);

            player = gameInformation.getFlightBoard().getPlayerOrderList().get(i);
            PlayerFlightInputHandler.startPlayerTurn(player);

            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);

            playerSingleEnginePower = player.getShipBoard().getShipBoardAttributes().getSingleEnginePower();
            playerRemainingBatteries = player.getShipBoard().getShipBoardAttributes().getRemainingBatteries();
            playerDoubleEngineNumber = player.getShipBoard().getShipBoardAttributes().getDoubleEnginePower();

            //Check if the player has any engine power to be used (if not the player is eliminated)
            if (!(playerSingleEnginePower > 0) && !(playerDoubleEngineNumber > 0 && playerRemainingBatteries > 0)) {

                message = "Player " + player.getNickName() + " has no engine power and can't go through the open space.";
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

                //Notify the player
                message = "You are lost in space.\n";
                playerMessenger.printMessage(message);

                gameInformation.getFlightBoard().eliminatePlayer(player);
                i--;

                //Notify everyone
                message = "Player" + player.getNickName() + " has been lost in the open space.";
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

            } else {

                try {
                    enginePowerChosen = chooseEnginePower(player, gameInformation);
                    changePlayerPosition(player, enginePowerChosen, gameInformation.getFlightBoard());

                    message = "Player " + player.getNickName() + " has moved " +
                            enginePowerChosen + " position forward!";
                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

                } catch (PlayerDisconnectedException e) {
                    PlayerFlightInputHandler.removePlayer(player);

                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(gameInformation, player);
                    i--;
                }

            }

            if (player != null) {
                PlayerFlightInputHandler.endPlayerTurn(player);
            }

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
}
