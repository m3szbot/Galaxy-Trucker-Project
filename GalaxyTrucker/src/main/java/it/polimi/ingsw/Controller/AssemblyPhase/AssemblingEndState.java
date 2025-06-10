package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Model.AssemblyModel.AssemblyProtocol;
import it.polimi.ingsw.Model.AssemblyModel.HourGlass;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.List;

public class AssemblingEndState extends GameState {
    // inherited attributes: assemblyProtocol, playerMessenger, player

    /**
     * Constructor inherited from GameState.
     */
    public AssemblingEndState(AssemblyProtocol assemblyProtocol, PlayerMessenger playerMessenger, Player player) {
        super(assemblyProtocol, playerMessenger, player);
    }

    @Override
    public void enter(AssemblyThread assemblyThread) {
        assemblyThread.getIsfinished().set(true);
        if (assemblyProtocol.getFlightBoard().getPlayerOrderList().size() == assemblyThread.getGameInformation().getPlayerList().size()) {
            assemblyThread.setRunning(false);
            return;
        }
        if (!assemblyThread.getRunning().get()) {
            playerMessenger.printMessage("Waiting for other players position choice");
            // TODO delete commented code?
            //assemblyPhase.end.set(true);
            //return;
        } else {
            String message = "Do you want to turn the hourglass? (write ---> yes <---, or wait for other players to complete their shipboard)";
            playerMessenger.printMessage(message);
        }
    }

    @Override
    public void handleInput(String input, AssemblyThread assemblyThread) {
        HourGlass hourGlass = assemblyProtocol.getHourGlass();
        List<Player> playerList = assemblyThread.getGameInformation().getPlayerList();

        if (input.equalsIgnoreCase("yes")) {
            // "yes" input
            if (hourGlass.isFinished() == true) {
                if (assemblyThread.getGameInformation().getGameType().equals(GameType.NORMALGAME)) {
                    if (hourGlass.getState() == 2) {
                        playerMessenger.printMessage("Waiting for other players position choice");
                        hourGlass.twist(assemblyProtocol, playerList);
                    } else {
                        hourGlass.twist(assemblyProtocol, playerList);
                    }
                } else {
                    if (hourGlass.getState() == 1) {
                        playerMessenger.printMessage("Waiting for other players position choice");
                        hourGlass.twist(assemblyProtocol, playerList);
                    } else {
                        hourGlass.twist(assemblyProtocol, playerList);
                    }
                }
            } else {
                String message = "HourGlass is already running";
                playerMessenger.printMessage(message);
            }
            assemblyThread.setState(new AssemblingEndState(assemblyProtocol, playerMessenger, player));

        } else {
            // invalid input
            String message = "Invalid input";
            playerMessenger.printMessage(message);
            assemblyThread.setState(new AssemblingEndState(assemblyProtocol, playerMessenger, player));
        }
    }
}
