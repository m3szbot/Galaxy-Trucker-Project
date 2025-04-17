package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Application.FlightPhase.FlightView;
import it.polimi.ingsw.Application.GameInformation;
import it.polimi.ingsw.Application.GameType;
import it.polimi.ingsw.Components.Component;
import it.polimi.ingsw.Components.SideType;
import it.polimi.ingsw.Components.Storage;
import it.polimi.ingsw.FlightBoard.FlightBoard;
import it.polimi.ingsw.Shipboard.Color;
import it.polimi.ingsw.Shipboard.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GoodsGainTest {

    class Operator implements GoodsGain{};

    Operator operator = new Operator();
    GameInformation gameInformation;
    Player player;
    FlightView flightView;
    FlightBoard flightBoard;

    @BeforeEach
    void initialize(){

        gameInformation = new GameInformation();
        gameInformation.setGameType(GameType.TestGame);

        CardBuilder cardBuilder = new CardBuilder();
        cardBuilder.buildCardName("Epidemic").buildCardLevel(1);

        ArrayList<Card> cardsList = new ArrayList<>();

        //8 is the size of a test game deck

        for(int i = 0; i < 8; i++){

            cardsList.add(new Epidemic(cardBuilder));

        }

        flightBoard = new FlightBoard(GameType.TestGame, cardsList);

        player = new Player("player", Color.BLUE, gameInformation);
    }

    @AfterEach
    void reestablishStdin(){
        System.setIn(System.in);
    }

    @Test
    void playerOnlyDiscardGoodsWithWrongEnteredValues(){

        player.getShipBoard().addComponent(new Storage(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}, true, 5), 6, 6);
        player.getShipBoard().addComponent(new Component(new SideType[]{SideType.Double, SideType.Universal, SideType.Double, SideType.Single}), 4, 4);
        player.getShipBoard().addComponent(new Storage(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}, true, 5), 5, 5);

        ((Storage)player.getShipBoard().getComponent(5, 5)).addGoods(new int[]{3, 2, 0, 0});

        //available red slots are 10 in total. Now 5 were occupied so I must update it to 5

        player.getShipBoard().getShipBoardAttributes().updateAvailableSlots(1, -5);

        String inputString = "true\n3 3\ntrue\n4 4\ntrue\n5 5\ntrue\n3\nfalse\nfalse\nfalse\n" +
                "true\n5 5\nfalse\ntrue\n2\nfalse\nfalse\nfalse\nfalse";
        ByteArrayInputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);

        flightView = new FlightView(gameInformation);

        operator.giveGoods(player, new int[]{0, 0, 0, 0}, flightBoard, flightView);

        assertTrue(((Storage)player.getShipBoard().getComponent(5, 5)).isEmpty());
        assertTrue(player.getShipBoard().getShipBoardAttributes().getAvailableRedSlots() == 10);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getAvailableBlueSlots() == 0);

    }

    @Test
    void playerRearrangeGoodsWithWrongEnteredValues(){

        //source red storage component
        player.getShipBoard().addComponent(new Storage(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}, true, 5), 6, 6);
        player.getShipBoard().addComponent(new Component(new SideType[]{SideType.Double, SideType.Universal, SideType.Double, SideType.Single}), 4, 4);
        //destination blue storage component
        player.getShipBoard().addComponent(new Storage(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}, false, 5), 5, 5);
        player.getShipBoard().addComponent(new Storage(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}, true, 5), 3, 3);

        Storage sourceStorage = ((Storage)player.getShipBoard().getComponent(5, 5));
        Storage destStorage = ((Storage)player.getShipBoard().getComponent(4, 4));

        sourceStorage.addGoods(new int[]{1, 3, 1, 0});
        destStorage.addGoods(new int[]{0, 0, 0, 1});

        player.getShipBoard().getShipBoardAttributes().updateAvailableSlots(1, -5);
        player.getShipBoard().getShipBoardAttributes().updateAvailableSlots(0, -1);

        /*
        BlueAvailableSlots are 4
        RedAvailableSlots are 5
         */

        String inputString = "false\ntrue\n3 3\n5 5\ntrue\n2 2\n5 5\ntrue\n5 5\n4 4\n" +
                "true\n1\nfalse\nfalse\nfalse\nfalse\ntrue\n4\ntrue\n5\ntrue\n1\n" +
                "false\ntrue\n3\ntrue\n1\nfalse\nfalse\n";

        ByteArrayInputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);

        flightView = new FlightView(gameInformation);

        operator.giveGoods(player, new int[]{0, 0, 0, 0}, flightBoard, flightView);

        assertTrue(sourceStorage.getAvailableRedSlots() == 4);
        assertTrue(destStorage.getAvailableBlueSlots() == 0);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getAvailableRedSlots() == 9);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getAvailableBlueSlots() == 0);

    }



}