package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Controller.AssemblyPhase.NotPermittedPlacementException;
import it.polimi.ingsw.Model.Components.*;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
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
import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SufferBlowsTest {
    class Operator implements SufferBlows {
    }

    ;

    Operator operator = new Operator();

    GameInformation gameInformation;
    Player player;
    FlightView flightView;
    FlightBoard flightBoard;

    @BeforeEach
    void initialize() {

        gameInformation = new GameInformation();
        gameInformation.setGameType(GameType.TESTGAME);

        CardBuilder cardBuilder = new CardBuilder();
        cardBuilder.buildCardName("Epidemic").buildCardLevel(1);

        ArrayList<Card> cardsList = new ArrayList<>();

        //8 is the size of a test game deck

        for (int i = 0; i < 8; i++) {

            cardsList.add(new Epidemic(cardBuilder));

        }

        flightBoard = new FlightBoard(GameType.TESTGAME, cardsList);

        player = new Player("player", Color.BLUE, gameInformation);
    }

    @AfterEach
    void reestablishStdin() {
        System.setIn(System.in);
    }

    @Test
    void blowDodgedVertically() throws NoSuchFieldException, IllegalAccessException, NotPermittedPlacementException {

        player.getShipBoard().addComponent(new Component(), 5, 5);
        player.getShipBoard().addComponent(new Component(), 4, 4);

        //creating 2 blows and setting the private field

        Blow blows[] = new Blow[2];

        blows[0] = new Blow(0, true);
        blows[1] = new Blow(2, false);

        Field rollField = Blow.class.getDeclaredField("roll");
        rollField.setAccessible(true);
        rollField.setInt(blows[0], 8);
        rollField.setInt(blows[1], 9);

        flightView = new FlightViewTUI();

        operator.hit(player, blows, ElementType.CannonBlow, gameInformation);

        assertNotNull(player.getShipBoard().getComponent(4, 4));
        assertNotNull(player.getShipBoard().getComponent(3, 3));

    }

    @Test
    void blowDodgedHorizontally() throws NoSuchFieldException, IllegalAccessException, NotPermittedPlacementException {

        player.getShipBoard().addComponent(new Component(), 5, 5);
        player.getShipBoard().addComponent(new Component(), 4, 4);

        //creating 2 blows and setting the private field

        Blow blows[] = new Blow[2];

        blows[0] = new Blow(1, true);
        blows[1] = new Blow(3, false);

        Field rollField = Blow.class.getDeclaredField("roll");
        rollField.setAccessible(true);
        rollField.setInt(blows[0], 8);
        rollField.setInt(blows[1], 9);

        flightView = new FlightViewTUI();

        operator.hit(player, blows, ElementType.CannonBlow, gameInformation);

        assertNotNull(player.getShipBoard().getComponent(4, 4));
        assertNotNull(player.getShipBoard().getComponent(3, 3));

    }

    @Test
    void bigCannonBlowHit() throws NoSuchFieldException, IllegalAccessException, NotPermittedPlacementException {

        player.getShipBoard().addComponent(new Storage(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}, true, 5), 6, 7);
        player.getShipBoard().addComponent(new Battery(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}, 3), 5, 7);
        player.getShipBoard().addComponent(new Cabin(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}), 4, 7);

        ((Storage) player.getShipBoard().getComponent(5, 6)).addGoods(new int[]{3, 1, 1, 0});


        Blow blows[] = new Blow[3];

        blows[0] = new Blow(0, true);
        blows[1] = new Blow(0, true);
        blows[2] = new Blow(0, true);

        Field rollField = Blow.class.getDeclaredField("roll");
        rollField.setAccessible(true);
        rollField.setInt(blows[0], 3); //targets cabin
        rollField.setInt(blows[1], 4); //targets battery
        rollField.setInt(blows[2], 5); //targets storage

        flightView = new FlightViewTUI();

        operator.hit(player, blows, ElementType.CannonBlow, gameInformation);

        assertNull(player.getShipBoard().getComponent(6, 5));
        assertNull(player.getShipBoard().getComponent(6, 4));
        assertNull(player.getShipBoard().getComponent(6, 3));

        assertEquals(2, player.getShipBoard().getShipBoardAttributes().getCrewMembers()); //only the main cabin crew remains
        assertEquals(3, player.getShipBoard().getShipBoardAttributes().getDestroyedComponents());
        assertEquals(0, player.getShipBoard().getShipBoardAttributes().getGoods()[0]);
        assertEquals(0, player.getShipBoard().getShipBoardAttributes().getGoods()[1]);
        assertEquals(0, player.getShipBoard().getShipBoardAttributes().getGoods()[2]);
        assertEquals(0, player.getShipBoard().getShipBoardAttributes().getGoods()[3]);

    }

    @Test
    void smallCannonBlowHitTarget() throws NoSuchFieldException, IllegalAccessException, NotPermittedPlacementException {

        player.getShipBoard().addComponent(new Component(new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal}), 5, 7); //target component
        player.getShipBoard().addComponent(new Component(new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal}), 6, 7); //target component
        player.getShipBoard().addComponent(new Shield(new SideType[]{SideType.Special, SideType.Universal, SideType.Universal, SideType.Universal}), 8, 7);


        Blow blows[] = new Blow[2];

        blows[0] = new Blow(0, false);
        blows[1] = new Blow(0, false);

        Field rollField = Blow.class.getDeclaredField("roll");
        rollField.setAccessible(true);
        rollField.setInt(blows[0], 4);
        rollField.setInt(blows[1], 5);

        String inputString = "false\nfalse\n";
        ByteArrayInputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);

        flightView = new FlightViewTUI();

        operator.hit(player, blows, ElementType.CannonBlow, gameInformation);

        assertNull(player.getShipBoard().getComponent(5, 7));
        assertNull(player.getShipBoard().getComponent(6, 7));
        assertNotNull(player.getShipBoard().getComponent(7, 6));
        assertNotNull(player.getShipBoard().getComponent(6, 6));
        assertEquals(2, player.getShipBoard().getShipBoardAttributes().getDestroyedComponents());

    }

    @Test
    void smallCannonBlowHitShield() throws NoSuchFieldException, IllegalAccessException, NotPermittedPlacementException {

        player.getShipBoard().addComponent(new Component(), 6, 6); //target component
        player.getShipBoard().addComponent(new Shield(new SideType[]{SideType.Special, SideType.Special, SideType.Single, SideType.Single}), 5, 5);
        player.getShipBoard().addComponent(new Battery(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}, 3), 4, 4);


        Blow blow = new Blow(0, false);

        Field rollField = Blow.class.getDeclaredField("roll");
        rollField.setAccessible(true);
        rollField.setInt(blow, 5);


        String inputString = "true\n5 5\n3 3\n";
        ByteArrayInputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);

        flightView = new FlightViewTUI();

        operator.hit(player, new Blow[]{blow}, ElementType.CannonBlow, gameInformation);

        assertEquals(2, player.getShipBoard().getShipBoardAttributes().getRemainingBatteries());
        assertEquals(2, ((Battery) player.getShipBoard().getComponent(3, 3)).getBatteryPower());
        assertNotNull(player.getShipBoard().getComponent(5, 5));
        assertEquals(0, player.getShipBoard().getShipBoardAttributes().getDestroyedComponents());

    }

    @Test
    void bigMeteorBlowHitTarget() throws NoSuchFieldException, IllegalAccessException, NotPermittedPlacementException {

        player.getShipBoard().addComponent(new Storage(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}, true, 5), 4, 7);
        player.getShipBoard().addComponent(new Battery(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}, 3), 5, 7);
        player.getShipBoard().addComponent(new Cabin(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}), 6, 7);

        ((Storage) player.getShipBoard().getComponent(3, 6)).addGoods(new int[]{3, 1, 1, 0});


        Blow blows[] = new Blow[3];

        blows[0] = new Blow(3, true);
        blows[1] = new Blow(3, true);
        blows[2] = new Blow(3, true);

        Field rollField = Blow.class.getDeclaredField("roll");
        rollField.setAccessible(true);
        rollField.setInt(blows[0], 6);
        rollField.setInt(blows[1], 6);
        rollField.setInt(blows[2], 6);

        flightView = new FlightViewTUI();

        operator.hit(player, blows, ElementType.Meteorite, gameInformation);

        assertNull(player.getShipBoard().getComponent(3, 6));
        assertNull(player.getShipBoard().getComponent(4, 6));
        assertNull(player.getShipBoard().getComponent(5, 6));
        assertNotNull(player.getShipBoard().getComponent(6, 6));
        assertEquals(0, player.getShipBoard().getShipBoardAttributes().getRemainingBatteries());
        assertEquals(0, player.getShipBoard().getShipBoardAttributes().getRemainingRedSlots());
        assertEquals(2, player.getShipBoard().getShipBoardAttributes().getCrewMembers());
        assertEquals(3, player.getShipBoard().getShipBoardAttributes().getDestroyedComponents());

    }

    @Test
    void bigMeteorBlowWithCannonNotActivated() throws NoSuchFieldException, IllegalAccessException, NotPermittedPlacementException {

        player.getShipBoard().addComponent(new Component(new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal}), 6, 7);
        player.getShipBoard().addComponent(new Component(new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal}), 7, 6);
        player.getShipBoard().addComponent(new Component(new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal}), 8, 8);

        //front pointing cannon
        player.getShipBoard().addComponent(new Cannon(new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Special}, false), 7, 8);

        //right pointing cannon
        player.getShipBoard().addComponent(new Cannon(new SideType[]{SideType.Special, SideType.Universal, SideType.Universal, SideType.Universal}, false), 8, 9);

        //back pointing cannon
        player.getShipBoard().addComponent(new Cannon(new SideType[]{SideType.Universal, SideType.Universal, SideType.Special, SideType.Universal}, false), 8, 7);

        //left pointing cannon
        player.getShipBoard().addComponent(new Cannon(new SideType[]{SideType.Universal, SideType.Special, SideType.Universal, SideType.Universal}, false), 9, 7);

        //fictitious battery

        Blow blows[] = new Blow[4];

        blows[0] = new Blow(0, true);
        blows[1] = new Blow(1, true);
        blows[2] = new Blow(2, true);
        blows[3] = new Blow(3, true);

        Field rollField = Blow.class.getDeclaredField("roll");
        rollField.setAccessible(true);
        rollField.setInt(blows[0], 7);
        rollField.setInt(blows[1], 7);
        rollField.setInt(blows[2], 6);
        rollField.setInt(blows[3], 6);

        String inputString = "false\nfalse\nfalse\nfalse\n";
        ByteArrayInputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);

        flightView = new FlightViewTUI();

        operator.hit(player, blows, ElementType.Meteorite, gameInformation);

        System.out.println("Components destroyed: " + player.getShipBoard().getShipBoardAttributes().getDestroyedComponents());

        assertNull(player.getShipBoard().getComponent(5, 6));
        assertNull(player.getShipBoard().getComponent(6, 5));
        assertNull(player.getShipBoard().getComponent(7, 7));
        assertNull(player.getShipBoard().getComponent(7, 8));
        assertNotNull(player.getShipBoard().getComponent(6, 6));
        assertNotNull(player.getShipBoard().getComponent(6, 7));
        assertNotNull(player.getShipBoard().getComponent(7, 6));
        assertNotNull(player.getShipBoard().getComponent(8, 6));


        assertEquals(1, player.getShipBoard().getShipBoardAttributes().getRemainingBatteries());
        assertEquals(4, player.getShipBoard().getShipBoardAttributes().getDestroyedComponents());
    }

    @Test
    void bigMeteorBlowWithCannonActivated() throws NoSuchFieldException, IllegalAccessException, NotPermittedPlacementException {

        player.getShipBoard().addComponent(new Cannon(new SideType[]{SideType.Special, SideType.Universal, SideType.Universal, SideType.Universal}, true), 7, 8);
        player.getShipBoard().addComponent(new Battery(new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Special}, 1), 8, 7);
        player.getShipBoard().addComponent(new Cannon(new SideType[]{SideType.Universal, SideType.Special, SideType.Universal, SideType.Universal}, false), 8, 6);

        Blow blows[] = new Blow[2];

        blows[0] = new Blow(0, true);
        blows[1] = new Blow(1, true);

        Field rollField = Blow.class.getDeclaredField("roll");
        rollField.setAccessible(true);
        rollField.setInt(blows[0], 6);
        rollField.setInt(blows[1], 6);

        String inputString = "true\n1 1\n7 6\n";
        ByteArrayInputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);

        flightView = new FlightViewTUI();

        operator.hit(player, blows, ElementType.Meteorite, gameInformation);

        assertNotNull(player.getShipBoard().getComponent(6, 6));
        assertNotNull(player.getShipBoard().getComponent(6, 7));
        assertNotNull(player.getShipBoard().getComponent(7, 6));
        assertNotNull(player.getShipBoard().getComponent(7, 5));

        assertEquals(0, player.getShipBoard().getShipBoardAttributes().getRemainingBatteries());

    }

    @Test
    void smallMeteorBlowHitSmoothSide() throws NoSuchFieldException, IllegalAccessException, NotPermittedPlacementException {

        //target component
        player.getShipBoard().addComponent(new Component(new SideType[]{SideType.Smooth, SideType.Smooth, SideType.Smooth, SideType.Smooth}), 6, 6);

        Blow blows[] = new Blow[4];

        blows[0] = new Blow(0, false);
        blows[1] = new Blow(1, false);
        blows[2] = new Blow(2, false);
        blows[3] = new Blow(3, false);

        Field rollField = Blow.class.getDeclaredField("roll");
        rollField.setAccessible(true);
        rollField.setInt(blows[0], 5);
        rollField.setInt(blows[1], 5);
        rollField.setInt(blows[2], 5);
        rollField.setInt(blows[3], 5);

        flightView = new FlightViewTUI();

        operator.hit(player, blows, ElementType.Meteorite, gameInformation);

        assertNotNull(player.getShipBoard().getComponent(5, 5));

    }

    @Test
    void smallMeteorBlowWithShieldActivated() throws NoSuchFieldException, IllegalAccessException, NotPermittedPlacementException {

        //target component
        player.getShipBoard().addComponent(new Battery(new SideType[]{SideType.Single, SideType.Single, SideType.Single, SideType.Single}, 4), 6, 6);
        player.getShipBoard().addComponent(new Shield(new SideType[]{SideType.Special, SideType.Special, SideType.Single, SideType.Single}), 2, 2);
        player.getShipBoard().addComponent(new Shield(new SideType[]{SideType.Single, SideType.Single, SideType.Special, SideType.Special}), 1, 1);

        Blow blows[] = new Blow[4];

        blows[0] = new Blow(0, false);
        blows[1] = new Blow(1, false);
        blows[2] = new Blow(2, false);
        blows[3] = new Blow(3, false);

        Field rollField = Blow.class.getDeclaredField("roll");
        rollField.setAccessible(true);
        rollField.setInt(blows[0], 5);
        rollField.setInt(blows[1], 5);
        rollField.setInt(blows[2], 5);
        rollField.setInt(blows[3], 5);

        String inputString = "true\n5 5\ntrue\n4 4\n5 5\ntrue\n5 5\ntrue\n5 5\n";
        ByteArrayInputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);

        flightView = new FlightViewTUI();

        operator.hit(player, blows, ElementType.Meteorite, gameInformation);

        assertNotNull(player.getShipBoard().getComponent(5, 5));
        assertEquals(0, player.getShipBoard().getShipBoardAttributes().getRemainingBatteries());
        assertEquals(0, ((Battery) player.getShipBoard().getComponent(5, 5)).getBatteryPower());

    }

    @Test
    void smallMeteorBlowHit() throws NoSuchFieldException, IllegalAccessException, NotPermittedPlacementException {

        player.getShipBoard().addComponent(new Storage(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}, true, 5), 4, 7);
        player.getShipBoard().addComponent(new Battery(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}, 3), 5, 7);
        player.getShipBoard().addComponent(new Cabin(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}), 6, 7);

        ((Storage) player.getShipBoard().getComponent(3, 6)).addGoods(new int[]{3, 1, 1, 0});


        Blow blows[] = new Blow[3];

        blows[0] = new Blow(3, false);
        blows[1] = new Blow(3, false);
        blows[2] = new Blow(3, false);

        Field rollField = Blow.class.getDeclaredField("roll");
        rollField.setAccessible(true);
        rollField.setInt(blows[0], 6);
        rollField.setInt(blows[1], 6);
        rollField.setInt(blows[2], 6);

        flightView = new FlightViewTUI();

        operator.hit(player, blows, ElementType.Meteorite, gameInformation);

        assertNull(player.getShipBoard().getComponent(3, 6));
        assertNull(player.getShipBoard().getComponent(4, 6));
        assertNull(player.getShipBoard().getComponent(5, 6));
        assertNotNull(player.getShipBoard().getComponent(6, 6));
        assertEquals(0, player.getShipBoard().getShipBoardAttributes().getRemainingBatteries());
        assertEquals(0, player.getShipBoard().getShipBoardAttributes().getRemainingRedSlots());
        assertEquals(2, player.getShipBoard().getShipBoardAttributes().getCrewMembers());
        assertEquals(3, player.getShipBoard().getShipBoardAttributes().getDestroyedComponents());
    }

}