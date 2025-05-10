package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.View.FlightView.FlightView;

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

    @Override

    public void resolve(FlightBoard flightBoard, FlightView flightView) {

        int numberOfPlayers = flightBoard.getPlayerOrderList().size();
        int[] enginePowers = new int[numberOfPlayers];
        float[] firePowers = new float[numberOfPlayers];
        Player lowestInhabitantNumberPlayer, weakestEnginePowerPlayer, weakestFirePowerPlayer;
        String message;

        //calculating player with the lowest inhabitant number

        lowestInhabitantNumberPlayer = calculateSmallestCrew(flightBoard);

        //letting the players choose their firePower, from the leader backwards

        for (int i = 0; i < numberOfPlayers; i++) {

            firePowers[i] = chooseFirePower(flightBoard.getPlayerOrderList().get(i), flightView);

        }

        //letting the players choose their enginePower, from the leader backwards

        for (int i = 0; i < numberOfPlayers; i++) {

            enginePowers[i] = chooseEnginePower(flightBoard.getPlayerOrderList().get(i), flightView);

        }

        weakestFirePowerPlayer = findWeakestFirePowerPlayer(firePowers, numberOfPlayers, flightBoard);
        weakestEnginePowerPlayer = findWeakestEnginePowerPlayer(enginePowers, numberOfPlayers, flightBoard);

        //giving the various penalties to players

        changePlayerPosition(lowestInhabitantNumberPlayer, -daysLost, flightBoard);

        message = "Player " + lowestInhabitantNumberPlayer.getNickName() + " lost " + daysLost +
                " flight days as he is the one with the lowest number of inhabitants!";
        flightView.sendMessageToAll(message);

        inflictLoss(weakestEnginePowerPlayer, lossType, lossNumber, flightBoard, flightView);

        message = "Player " + weakestEnginePowerPlayer.getNickName() + " lost " + lossNumber +
                " crew members as he is the one with the weakest engine power!";
        flightView.sendMessageToAll(message);

        //rolling the dice for each shot and then hitting
        for (int i = 0; i < blows.length; i++) {
            blows[i].rollDice();
        }
        hit(weakestFirePowerPlayer, blows, blowType, flightBoard, flightView);

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
