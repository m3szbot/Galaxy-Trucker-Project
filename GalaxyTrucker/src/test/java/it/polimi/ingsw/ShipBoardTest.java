package it.polimi.ingsw;
import it.polimi.ingsw.Application.GameType;
import it.polimi.ingsw.Components.Engine;
import it.polimi.ingsw.Components.SideType;
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
    }
}
