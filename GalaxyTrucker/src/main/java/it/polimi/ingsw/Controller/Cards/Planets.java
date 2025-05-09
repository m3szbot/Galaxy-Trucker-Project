package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.Connection.ClientSide.View.FlightView.FlightView;

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

    @Override

    public void resolve(FlightBoard flightBoard, FlightView flightView) {

        String message;
        int planetChosen, numberOfPlanets, freePlanet;

        List<Player> players = flightBoard.getPlayerOrderList();

        //At the beginning all the planets are still to be occupied


        numberOfPlanets = countPlanetsToLandOn();
        freePlanet = numberOfPlanets;

        planetOccupation = new boolean[numberOfPlanets];

        for (Player player: players) {

            message = "Would you like to land on a planet ?";

            if (flightView.askPlayerGenericQuestion(player, message)) {

                message = "Enter the planet you want to land on(1-" + numberOfPlanets + "): ";

                while (true) {

                    planetChosen = flightView.askPlayerValue(player, message);

                    if (planetChosen > 0 && planetChosen <= numberOfPlanets) {

                        if (!planetOccupation[planetChosen - 1]) {

                            message = "Player " + player.getNickName() +
                                    "has landed on planet " + planetChosen + " !";
                            flightView.sendMessageToAll(message);
                            freePlanet--;

                            planetOccupation[planetChosen - 1] = true;

                            if (planetChosen == 1) {

                                giveGoods(player, planet1, flightBoard, flightView);

                            } else if (planetChosen == 2) {

                                giveGoods(player, planet2, flightBoard, flightView);

                            } else if (planetChosen == 3) {

                                giveGoods(player, planet3, flightBoard, flightView);

                            } else {

                                giveGoods(player, planet4, flightBoard, flightView);

                            }

                            break;

                        }
                        else{
                            message = "The planet you selected has already been occupied";
                            flightView.sendMessageToPlayer(player, message);
                        }
                    }
                    else{

                        message = "The planet you chose is invalid";
                        flightView.sendMessageToPlayer(player, message);

                    }

                }

                if (freePlanet == 0) {
                    message = "All planets were occupied!";
                    flightView.sendMessageToAll(message);
                    break;
                }

            } else {

                message = "Player " + player.getNickName() +
                        " decided to not land on any planet!";
                flightView.sendMessageToAll(message);
            }
        }

    }

    private int countPlanetsToLandOn(){

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
