package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
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

    }

    public void showCard() {

        System.out.println("Card name: " + getCardName());
        System.out.println("Card level: " + getCardLevel());
        System.out.println("Days lost: " + daysLost);
        System.out.println("Loss type: " + lossType.toString());
        System.out.println("Blow type: " + blowType.toString());

        printBlows(blows);

    }

    @Override

    public void resolve(GameInformation gameInformation) {

        int numberOfPlayers = gameInformation.getFlightBoard().getPlayerOrderList().size();
        int[] enginePowers = new int[numberOfPlayers];
        float[] firePowers = new float[numberOfPlayers];
        Player lowestInhabitantNumberPlayer, weakestEnginePowerPlayer, weakestFirePowerPlayer;
        String message;

        //calculating player with the lowest inhabitant number

        lowestInhabitantNumberPlayer = calculateSmallestCrew(gameInformation.getFlightBoard());

        //letting the players choose their firePower, from the leader backwards

        for (int i = 0; i < numberOfPlayers; i++) {

            firePowers[i] = chooseFirePower(gameInformation.getFlightBoard().getPlayerOrderList().get(i), gameInformation);

        }

        //letting the players choose their enginePower, from the leader backwards

        for (int i = 0; i < numberOfPlayers; i++) {

            enginePowers[i] = chooseEnginePower(gameInformation.getFlightBoard().getPlayerOrderList().get(i), gameInformation);

        }

        weakestFirePowerPlayer = findWeakestFirePowerPlayer(firePowers, numberOfPlayers, gameInformation.getFlightBoard());
        weakestEnginePowerPlayer = findWeakestEnginePowerPlayer(enginePowers, numberOfPlayers, gameInformation.getFlightBoard());

        //giving the various penalties to players

        changePlayerPosition(lowestInhabitantNumberPlayer, -daysLost, gameInformation.getFlightBoard());

        message = "Player " + lowestInhabitantNumberPlayer.getNickName() + " lost " + daysLost +
                " flight days as he is the one with the lowest number of inhabitants!";
        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

        inflictLoss(weakestEnginePowerPlayer, lossType, lossNumber, gameInformation);

        message = "Player " + weakestEnginePowerPlayer.getNickName() + " lost " + lossNumber +
                " crew members as he is the one with the weakest engine power!";
        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

        //rolling the dice for each shot and then hitting
        for (int i = 0; i < blows.length; i++) {
            blows[i].rollDice();
        }
        hit(weakestFirePowerPlayer, blows, blowType, gameInformation);

        gameInformation.getFlightBoard().updateFlightBoard();
        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

    }

    private Player findWeakestFirePowerPlayer(float[] firePowers, int numberOfPlayers, FlightBoard flightBoard) {

        int minIndex = 0;

        for (int i = 0; i < numberOfPlayers; i++) {

            if (firePowers[i] < firePowers[minIndex]) {
                minIndex = i;
            }

        }

        return flightBoard.getPlayerOrderList().get(minIndex);

    }

    private Player findWeakestEnginePowerPlayer(int[] enginePowers, int numberOfPlayers, FlightBoard flightBoard) {

        int minIndex = 0;

        for (int i = 0; i < numberOfPlayers; i++) {

            if (enginePowers[i] < enginePowers[minIndex]) {
                minIndex = i;
            }

        }

        return flightBoard.getPlayerOrderList().get(minIndex);

    }

}
