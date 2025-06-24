package it.polimi.ingsw.Controller.EvaluationPhase;

import it.polimi.ingsw.Controller.Game.Game;
import it.polimi.ingsw.Mocker;
import it.polimi.ingsw.Model.IllegalSelectionException;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;
import org.junit.jupiter.api.Test;

class EvaluationPhaseTest {
    Game game = Mocker.mockNormalGame1Player();
    EvaluationPhase evaluationPhase = game.getEvaluationPhase();
    Player player = game.getGameInformation().getPlayerList().getFirst();
    ShipBoard shipBoard = player.getShipBoard();

    @Test
    void testNoPlayersOnFlightBoard() {
        try {
            evaluationPhase.start();
        } catch (Exception e) {
        }
    }

    @Test
    void test1PlayerOnFlightBoard() throws IllegalSelectionException {
        game.getGameInformation().getFlightBoard().addPlayer(player, 1);
        try {
            evaluationPhase.start();
        } catch (Exception e) {
        }
    }
}