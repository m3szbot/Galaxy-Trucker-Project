package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;

/**
 * class that represent the card abbandonedShip
 *
 * @author carlo
 */

public class AbandonedShip extends Card implements Movable, TokenLoss, CreditsGain {

    private int daysLost;
    private ElementType lossType;
    private int lossNumber;
    private int gainedCredit;

    public AbandonedShip(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.getCardLevel();
        this.cardName = cardBuilder.getCardName();
        this.daysLost = cardBuilder.getDaysLost();
        this.lossType = cardBuilder.getLossType();
        this.lossNumber = cardBuilder.getLossNumber();
        this.gainedCredit = cardBuilder.getGainedCredit();


    }

    public void showCard() {


        System.out.println("\nCard name: " + getCardName());
        System.out.println("Card level: " + getCardLevel());
        System.out.println("Days lost: " + daysLost);
        System.out.println("Loss type: " + lossType.toString());
        System.out.println("Loss number: " + lossNumber);
        System.out.println("Gained credit: " + gainedCredit);

    }

    @Override

    public void resolve(GameInformation gameInformation) {

        boolean solved = false;
        PlayerMessenger playerMessenger;

        for (Player player : gameInformation.getFlightBoard().getPlayerOrderList()) {

            //player can afford to lose some crew members to solve the card if he wants to

            if (player.getShipBoard().getShipBoardAttributes().getCrewMembers() >= lossNumber) {

                message = "Do you want to solve the card ? ";
                playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
                playerMessenger.printMessage(message);

                try {
                    if (playerMessenger.getPlayerBoolean()) {

                        solved = true;
                        //player decides to solve the card

                        inflictLoss(player, lossType, lossNumber, gameInformation);
                        giveCredits(player, gainedCredit);
                        changePlayerPosition(player, daysLost, gameInformation.getFlightBoard());

                        message = player.getNickName() + "has solved the card!";
                        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

                        break;
                    } else {
                        message = player.getNickName() + "hasn't solved the card.\n";
                        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);
                        gameInformation.getFlightBoard().updateFlightBoard();
                    }
                } catch (PlayerDisconnectedException e) {
                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(gameInformation, player);
                }
            }

            message = "You finished your turn, wait for the other players.\n";
            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
            playerMessenger.printMessage(message);

        }

        if (!solved) {
            message = "Nobody solved the card!";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);
            gameInformation.getFlightBoard().updateFlightBoard();
        }

        for (Player player : gameInformation.getFlightBoard().getPlayerOrderList()) {
            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
            playerMessenger.printFlightBoard(gameInformation.getFlightBoard());
        }
    }
}
