package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Controller.ExceptionsHandler;
import it.polimi.ingsw.Controller.FlightPhase.IndexChecker;
import it.polimi.ingsw.Controller.FlightPhase.PlayerFlightInputHandler;
import it.polimi.ingsw.Controller.Sleeper;
import it.polimi.ingsw.Model.FlightBoard.LappedPlayersException;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.NoHumanCrewLeftException;
import it.polimi.ingsw.Model.ShipBoard.Player;

/**
 * Class that represent the card meteorswarm
 *
 * @author carlo
 */

public class MeteorSwarm extends Card implements SufferBlows {

    private Blow[] blows;
    private ElementType blowType;

    public MeteorSwarm(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.getCardLevel();
        this.cardName = cardBuilder.getCardName();
        this.blows = cardBuilder.getBlows();
        this.blowType = cardBuilder.getBlowType();
        this.imagePath = cardBuilder.getImagePath();

    }

    @Override

    public void resolve(GameInformation gameInformation) {

        PlayerMessenger playerMessenger;
        Player player;

        //rolling all dices
        for (int i = 0; i < blows.length; i++) {
            if (blows[i] != null) {
                blows[i].rollDice();
            }
        }

        for (int i = 0; i < gameInformation.getFlightBoard().getPlayerOrderList().size(); i++) {

            //Checks the validity of the current index (precaution for disconnection)
            i = IndexChecker.checkIndex(gameInformation, i);

            player = gameInformation.getFlightBoard().getPlayerOrderList().get(i);
            PlayerFlightInputHandler.startPlayerTurn(player);

            message = "It's " + player.getColouredNickName() + "'s turn.\n";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);

            message = "Player " + player.getColouredNickName() + " is in a meteor swarm!\n";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

            try {
                hit(player, blows, blowType, gameInformation);

            } catch (NoHumanCrewLeftException e) {

                message = e.getMessage();
                playerMessenger.printMessage(message);

                message = "Player " + player.getColouredNickName() + " has no crew members left to continue the voyage and was eliminated!\n";
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

                gameInformation.getFlightBoard().eliminatePlayer(player);
                i--;


            } catch (PlayerDisconnectedException e) {
                PlayerFlightInputHandler.removePlayer(player);

                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(gameInformation, player);
                i--;

            }

            if (ClientMessenger.getGameMessenger(gameInformation.getGameCode()).checkPlayerMessengerPresence(player)) {

                message = "You survived the meteor storm!\n";
                playerMessenger.printMessage(message);

            }

            Sleeper.sleepXSeconds(2);

            if (PlayerFlightInputHandler.checkInputThreadActivity(player)) {
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
        System.out.println("Blow type: " + blowType.toString());
        printBlows(blows);
        System.out.println();
    }
}
