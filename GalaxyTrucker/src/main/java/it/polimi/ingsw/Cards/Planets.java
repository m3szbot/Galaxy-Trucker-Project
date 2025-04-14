package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Application.FlightPhase.FlightView;
import it.polimi.ingsw.FlightBoard.FlightBoard;
import it.polimi.ingsw.Shipboard.Player;

import java.util.List;

/**
 * class that represent the card planets
 *
 * @author carlo
 */

public class Planets extends Card implements GoodsGain, Movable{

    public int daysLost;
    private boolean[] playerOccupation, planetOccupation;

    private int[] planet1;
    private int[] planet2;
    private int[] planet3;
    private int[] planet4;

    public Planets(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.cardLevel;
        this.cardName = cardBuilder.cardName;
        this.planet1 = cardBuilder.planet1;
        this.planet2 = cardBuilder.planet2;
        this.planet3 = cardBuilder.planet3;
        this.planet4 = cardBuilder.planet4;

    }

    @Override

    public void resolve(FlightBoard flightBoard, FlightView flightView) {

        String message;
        int planetChosen, numberOfPlayers = flightBoard.getPlayerOrderList().size(), i, numberOfPlanets;
        int freePlanet;

        List<Player> players = flightBoard.getPlayerOrderList();


        //At the beginning all the planets are still to be occupied

        for(i = 0, numberOfPlanets = 0; i < 5; i++){

            if(i == 0){

                if(planet1 != null){
                    numberOfPlanets++;
                }
                else{
                    break;
                }

            }
            else if(i == 1){

                if(planet2 != null){
                    numberOfPlanets++;
                }
                else{
                    break;
                }

            }
            else if(i == 2){

                if(planet2 != null){
                    numberOfPlanets++;
                }
                else{
                    break;
                }

            }
            else if(i == 3){

                if(planet3 != null){
                    numberOfPlanets++;
                }
                else{
                    break;
                }

            }
            else{

                if(planet4 != null){
                    numberOfPlanets++;
                }
                else{
                    break;
                }
            }
        }

        freePlanet = numberOfPlanets;

        planetOccupation = new boolean[numberOfPlanets];

        for(i = 0; i < numberOfPlayers; i++){

            message = "Would you like to land on a planet ?";

            if(flightView.askPlayerGenericQuestion(players.get(i), message)){

                message = "Enter the planet you want to land on: ";

                while(true) {

                    planetChosen = flightView.askPlayerValue(players.get(i), message);

                    if(planetChosen > 0 && planetChosen <= numberOfPlanets){

                        if(!planetOccupation[planetChosen - 1]){

                            message = "Player " + players.get(i).getNickName() +
                                    "has landed on planet " + planetChosen + " !";
                            flightView.sendMessageToAll(message);
                            freePlanet--;

                            planetOccupation[planetChosen - 1] = true;

                            if(planetChosen == 1){

                                giveGoods(players.get(i), planet1, flightBoard, flightView);

                            }
                            else if(planetChosen == 2){

                                giveGoods(players.get(i), planet2, flightBoard, flightView);

                            }
                            else if(planetChosen == 3){

                                giveGoods(players.get(i), planet3, flightBoard, flightView);

                            }
                            else{

                                giveGoods(players.get(i), planet4, flightBoard, flightView);

                            }

                            break;

                        }
                    }

                    message = "The planet you entered is invalid, please enter a valid one: ";

                }

                if(freePlanet == 0){
                    message = "All planets were occupied!";
                    flightView.sendMessageToAll(message);
                    break;
                }

            }
            else{

                message = "Player " + flightBoard.getPlayerOrderList().get(i).getNickName() +
                        " decided to not land on any planet!";
                flightView.sendMessageToAll(message);
            }
        }

    }
}
