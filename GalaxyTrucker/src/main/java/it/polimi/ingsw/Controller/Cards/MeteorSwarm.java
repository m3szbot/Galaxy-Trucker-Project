package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.GameMessenger;
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

        GameMessenger gameMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode());
        PlayerMessenger playerMessenger;
        Player player;
        boolean isEliminated;

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

            isEliminated = false;

            message = "It's " + player.getColouredNickName() + "'s turn.\n";
            gameMessenger.sendMessageToAll(message);

            playerMessenger = gameMessenger.getPlayerMessenger(player);

            message = "Player " + player.getColouredNickName() + " is in a meteor swarm!\n";
            gameMessenger.sendMessageToAll(message);

            try {
                hit(player, blows, blowType, gameInformation);

            } catch (NoHumanCrewLeftException e) {

                message = "Player " + player.getColouredNickName() + " has no crew members left to continue the voyage and was eliminated!\n";
                gameMessenger.sendMessageToAll(message);

                gameInformation.getFlightBoard().eliminatePlayer(player);

                isEliminated = true;
                i--;


            } catch (PlayerDisconnectedException e) {
                PlayerFlightInputHandler.removePlayer(player);

                gameMessenger.disconnectPlayer(player);
                isEliminated = true;
                i--;

            }

            if (!isEliminated) {

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
            ExceptionsHandler.handleLappedPlayersException(gameMessenger, e);
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
