package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Model.AssemblyModel.AssemblyProtocol;
import it.polimi.ingsw.Model.Components.ComponentRotatorVisitor;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.IllegalSelectionException;
import it.polimi.ingsw.Model.ShipBoard.Player;

/**
 * AssemblyState handles the player's turn during the assembly phase,
 * managing their input and timing.
 *
 * @author Giacomo
 */
public class AssemblyState extends GameState {
    private long startTime;
    private boolean actionTaken = false;

    public AssemblyState(AssemblyProtocol assemblyProtocol, PlayerMessenger playerMessenger, Player player) {
        super(assemblyProtocol, playerMessenger, player);
    }

    /**
     * Called when this state becomes active. Initializes the timer and resets the action flag.
     */
    @Override
    public void enter(AssemblyThread assemblyThread) {

        startTime = System.currentTimeMillis();
        actionTaken = false;
        playerMessenger.printShipboard(player.getShipBoard());
        if (assemblyProtocol.getPlayersInHandMap().get(player) != null) {
            playerMessenger.printComponent(assemblyProtocol.getPlayersInHandMap().get(player));
        }
        playerMessenger.printMessage("👾AssemblyPhase: enter command:");
        playerMessenger.printMessage("(Place (current component) / Draw (a new component) / Choose (an uncovered component) / Show (a deck) / Rotate (current component)");
        playerMessenger.printMessage("/ Turn (the hourglass) / Book (current component and have a new one) / Place booked (component) / End (finish your assembling phase))");
    }

    /**
     * Handles user input commands during the assembly phase.
     */
    @Override
    public void handleInput(String input, AssemblyThread assemblyThread) {
        String message;

        if (actionTaken) return; // Ignore input after an action is taken

        switch (input.toLowerCase()) {

            case "place":
                actionTaken = true;
                assemblyThread.setState(new ComponentPlacingState(assemblyProtocol, playerMessenger, player, false));
                break;

            case "draw":
                actionTaken = true;
                synchronized (assemblyProtocol.lockCoveredList) {
                    try {
                        assemblyProtocol.newComponent(player);
                    } catch (IllegalSelectionException e) {
                        playerMessenger.printMessage("Sorry brother, we have finished all components! This situation can't happen so you must be very lucky to be here. I want to reward you. Listen carefully to my words. The Answer to the Great Question... Of Life, the Universe and Everything... Is... Forty-two.");
                        assemblyThread.setState(new AssemblyState(assemblyProtocol, playerMessenger, player));
                        break;
                    }
                }
                assemblyThread.setState(new AssemblyState(assemblyProtocol, playerMessenger, player));
                break;

            case "choose":
                actionTaken = true;
                assemblyThread.setState(new ComponentChoiceState(assemblyProtocol, playerMessenger, player));
                break;

            case "rotate":
                if (assemblyProtocol.getPlayersInHandMap().get(player) != null) {
                    assemblyProtocol.getPlayersInHandMap().get(player).accept(new ComponentRotatorVisitor());
                    message = "Component successfully rotated:";
                    playerMessenger.printMessage(message);
                } else {
                    message = "Your hand is empty";
                    playerMessenger.printMessage(message);
                }
                assemblyThread.setState(new AssemblyState(assemblyProtocol, playerMessenger, player));
                break;

            case "turn":
                actionTaken = true;
                if (assemblyProtocol.getHourGlass().isFinished() == true) {
                    if (assemblyThread.getGameInformation().getGameType().equals(GameType.NORMALGAME)) {
                        if (assemblyProtocol.getHourGlass().getState() == 2) {
                            message = "The hourglass is in it's final state, to finish the assembly phase you have to write 'end'";
                            playerMessenger.printMessage(message);
                        } else {
                            message = "Turn the hourglass";
                            playerMessenger.printMessage(message);
                            assemblyProtocol.getHourGlass().twist(assemblyProtocol, assemblyThread.getGameInformation().getPlayerList());
                        }
                    } else {
                        if (assemblyProtocol.getHourGlass().getState() == 1) {
                            message = "The hourglass is in it's final state, to finish the assembly phase you have to write 'end'";
                            playerMessenger.printMessage(message);
                        } else {
                            message = "Turn the hourglass";
                            playerMessenger.printMessage(message);
                            assemblyProtocol.getHourGlass().twist(assemblyProtocol, assemblyThread.getGameInformation().getPlayerList());
                        }
                    }
                } else {
                    message = "HourGlass is already running";
                    playerMessenger.printMessage(message);
                }
                assemblyThread.setState(new AssemblyState(assemblyProtocol, playerMessenger, player));
                break;

            case "show":
                actionTaken = true;
                assemblyThread.setState(new ShowDeckState(assemblyProtocol, playerMessenger, player));
                break;

            case "book":
                actionTaken = true;
                if (!assemblyProtocol.getGameType().equals(GameType.NORMALGAME)) {
                    message = "You can't book components at this level";
                    playerMessenger.printMessage(message);
                    assemblyThread.setState(new AssemblyState(assemblyProtocol, playerMessenger, player));
                    break;
                }
                if (assemblyProtocol.getPlayersBookedMap().get(player).size() < 2) {
                    if (assemblyProtocol.getPlayersInHandMap().get(player) != null) {
                        try {
                            assemblyProtocol.bookComponent(player);
                        } catch (IllegalSelectionException e) {
                            playerMessenger.printMessage("Omg! Arriving here it's almost impossible... You must have finished inside a black hole! OK, don't panic, now I'll bring you back");
                            assemblyThread.setState(new AssemblyState(assemblyProtocol, playerMessenger, player));
                            break;
                        }
                    } else {
                        message = "Your hand is empty";
                        playerMessenger.printMessage(message);
                    }
                } else {
                    message = "You don't have any remaining space for booking components, place them in order to gain new space";
                    playerMessenger.printMessage(message);
                }
                assemblyThread.setState(new AssemblyState(assemblyProtocol, playerMessenger, player));
                break;

            case "place booked":
                actionTaken = true;
                assemblyThread.setState(new PlaceBookedComponentState(assemblyProtocol, playerMessenger, player));
                break;

            case "end":
                actionTaken = true;
                assemblyThread.setState(new ChooseStartingPositionState(assemblyProtocol, playerMessenger, player));
                break;

            case "fastshipboardbuild":
                actionTaken = true;
                player.getShipBoard().preBuildShipBoard();
                assemblyThread.setState(new ChooseStartingPositionState(assemblyProtocol, playerMessenger, player));
                break;

            default:
                message = "Invalid command";
                playerMessenger.printMessage(message);
                assemblyThread.setState(new AssemblyState(assemblyProtocol, playerMessenger, player));
        }
    }

    /**
     * Periodically called to check if the player has timed out.
     */
    // TODO method never used
    public void update(AssemblyPhase assemblyPhase) {
        if (!actionTaken) {
            long now = System.currentTimeMillis();
            if (now - startTime >= 50000) {
                String message = "👾AssemblyPhase (place (current component) / draw (a new component) / Choose (a component) / Rotate (current component) / turn (the hourglass) / book (current component and have a new one) / place booked (component) / end (finish your assembling phase)"; // 50 seconds timeout
                playerMessenger.printMessage(message);

                actionTaken = true;
                assemblyPhase.setState(new AssemblyState(assemblyProtocol, playerMessenger, player));
            }
        }
    }
}
