package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Color;
import it.polimi.ingsw.Model.ShipBoard.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testing the method implemented by the interface. The goal is to test all
 * possible scenarios, i.e:
 * First player has the smallest crew
 * Second player has the smallest crew
 * Third plauer has the smallest crew
 * Last player has the smallest crew
 * If there is a tie, the first player in order must be returned.
 *
 * @author carlo
 */

public class SmallestCrewTest {

    class Operator implements SmallestCrew {
    }

    ;

    Operator operator = new Operator();

    FlightBoard flightBoard;

    Player player1;
    Player player2;
    Player player3;
    Player player4;

    //initializing everything each time before a test in order to guarantee that
    //each test is isolated and independent of the orhers

    @BeforeEach
    void initialize() {

        //creating a fictitious game model
        GameInformation gameInformation = new GameInformation();
        gameInformation.setGameType(GameType.TestGame);

        //creating a deck of only one type of card, easy to initialize, as
        //cards are futile for this test.

        CardBuilder cardBuilder = new CardBuilder();
        cardBuilder.buildCardName("Epidemic").buildCardLevel(1);

        ArrayList<Card> cardsList = new ArrayList<>();

        //8 is the size of a test game deck

        for (int i = 0; i < 8; i++) {

            cardsList.add(new Epidemic(cardBuilder));

        }

        flightBoard = new FlightBoard(GameType.TestGame, cardsList);

        player1 = new Player("player1", Color.BLUE, gameInformation);
        player2 = new Player("player2", Color.GREEN, gameInformation);
        player3 = new Player("player3", Color.YELLOW, gameInformation);
        player4 = new Player("player4", Color.RED, gameInformation);

        //adding the players to the flightboard

        flightBoard.addPlayer(player1, 4);
        flightBoard.addPlayer(player2, 3);
        flightBoard.addPlayer(player3, 2);
        flightBoard.addPlayer(player4, 1);

    }

    @Test
    void firstPlayerSmallestCrew() {

        player1.getShipBoard().getShipBoardAttributes().updateCrewMembers(1);
        player2.getShipBoard().getShipBoardAttributes().updateCrewMembers(4);
        player3.getShipBoard().getShipBoardAttributes().updateCrewMembers(20);
        player4.getShipBoard().getShipBoardAttributes().updateCrewMembers(10);

        assertTrue(operator.calculateSmallestCrew(flightBoard).getNickName().equals("player1"));


    }

    @Test
    void secondPlayerSmallestCrew() {

        player1.getShipBoard().getShipBoardAttributes().updateCrewMembers(3);
        player2.getShipBoard().getShipBoardAttributes().updateCrewMembers(2);
        player3.getShipBoard().getShipBoardAttributes().updateCrewMembers(5);
        player4.getShipBoard().getShipBoardAttributes().updateCrewMembers(3);

        assertTrue(operator.calculateSmallestCrew(flightBoard).getNickName().equals("player2"));


    }

    @Test
    void thirdPlayerSmallestCrew() {

        player1.getShipBoard().getShipBoardAttributes().updateCrewMembers(3);
        player2.getShipBoard().getShipBoardAttributes().updateCrewMembers(2);
        player3.getShipBoard().getShipBoardAttributes().updateCrewMembers(1);
        player4.getShipBoard().getShipBoardAttributes().updateCrewMembers(10);

        assertTrue(operator.calculateSmallestCrew(flightBoard).getNickName().equals("player3"));


    }

    @Test
    void lastPlayerSmallestCrew() {

        player1.getShipBoard().getShipBoardAttributes().updateCrewMembers(3);
        player2.getShipBoard().getShipBoardAttributes().updateCrewMembers(6);
        player3.getShipBoard().getShipBoardAttributes().updateCrewMembers(5);
        player4.getShipBoard().getShipBoardAttributes().updateCrewMembers(2);

        assertTrue(operator.calculateSmallestCrew(flightBoard).getNickName().equals("player4"));


    }

    @Test
    void firstTieTest() {

        player1.getShipBoard().getShipBoardAttributes().updateCrewMembers(1);
        player2.getShipBoard().getShipBoardAttributes().updateCrewMembers(1);
        player3.getShipBoard().getShipBoardAttributes().updateCrewMembers(2);
        player4.getShipBoard().getShipBoardAttributes().updateCrewMembers(5);

        assertTrue(operator.calculateSmallestCrew(flightBoard).getNickName().equals("player1"));

    }

    @Test
    void secondTieTest() {

        player1.getShipBoard().getShipBoardAttributes().updateCrewMembers(2);
        player2.getShipBoard().getShipBoardAttributes().updateCrewMembers(2);
        player3.getShipBoard().getShipBoardAttributes().updateCrewMembers(1);
        player4.getShipBoard().getShipBoardAttributes().updateCrewMembers(1);

        assertTrue(operator.calculateSmallestCrew(flightBoard).getNickName().equals("player3"));

    }

    @Test
    void thirdTieTest() {

        player1.getShipBoard().getShipBoardAttributes().updateCrewMembers(2);
        player2.getShipBoard().getShipBoardAttributes().updateCrewMembers(2);
        player3.getShipBoard().getShipBoardAttributes().updateCrewMembers(5);
        player4.getShipBoard().getShipBoardAttributes().updateCrewMembers(1);

        assertTrue(operator.calculateSmallestCrew(flightBoard).getNickName().equals("player4"));

    }


    @Test
    void lastTieTest() {

        player1.getShipBoard().getShipBoardAttributes().updateCrewMembers(1);
        player2.getShipBoard().getShipBoardAttributes().updateCrewMembers(1);
        player3.getShipBoard().getShipBoardAttributes().updateCrewMembers(1);
        player4.getShipBoard().getShipBoardAttributes().updateCrewMembers(0);

        assertTrue(operator.calculateSmallestCrew(flightBoard).getNickName().equals("player4"));

    }

}