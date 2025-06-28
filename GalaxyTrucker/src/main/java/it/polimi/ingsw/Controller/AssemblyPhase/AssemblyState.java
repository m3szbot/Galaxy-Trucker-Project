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
    private boolean actionTaken = false;

    public AssemblyState(AssemblyProtocol assemblyProtocol, PlayerMessenger playerMessenger, Player player) {
        super(assemblyProtocol, playerMessenger, player);
    }

    /**
     * Called when this state becomes active. Initializes the timer and resets the action flag.
     */
    @Override
    public void enter(AssemblyThread assemblyThread) {

        actionTaken = false;
        playerMessenger.printShipboard(player.getShipBoard());
        if (assemblyProtocol.getPlayersInHandComponents().get(player) != null) {
            playerMessenger.printComponent(assemblyProtocol.getPlayersInHandComponents().get(player));
        }
        playerMessenger.printMessage("Possible commands:");
        playerMessenger.printMessage("Place (current component) / Rotate (current component) / Draw (a new component) / Choose (an uncovered component) /");
        playerMessenger.printMessage("Book (current component) / Place booked (component) / Turn (the hourglass) / Show (a deck) / End (finish assembling)");
        playerMessenger.printMessage("Enter command:");
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
                try {
                    assemblyProtocol.newComponent(player);
                } catch (IllegalSelectionException e) {
                    playerMessenger.printMessage("Sorry brother, we have finished all components! This situation can't happen so you must be very lucky to be here. I want to reward you. Listen carefully to my words. The Answer to the Great Question... Of Life, the Universe and Everything... Is... Forty-two.");
                    assemblyThread.setState(new AssemblyState(assemblyProtocol, playerMessenger, player));
                    break;
                }
                assemblyThread.setState(new AssemblyState(assemblyProtocol, playerMessenger, player));
                break;

            case "choose":
                actionTaken = true;
                assemblyThread.setState(new ComponentChoiceState(assemblyProtocol, playerMessenger, player));
                break;

            case "rotate":
                if (assemblyProtocol.getPlayersInHandComponents().get(player) != null) {
                    assemblyProtocol.getPlayersInHandComponents().get(player).accept(new ComponentRotatorVisitor());
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
                            message = "To do the final turn of the hourglass finish assembling first";
                            playerMessenger.printMessage(message);
                        } else {
                            message = "Turn the hourglass";
                            playerMessenger.printMessage(message);
                            assemblyProtocol.getHourGlass().twist(assemblyProtocol);
                        }
                    } else {
                        playerMessenger.printMessage("You can't use the hourglass in test game");
                    }
                } else {
                    message = "HourGlass is already running";
                    playerMessenger.printMessage(message);
                }
                assemblyThread.setState(new AssemblyState(assemblyProtocol, playerMessenger, player));
                break;

            case "show":
                if (assemblyThread.getGameInformation().getGameType().equals(GameType.NORMALGAME)) {
                    actionTaken = true;
                    assemblyThread.setState(new ShowDeckState(assemblyProtocol, playerMessenger, player));
                    break;
                } else {
                    playerMessenger.printMessage("You are not allowed to see the cards in testgame mode");
                    assemblyThread.setState(new AssemblyState(assemblyProtocol, playerMessenger, player));
                    break;
                }
            case "book":
                actionTaken = true;
                if (!assemblyProtocol.getGameType().equals(GameType.NORMALGAME)) {
                    message = "You can't book components at this level";
                    playerMessenger.printMessage(message);
                    assemblyThread.setState(new AssemblyState(assemblyProtocol, playerMessenger, player));
                    break;
                }
                if (assemblyProtocol.getPlayersBookedComponents().get(player).size() < 2) {
                    if (assemblyProtocol.getPlayersInHandComponents().get(player) != null) {
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
}
