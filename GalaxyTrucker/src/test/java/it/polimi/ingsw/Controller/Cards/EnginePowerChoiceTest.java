package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Model.Components.Battery;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.Components.SideType;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Color;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.View.FlightView.FlightView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author carlo
 */

class EnginePowerChoiceTest {

    class Operator implements EnginePowerChoice {
    }

    ;

    Operator operator = new Operator();

    GameInformation gameInformation;

    Player player;

    FlightView flightView;

    @BeforeEach
    void initialize() {

        gameInformation = new GameInformation();
        gameInformation.setGameType(GameType.TestGame);

        player = new Player("player", Color.BLUE, gameInformation);
    }

    @AfterEach
    void reestablishStdin() {
        System.setIn(System.in);
    }

    @Test
    void noDoubleEngines() {

        player.getShipBoard().getShipBoardAttributes().updateDrivingPower(5);
        player.getShipBoard().getShipBoardAttributes().updateBatteryPower(4);

        assertTrue(operator.chooseEnginePower(player, flightView) == 5);

    }

    @Test
    void noBatteriesAvailable() {

        player.getShipBoard().getShipBoardAttributes().updateDrivingPower(5);
        player.getShipBoard().getShipBoardAttributes().updateNumberDoubleEngines(1);

        assertTrue(operator.chooseEnginePower(player, flightView) == 5);

    }

    @Test
    void playerRefuseToUseDoubleEngines() {

        player.getShipBoard().getShipBoardAttributes().updateDrivingPower(3);
        player.getShipBoard().getShipBoardAttributes().updateNumberDoubleEngines(1);
        player.getShipBoard().getShipBoardAttributes().updateBatteryPower(4);

        String playerResponse = "false";
        ByteArrayInputStream in = new ByteArrayInputStream(playerResponse.getBytes());
        System.setIn(in);

        flightView = new FlightView(gameInformation);

        assertTrue(operator.chooseEnginePower(player, flightView) == 3);

    }

    @Test
    void twoDoubleEnginesActivated() {

        player.getShipBoard().getShipBoardAttributes().updateDrivingPower(1);
        player.getShipBoard().getShipBoardAttributes().updateNumberDoubleEngines(2);
        player.getShipBoard().addComponent(new Battery(new SideType[]{SideType.Single, SideType.Double, SideType.Universal, SideType.Double}, 2), 6, 6);

        String playerResponses = "true\n2\n5 5\n5 5\n";
        ByteArrayInputStream in = new ByteArrayInputStream(playerResponses.getBytes());
        System.setIn(in);

        flightView = new FlightView(gameInformation);

        assertTrue(operator.chooseEnginePower(player, flightView) == 5);
    }

    @Test
    void threeEnginesActivatedWithIncorrectValues() {

        player.getShipBoard().getShipBoardAttributes().updateDrivingPower(5);
        player.getShipBoard().getShipBoardAttributes().updateNumberDoubleEngines(4);
        player.getShipBoard().addComponent(new Battery(new SideType[]{SideType.Double, SideType.Universal, SideType.Single, SideType.Double}, 3), 6, 6);
        player.getShipBoard().addComponent(new Component(new SideType[]{SideType.Double, SideType.Universal, SideType.Double, SideType.Single}), 4, 4);

        String playerResponses = "true\n0\n4\n5\n3\n5 5\n3 3\n5 5\n3 3\n5 5\n";
        ByteArrayInputStream in = new ByteArrayInputStream(playerResponses.getBytes());
        System.setIn(in);

        flightView = new FlightView(gameInformation);

        assertTrue(operator.chooseEnginePower(player, flightView) == 11);

    }

}