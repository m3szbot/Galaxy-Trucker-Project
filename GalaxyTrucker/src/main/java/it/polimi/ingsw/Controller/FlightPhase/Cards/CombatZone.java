package it.polimi.ingsw.Controller.FlightPhase.Cards;

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
        PlayerMessenger playerMessenger;

        //If there is only one player left this card needs to be skipped
        if (gameInformation.getFlightBoard().getPlayerOrderList().size() <= 1) {

            message = "This card cannot be played with less than 2 players.\n";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

            return;

        }

        //Explanation of the card

        message = "The player with the lowest crew count will lose " + daysLost + " days.\n" +
                "The player with the lowest activated engine power will lose" + lossNumber + " crew members.\n" +
                "The player with the lowest activated fire power will be shot at.\n" +
                "Choose wisely!\n";
        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

        //letting the players choose their firePower, from the leader backwards

        for (int i = 0; i < gameInformation.getFlightBoard().getPlayerOrderList().size(); i++) {

            //Checks the validity of the current index (precaution for disconnection)
            i = IndexChecker.checkIndex(gameInformation, i);

            player = gameInformation.getFlightBoard().getPlayerOrderList().get(i);
            PlayerFlightInputHandler.startPlayerTurn(player);

            message = "It's " + player.getColouredNickName() + "'s turn.\n";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);

            try {
                firePowers[i] = chooseFirePower(player, gameInformation);

            } catch (PlayerDisconnectedException e) {
                PlayerFlightInputHandler.removePlayer(player);

                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(player);
                i--;

            }

            if (ClientMessenger.getGameMessenger(gameInformation.getGameCode()).checkPlayerMessengerPresence(player)) {
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

            message = "It's " + player.getColouredNickName() + "'s turn.\n";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);

            try {
                enginePowers[i] = chooseEnginePower(gameInformation.getFlightBoard().getPlayerOrderList().get(i), gameInformation);

            } catch (PlayerDisconnectedException e) {
                PlayerFlightInputHandler.removePlayer(player);

                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(player);
                i--;

            }

            if (ClientMessenger.getGameMessenger(gameInformation.getGameCode()).checkPlayerMessengerPresence(player)) {
                message = "The other players are choosing their engine power.\n";
                playerMessenger.printMessage(message);
            }

            if (PlayerFlightInputHandler.checkInputThreadActivity(player)) {
                PlayerFlightInputHandler.endPlayerTurn(player);
            }

        }

        //giving the various penalties to players

        //calculating player with the lowest inhabitant number

        lowestInhabitantNumberPlayer = calculateSmallestCrew(gameInformation);

        //lowest inhabitants
        if (gameInformation.checkPlayerConnectivity(lowestInhabitantNumberPlayer) && gameInformation.getFlightBoard().isInFlight(lowestInhabitantNumberPlayer)) {

            changePlayerPosition(lowestInhabitantNumberPlayer, -daysLost, gameInformation.getFlightBoard());

            message = "Player " + lowestInhabitantNumberPlayer.getColouredNickName() + " lost " + daysLost +
                    " flight days as he has the lowest number of inhabitants!";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

        }

        //lowest engine power

        weakestEnginePowerPlayer = findWeakestEnginePowerPlayer(enginePowers, gameInformation.getFlightBoard());

        if (gameInformation.checkPlayerConnectivity(weakestEnginePowerPlayer) && gameInformation.getFlightBoard().isInFlight(weakestEnginePowerPlayer)) {

            PlayerFlightInputHandler.startPlayerTurn(weakestEnginePowerPlayer);

            message = "It's " + weakestEnginePowerPlayer.getColouredNickName() + "'s turn.\n";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

            try {
                message = "You have the weakest engine power!\n";
                playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(weakestEnginePowerPlayer);
                playerMessenger.printMessage(message);

                message = "Player " + weakestEnginePowerPlayer.getColouredNickName() + " will lose " + lossNumber +
                        " crew members as he has the weakest engine power!";
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

                inflictLoss(weakestEnginePowerPlayer, lossType, lossNumber, gameInformation);

            } catch (NoHumanCrewLeftException e) {

                message = "Player " + weakestEnginePowerPlayer.getColouredNickName() + " has no crew members left to continue the voyage and was eliminated!\n";
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

                gameInformation.getFlightBoard().eliminatePlayer(weakestEnginePowerPlayer);

            } catch (PlayerDisconnectedException e) {
                PlayerFlightInputHandler.removePlayer(weakestEnginePowerPlayer);

                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(weakestEnginePowerPlayer);
            }

            if (PlayerFlightInputHandler.checkInputThreadActivity(weakestEnginePowerPlayer)) {
                PlayerFlightInputHandler.endPlayerTurn(weakestEnginePowerPlayer);
            }

        }

        //rolling the dice for each shot and then hitting
        for (int i = 0; i < blows.length; i++) {
            blows[i].rollDice();
        }

        //lowest firepower

        weakestFirePowerPlayer = findWeakestFirePowerPlayer(firePowers, gameInformation.getFlightBoard());

        if (gameInformation.checkPlayerConnectivity(weakestFirePowerPlayer) && gameInformation.getFlightBoard().isInFlight(weakestFirePowerPlayer)) {

            PlayerFlightInputHandler.startPlayerTurn(weakestFirePowerPlayer);

            message = "It's " + weakestFirePowerPlayer.getColouredNickName() + "'s turn.\n";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

            try {
                message = "You have the lowest fire power!\n";
                playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(weakestFirePowerPlayer);
                playerMessenger.printMessage(message);

                message = "Player " + weakestFirePowerPlayer.getColouredNickName() + " is getting shot at!\n";
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

                hit(weakestFirePowerPlayer, blows, blowType, gameInformation);

            } catch (NoHumanCrewLeftException e) {

                message = "Player " + weakestFirePowerPlayer.getColouredNickName() + " has no crew members left to continue the voyage and was eliminated!\n";
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

                gameInformation.getFlightBoard().eliminatePlayer(weakestFirePowerPlayer);

            } catch (PlayerDisconnectedException e) {
                PlayerFlightInputHandler.removePlayer(weakestFirePowerPlayer);

                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(weakestFirePowerPlayer);
            }

            if (PlayerFlightInputHandler.checkInputThreadActivity(weakestFirePowerPlayer)) {
                PlayerFlightInputHandler.endPlayerTurn(weakestFirePowerPlayer);
            }

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
        System.out.println("Loss number:" + lossNumber);
        System.out.println("Blow type: " + blowType.toString());
        printBlows(blows);
        System.out.println();

    }

    private Player findWeakestFirePowerPlayer(float[] firePowers, FlightBoard flightBoard) {

        int minIndex = 0;

        for (int i = 0; i < flightBoard.getPlayerOrderList().size(); i++) {

            if (firePowers[i] < firePowers[minIndex] && flightBoard.isInFlight(flightBoard.getPlayerOrderList().get(i))) {
                minIndex = i;
            }

        }

        return flightBoard.getPlayerOrderList().get(minIndex);

    }

    private Player findWeakestEnginePowerPlayer(int[] enginePowers, FlightBoard flightBoard) {

        int minIndex = 0;

        for (int i = 0; i < flightBoard.getPlayerOrderList().size(); i++) {

            if (enginePowers[i] < enginePowers[minIndex] && flightBoard.isInFlight(flightBoard.getPlayerOrderList().get(i))) {
                minIndex = i;
            }

        }

        return flightBoard.getPlayerOrderList().get(minIndex);

    }

}
