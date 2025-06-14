package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Controller.ExceptionsHandler;
import it.polimi.ingsw.Controller.FlightPhase.IndexChecker;
import it.polimi.ingsw.Controller.FlightPhase.PlayerFlightInputHandler;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.FlightBoard.LappedPlayersException;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.NoHumanCrewLeftException;
import it.polimi.ingsw.Model.ShipBoard.Player;

import static it.polimi.ingsw.Controller.FlightPhase.PlayerFlightInputHandler.checkInputThreadActivity;

/**
 * Class that represent the card combat
 *
 * @author carlo
 */

public class CombatZone extends Card implements SmallestCrew, SufferBlows, Movable, TokenLoss, EnginePowerChoice, FirePowerChoice {

    private int daysLost;
    private int lossNumber;
    private Blow[] blows;
    private ElementType blowType;
    private ElementType lossType;

    public CombatZone(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.getCardLevel();
        this.cardName = cardBuilder.getCardName();
        this.daysLost = cardBuilder.getDaysLost();
        this.lossNumber = cardBuilder.getLossNumber();
        this.blows = cardBuilder.getBlows();
        this.blowType = cardBuilder.getBlowType();
        this.lossType = cardBuilder.getLossType();
        this.imagePath = cardBuilder.getImagePath();

    }

    @Override

    public void resolve(GameInformation gameInformation) {

        int numberOfPlayers = gameInformation.getFlightBoard().getPlayerOrderList().size();
        int[] enginePowers = new int[numberOfPlayers];
        float[] firePowers = new float[numberOfPlayers];
        Player lowestInhabitantNumberPlayer, weakestEnginePowerPlayer, weakestFirePowerPlayer, player;
        String message;
        PlayerMessenger playerMessenger;

        //calculating player with the lowest inhabitant number

        lowestInhabitantNumberPlayer = calculateSmallestCrew(gameInformation);

        //letting the players choose their firePower, from the leader backwards

        for (int i = 0; i < gameInformation.getFlightBoard().getPlayerOrderList().size(); i++) {

            //If there is only one player left this card needs to be skipped
            if (gameInformation.getFlightBoard().getPlayerOrderList().size() <= 1) {

                message = "This card cannot be played with less than 2 players.\n";
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);
                break;

            }

            //Checks the validity of the current index (precaution for disconnection)
            i = IndexChecker.checkIndex(gameInformation, i);

            player = gameInformation.getFlightBoard().getPlayerOrderList().get(i);
            PlayerFlightInputHandler.startPlayerTurn(player);

            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);

            try {
                firePowers[i] = chooseFirePower(player, gameInformation);

            } catch (PlayerDisconnectedException e) {
                PlayerFlightInputHandler.removePlayer(player);

                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(gameInformation, player);
                i--;

            }

            if (playerMessenger != null) {
                message = "The other players are choosing their fire power.\n";
                playerMessenger.printMessage(message);
            }

            if (PlayerFlightInputHandler.checkInputThreadActivity(player)) {
                PlayerFlightInputHandler.endPlayerTurn(player);
            }

        }

        //letting the players choose their enginePower, from the leader backwards

        for (int i = 0; i < gameInformation.getFlightBoard().getPlayerOrderList().size(); i++) {

            //Checks the validity of the current index (precaution for disconnection)
            i = IndexChecker.checkIndex(gameInformation, i);

            player = gameInformation.getFlightBoard().getPlayerOrderList().get(i);
            PlayerFlightInputHandler.startPlayerTurn(player);

            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);

            try {
                enginePowers[i] = chooseEnginePower(gameInformation.getFlightBoard().getPlayerOrderList().get(i), gameInformation);

            } catch (PlayerDisconnectedException e) {
                PlayerFlightInputHandler.removePlayer(player);

                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(gameInformation, player);
                i--;

            }

            if (playerMessenger != null) {
                message = "The other players are choosing their engine power.\n";
                playerMessenger.printMessage(message);
            }

            if (PlayerFlightInputHandler.checkInputThreadActivity(player)) {
                PlayerFlightInputHandler.endPlayerTurn(player);
            }

        }

        weakestFirePowerPlayer = findWeakestFirePowerPlayer(firePowers, gameInformation.getFlightBoard());
        weakestEnginePowerPlayer = findWeakestEnginePowerPlayer(enginePowers, gameInformation.getFlightBoard());

        //giving the various penalties to players

        //lowest inhabitants
        changePlayerPosition(lowestInhabitantNumberPlayer, -daysLost, gameInformation.getFlightBoard());

        message = "Player " + lowestInhabitantNumberPlayer.getColouredNickName() + " lost " + daysLost +
                " flight days as he has the lowest number of inhabitants!";
        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

        //lowest engine power

        try {
            inflictLoss(weakestEnginePowerPlayer, lossType, lossNumber, gameInformation);

            message = "Player " + weakestEnginePowerPlayer.getColouredNickName() + " lost " + lossNumber +
                    " crew members as he has the weakest engine power!";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

        } catch (NoHumanCrewLeftException e) {

            message = e.getMessage();
            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(weakestEnginePowerPlayer);
            playerMessenger.printMessage(message);

            message = "Player " + weakestEnginePowerPlayer.getColouredNickName() + " has no crew members left to continue the voyage and was eliminated!\n";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

            PlayerFlightInputHandler.removePlayer(weakestEnginePowerPlayer);

            gameInformation.getFlightBoard().eliminatePlayer(weakestEnginePowerPlayer);

        } catch (PlayerDisconnectedException e) {
            PlayerFlightInputHandler.removePlayer(weakestEnginePowerPlayer);

            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(gameInformation, weakestEnginePowerPlayer);
        }

        //rolling the dice for each shot and then hitting
        for (int i = 0; i < blows.length; i++) {
            blows[i].rollDice();
        }

        message = "Player " + weakestFirePowerPlayer.getColouredNickName() + " is getting shot at!\n";
        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

        //lowest firepower

        try {
            hit(weakestFirePowerPlayer, blows, blowType, gameInformation);

        } catch (NoHumanCrewLeftException e) {
            message = e.getMessage();
            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(weakestFirePowerPlayer);
            playerMessenger.printMessage(message);

            message = "Player " + weakestFirePowerPlayer.getColouredNickName() + " has no crew members left to continue the voyage and was eliminated!\n";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

            PlayerFlightInputHandler.removePlayer(weakestFirePowerPlayer);

            gameInformation.getFlightBoard().eliminatePlayer(weakestFirePowerPlayer);

        } catch (PlayerDisconnectedException e) {
            PlayerFlightInputHandler.removePlayer(weakestFirePowerPlayer);

            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(gameInformation, weakestFirePowerPlayer);
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
        System.out.println("Blow type: " + blowType.toString());
        printBlows(blows);
        System.out.println();

    }

    private Player findWeakestFirePowerPlayer(float[] firePowers, FlightBoard flightBoard) {

        int minIndex = 0;

        for (int i = 0; i < flightBoard.getPlayerOrderList().size(); i++) {

            if (firePowers[i] < firePowers[minIndex]) {
                minIndex = i;
            }

        }

        return flightBoard.getPlayerOrderList().get(minIndex);

    }

    private Player findWeakestEnginePowerPlayer(int[] enginePowers, FlightBoard flightBoard) {

        int minIndex = 0;

        for (int i = 0; i < flightBoard.getPlayerOrderList().size(); i++) {

            if (enginePowers[i] < enginePowers[minIndex]) {
                minIndex = i;
            }

        }

        return flightBoard.getPlayerOrderList().get(minIndex);

    }

}
