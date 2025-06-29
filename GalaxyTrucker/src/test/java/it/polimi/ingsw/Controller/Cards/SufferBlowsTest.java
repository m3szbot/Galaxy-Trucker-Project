package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Controller.FlightPhase.Cards.Blow;
import it.polimi.ingsw.Controller.FlightPhase.Cards.ElementType;
import it.polimi.ingsw.Controller.FlightPhase.Cards.SufferBlows;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.*;
import it.polimi.ingsw.Mocker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SufferBlowsTest {
    private SufferBlows sufferBlows;
    private Player player;
    private GameInformation gameInformation;
    private ShipBoard shipBoard;
    private ShipBoardAttributes shipBoardAttributes;

    @BeforeEach
    void setUp() {
        // Create a mock implementation of the interface
        sufferBlows = new SufferBlows() {
        };

        // Initialize game with Mocker
        Mocker.mockNormalGame1Player();

        // Get initialized objects from Mocker
        player = Mocker.getFirstPlayer();
        gameInformation = Mocker.getGameInformation();
        shipBoard = player.getShipBoard();
        shipBoardAttributes = shipBoard.getShipBoardAttributes();

        // Pre-build the shipboard for testing
        shipBoard.preBuildShipBoard();
    }

    @Test
    void testBigCannonBlowHit() throws NoHumanCrewLeftException, PlayerDisconnectedException {
        // Setup a blow from the front (direction 0)
        Blow blow = new Blow(0, true);
        blow.setRoll(6); // Fixed roll for testing

        // Test the hit
        sufferBlows.hit(player, new Blow[]{blow}, ElementType.CannonBlow, gameInformation);

        // Verify component was hit
        assertNull(shipBoard.getComponent(7, 6));
    }

    @Test
    void testSmallCannonBlowHitWithShieldProtection() throws NoHumanCrewLeftException, PlayerDisconnectedException {
        // Setup a blow from the front (direction 0)
        Blow blow = new Blow(0, false);
        blow.setRoll(4); // Fixed roll for testing

        int batteries = shipBoardAttributes.getRemainingBatteries();

        // Simulate player choosing to defend with shield
        Mocker.simulateClientInput("yes\n8 8\n"); // Player chooses to defend

        // Test the hit
        sufferBlows.hit(player, new Blow[]{blow}, ElementType.CannonBlow, gameInformation);

        // Verify battery was used (reduced by 1)
        assertTrue(shipBoardAttributes.getRemainingBatteries() < batteries);
        assertNotNull(shipBoard.getComponent(5, 6));
    }

    @Test
    void testBigMeteorBlowHitWithCannonDefense() throws NoHumanCrewLeftException, PlayerDisconnectedException {
        // Setup a blow from the front (direction 0)
        Blow blow = new Blow(1, true);
        blow.setRoll(6); // Fixed roll for testing

        int batteries = shipBoardAttributes.getRemainingBatteries();

        // Simulate player choosing to defend with cannon
        Mocker.simulateClientInput("yes\n8 8\n"); // Player chooses to defend

        // Test the hit
        sufferBlows.hit(player, new Blow[]{blow}, ElementType.Meteorite, gameInformation);

        // Verify battery was used (reduced by 1)
        assertTrue(shipBoardAttributes.getRemainingBatteries() < batteries);
        assertNotNull(shipBoard.getComponent(10, 7));
    }

    @Test
    void testSmallMeteorBlowHitWithSmoothSide() throws NoHumanCrewLeftException, PlayerDisconnectedException {
        // Setup a blow from the front (direction 0) hitting a smooth side
        Blow blow = new Blow(0, false);
        blow.setRoll(9); // Fixed roll for testing a smooth side component

        // Test the hit
        sufferBlows.hit(player, new Blow[]{blow}, ElementType.Meteorite, gameInformation);

        // Verify no component was removed (smooth side should deflect)
        assertNotNull(shipBoard.getComponent(10, 7)); // Starter cabin should still be there
    }

}