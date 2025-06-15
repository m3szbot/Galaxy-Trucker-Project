package it.polimi.ingsw.Controller.Cards;

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
 * class that represent the card smugglers
 *
 * @author carlo
 */

public class Smugglers extends AttackStatesSetting implements Movable, GoodsGain, TokenLoss, FirePowerChoice {

    private int daysLost;
    private ElementType lossType;
    private int requirementNumber;
    private int lossNumber;
    private int[] goods;

    public Smugglers(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.getCardLevel();
        this.cardName = cardBuilder.getCardName();
        this.daysLost = cardBuilder.getDaysLost();
        this.lossType = cardBuilder.getLossType();
        this.lossNumber = cardBuilder.getLossNumber();
        this.goods = cardBuilder.getGoods();
        this.requirementNumber = cardBuilder.getRequirementNumber();
        this.imagePath = cardBuilder.getImagePath();


    }

    @Override

    public void resolve(GameInformation gameInformation) {

        int i;
        PlayerMessenger playerMessenger;
        AttackStates[] results;
        Player player;

        //Cycles through the player list so doesn't need any catches
        results = setAttackStates(requirementNumber, gameInformation);

        for (i = 0; i < gameInformation.getFlightBoard().getPlayerOrderList().size(); i++) {

            //Checks the validity of the current index (precaution for disconnection)
            i = IndexChecker.checkIndex(gameInformation, i);

            player = gameInformation.getFlightBoard().getPlayerOrderList().get(i);
            PlayerFlightInputHandler.startPlayerTurn(player);

            message = "It's " + player.getColouredNickName() + "'s turn.\n";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);

            if (results[i] == AttackStates.EnemyDefeated) {

                message = "Would you like to collect the reward for defeating the enemies ?";
                playerMessenger.printMessage(message);

                changePlayerPosition(gameInformation.getFlightBoard().getPlayerOrderList().get(i), -daysLost, gameInformation.getFlightBoard());

                try {
                    if (playerMessenger.getPlayerBoolean()) {

                        message = "Player " + gameInformation.getFlightBoard().getPlayerOrderList().get(i).getColouredNickName() +
                                " has collected the reward!";
                        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

                        giveGoods(gameInformation.getFlightBoard().getPlayerOrderList().get(i), goods, gameInformation);

                    } else {

                        message = "Player " + gameInformation.getFlightBoard().getPlayerOrderList().get(i).getColouredNickName() +
                                " hasn't collected the reward!";
                        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

                    }
                } catch (PlayerDisconnectedException e) {
                    PlayerFlightInputHandler.removePlayer(player);

                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(gameInformation, player);
                }
                break;

            } else if (results[i] == AttackStates.PlayerDefeated) {

                try {

                    inflictLoss(gameInformation.getFlightBoard().getPlayerOrderList().get(i), lossType, lossNumber, gameInformation);

                } catch (NoHumanCrewLeftException e) {

                    message = e.getMessage();
                    playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
                    playerMessenger.printMessage(message);

                    message = "Player " + player.getColouredNickName() + " has no crew members left to continue the voyage and was eliminated!\n";
                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

                    PlayerFlightInputHandler.endPlayerTurn(player);

                    gameInformation.getFlightBoard().eliminatePlayer(player);
                    i--;

                } catch (PlayerDisconnectedException e) {
                    PlayerFlightInputHandler.removePlayer(player);

                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(gameInformation, player);
                    i--;
                }

            }

            if (ClientMessenger.getGameMessenger(gameInformation.getGameCode()).checkPlayerMessengerPresence(player)) {
                message = "You finished your turn, wait for the other players.\n";
                playerMessenger.printMessage(message);
            }

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
        System.out.println("Days lost: " + daysLost);
        System.out.println("Loss type: " + lossType.toString());
        System.out.println("Loss number: " + lossNumber);
        System.out.println("Requirement number: " + requirementNumber + " (fire power)");
        printGoods(goods);
        System.out.println();

    }
}
