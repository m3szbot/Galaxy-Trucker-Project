package it.polimi.ingsw.Controller.FlightPhase;

import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Controller.Cards.Blow;
import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Controller.Cards.CardBuilder;
import it.polimi.ingsw.Controller.Cards.ElementType;
import it.polimi.ingsw.Controller.Phase;
import it.polimi.ingsw.Controller.Sleeper;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GamePhase;
import it.polimi.ingsw.Model.ShipBoard.Player;

public class FlightPhase extends Phase {

    public FlightPhase(GameInformation gameInformation) {
        super(gameInformation);
    }

    public void start() {
        Card card;
        PlayerMessenger playerMessenger;

        setGamePhaseToClientServer(GamePhase.Flight);
        System.out.println("Flight phase has started");

        FlightBoard flightBoard = gameInformation.getFlightBoard();

        // send initial flightBoard to players and starting the threads
        for (Player player : flightBoard.getPlayerOrderList()) {

            playerMessenger = gameMessenger.getPlayerMessenger(player);
            playerMessenger.printFlightBoard(flightBoard);

            playerMessenger.printMessage("Your shipboard:\n");

            playerMessenger.printShipboard(player.getShipBoard());

            PlayerFlightInputHandler.addPlayer(player, gameInformation);

        }

/*
        //Testing
        CardBuilder cardBuilder = new CardBuilder();


        //First test card


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println("Error while sleeping");
        }

        card = cardBuilder.buildCardLevel(2).buildCardName("Epidemic").buildBlowType(null).buildRequirementType(null).buildLossType(null).buildDaysLost(0).buildGainedCredits(0).buildRequirementNumber(0).buildLossNumber(0).buildGoods(null).buildBlows(null).buildPlanets(null, null, null, null).getBuiltCard();
        for (Player player : flightBoard.getPlayerOrderList()) {

            playerMessenger = gameMessenger.getPlayerMessenger(player);
            playerMessenger.printCard(card);

        }
        card.resolve(gameInformation);

        for (Player player : flightBoard.getPlayerOrderList()) {

            playerMessenger = gameMessenger.getPlayerMessenger(player);

            playerMessenger.printMessage("Your shipboard:\n");

            playerMessenger.printShipboard(player.getShipBoard());

        }


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println("Error while sleeping");
        }

        card = cardBuilder.buildCardLevel(2).buildCardName("Sabotage").buildBlowType(null).buildRequirementType(null).buildLossType(null).buildDaysLost(0).buildGainedCredits(0).buildRequirementNumber(0).buildLossNumber(0).buildGoods(null).buildBlows(null).buildPlanets(null, null, null, null).getBuiltCard();
        for (Player player : flightBoard.getPlayerOrderList()) {

            playerMessenger = gameMessenger.getPlayerMessenger(player);
            playerMessenger.printCard(card);

        }
        card.resolve(gameInformation);

        for (Player player : flightBoard.getPlayerOrderList()) {

            playerMessenger = gameMessenger.getPlayerMessenger(player);

            playerMessenger.printMessage("Your shipboard:\n");

            playerMessenger.printShipboard(player.getShipBoard());

        }


        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            System.out.println("Error while sleeping");
        }

        card = cardBuilder.buildCardLevel(1).buildCardName("CombatZone").buildBlowType(ElementType.CannonBlow).buildRequirementType(null).buildLossType(ElementType.Goods).buildDaysLost(3).buildGainedCredits(0).buildRequirementNumber(0).buildLossNumber(2).buildGoods(null).buildBlows(new Blow[]{new Blow(2, false), new Blow(0, true)}).buildPlanets(null, null, null, null).getBuiltCard();
        for (Player player : flightBoard.getPlayerOrderList()) {

            playerMessenger = gameMessenger.getPlayerMessenger(player);
            playerMessenger.printCard(card);

        }
        card.resolve(gameInformation);

        for (Player player : flightBoard.getPlayerOrderList()) {

            playerMessenger = gameMessenger.getPlayerMessenger(player);

            playerMessenger.printMessage("Your shipboard:\n");

            playerMessenger.printShipboard(player.getShipBoard());

        }


        card = cardBuilder.buildCardLevel(2).buildCardName("Pirates").buildBlowType(ElementType.CannonBlow).buildRequirementType(ElementType.FirePower).buildLossType(null).buildDaysLost(2).buildGainedCredits(7).buildRequirementNumber(6).buildLossNumber(0).buildGoods(null).buildBlows(new Blow[]{new Blow(2, false), new Blow(1, false), new Blow(3, true)}).buildPlanets(null, null, null, null).getBuiltCard();
        for (Player player : flightBoard.getPlayerOrderList()) {

            playerMessenger = gameMessenger.getPlayerMessenger(player);
            playerMessenger.printCard(card);

        }
        card.resolve(gameInformation);

        for (Player player : flightBoard.getPlayerOrderList()) {

            playerMessenger = gameMessenger.getPlayerMessenger(player);

            playerMessenger.printMessage("Your shipboard:\n");

            playerMessenger.printShipboard(player.getShipBoard());

        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            System.out.println("Error while sleeping");
        }

        card = cardBuilder.buildCardLevel(1).buildCardName("SmallMeteorSwarm").buildBlowType(ElementType.Meteorite).buildRequirementType(null).buildLossType(null).buildDaysLost(0).buildGainedCredits(0).buildRequirementNumber(0).buildLossNumber(0).buildGoods(null).buildBlows(new Blow[]{new Blow(3, false), new Blow(1, false), new Blow(2, true)}).buildPlanets(null, null, null, null).getBuiltCard();
        for (Player player : flightBoard.getPlayerOrderList()) {

            playerMessenger = gameMessenger.getPlayerMessenger(player);
            playerMessenger.printCard(card);

        }
        card.resolve(gameInformation);

        for (Player player : flightBoard.getPlayerOrderList()) {

            playerMessenger = gameMessenger.getPlayerMessenger(player);

            playerMessenger.printMessage("Your shipboard:\n");

            playerMessenger.printShipboard(player.getShipBoard());

        }


        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            System.out.println("Error while sleeping");
        }

        card = cardBuilder.buildCardLevel(1).buildCardName("BigMeteorSwarm").buildBlowType(ElementType.Meteorite).buildRequirementType(null).buildLossType(null).buildDaysLost(0).buildGainedCredits(0).buildRequirementNumber(0).buildLossNumber(0).buildGoods(null).buildBlows(new Blow[]{new Blow(3, false), new Blow(2, false), new Blow(2, true), new Blow(1, false), new Blow(2, false)}).buildPlanets(null, null, null, null).getBuiltCard();
        for (Player player : flightBoard.getPlayerOrderList()) {

            playerMessenger = gameMessenger.getPlayerMessenger(player);
            playerMessenger.printCard(card);

        }
        card.resolve(gameInformation);

        for (Player player : flightBoard.getPlayerOrderList()) {

            playerMessenger = gameMessenger.getPlayerMessenger(player);

            playerMessenger.printMessage("Your shipboard:\n");

            playerMessenger.printShipboard(player.getShipBoard());

        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            System.out.println("Error while sleeping");
        }

        card = cardBuilder.buildCardLevel(1).buildCardName("BigMeteorSwarm").buildBlowType(ElementType.Meteorite).buildRequirementType(null).buildLossType(null).buildDaysLost(0).buildGainedCredits(0).buildRequirementNumber(0).buildLossNumber(0).buildGoods(null).buildBlows(new Blow[]{new Blow(2, false), new Blow(1, false), new Blow(3, true), new Blow(0, false), new Blow(0, false)}).buildPlanets(null, null, null, null).getBuiltCard();
        for (Player player : flightBoard.getPlayerOrderList()) {

            playerMessenger = gameMessenger.getPlayerMessenger(player);
            playerMessenger.printCard(card);

        }
        card.resolve(gameInformation);

        for (Player player : flightBoard.getPlayerOrderList()) {

            playerMessenger = gameMessenger.getPlayerMessenger(player);

            playerMessenger.printMessage("Your shipboard:\n");

            playerMessenger.printShipboard(player.getShipBoard());

        }

        //End of testing

 */

        // resolve cards
        while (flightBoard.getCardsNumber() > 0) {

            Sleeper.sleepXSeconds(3);

            card = flightBoard.getNewCard();

            for (Player player : flightBoard.getPlayerOrderList()) {

                playerMessenger = gameMessenger.getPlayerMessenger(player);
                playerMessenger.printCard(card);

            }

            Sleeper.sleepXSeconds(3);

            card.resolve(gameInformation);

            Sleeper.sleepXSeconds(3);

            //Printing necessary information after each card to every player
            for (Player player : flightBoard.getPlayerOrderList()) {

                playerMessenger = gameMessenger.getPlayerMessenger(player);

                playerMessenger.printFlightBoard(flightBoard);

                Sleeper.sleepXSeconds(3);

                playerMessenger.printMessage("Your shipboard:\n");

                playerMessenger.printShipboard(player.getShipBoard());

            }
        }


        //ending input thread
        for (Player player : flightBoard.getPlayerOrderList()) {
            PlayerFlightInputHandler.removePlayer(player);
        }

        gameMessenger.sendMessageToAll("Flight phase has ended.\n");

    }

}
