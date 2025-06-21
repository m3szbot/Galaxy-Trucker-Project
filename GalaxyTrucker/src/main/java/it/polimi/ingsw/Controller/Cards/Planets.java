package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Controller.ExceptionsHandler;
import it.polimi.ingsw.Controller.FlightPhase.IndexChecker;
import it.polimi.ingsw.Controller.FlightPhase.PlayerFlightInputHandler;
import it.polimi.ingsw.Model.FlightBoard.LappedPlayersException;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;

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
        this.daysLost = cardBuilder.getDaysLost();
        this.planet1 = cardBuilder.getPlanet1();
        this.planet2 = cardBuilder.getPlanet2();
        this.planet3 = cardBuilder.getPlanet3();
        this.planet4 = cardBuilder.getPlanet4();
        this.imagePath = cardBuilder.getImagePath();

    }

    @Override

    public void resolve(GameInformation gameInformation) {

        PlayerMessenger playerMessenger;
        int planetChosen, numberOfPlanets, freePlanet;
        Player player;

        gameInformation.getFlightBoard().getPlayerOrderList();

        //At the beginning all the planets are still to be occupied
        numberOfPlanets = countPlanetsToLandOn();
        freePlanet = numberOfPlanets;

        planetOccupation = new boolean[numberOfPlanets];

        for (int i = 0; i < gameInformation.getFlightBoard().getPlayerOrderList().size(); i++) {

            //Checks the validity of the current index (precaution for disconnection)
            i = IndexChecker.checkIndex(gameInformation, i);

            player = gameInformation.getFlightBoard().getPlayerOrderList().get(i);
            PlayerFlightInputHandler.startPlayerTurn(player);

            message = "It's " + player.getColouredNickName() + "'s turn.\n";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);

            message = "\nWould you like to land on a planet ?";
            playerMessenger.printMessage(message);

            try {
                if (playerMessenger.getPlayerBoolean()) {

                    message = "Enter the planet you want to land on(1-" + numberOfPlanets + "): ";
                    playerMessenger.printMessage(message);

                    while (true) {

                        planetChosen = playerMessenger.getPlayerInt();

                        if (planetChosen > 0 && planetChosen <= numberOfPlanets) {

                            if (!planetOccupation[planetChosen - 1]) {

                                message = "Player " + player.getColouredNickName() +
                                        " has landed on planet " + planetChosen + " !\n";
                                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);
                                freePlanet--;

                                planetOccupation[planetChosen - 1] = true;

                                if (planetChosen == 1) {

                                    giveGoods(player, planet1, gameInformation);
                                    changePlayerPosition(player, -daysLost, gameInformation.getFlightBoard());

                                } else if (planetChosen == 2) {

                                    giveGoods(player, planet2, gameInformation);
                                    changePlayerPosition(player, -daysLost, gameInformation.getFlightBoard());

                                } else if (planetChosen == 3) {

                                    giveGoods(player, planet3, gameInformation);
                                    changePlayerPosition(player, -daysLost, gameInformation.getFlightBoard());

                                } else {

                                    giveGoods(player, planet4, gameInformation);
                                    changePlayerPosition(player, -daysLost, gameInformation.getFlightBoard());

                                }

                                break;

                            } else {

                                message = "The planet you selected has already been occupied. Please select another one:";
                                playerMessenger.printMessage(message);

                            }
                        } else {

                            message = "The planet you chose is invalid. Please select another one:";
                            playerMessenger.printMessage(message);

                        }

                    }

                    if (freePlanet == 0) {
                        message = "All planets have been occupied.\n";
                        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);
                        break;
                    }

                } else {

                    message = "Player " + player.getColouredNickName() +
                            " decided to not land on any planet.\n";
                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

                }
            } catch (PlayerDisconnectedException e) {
                PlayerFlightInputHandler.removePlayer(player);

                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(player);
                i--;

            }

            if (ClientMessenger.getGameMessenger(gameInformation.getGameCode()).checkPlayerMessengerPresence(player)) {
                message = "You finished your turn, wait for the other players.\n";
                playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
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
        }


    }

    public void showCard() {
        System.out.println("Card name: " + getCardName());
        System.out.println("Card level: " + getCardLevel());
        System.out.println("Days lost: " + daysLost);

        if (planet1 != null) {

            System.out.println("\nPlanet 1 offers: ");
            printGoods(planet1);

        }

        if (planet2 != null) {

            System.out.println("\nPlanet 2 offers: ");
            printGoods(planet2);

        }

        if (planet3 != null) {

            System.out.println("\nPlanet 3 offers: ");
            printGoods(planet3);
        }

        if (planet4 != null) {

            System.out.println("\nPlanet 4 offers: ");
            printGoods(planet4);
        }
        System.out.println();

    }

    private int countPlanetsToLandOn() {

        int i, numberOfPlanets;

        for (i = 0, numberOfPlanets = 0; i < 4; i++) {

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
