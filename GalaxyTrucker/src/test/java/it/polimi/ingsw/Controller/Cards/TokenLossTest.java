package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Model.Components.*;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
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
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author carlo
 */

class TokenLossTest {

    class Operator implements TokenLoss {
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
    void removingInhabitantsWithWrongEnteredValues() throws NotPermittedPlacementException {

        Cabin humanCabin = new Cabin(new SideType[]{SideType.Single, SideType.Single, SideType.Single, SideType.Single, SideType.Single}, CrewType.Human, 2);
        humanCabin.setCrewType(CrewType.Human);
        Cabin alienCabin = new Cabin(new SideType[]{SideType.Single, SideType.Single, SideType.Single, SideType.Single, SideType.Single}, CrewType.Human, 2);
        alienCabin.setCrewType(CrewType.Purple);
        //this adds 2 crew members to the shipboard, which is not normal
        Cabin emptyCabin = new Cabin(new SideType[]{SideType.Single, SideType.Single, SideType.Single, SideType.Single, SideType.Single}, CrewType.Human, 2);
        alienCabin.setCrewType(CrewType.Brown);
        //making the cabin (3, 3) empty for testing purposes


        //should be only one time, not normal
        emptyCabin.removeInhabitant();
        emptyCabin.removeInhabitant();

        player.getShipBoard().addComponent(humanCabin, 6, 6);

        player.getShipBoard().addComponent(alienCabin, 5, 5);

        player.getShipBoard().addComponent(emptyCabin, 4, 4);

        player.getShipBoard().addComponent(new Component(new SideType[]{SideType.Double, SideType.Universal, SideType.Double, SideType.Single}), 3, 3);

        //making the cabin (3, 3) empty for testing purposes

        int initialCrewNumber = player.getShipBoard().getShipBoardAttributes().getCrewMembers();

        String inputString = "2 2\n3 3\n4 4\n5 5\n3\n5 5\n2\n";
        ByteArrayInputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);

        flightView = new FlightViewTUI();

        operator.inflictLoss(player, ElementType.CrewMember, 3, gameInformation);

        assertTrue(((Cabin) player.getShipBoard().getComponent(5, 5)).getCrewMembers() == 0);
        assertTrue(((Cabin) player.getShipBoard().getComponent(4, 4)).getCrewMembers() == 0);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getCrewMembers() == initialCrewNumber - 3);
    }

    @Test
    void removingGoodsWithWrongEnteredValues() throws NotPermittedPlacementException {

        player.getShipBoard().addComponent(new Storage(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}, true, 5), 6, 6);
        player.getShipBoard().addComponent(new Storage(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}, false, 5), 5, 5);
        player.getShipBoard().addComponent(new Storage(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}, false, 5), 4, 4);
        player.getShipBoard().addComponent(new Component(new SideType[]{SideType.Double, SideType.Universal, SideType.Double, SideType.Single}), 3, 3);

        Storage redStorage = (Storage) player.getShipBoard().getComponent(5, 5);
        Storage blueStorage1 = (Storage) player.getShipBoard().getComponent(4, 4);
        Storage blueStorage2 = (Storage) player.getShipBoard().getComponent(3, 3);

        /*
        Av.RedSlots = 5
        Av.BlueSlots = 10
         */

        redStorage.addGoods(new int[]{3, 1, 1, 0});
        blueStorage1.addGoods(new int[]{0, 2, 2, 1});


        /*
        Av.RedSlots = 0
        Av.BlueSlots = 5
         */


        String inputString = "2 2\n4 4\n3 3\n5 5\n2\n5 5\n1\n5 5\n1\n5 5\n4 4\n2\n";
        ByteArrayInputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);

        flightView = new FlightViewTUI();

        operator.inflictLoss(player, ElementType.Goods, 6, gameInformation);

        /*
        Av.RedSlots = 4
        Av.BlueSlots = 7
         */

        assertTrue(player.getShipBoard().getShipBoardAttributes().getGoods()[0] == 0);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getGoods()[1] == 0);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getGoods()[2] == 3);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getGoods()[3] == 1);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getRemainingRedSlots() == 4);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getRemainingBlueSlots() == 7);

        inputString = "4 4\n1\n4 4\n2\n4 4\n1\n4 4\n5 5\n-1\n5 5\n1\n5 5\n4 4\n1\n";
        in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);

        flightView = new FlightViewTUI();

        operator.inflictLoss(player, ElementType.Goods, 4, gameInformation);
        /*
        Av.RedSlots = 5
        Av.BlueSlots = 10
         */

        assertTrue(player.getShipBoard().getShipBoardAttributes().getGoods()[0] == 0);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getGoods()[1] == 0);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getGoods()[2] == 0);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getGoods()[3] == 0);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getRemainingRedSlots() == 5);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getRemainingBlueSlots() == 10);

    }

    @Test
    void remvovingBatteries() throws NotPermittedPlacementException {

        player.getShipBoard().addComponent(new Storage(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}, true, 5), 6, 6);
        player.getShipBoard().addComponent(new Storage(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}, false, 5), 5, 5);
        player.getShipBoard().addComponent(new Storage(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double}, false, 5), 4, 4);
        player.getShipBoard().addComponent(new Component(new SideType[]{SideType.Double, SideType.Universal, SideType.Double, SideType.Single}), 3, 3);
        player.getShipBoard().addComponent(new Battery(new SideType[]{SideType.Single, SideType.Double, SideType.Double, SideType.Double}, 2), 9, 9);
        player.getShipBoard().addComponent(new Battery(new SideType[]{SideType.Single, SideType.Double, SideType.Double, SideType.Double}, 3), 10, 10);

        Storage redStorage = (Storage) player.getShipBoard().getComponent(5, 5);
        Storage blueStorage1 = (Storage) player.getShipBoard().getComponent(4, 4);
        Storage blueStorage2 = (Storage) player.getShipBoard().getComponent(3, 3);

        /*
        Av.RedSlots = 5
        Av.BlueSlots = 10
         */

        redStorage.addGoods(new int[]{3, 1, 1, 0});
        blueStorage1.addGoods(new int[]{0, 2, 2, 1});


        String inputString = "5 5\n3\n5 5\n1\n4 4\n2\n5 5\n1\n4 4\n2\n4 4\n1\n" +
                "8 8\n1\n9 9\n2\n";
        ByteArrayInputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);

        flightView = new FlightViewTUI();

        operator.inflictLoss(player, ElementType.Goods, 13, gameInformation);


        assertTrue(player.getShipBoard().getShipBoardAttributes().getGoods()[0] == 0);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getGoods()[1] == 0);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getGoods()[2] == 0);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getGoods()[3] == 0);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getRemainingRedSlots() == 5);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getRemainingBlueSlots() == 10);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getRemainingBatteries() == 2);
        assertTrue(((Battery) player.getShipBoard().getComponent(8, 8)).getBatteryPower() == 1);
        assertTrue(((Battery) player.getShipBoard().getComponent(9, 9)).getBatteryPower() == 1);

    }

    @Test
    void notEnoughBatteries() throws NotPermittedPlacementException {

        player.getShipBoard().addComponent(new Battery(new SideType[]{SideType.Single, SideType.Double, SideType.Double, SideType.Double}, 2), 9, 9);
        player.getShipBoard().addComponent(new Battery(new SideType[]{SideType.Single, SideType.Double, SideType.Double, SideType.Double}, 3), 10, 10);

        String inputString = "8 8\n1\n8 8\n1\n9 9\n10\n9 9\n-1\n9 9\n3\n";
        ByteArrayInputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);

        flightView = new FlightViewTUI();

        operator.inflictLoss(player, ElementType.Goods, 10, gameInformation);

        assertTrue(player.getShipBoard().getShipBoardAttributes().getRemainingBatteries() == 0);
        assertTrue(((Battery) player.getShipBoard().getComponent(8, 8)).getBatteryPower() == 0);
        assertTrue(((Battery) player.getShipBoard().getComponent(9, 9)).getBatteryPower() == 0);

    }

}