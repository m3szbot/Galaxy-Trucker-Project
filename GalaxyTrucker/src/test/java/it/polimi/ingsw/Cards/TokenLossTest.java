package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Application.FlightPhase.FlightView;
import it.polimi.ingsw.Application.GameInformation;
import it.polimi.ingsw.Application.GameType;
import it.polimi.ingsw.Components.Cabin;
import it.polimi.ingsw.Components.Component;
import it.polimi.ingsw.Components.CrewType;
import it.polimi.ingsw.Components.SideType;
import it.polimi.ingsw.FlightBoard.FlightBoard;
import it.polimi.ingsw.Shipboard.Color;
import it.polimi.ingsw.Shipboard.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author carlo
 */

class TokenLossTest {

    class Operator implements TokenLoss{};

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
    void removingInhabitantsWithWrongEnteredValues(){

        Cabin humanCabin = new Cabin(new SideType[]{SideType.Single, SideType.Single, SideType.Single, SideType.Single, SideType.Single});
        humanCabin.setCrewType(CrewType.Human);
        Cabin alienCabin = new Cabin(new SideType[]{SideType.Single, SideType.Single, SideType.Single, SideType.Single, SideType.Single});
        alienCabin.setCrewType(CrewType.Purple);
        //this adds 2 crew members to the shipboard, which is not normal
        Cabin emptyCabin = new Cabin(new SideType[]{SideType.Single, SideType.Single, SideType.Single, SideType.Single, SideType.Single});
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

        flightView = new FlightView(gameInformation);

        operator.inflictLoss(player, ElementType.CrewMember, 3, flightBoard, flightView);

        assertTrue(((Cabin)player.getShipBoard().getComponent(5, 5)).getCrewMembers() == 0);
        assertTrue(((Cabin)player.getShipBoard().getComponent(4, 4)).getCrewMembers() == 0);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getCrewMembers() == initialCrewNumber - 3);
        assertTrue(player.getShipBoard().getShipBoardAttributes().getAlienType() == 0);
    }



}