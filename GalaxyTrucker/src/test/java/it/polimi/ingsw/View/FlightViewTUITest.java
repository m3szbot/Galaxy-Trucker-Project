package it.polimi.ingsw.View;

import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.View.EvaluationView.EvaluationViewTUI;
import it.polimi.ingsw.View.FlightView.FlightView;
import it.polimi.ingsw.View.FlightView.FlightViewTUI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

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
        dataContainer = new DataContainer();
        // abstract class cannot be instantiated
        generalViewTUI = new EvaluationViewTUI();
        flightView = new FlightViewTUI();
    }

    @Test
    public void printFlightBoardTest() {
        FlightBoard flightBoard = new FlightBoard(GameType.NORMALGAME, gameInformation.getCardsList());

        for (Player player : gameInformation.getPlayerList()) {
            flightBoard.getPlayerOrderList().add(player);
        }
        dataContainer.setFlightBoard(flightBoard);
        flightView.printFlightBoard(dataContainer);
    }
}
