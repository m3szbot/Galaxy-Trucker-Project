package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.Components.SideType;
import it.polimi.ingsw.Model.Components.Storage;
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
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author carlo
 */

class GoodsGainTest {

    class Operator implements GoodsGain {
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
    void playerOnlyDiscardGoodsWithWrongEnteredValues() {

        player.getShipBoard().addComponent(new Storage(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}, true, 5), 6, 6);
        player.getShipBoard().addComponent(new Component(new SideType[]{SideType.Double, SideType.Universal, SideType.Double, SideType.Single}), 4, 4);
        player.getShipBoard().addComponent(new Storage(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}, true, 5), 5, 5);

        ((Storage) player.getShipBoard().getComponent(5, 5)).addGoods(new int[]{3, 2, 0, 0});

        //available red slots are 10 in total. Now 5 were occupied so I must update it to 5

        player.getShipBoard().getShipBoardAttributes().updateAvailableSlots(1, -5);
        player.getShipBoard().getShipBoardAttributes().updateGoods(new int[]{3, 2, 0, 0});

        String inputString = "true\n3 3\ntrue\n4 4\ntrue\n5 5\n3\n0\n0\n0\n" +
                "true\n5 5\n0\n2\n0\n0\nfalse\nfalse";
        ByteArrayInputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);

        flightView = new FlightViewTUI();

        operator.giveGoods(player, new int[]{0, 0, 0, 0}, flightBoard, flightView);

        assertTrue(((Storage) player.getShipBoard().getComponent(5, 5)).isEmpty());
        assertTrue(player.getShipBoard().getShipBoardAttributes().getGoods()[0] == 0);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getGoods()[1] == 0);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getGoods()[2] == 0);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getGoods()[3] == 0);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getAvailableRedSlots() == 10);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getAvailableBlueSlots() == 0);

    }

    @Test
    void playerRearrangeGoodsWithWrongEnteredValues() {

        //source red storage component
        player.getShipBoard().addComponent(new Storage(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}, true, 5), 6, 6);
        player.getShipBoard().addComponent(new Component(new SideType[]{SideType.Double, SideType.Universal, SideType.Double, SideType.Single}), 4, 4);
        //destination blue storage component
        player.getShipBoard().addComponent(new Storage(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}, false, 5), 5, 5);
        player.getShipBoard().addComponent(new Storage(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}, true, 5), 3, 3);

        Storage sourceStorage = ((Storage) player.getShipBoard().getComponent(5, 5));
        Storage destStorage = ((Storage) player.getShipBoard().getComponent(4, 4));

        sourceStorage.addGoods(new int[]{1, 3, 1, 0});
        destStorage.addGoods(new int[]{0, 0, 0, 1});

        player.getShipBoard().getShipBoardAttributes().updateAvailableSlots(1, -5);
        player.getShipBoard().getShipBoardAttributes().updateAvailableSlots(0, -1);
        player.getShipBoard().getShipBoardAttributes().updateGoods(new int[]{1, 3, 1, 1});

        /*
        BlueAvailableSlots are 4
        RedAvailableSlots are 5
         */

        String inputString = "false\ntrue\n3 3\n5 5\ntrue\n2 2\n5 5\ntrue\n5 5\n4 4\n" +
                "1\n0\n0\n0\n0\n4\n5\n1\n" +
                "0\n3\n1\n0\nfalse\n";

        ByteArrayInputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);

        flightView = new FlightViewTUI();

        operator.giveGoods(player, new int[]{0, 0, 0, 0}, flightBoard, flightView);

        assertTrue(sourceStorage.getAvailableRedSlots() == 4);
        assertTrue(destStorage.getAvailableBlueSlots() == 0);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getGoods()[0] == 1);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getGoods()[1] == 3);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getGoods()[2] == 1);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getGoods()[3] == 1);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getAvailableRedSlots() == 9);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getAvailableBlueSlots() == 0);

    }

    @Test
    void addingRedGoodsWithWrongEnteredValues() {

        player.getShipBoard().addComponent(new Storage(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}, true, 5), 6, 6);
        player.getShipBoard().addComponent(new Component(new SideType[]{SideType.Double, SideType.Universal, SideType.Double, SideType.Single}), 4, 4);
        player.getShipBoard().addComponent(new Storage(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}, false, 5), 5, 5);
        player.getShipBoard().addComponent(new Storage(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}, true, 5), 3, 3);

        //Av.RedSlots = 10; Av.BlueSlots = 5

        //making it full

        ((Storage) player.getShipBoard().getComponent(2, 2)).addGoods(new int[]{5, 0, 0, 0});

        player.getShipBoard().getShipBoardAttributes().updateAvailableSlots(1, -5);
        //Av.RedSlots = 5
        player.getShipBoard().getShipBoardAttributes().updateGoods(new int[]{5, 0, 0, 0});

        String inputString = "false\nfalse\ntrue\n3 3\ntrue\n2 2\ntrue\n4 4\ntrue\n" +
                "5 5\n2\ntrue\n5 5\n3\nfalse\n";
        ByteArrayInputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);

        flightView = new FlightViewTUI();

        operator.giveGoods(player, new int[]{5, 0, 0, 0}, flightBoard, flightView);

        assertTrue(((Storage) player.getShipBoard().getComponent(5, 5)).getAvailableRedSlots() == 0);
        assertTrue(((Storage) player.getShipBoard().getComponent(5, 5)).isFull());
        assertTrue(player.getShipBoard().getShipBoardAttributes().getGoods()[0] == 10);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getGoods()[1] == 0);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getGoods()[2] == 0);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getGoods()[3] == 0);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getAvailableRedSlots() == 0);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getAvailableBlueSlots() == 5);

    }

    @Test
    void addingNonRedGoodsWithWrongEnteredValues() {

        player.getShipBoard().addComponent(new Storage(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}, true, 5), 6, 6);
        player.getShipBoard().addComponent(new Component(new SideType[]{SideType.Double, SideType.Universal, SideType.Double, SideType.Single}), 4, 4);
        player.getShipBoard().addComponent(new Storage(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}, false, 5), 5, 5);
        player.getShipBoard().addComponent(new Storage(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}, true, 5), 3, 3);

        //Av.RedSlots = 10; Av.BlueSlots = 5

        //making it full

        ((Storage) player.getShipBoard().getComponent(2, 2)).addGoods(new int[]{5, 0, 0, 0});

        player.getShipBoard().getShipBoardAttributes().updateAvailableSlots(1, -5);
        //Av.RedSlots = 5

        player.getShipBoard().getShipBoardAttributes().updateGoods(new int[]{5, 0, 0, 0});

        String inputString = "false\nfalse\ntrue\n3 3\ntrue\n2 2\ntrue\n4 4\n3\n0\n0\n" +
                "true\n5 5\n0\n2\n0\nfalse\n";

        /*
        I have added 3 yellow goods to a blue storage and 2 green goods to a red storage, therefore
        I should have 3 available red slots and 2 available blue slots.
         */

        ByteArrayInputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);

        flightView = new FlightViewTUI();

        operator.giveGoods(player, new int[]{0, 3, 2, 1}, flightBoard, flightView);

        assertTrue(((Storage) player.getShipBoard().getComponent(4, 4)).getAvailableBlueSlots() == 2);
        assertTrue(((Storage) player.getShipBoard().getComponent(5, 5)).getAvailableRedSlots() == 3);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getAvailableBlueSlots() == 2);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getAvailableRedSlots() == 3);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getGoods()[0] == 5);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getGoods()[1] == 3);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getGoods()[2] == 2);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getGoods()[3] == 0);


    }

}