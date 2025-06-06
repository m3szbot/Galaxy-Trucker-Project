package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.Messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.Messengers.PlayerMessenger;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.List;

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
        int numberOfPlayers = gameInformation.getFlightBoard().getPlayerOrderList().size();
        int enginePowerChosen, playerSingleEnginePower, playerDoubleEngineNumber, playerRemainingBatteries;
        List<Player> players = gameInformation.getFlightBoard().getPlayerOrderList();

        for (int i = 0; i < numberOfPlayers; i++) {

            player = players.get(i);

            playerSingleEnginePower = player.getShipBoard().getShipBoardAttributes().getSingleEnginePower();
            playerRemainingBatteries = player.getShipBoard().getShipBoardAttributes().getRemainingBatteries();
            playerDoubleEngineNumber = player.getShipBoard().getShipBoardAttributes().getDoubleEnginePower();

            //Check if the player has any engine power to be used (if it's 0 anyway the player is eliminated)
            if (!(playerSingleEnginePower > 0) && !(playerDoubleEngineNumber > 0 && playerRemainingBatteries > 0)) {

                message = "Player" + player.getNickName() + " has no engine power and can't go through open space.";
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

                gameInformation.getFlightBoard().eliminatePlayer(player);
                numberOfPlayers = gameInformation.getFlightBoard().getPlayerOrderList().size();
                i--;

                //Notify the player
                message = "You are lost in space.\n";
                playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
                playerMessenger.printMessage(message);

                //Notify everyone
                message = "Player" + player.getNickName() + " has been lost in the open space.";
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

            } else {

                enginePowerChosen = chooseEnginePower(player, gameInformation);
                changePlayerPosition(player, enginePowerChosen, gameInformation.getFlightBoard());

                message = "Player " + player.getNickName() + " has moved " +
                        enginePowerChosen + " position forward!";
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

            }

        }

        gameInformation.getFlightBoard().updateFlightBoard();

        for (Player player1 : gameInformation.getFlightBoard().getPlayerOrderList()) {
            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player1);
            playerMessenger.printFlightBoard(gameInformation.getFlightBoard());
        }
    }

    public void showCard() {

        System.out.println("Card name: " + getCardName());
        System.out.println("Card level: " + getCardLevel());

    }
}
