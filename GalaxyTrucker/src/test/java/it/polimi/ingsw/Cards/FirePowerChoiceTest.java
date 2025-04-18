package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Application.FlightPhase.FlightView;
import it.polimi.ingsw.Application.GameInformation;
import it.polimi.ingsw.Application.GameType;
import it.polimi.ingsw.Components.Battery;
import it.polimi.ingsw.Components.Component;
import it.polimi.ingsw.Components.SideType;
import it.polimi.ingsw.Shipboard.Color;
import it.polimi.ingsw.Shipboard.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author carlo
 */

class FirePowerChoiceTest {

    class Operator implements FirePowerChoice{}

    Operator operator = new Operator();

    GameInformation gameInformation;

    Player player;

    FlightView flightView;

    @BeforeEach
    void initialize(){

        gameInformation = new GameInformation();
        gameInformation.setGameType(GameType.TestGame);

        player = new Player("player", Color.BLUE, gameInformation);
    }

    @AfterEach
    void reestablishStdin(){
        System.setIn(System.in);
    }


    @Test
    void noDoubleCannonsAvailable(){

       player.getShipBoard().getShipBoardAttributes().updateFirePower(5);
       player.getShipBoard().getShipBoardAttributes().updateBatteryPower(1);

       flightView = new FlightView(gameInformation);

       assertTrue(operator.chooseFirePower(player, flightView) == 5);

    }

    @Test
    void noBatteriesAvailable(){

        player.getShipBoard().getShipBoardAttributes().updateFirePower(2);
        player.getShipBoard().getShipBoardAttributes().updateNumberForwardDoubleCannons(3);

        flightView = new FlightView(gameInformation);

        assertTrue(operator.chooseFirePower(player, flightView) == 2);

    }

    @Test
    void playerRefuseToUseDoubleCannons(){

        player.getShipBoard().getShipBoardAttributes().updateFirePower(1);
        player.getShipBoard().getShipBoardAttributes().updateNumberForwardDoubleCannons(2);
        player.getShipBoard().getShipBoardAttributes().updateBatteryPower(3);

        String inputString = "false\n";
        ByteArrayInputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);

        flightView = new FlightView(gameInformation);

        assertTrue(operator.chooseFirePower(player, flightView) == 1);
    }

    @Test
    void threeDoubleCannonsActivated(){

        player.getShipBoard().getShipBoardAttributes().updateFirePower(1);
        player.getShipBoard().getShipBoardAttributes().updateNumberForwardDoubleCannons(2);
        player.getShipBoard().getShipBoardAttributes().updateNumberNotForwardDoubleCannons(1);
        player.getShipBoard().getShipBoardAttributes().updateBatteryPower(3);

        player.getShipBoard().addComponent(new Battery(new SideType[]{SideType.Single, SideType.Double, SideType.Universal, SideType.Double}, 3), 6, 6);

        String inputString = "true\n3\n5\n5\n5\n5\n5\n5\n";
        ByteArrayInputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);

        flightView = new FlightView(gameInformation);

        assertTrue(operator.chooseFirePower(player, flightView) == 6);

    }

    @Test
    void threeDoubleCannonsActivatedWithIncorrectValues(){

        player.getShipBoard().getShipBoardAttributes().updateFirePower(1);
        player.getShipBoard().getShipBoardAttributes().updateNumberForwardDoubleCannons(2);
        player.getShipBoard().getShipBoardAttributes().updateNumberNotForwardDoubleCannons(1);
        player.getShipBoard().getShipBoardAttributes().updateBatteryPower(3);

        player.getShipBoard().addComponent(new Battery(new SideType[]{SideType.Single, SideType.Double, SideType.Universal, SideType.Double}, 3), 6, 6);
        player.getShipBoard().addComponent(new Component(new SideType[]{SideType.Double, SideType.Universal, SideType.Double, SideType.Single}), 4, 4);

        String inputString = "true\n0\n4\n3\n3 3\n5 5\n3 3\n3 3\n5 5\n5 5\n";
        ByteArrayInputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);

        flightView = new FlightView(gameInformation);

        assertTrue(operator.chooseFirePower(player, flightView) == 6);

    }


}