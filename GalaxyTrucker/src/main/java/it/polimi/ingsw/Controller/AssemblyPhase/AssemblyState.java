package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.socket.DataContainer;
import it.polimi.ingsw.Model.AssemblyModel.AssemblyProtocol;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Player;

/**
 * AssemblyState handles the player's turn during the assembly phase,
 * managing their input and timing.
 *
 * @author Giacomo
 */
public class AssemblyState implements GameState {
    private long startTime;
    private boolean actionTaken = false;
    private AssemblyProtocol protocol;
    private Player player;
    DataContainer dataContainer;
    /**
     * Constructs a new AssemblyState for the given player.
     */
    public AssemblyState(AssemblyProtocol protocol, Player player) {
        this.protocol = protocol;
        this.player = player;
    }

    /**
     * Called when this state becomes active. Initializes the timer and resets the action flag.
     */
    @Override
    public void enter(AssemblyThread assemblyPhase) {

        startTime = System.currentTimeMillis();
        actionTaken = false;
        ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printShipboard(player.getShipBoard());
        if(assemblyPhase.getAssemblyProtocol().getInHandMap().get(player)!= null) {
            ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printComponent(assemblyPhase.getAssemblyProtocol().getInHandMap().get(player));
        }
        String message = "👾AssemblyPhase (Place (current component) / Draw (a new component) / Choose (a component) / Show (a deck) / Rotate (current component) / Turn (the hourglass) / Book (current component and have a new one) / Place booked (component) / End (finish your assembling phase)";
        ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);
    }

    /**
     * Handles user input commands during the assembly phase.
     */
    @Override
    public void handleInput(String input, AssemblyThread assemblyPhase) {
        String message;

        if (actionTaken) return; // Ignore input after an action is taken

        switch (input.toLowerCase()) {
            case "place":
                actionTaken = true;
                assemblyPhase.setState(new ComponentPlacingState(protocol, player, false));
                break;
            case "draw":
                actionTaken = true;
                synchronized (assemblyPhase.getAssemblyProtocol().lockCoveredList) {
                    assemblyPhase.getAssemblyProtocol().newComponent(player);
                }
                assemblyPhase.setState(new AssemblyState(protocol, player));
                break;
            case "choose":
                actionTaken = true;
                assemblyPhase.setState(new ComponentChoiceState(protocol, player));
                break;
            case "rotate":
                if (assemblyPhase.getAssemblyProtocol().getInHandMap().get(player) != null) {
                    assemblyPhase.getAssemblyProtocol().getInHandMap().get(player).rotate();
                    message = "Component successfully rotated:";
                    ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);
                } else {
                    message = "Your hand is empty";
                    ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);
                }
                assemblyPhase.setState(new AssemblyState(protocol, player));
                break;
            case "turn":
                actionTaken = true;
                if(assemblyPhase.getAssemblyProtocol().getHourGlass().isFinished() == true) {
                    if (assemblyPhase.getGameInformation().getGameType().equals(GameType.NORMALGAME)) {
                        if (assemblyPhase.getAssemblyProtocol().getHourGlass().getState() == 2) {
                            message = "The hourglass is in it's final state, to finish the assembly phase you have to write 'end'";
                            ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);
                        } else {
                            message = "Turn the hourglass";
                            ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);
                            assemblyPhase.getAssemblyProtocol().getHourGlass().twist(assemblyPhase.getAssemblyProtocol(), assemblyPhase.getGameInformation().getPlayerList());
                        }
                    } else {
                        if (assemblyPhase.getAssemblyProtocol().getHourGlass().getState() == 1) {
                            message = "The hourglass is in it's final state, to finish the assembly phase you have to write 'end'";
                            ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);
                        } else {
                            message = "Turn the hourglass";
                            ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);
                            assemblyPhase.getAssemblyProtocol().getHourGlass().twist(assemblyPhase.getAssemblyProtocol(), assemblyPhase.getGameInformation().getPlayerList());
                        }
                    }
                }
                else{
                    message = "HourGlass is already running";
                    ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);
                }
                assemblyPhase.setState(new AssemblyState(protocol, player));
                break;
            case "show":
                actionTaken = true;
                assemblyPhase.setState(new ShowDeckState(protocol, player));
                break;
            case "book":
                actionTaken = true;
                if(assemblyPhase.getAssemblyProtocol().getBookedMap().get(player).size() < 2) {
                    if (assemblyPhase.getAssemblyProtocol().getInHandMap().get(player) != null) {
                        assemblyPhase.getAssemblyProtocol().bookComponent(player);
                    } else {
                        message = "Your hand is empty";
                        ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);
                    }
                }else{
                    message = "You don't have any remaining space for booking components, place them in order to gain new space";
                    ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);
                }
                assemblyPhase.setState(new AssemblyState(protocol, player));
                break;
            case "place booked":
                actionTaken = true;
                assemblyPhase.setState(new PlaceBookedComponentState(protocol, player));
                break;
            case "end":
                actionTaken = true;
                assemblyPhase.setState(new ChooseStartingPositionState(protocol, player));
                break;
            default:
                message = "Invalid command";
                ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);
                assemblyPhase.setState(new AssemblyState(protocol, player));
        }


    }

    /**
     * Periodically called to check if the player has timed out.
     */
    public void update(AssemblyPhase assemblyPhase) {
        if (!actionTaken) {
            long now = System.currentTimeMillis();
            if (now - startTime >= 50000) {
                String message = "👾AssemblyPhase (place (current component) / draw (a new component) / Choose (a component) / Rotate (current component) / turn (the hourglass) / book (current component and have a new one) / place booked (component) / end (finish your assembling phase)"; // 50 seconds timeout
                ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);

                actionTaken = true;
                assemblyPhase.setState(new AssemblyState(protocol, player));
            }
        }
    }
}
