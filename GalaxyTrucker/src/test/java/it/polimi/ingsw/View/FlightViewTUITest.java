package it.polimi.ingsw.View;

import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Color;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.View.EvaluationView.EvaluationViewTUI;
import it.polimi.ingsw.View.FlightView.FlightView;
import it.polimi.ingsw.View.FlightView.FlightViewTUI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FlightViewTUITest {

    GameInformation gameInformation;
    DataContainer dataContainer;
    GeneralView generalViewTUI;
    FlightView flightView;
    Random randomizer;


    @BeforeEach
    void setup() {
        randomizer = new Random();
        gameInformation = new GameInformation();
        gameInformation.setUpGameInformation(GameType.NORMALGAME, 4);

        for (Player player : gameInformation.getPlayerList()) {

            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 12; j++) {
                    System.out.print(player.getShipBoard().getMatr()[i][j] + "");
                }
            }
            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 12; j++) {
                    System.out.print(player.getShipBoard().getComponentMatrix()[i][j] + "");
                }
            }
        }
        dataContainer = new DataContainer();
        // abstract class cannot be instantiated
        generalViewTUI = new EvaluationViewTUI();
        flightView = new FlightViewTUI();
    }

    @Test
    public void printFlightBoardTest() {
        FlightBoard flightBoard = new FlightBoard(GameType.NORMALGAME, gameInformation.getCardsList());

        Player player1 = new Player("Player1", Color.RED, gameInformation);
        Player player2 = new Player("Player2", Color.BLUE, gameInformation);
        Player player3 = new Player("Player3", Color.GREEN, gameInformation);
        Player player4 = new Player("Player4", Color.YELLOW, gameInformation);

        gameInformation.addPlayers(player1);
        gameInformation.addPlayers(player2);
        gameInformation.addPlayers(player3);
        gameInformation.addPlayers(player4);

        flightBoard.addPlayer(player1, 7);
        flightBoard.addPlayer(player2, 4);
        flightBoard.addPlayer(player3, 2);
        flightBoard.addPlayer(player4, 1);

        dataContainer.setFlightBoard(flightBoard);
        assertNotNull(dataContainer);
        flightView.printFlightBoard(dataContainer);
    }
}
