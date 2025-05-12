package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.List;

/**
 * class that represent the card planets
 *
 * @author carlo
 */

public class Planets extends Card implements GoodsGain, Movable {

    public int daysLost;
    private boolean[] planetOccupation;

    private int[] planet1;
    private int[] planet2;
    private int[] planet3;
    private int[] planet4;

    public Planets(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.getCardLevel();
        this.cardName = cardBuilder.getCardName();
        this.planet1 = cardBuilder.getPlanet1();
        this.planet2 = cardBuilder.getPlanet2();
        this.planet3 = cardBuilder.getPlanet3();
        this.planet4 = cardBuilder.getPlanet4();

    }

    public void showCard() {
        System.out.println("Card name: " + getCardName());
        System.out.println("Card level: " + getCardLevel());
        System.out.println("Days lost: " + daysLost);

        if (planet1 != null) {

            System.out.println("Planet 1 offers: ");
            printGoods(planet1);

        }

        if (planet2 != null) {

            System.out.println("Planet 2 offers: ");
            printGoods(planet2);

        }

        if (planet3 != null) {

            System.out.println("Planet 3 offers: ");
            printGoods(planet3);
        }

        if (planet4 != null) {

            System.out.println("Planet 4 offers: ");
            printGoods(planet4);
        }


    }

    @Override

    public void resolve(GameInformation gameInformation) {

        String message;
        DataContainer dataContainer;
        int planetChosen, numberOfPlanets, freePlanet;

        List<Player> players = gameInformation.getFlightBoard().getPlayerOrderList();

        //At the beginning all the planets are still to be occupied


        numberOfPlanets = countPlanetsToLandOn();
        freePlanet = numberOfPlanets;

        planetOccupation = new boolean[numberOfPlanets];

        for (Player player : players) {

            message = "Would you like to land on a planet ?";
            dataContainer = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerContainer(player);
            dataContainer.setMessage(message);
            dataContainer.setCommand("printMessage");
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerData(player);

            try {
                if (ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerBoolean(player)) {

                    message = "Enter the planet you want to land on(1-" + numberOfPlanets + "): ";
                    dataContainer = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerContainer(player);
                    dataContainer.setMessage(message);
                    dataContainer.setCommand("printMessage");
                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerData(player);

                    while (true) {

                        planetChosen = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerInt(player);

                        if (planetChosen > 0 && planetChosen <= numberOfPlanets) {

                            if (!planetOccupation[planetChosen - 1]) {

                                message = "Player " + player.getNickName() +
                                        "has landed on planet " + planetChosen + " !";
                                for (Player player1 : gameInformation.getFlightBoard().getPlayerOrderList()) {
                                    dataContainer = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerContainer(player1);
                                    dataContainer.setMessage(message);
                                    dataContainer.setCommand("printMessage");
                                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerData(player1);
                                }
                                freePlanet--;

                                planetOccupation[planetChosen - 1] = true;

                                if (planetChosen == 1) {

                                    giveGoods(player, planet1, gameInformation.getFlightBoard(), gameInformation.getGameCode());

                                } else if (planetChosen == 2) {

                                    giveGoods(player, planet2, gameInformation.getFlightBoard(), gameInformation.getGameCode());

                                } else if (planetChosen == 3) {

                                    giveGoods(player, planet3, gameInformation.getFlightBoard(), gameInformation.getGameCode());

                                } else {

                                    giveGoods(player, planet4, gameInformation.getFlightBoard(), gameInformation.getGameCode());

                                }

                                break;

                            } else {

                                message = "The planet you selected has already been occupied";
                                dataContainer = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerContainer(player);
                                dataContainer.setMessage(message);
                                dataContainer.setCommand("printMessage");
                                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerData(player);

                            }
                        } else {

                            message = "The planet you chose is invalid";
                            dataContainer = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerContainer(player);
                            dataContainer.setMessage(message);
                            dataContainer.setCommand("printMessage");
                            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerData(player);

                        }

                    }

                    if (freePlanet == 0) {
                        message = "All planets were occupied!";
                        for (Player player1 : gameInformation.getFlightBoard().getPlayerOrderList()) {
                            dataContainer = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerContainer(player1);
                            dataContainer.setMessage(message);
                            dataContainer.setCommand("printMessage");
                            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerData(player1);
                        }
                        break;
                    }

                } else {

                    message = "Player " + player.getNickName() +
                            " decided to not land on any planet!";
                    for (Player player1 : gameInformation.getFlightBoard().getPlayerOrderList()) {
                        dataContainer = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerContainer(player1);
                        dataContainer.setMessage(message);
                        dataContainer.setCommand("printMessage");
                        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerData(player1);
                    }
                }
            } catch (PlayerDisconnectedException e) {
                gameInformation.disconnectPlayer(player);
                message = e.getMessage();
                for (Player player1 : gameInformation.getPlayerList()) {
                    dataContainer = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerContainer(player1);
                    dataContainer.setMessage(message);
                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerData(player1);
                }
            }
        }

        gameInformation.getFlightBoard().updateFlightBoard();
        for (Player player : gameInformation.getFlightBoard().getPlayerOrderList()) {
            dataContainer = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerContainer(player);
            dataContainer.setFlightBoard(gameInformation.getFlightBoard());
            dataContainer.setCommand("printFlightBoard");
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerData(player);
        }

    }

    private int countPlanetsToLandOn() {

        int i, numberOfPlanets;

        for (i = 0, numberOfPlanets = 0; i < 5; i++) {

            if (i == 0) {

                if (planet1 != null) {
                    numberOfPlanets++;
                } else {
                    break;
                }

            } else if (i == 1) {

                if (planet2 != null) {
                    numberOfPlanets++;
                } else {
                    break;
                }

            } else if (i == 2) {

                if (planet2 != null) {
                    numberOfPlanets++;
                } else {
                    break;
                }

            } else if (i == 3) {

                if (planet3 != null) {
                    numberOfPlanets++;
                } else {
                    break;
                }

            } else {

                if (planet4 != null) {
                    numberOfPlanets++;
                } else {
                    break;
                }
            }
        }

        return numberOfPlanets;
    }
}
