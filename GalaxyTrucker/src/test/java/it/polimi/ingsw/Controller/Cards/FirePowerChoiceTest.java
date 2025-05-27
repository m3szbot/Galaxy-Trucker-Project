package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Controller.AssemblyPhase.NotPermittedPlacementException;
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
        gameInformation.setGameType(GameType.TESTGAME);

        player = new Player("player", Color.BLUE, gameInformation);
    }

    @AfterEach
    void reestablishStdin() {
        System.setIn(System.in);
    }


    @Test
    void playerRefuseToUseDoubleCannons() {

        String inputString = "false\n";
        ByteArrayInputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);

        flightView = new FlightViewTUI();

        assertTrue(operator.chooseFirePower(player, gameInformation) == 1);
    }

    @Test
    void threeDoubleCannonsActivated() throws NotPermittedPlacementException {


        player.getShipBoard().addComponent(new Battery(new SideType[]{SideType.Single, SideType.Double, SideType.Universal, SideType.Double}, 3), 6, 6);

        String inputString = "true\n3\n5\n5\n5\n5\n5\n5\n";
        ByteArrayInputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);

        flightView = new FlightViewTUI();

        assertTrue(operator.chooseFirePower(player, gameInformation) == 6);

    }

    @Test
    void threeDoubleCannonsActivatedWithIncorrectValues() throws NotPermittedPlacementException {
        

        player.getShipBoard().addComponent(new Battery(new SideType[]{SideType.Single, SideType.Double, SideType.Universal, SideType.Double}, 3), 6, 6);
        player.getShipBoard().addComponent(new Component(new SideType[]{SideType.Double, SideType.Universal, SideType.Double, SideType.Single}), 4, 4);

        String inputString = "true\n0\n4\n3\n3 3\n5 5\n3 3\n3 3\n5 5\n5 5\n";
        ByteArrayInputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);

        flightView = new FlightViewTUI();

        assertTrue(operator.chooseFirePower(player, gameInformation) == 6);

    }


}