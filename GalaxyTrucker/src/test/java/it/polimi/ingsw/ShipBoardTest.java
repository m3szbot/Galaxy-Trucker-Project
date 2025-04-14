package it.polimi.ingsw;
import it.polimi.ingsw.Application.GameType;
import it.polimi.ingsw.Components.*;
import it.polimi.ingsw.Shipboard.ShipBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class ShipBoardTest {
    ShipBoard shipBoard;
    @BeforeEach
    void setUp() {
        shipBoard = new ShipBoard(GameType.NormalGame);
        for(int i = 0; i < 12; i++){
            for(int j = 0; j < 12; j++){
                System.out.print(shipBoard.getMatr()[i][j] + "");
            }
            System.out.println();
        }
        for(int i = 0; i < 12; i++){
            for(int j = 0; j < 12; j++){
                System.out.print(shipBoard.getStructureMatrix()[i][j] + "");
            }
            System.out.println();
        }
    }

    @Test
    void addComponent(){
        shipBoard.addComponent(new Engine( new SideType[]{SideType.Universal, SideType.Universal, SideType.Special, SideType.Universal}, true), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getDrivingPower(), 1);
        shipBoard.removeComponent(7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getDrivingPower(), 0);
        assertEquals(shipBoard.getShipBoardAttributes().getDestroyedComponents(), 1);

    }



    @Test
    void addComponent2(){
        shipBoard.addComponent(new Cannon( new SideType[]{SideType.Special, SideType.Universal, SideType.Universal, SideType.Universal}, true), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getFirePower(), 1);
        shipBoard.removeComponent(7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getFirePower(), 0);
    }

    @Test
    void addComponent3(){
        shipBoard.addComponent(new Shield( new SideType[]{SideType.Universal, SideType.Special, SideType.Special, SideType.Universal}), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().checkSide(0), false);
        assertEquals(shipBoard.getShipBoardAttributes().checkSide(1), true);
        assertEquals(shipBoard.getShipBoardAttributes().checkSide(2), true);
        assertEquals(shipBoard.getShipBoardAttributes().checkSide(3), false);
        shipBoard.removeComponent(7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().checkSide(0), false);
        assertEquals(shipBoard.getShipBoardAttributes().checkSide(1), false);
        assertEquals(shipBoard.getShipBoardAttributes().checkSide(2), false);
        assertEquals(shipBoard.getShipBoardAttributes().checkSide(3), false);
    }

    @Test
    void addComponent4(){
        shipBoard.addComponent(new Cabin(new SideType[]{SideType.Universal, SideType.Special, SideType.Special, SideType.Universal}), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getCrewMembers(),  2);
        shipBoard.removeComponent(7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getCrewMembers(),  0);
    }

    @Test
    void addComponent5(){
        shipBoard.addComponent();
    }
}
