package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Model.Components.Battery;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.Components.SideType;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Color;
import it.polimi.ingsw.Model.ShipBoard.NotPermittedPlacementException;
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
        gameInformation.setGameType(GameType.TESTGAME);

        player = new Player("player", Color.BLUE, gameInformation);
    }

    @AfterEach
    void reestablishStdin() {
        System.setIn(System.in);
    }


    @Test
    void threeEnginesActivatedWithIncorrectValues() throws NotPermittedPlacementException {

        player.getShipBoard().addComponent(new Battery(new SideType[]{SideType.Double, SideType.Universal, SideType.Single, SideType.Double}, 3), 6, 6);
        player.getShipBoard().addComponent(new Component(new SideType[]{SideType.Double, SideType.Universal, SideType.Double, SideType.Single}), 4, 4);

        String playerResponses = "true\n0\n4\n5\n3\n5 5\n3 3\n5 5\n3 3\n5 5\n";
        ByteArrayInputStream in = new ByteArrayInputStream(playerResponses.getBytes());
        System.setIn(in);

        flightView = new FlightViewTUI();

        assertTrue(operator.chooseEnginePower(player, gameInformation) == 11);

    }

}