package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Application.FlightPhase.FlightView;
import it.polimi.ingsw.Application.GameInformation;
import it.polimi.ingsw.Application.GameType;
import it.polimi.ingsw.Components.*;
import it.polimi.ingsw.FlightBoard.FlightBoard;
import it.polimi.ingsw.Shipboard.Color;
import it.polimi.ingsw.Shipboard.Player;
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
        gameInformation.setGameType(GameType.TestGame);

        CardBuilder cardBuilder = new CardBuilder();
        cardBuilder.buildCardName("Epidemic").buildCardLevel(1);

        ArrayList<Card> cardsList = new ArrayList<>();

        //8 is the size of a test game deck

        for (int i = 0; i < 8; i++) {

            cardsList.add(new Epidemic(cardBuilder));

        }

        flightBoard = new FlightBoard(GameType.TestGame, cardsList);

        player = new Player("player", Color.BLUE, gameInformation);
    }

    @AfterEach
    void reestablishStdin() {
        System.setIn(System.in);
    }

    @Test
    void blowDodgedVertically() throws NoSuchFieldException, IllegalAccessException {

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

        flightView = new FlightView(gameInformation);

        operator.hit(player, blows, ElementType.CannonBlow, flightBoard, flightView);

        assertNotNull(player.getShipBoard().getComponent(4, 4));
        assertNotNull(player.getShipBoard().getComponent(3, 3));

    }

    @Test
    void blowDodgedHorizontally() throws NoSuchFieldException, IllegalAccessException {

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

        flightView = new FlightView(gameInformation);

        operator.hit(player, blows, ElementType.CannonBlow, flightBoard, flightView);

        assertNotNull(player.getShipBoard().getComponent(4, 4));
        assertNotNull(player.getShipBoard().getComponent(3, 3));

    }

    @Test
    void bigCannonBlowHit() throws NoSuchFieldException, IllegalAccessException {

        player.getShipBoard().addComponent(new Storage(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}, true, 5), 6, 6);
        player.getShipBoard().addComponent(new Battery(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}, 3), 5, 5);
        player.getShipBoard().addComponent(new Cabin(), 4, 4);
        ((Cabin) player.getShipBoard().getComponent(3, 3)).setCrewType(CrewType.Human);

        player.getShipBoard().getShipBoardAttributes().updateGoods(new int[]{3, 1, 1, 0});
        player.getShipBoard().getShipBoardAttributes().updateAvailableSlots(1, 3);
        player.getShipBoard().getShipBoardAttributes().updateAvailableSlots(0, 2);

        //Av.RedSlots = 0


        Blow blows[] = new Blow[3];

        blows[0] = new Blow(1, true);
        blows[1] = new Blow(3, true);
        blows[2] = new Blow(0, true);

        Field rollField = Blow.class.getDeclaredField("roll");
        rollField.setAccessible(true);
        rollField.setInt(blows[0], 5); //targets storage
        rollField.setInt(blows[1], 4); //targets battery
        rollField.setInt(blows[2], 3); //targets carbin

        flightView = new FlightView(gameInformation);

        operator.hit(player, blows, ElementType.CannonBlow, flightBoard, flightView);

        assertNull(player.getShipBoard().getComponent(5, 5));
        assertNull(player.getShipBoard().getComponent(4, 4));
        assertNull(player.getShipBoard().getComponent(3, 3));
        assertEquals(0, player.getShipBoard().getShipBoardAttributes().getBatteryPower());
        assertEquals(5, player.getShipBoard().getShipBoardAttributes().getAvailableRedSlots());
        assertEquals(0, player.getShipBoard().getShipBoardAttributes().getCrewMembers());
        assertEquals(3, player.getShipBoard().getShipBoardAttributes().getDestroyedComponents());
        assertEquals(new int[]{0, 0, 0, 0}, player.getShipBoard().getShipBoardAttributes().getGoods());


    }

    @Test
    void smallCannonBlowHitTarget() throws NoSuchFieldException, IllegalAccessException {

        player.getShipBoard().addComponent(new Component(), 6, 6); //target component
        player.getShipBoard().addComponent(new Component(), 5, 5); //target component
        player.getShipBoard().addComponent(new Shield(new SideType[]{SideType.Special, SideType.Special, SideType.Single, SideType.Single}), 4, 4);

        player.getShipBoard().getShipBoardAttributes().updateBatteryPower(1);

        Blow blows[] = new Blow[2];

        blows[0] = new Blow(3, false);
        blows[1] = new Blow(0, false);

        Field rollField = Blow.class.getDeclaredField("roll");
        rollField.setAccessible(true);
        rollField.setInt(blows[0], 5);
        rollField.setInt(blows[1], 4);

        String inputString = "false\n";
        ByteArrayInputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);

        flightView = new FlightView(gameInformation);

        operator.hit(player, blows, ElementType.CannonBlow, flightBoard, flightView);

        assertNull(player.getShipBoard().getComponent(5, 5));
        assertNull(player.getShipBoard().getComponent(4, 4));
        assertEquals(2, player.getShipBoard().getShipBoardAttributes().getDestroyedComponents());

    }

    @Test
    void smallCannonBlowHitShield() throws NoSuchFieldException, IllegalAccessException {

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

        flightView = new FlightView(gameInformation);

        operator.hit(player, new Blow[]{blow}, ElementType.CannonBlow, flightBoard, flightView);

        assertEquals(2, player.getShipBoard().getShipBoardAttributes().getBatteryPower());
        assertEquals(2, ((Battery) player.getShipBoard().getComponent(3, 3)).getBatteryPower());
        assertNotNull(player.getShipBoard().getComponent(5, 5));
        assertEquals(0, player.getShipBoard().getShipBoardAttributes().getDestroyedComponents());

    }

    @Test
    void bigMeteorBlowHitTarget() throws NoSuchFieldException, IllegalAccessException {

        player.getShipBoard().addComponent(new Storage(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}, true, 5), 6, 6);
        player.getShipBoard().addComponent(new Battery(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}, 3), 5, 5);
        player.getShipBoard().addComponent(new Cabin(), 4, 4);
        ((Cabin) player.getShipBoard().getComponent(3, 3)).setCrewType(CrewType.Human);

        player.getShipBoard().getShipBoardAttributes().updateGoods(new int[]{3, 1, 1, 0});
        player.getShipBoard().getShipBoardAttributes().updateAvailableSlots(1, 3);
        player.getShipBoard().getShipBoardAttributes().updateAvailableSlots(0, 2);

        //Av.RedSlots = 0


        Blow blows[] = new Blow[3];

        blows[0] = new Blow(1, true);
        blows[1] = new Blow(3, true);
        blows[2] = new Blow(0, true);

        Field rollField = Blow.class.getDeclaredField("roll");
        rollField.setAccessible(true);
        rollField.setInt(blows[0], 5);
        rollField.setInt(blows[1], 4);
        rollField.setInt(blows[2], 3);

        flightView = new FlightView(gameInformation);

        operator.hit(player, blows, ElementType.Meteorite, flightBoard, flightView);

        assertNull(player.getShipBoard().getComponent(5, 5));
        assertNull(player.getShipBoard().getComponent(4, 4));
        assertNull(player.getShipBoard().getComponent(3, 3));
        assertEquals(0, player.getShipBoard().getShipBoardAttributes().getBatteryPower());
        assertEquals(5, player.getShipBoard().getShipBoardAttributes().getAvailableRedSlots());
        assertEquals(0, player.getShipBoard().getShipBoardAttributes().getCrewMembers());
        assertEquals(3, player.getShipBoard().getShipBoardAttributes().getDestroyedComponents());
        assertEquals(new int[]{0, 0, 0, 0}, player.getShipBoard().getShipBoardAttributes().getGoods());

    }

    @Test
    void bigMeteorBlowWithCannonNotActivated() throws NoSuchFieldException, IllegalAccessException {

        player.getShipBoard().addComponent(new Component(), 6, 6);
        player.getShipBoard().addComponent(new Component(), 5, 5);
        player.getShipBoard().addComponent(new Component(), 4, 4);
        player.getShipBoard().addComponent(new Component(), 3, 3);
        player.getShipBoard().addComponent(new Battery(new SideType[]{SideType.Single, SideType.Single, SideType.Single, SideType.Single}, 5), 2, 2);
        //front pointing cannon
        player.getShipBoard().addComponent(new Cannon(new SideType[]{SideType.Special, SideType.Single, SideType.Single, SideType.Single}, false), 4, 6);

        //right pointing cannon
        player.getShipBoard().addComponent(new Cannon(new SideType[]{SideType.Single, SideType.Special, SideType.Single, SideType.Single}, false), 5, 3);

        //back pointing cannon
        player.getShipBoard().addComponent(new Cannon(new SideType[]{SideType.Single, SideType.Single, SideType.Special, SideType.Single}, false), 6, 4);

        //left pointing cannon
        player.getShipBoard().addComponent(new Cannon(new SideType[]{SideType.Single, SideType.Single, SideType.Single, SideType.Special}, false), 3, 5);

        Blow blows[] = new Blow[4];

        blows[0] = new Blow(0, true);
        blows[1] = new Blow(1, true);
        blows[2] = new Blow(2, true);
        blows[3] = new Blow(3, true);

        Field rollField = Blow.class.getDeclaredField("roll");
        rollField.setAccessible(true);
        rollField.setInt(blows[0], 5);
        rollField.setInt(blows[1], 4);
        rollField.setInt(blows[2], 3);
        rollField.setInt(blows[3], 2);

        String inputString = "false\nfalse\nfalse\nfalse\n";
        ByteArrayInputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);

        flightView = new FlightView(gameInformation);

        operator.hit(player, blows, ElementType.Meteorite, flightBoard, flightView);

        assertNull(player.getShipBoard().getComponent(5, 5));
        assertNull(player.getShipBoard().getComponent(4, 4));
        assertNull(player.getShipBoard().getComponent(3, 3));
        assertNull(player.getShipBoard().getComponent(2, 2));

        assertEquals(5, player.getShipBoard().getShipBoardAttributes().getBatteryPower());
        assertEquals(5, ((Battery) player.getShipBoard().getComponent(1, 1)).getBatteryPower());
    }

    @Test
    void bigMeteorBlowWithCannonActivated() throws NoSuchFieldException, IllegalAccessException {

        //target component
        player.getShipBoard().addComponent(new Component(), 6, 6);

        //single cannon
        player.getShipBoard().addComponent(new Cannon(new SideType[]{SideType.Single, SideType.Special, SideType.Single, SideType.Single}, true), 6, 4);

        //double cannon
        player.getShipBoard().addComponent(new Cannon(new SideType[]{SideType.Special, SideType.Single, SideType.Single, SideType.Single}, false), 4, 6);

        player.getShipBoard().addComponent(new Battery(new SideType[]{SideType.Single, SideType.Single, SideType.Single, SideType.Single}, 1), 2, 2);


        Blow blows[] = new Blow[4];

        blows[3] = new Blow(0, true);
        blows[1] = new Blow(1, true);
        blows[2] = new Blow(1, true);
        blows[0] = new Blow(1, true);

        Field rollField = Blow.class.getDeclaredField("roll");
        rollField.setAccessible(true);
        rollField.setInt(blows[0], 6);
        rollField.setInt(blows[1], 5);
        rollField.setInt(blows[2], 4);
        rollField.setInt(blows[3], 5);

        String inputString = "true\n3 5\n1 1\n";
        ByteArrayInputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);

        flightView = new FlightView(gameInformation);

        operator.hit(player, blows, ElementType.Meteorite, flightBoard, flightView);

        assertNotNull(player.getShipBoard().getComponent(5, 5));
        assertEquals(0, player.getShipBoard().getShipBoardAttributes().getBatteryPower());
        assertEquals(0, ((Battery) player.getShipBoard().getComponent(1, 1)).getBatteryPower());

    }

    @Test
    void smallMeteorBlowHitSmoothSide() throws NoSuchFieldException, IllegalAccessException {

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

        flightView = new FlightView(gameInformation);

        operator.hit(player, blows, ElementType.Meteorite, flightBoard, flightView);

        assertNotNull(player.getShipBoard().getComponent(5, 5));

    }

    @Test
    void smallMeteorBlowWithShieldActivated() throws NoSuchFieldException, IllegalAccessException {

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

        flightView = new FlightView(gameInformation);

        operator.hit(player, blows, ElementType.Meteorite, flightBoard, flightView);

        assertNotNull(player.getShipBoard().getComponent(5, 5));
        assertEquals(0, player.getShipBoard().getShipBoardAttributes().getBatteryPower());
        assertEquals(0, ((Battery) player.getShipBoard().getComponent(5, 5)).getBatteryPower());

    }

    @Test
    void smallMeteorBlowHit() throws NoSuchFieldException, IllegalAccessException {

        player.getShipBoard().addComponent(new Storage(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}, true, 5), 6, 6);
        player.getShipBoard().addComponent(new Battery(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}, 3), 5, 5);
        player.getShipBoard().addComponent(new Cabin(), 4, 4);
        ((Cabin) player.getShipBoard().getComponent(3, 3)).setCrewType(CrewType.Human);

        player.getShipBoard().getShipBoardAttributes().updateGoods(new int[]{3, 1, 1, 0});
        player.getShipBoard().getShipBoardAttributes().updateAvailableSlots(1, 3);
        player.getShipBoard().getShipBoardAttributes().updateAvailableSlots(0, 2);

        //Av.RedSlots = 0


        Blow blows[] = new Blow[3];

        blows[0] = new Blow(1, false);
        blows[1] = new Blow(3, false);
        blows[2] = new Blow(0, false);

        Field rollField = Blow.class.getDeclaredField("roll");
        rollField.setAccessible(true);
        rollField.setInt(blows[0], 5); //targets storage
        rollField.setInt(blows[1], 4); //targets battery
        rollField.setInt(blows[2], 3); //targets carbin

        flightView = new FlightView(gameInformation);

        operator.hit(player, blows, ElementType.Meteorite, flightBoard, flightView);

        assertNull(player.getShipBoard().getComponent(5, 5));
        assertNull(player.getShipBoard().getComponent(4, 4));
        assertNull(player.getShipBoard().getComponent(3, 3));
        assertEquals(0, player.getShipBoard().getShipBoardAttributes().getBatteryPower());
        assertEquals(5, player.getShipBoard().getShipBoardAttributes().getAvailableRedSlots());
        assertEquals(0, player.getShipBoard().getShipBoardAttributes().getCrewMembers());
        assertEquals(3, player.getShipBoard().getShipBoardAttributes().getDestroyedComponents());
        assertEquals(new int[]{0, 0, 0, 0}, player.getShipBoard().getShipBoardAttributes().getGoods());

    }

}