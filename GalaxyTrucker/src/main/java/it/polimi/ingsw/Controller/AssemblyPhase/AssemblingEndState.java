package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Model.AssemblyModel.AssemblyProtocol;
import it.polimi.ingsw.Model.AssemblyModel.HourGlass;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Player;

/**
 * {@code AssemblingEndState} is the final state reached during the assembly phase
 * for a given player. It handles the case where a player has completed their setup
 * and waits for other players or optionally triggers the hourglass mechanism.
 */
public class AssemblingEndState extends GameState {
    // inherited attributes: assemblyProtocol, playerMessenger, player

    /**
     * Constructs an {@code AssemblingEndState} object.
     *
     * @param assemblyProtocol the protocol managing the shared game state during assembly
     * @param playerMessenger  the messenger used to communicate with the player
     * @param player           the player associated with this state
     */
    public AssemblingEndState(AssemblyProtocol assemblyProtocol, PlayerMessenger playerMessenger, Player player) {
        super(assemblyProtocol, playerMessenger, player);
    }

    /**
     * Called when the player enters this state.
     * <p>
     * If all players have completed their ship setup, the assembly phase is terminated.
     * Otherwise, the player is either invited to wait or prompted to possibly turn the hourglass.
     *
     * @param assemblyThread the thread managing the current player's assembly flow
     */
    @Override
    public void enter(AssemblyThread assemblyThread) {
        assemblyThread.getIsfinished().set(true);
        if (assemblyProtocol.getFlightBoard().getPlayerOrderList().size() == assemblyThread.getGameInformation().getPlayerList().size()) {
            assemblyThread.setRunning(false);
            return;
        }
        if (!assemblyThread.getRunning().get()) {
            playerMessenger.printMessage("Waiting for other players position choice");
        } else {
            String message;
            if (assemblyThread.getGameInformation().getGameType().equals(GameType.NORMALGAME)) {
                message = "Do you want to turn the hourglass? (write ---> yes <---, or wait for other players to complete their shipboard)";
            }else{
                message = "Waiting for other players to complete their shipboard";
            }
            playerMessenger.printMessage(message);
        }
    }

    /**
     * Handles the player's input after completing their setup.
     * <p>
     * If the input is "yes" and the hourglass is in a finished state,
     * this may trigger a twist (turn) of the hourglass depending on its state and the game type.
     * Otherwise, the player is prompted accordingly.
     *
     * @param input           the input string from the player
     * @param assemblyThread  the thread managing the current player's assembly flow
     */
    @Override
    public void handleInput(String input, AssemblyThread assemblyThread) {
        HourGlass hourGlass = assemblyProtocol.getHourGlass();

        if (input.equalsIgnoreCase("yes")) {
            // "yes" input
            if (hourGlass.isFinished() == true) {
                if (assemblyThread.getGameInformation().getGameType().equals(GameType.NORMALGAME)) {
                    if (hourGlass.getState() == 2) {
                        playerMessenger.printMessage("Waiting for other players position choice");
                        hourGlass.twist(assemblyProtocol);
                    } else {
                        hourGlass.twist(assemblyProtocol);
                    }
                } else {
                    playerMessenger.printMessage("You can't turn the hourglass in test game");
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
