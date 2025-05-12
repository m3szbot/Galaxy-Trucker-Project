package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
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

    public void resolve(GameInformation gameInformation) {

        int numberOfPlayers = gameInformation.getFlightBoard().getPlayerOrderList().size();
        int[] enginePowers = new int[numberOfPlayers];
        float[] firePowers = new float[numberOfPlayers];
        Player lowestInhabitantNumberPlayer, weakestEnginePowerPlayer, weakestFirePowerPlayer;
        String message;
        DataContainer dataContainer;

        //calculating player with the lowest inhabitant number

        lowestInhabitantNumberPlayer = calculateSmallestCrew(gameInformation.getFlightBoard());

        //letting the players choose their firePower, from the leader backwards

        for (int i = 0; i < numberOfPlayers; i++) {

            firePowers[i] = chooseFirePower(gameInformation.getFlightBoard().getPlayerOrderList().get(i), gameInformation.getGameCode());

        }

        //letting the players choose their enginePower, from the leader backwards

        for (int i = 0; i < numberOfPlayers; i++) {

            enginePowers[i] = chooseEnginePower(gameInformation.getFlightBoard().getPlayerOrderList().get(i), gameInformation.getGameCode());

        }

        weakestFirePowerPlayer = findWeakestFirePowerPlayer(firePowers, numberOfPlayers, gameInformation.getFlightBoard());
        weakestEnginePowerPlayer = findWeakestEnginePowerPlayer(enginePowers, numberOfPlayers, gameInformation.getFlightBoard());

        //giving the various penalties to players

        changePlayerPosition(lowestInhabitantNumberPlayer, -daysLost, gameInformation.getFlightBoard());

        message = "Player " + lowestInhabitantNumberPlayer.getNickName() + " lost " + daysLost +
                " flight days as he is the one with the lowest number of inhabitants!";
        for (Player player : gameInformation.getFlightBoard().getPlayerOrderList()) {
            dataContainer = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerContainer(player);
            dataContainer.setMessage(message);
            dataContainer.setCommand("printMessage");
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerData(player);
        }

        inflictLoss(weakestEnginePowerPlayer, lossType, lossNumber, gameInformation.getFlightBoard(), gameInformation.getGameCode());

        message = "Player " + weakestEnginePowerPlayer.getNickName() + " lost " + lossNumber +
                " crew members as he is the one with the weakest engine power!";
        for (Player player : gameInformation.getFlightBoard().getPlayerOrderList()) {
            dataContainer = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerContainer(player);
            dataContainer.setMessage(message);
            dataContainer.setCommand("printMessage");
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerData(player);
        }

        //rolling the dice for each shot and then hitting
        for (int i = 0; i < blows.length; i++) {
            blows[i].rollDice();
        }
        hit(weakestFirePowerPlayer, blows, blowType, gameInformation.getFlightBoard(), gameInformation.getGameCode());

        gameInformation.getFlightBoard().updateFlightBoard();
        for (Player player : gameInformation.getFlightBoard().getPlayerOrderList()) {
            dataContainer = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerContainer(player);
            dataContainer.setFlightBoard(gameInformation.getFlightBoard());
            dataContainer.setCommand("printFlightBoard");
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerData(player);
        }

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
