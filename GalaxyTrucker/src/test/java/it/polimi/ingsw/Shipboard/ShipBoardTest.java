package it.polimi.ingsw.Shipboard;
import it.polimi.ingsw.Application.GameType;
import it.polimi.ingsw.Components.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


//already tested: addComponent, removeComponent, goDownChecking, isCompatible, countExternalJunctions, setCrewType, checkNotReachable
//remaining methods: checkErrors, addGoods, checkSlots

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
        shipBoard.removeComponent(7, 8, true);
        assertEquals(shipBoard.getShipBoardAttributes().getDrivingPower(), 0);
        assertEquals(shipBoard.getShipBoardAttributes().getDestroyedComponents(), 1);

    }



    @Test
    void addComponent2(){
        shipBoard.addComponent(new Cannon( new SideType[]{SideType.Special, SideType.Universal, SideType.Universal, SideType.Universal}, true), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getFirePower(), 1);
        shipBoard.removeComponent(7, 8, true);
        assertEquals(shipBoard.getShipBoardAttributes().getFirePower(), 0);
    }

    @Test
    void addComponent3(){
        shipBoard.addComponent(new Shield( new SideType[]{SideType.Universal, SideType.Special, SideType.Special, SideType.Universal}), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().checkSide(0), false);
        assertEquals(shipBoard.getShipBoardAttributes().checkSide(1), true);
        assertEquals(shipBoard.getShipBoardAttributes().checkSide(2), true);
        assertEquals(shipBoard.getShipBoardAttributes().checkSide(3), false);
        shipBoard.removeComponent(7, 8, true);
        assertEquals(shipBoard.getShipBoardAttributes().checkSide(0), false);
        assertEquals(shipBoard.getShipBoardAttributes().checkSide(1), false);
        assertEquals(shipBoard.getShipBoardAttributes().checkSide(2), false);
        assertEquals(shipBoard.getShipBoardAttributes().checkSide(3), false);
    }

    @Test
    void addComponent4(){
        shipBoard.addComponent(new Cabin(new SideType[]{SideType.Universal, SideType.Special, SideType.Special, SideType.Universal}), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getCrewMembers(),  4);
        shipBoard.removeComponent(7, 8, true);
        assertEquals(shipBoard.getShipBoardAttributes().getCrewMembers(),  2);
    }

    @Test
    void addComponent5(){
        shipBoard.addComponent(new Battery(new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal}, 2), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getBatteryPower(),  2);
        shipBoard.removeComponent(7, 8, true);
        assertEquals(shipBoard.getShipBoardAttributes().getBatteryPower(),  0);
    }

    @Test
    void addComponent6(){
        shipBoard.addComponent(new Storage(new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal}, true, 20), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getAvailableRedSlots(),  20);
        shipBoard.removeComponent(7, 8, true);
        assertEquals(shipBoard.getShipBoardAttributes().getAvailableRedSlots(),  0);
    }

    @Test
    void addComponent7(){
        shipBoard.addComponent(new Storage(new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal}, false, 20), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getAvailableBlueSlots(),  20);
        shipBoard.removeComponent(7, 8, true);
        assertEquals(shipBoard.getShipBoardAttributes().getAvailableBlueSlots(),  0);
    }

    @Test
    void addComponent8(){
        shipBoard.addComponent(new Cabin(new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal}), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getCrewMembers(),  4);
        shipBoard.addComponent(new AlienSupport(new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal}, true), 8, 8);
        shipBoard.setCrewType(CrewType.Purple, 7,8);
        assertEquals(shipBoard.getShipBoardAttributes().getCrewMembers(),  3);
        assertEquals(shipBoard.getShipBoardAttributes().getAlienType(), 2);
        shipBoard.removeComponent(7, 8, true);
        shipBoard.removeComponent(8, 8, true);
        assertEquals(shipBoard.getShipBoardAttributes().getAvailableBlueSlots(),  0);
    }

    @Test
    void CountExternalJunctions(){
        assertEquals(shipBoard.countExternalJunctions(), 4);
        shipBoard.addComponent(new Component(new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal}), 7, 8);
        assertEquals(shipBoard.countExternalJunctions(), 6);
        shipBoard.removeComponent(7, 8, true);
        assertEquals(shipBoard.countExternalJunctions(), 4);
    }

    @Test
    void testError(){ //Correct junctions
        int errors;
        shipBoard.addComponent(new Component(new SideType[]{SideType.Universal, SideType.Single, SideType.Smooth, SideType.Double}), 7, 8);
        shipBoard.addComponent(new Component(new SideType[]{SideType.Universal, SideType.Single, SideType.Smooth, SideType.Double}), 6, 8);
        shipBoard.addComponent(new Component(new SideType[]{SideType.Universal, SideType.Single, SideType.Smooth, SideType.Double}), 8, 8);
        errors = shipBoard.checkErrors();
        assertEquals(errors, 3);

        assertTrue(shipBoard.getMatrErrors()[7][5]);
        assertTrue(shipBoard.getMatrErrors()[7][6]);
        assertTrue(shipBoard.getMatrErrors()[7][6]);
        shipBoard.addComponent(new Storage(new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal}, true, 4), 8, 7);
        assertEquals(shipBoard.getShipBoardAttributes().getAvailableRedSlots(),  4);
        assertEquals(shipBoard.getShipBoardAttributes().getAvailableBlueSlots(), 0);
        shipBoard.removeComponent(8, 7, true);
        assertEquals(shipBoard.getShipBoardAttributes().getAvailableRedSlots(),  0);
        assertEquals(shipBoard.getShipBoardAttributes().getDestroyedComponents() , 3);
    }

}
