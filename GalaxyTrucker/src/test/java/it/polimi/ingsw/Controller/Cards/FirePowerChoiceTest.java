package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Model.Components.Battery;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.Components.SideType;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Color;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.View.FlightView.FlightView;
import it.polimi.ingsw.View.FlightView.FlightViewTUI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author carlo
 */

class FirePowerChoiceTest {

    class Operator implements FirePowerChoice {
    }

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
    void noDoubleCannonsAvailable() {

        player.getShipBoard().getShipBoardAttributes().updateFirePower(5);
        player.getShipBoard().getShipBoardAttributes().updateBatteryPower(1);

        flightView = new FlightViewTUI();

        assertTrue(operator.chooseFirePower(player, gameInformation) == 5);

    }

    @Test
    void noBatteriesAvailable() {

        player.getShipBoard().getShipBoardAttributes().updateFirePower(2);
        player.getShipBoard().getShipBoardAttributes().updateNumberForwardDoubleCannons(3);

        flightView = new FlightViewTUI();

        assertTrue(operator.chooseFirePower(player, gameInformation) == 2);

    }

    @Test
    void playerRefuseToUseDoubleCannons() {

        player.getShipBoard().getShipBoardAttributes().updateFirePower(1);
        player.getShipBoard().getShipBoardAttributes().updateNumberForwardDoubleCannons(2);
        player.getShipBoard().getShipBoardAttributes().updateBatteryPower(3);

        String inputString = "false\n";
        ByteArrayInputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);

        flightView = new FlightViewTUI();

        assertTrue(operator.chooseFirePower(player, gameInformation) == 1);
    }

    @Test
    void threeDoubleCannonsActivated() {

        player.getShipBoard().getShipBoardAttributes().updateFirePower(1);
        player.getShipBoard().getShipBoardAttributes().updateNumberForwardDoubleCannons(2);
        player.getShipBoard().getShipBoardAttributes().updateNumberNotForwardDoubleCannons(1);
        player.getShipBoard().getShipBoardAttributes().updateBatteryPower(3);

        player.getShipBoard().addComponent(new Battery(new SideType[]{SideType.Single, SideType.Double, SideType.Universal, SideType.Double}, 3), 6, 6);

        String inputString = "true\n3\n5\n5\n5\n5\n5\n5\n";
        ByteArrayInputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);

        flightView = new FlightViewTUI();

        assertTrue(operator.chooseFirePower(player, gameInformation) == 6);

    }

    @Test
    void threeDoubleCannonsActivatedWithIncorrectValues() {

        player.getShipBoard().getShipBoardAttributes().updateFirePower(1);
        player.getShipBoard().getShipBoardAttributes().updateNumberForwardDoubleCannons(2);
        player.getShipBoard().getShipBoardAttributes().updateNumberNotForwardDoubleCannons(1);
        player.getShipBoard().getShipBoardAttributes().updateBatteryPower(3);

        player.getShipBoard().addComponent(new Battery(new SideType[]{SideType.Single, SideType.Double, SideType.Universal, SideType.Double}, 3), 6, 6);
        player.getShipBoard().addComponent(new Component(new SideType[]{SideType.Double, SideType.Universal, SideType.Double, SideType.Single}), 4, 4);

        String inputString = "true\n0\n4\n3\n3 3\n5 5\n3 3\n3 3\n5 5\n5 5\n";
        ByteArrayInputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);

        flightView = new FlightViewTUI();

        assertTrue(operator.chooseFirePower(player, gameInformation) == 6);

    }


}