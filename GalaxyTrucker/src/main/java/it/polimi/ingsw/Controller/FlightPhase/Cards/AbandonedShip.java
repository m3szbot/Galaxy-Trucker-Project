package it.polimi.ingsw.Controller.FlightPhase.Cards;

import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Controller.ExceptionsHandler;
import it.polimi.ingsw.Controller.FlightPhase.IndexChecker;
import it.polimi.ingsw.Controller.FlightPhase.PlayerFlightInputHandler;
import it.polimi.ingsw.Model.FlightBoard.LappedPlayersException;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.NoHumanCrewLeftException;
import it.polimi.ingsw.Model.ShipBoard.Player;

/**
 * class that represents the card abandonedShip
 *
 * @author carlo
 * @author Ludo
 */

public class AbandonedShip extends Card implements Movable, TokenLoss, CreditsGain {

    private int daysLost;
    private ElementType lossType;
    private int lossNumber;
    private int gainedCredits;

    public AbandonedShip(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.getCardLevel();
        this.cardName = cardBuilder.getCardName();
        this.daysLost = cardBuilder.getDaysLost();
        this.lossType = cardBuilder.getLossType();
        this.lossNumber = cardBuilder.getLossNumber();
        this.gainedCredits = cardBuilder.getGainedCredits();
        this.imagePath = cardBuilder.getImagePath();


    }

    @Override

    public void resolve(GameInformation gameInformation) {

        boolean solved = false;
        Player player;
        PlayerMessenger playerMessenger;

        for (int i = 0; i < gameInformation.getFlightBoard().getPlayerOrderList().size(); i++) {

            //Checks the validity of the current index (precaution for disconnection)
            i = IndexChecker.checkIndex(gameInformation, i);

            player = gameInformation.getFlightBoard().getPlayerOrderList().get(i);
            PlayerFlightInputHandler.startPlayerTurn(player);

            message = "It's " + player.getColouredNickName() + "'s turn.\n";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);

            //player can afford to lose some crew members to solve the card if he wants to

            if (player.getShipBoard().getShipBoardAttributes().getCrewMembers() > lossNumber) {

                message = "Do you want to solve the card (if you do you will lose part of your crew) ? ";
                playerMessenger.printMessage(message);

                try {
                    if (playerMessenger.getPlayerBoolean()) {

                        //player decides to solve the card
                        inflictLoss(player, lossType, lossNumber, gameInformation);
                        giveCredits(player, gainedCredits);
                        changePlayerPosition(player, -daysLost, gameInformation.getFlightBoard());

                        message = player.getColouredNickName() + " has solved the card!";
                        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);
                        solved = true;

                        if (PlayerFlightInputHandler.checkInputThreadActivity(player)) {
                            PlayerFlightInputHandler.endPlayerTurn(player);
                        }

                        break;

                    } else {

                        message = player.getColouredNickName() + " hasn't solved the card.\n";
                        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

                    }
                } catch (PlayerDisconnectedException e) {
                    PlayerFlightInputHandler.removePlayer(player);

                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(player);
                    i--;

                } catch (NoHumanCrewLeftException e) {

                    message = "Player " + player.getColouredNickName() + " has no crew members left to continue the voyage and was eliminated!\n";
                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

                    gameInformation.getFlightBoard().eliminatePlayer(player);
                    i--;

                }
            } else {

                message = "Not enough crew members to solve the card.\n";
                playerMessenger.printMessage(message);

            }

            if (ClientMessenger.getGameMessenger(gameInformation.getGameCode()).checkPlayerMessengerPresence(player)) {

                message = "You finished your turn, wait for the other players.\n";
                playerMessenger.printMessage(message);

            }

            if (PlayerFlightInputHandler.checkInputThreadActivity(player)) {
                PlayerFlightInputHandler.endPlayerTurn(player);
            }

        }

        if (!solved) {

            message = "Nobody solved the card!\n";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

        }

        try {
            gameInformation.getFlightBoard().updateFlightBoard();

        } catch (LappedPlayersException e) {
            ExceptionsHandler.handleLappedPlayersException(ClientMessenger.getGameMessenger(gameInformation.getGameCode()), e);
        }

    }

    public void showCard() {


        System.out.println("Card name: " + getCardName());
        System.out.println("Card level: " + getCardLevel());
        System.out.println("Days lost: " + daysLost);
        System.out.println("Loss type: " + lossType.toString());
        System.out.println("Loss number: " + lossNumber);
        System.out.println("Gained credit: " + gainedCredits);

    }
}
