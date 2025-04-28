package it.polimi.ingsw.Application.AssemblyPhase;

import it.polimi.ingsw.Assembly.AssemblyProtocol;
import it.polimi.ingsw.Shipboard.Player;

/**
 * AssemblyState handles the player's turn during the assembly phase,
 * managing their input and timing.
 *
 * @author Giacomo
 */
public class AssemblyState implements GameState {
    private long startTime;
    private boolean actionTaken = false;
    private AssemblyView view;
    private AssemblyProtocol protocol;
    private Player player;

    /**
     * Constructs a new AssemblyState for the given player.
     */
    public AssemblyState(AssemblyView assembly, AssemblyProtocol protocol, Player player) {
        this.view = assembly;
        this.protocol = protocol;
        this.player = player;
    }

    /**
     * Called when this state becomes active. Initializes the timer and resets the action flag.
     */
    @Override
    public void enter(AssemblyPhase assemblyPhase, AssemblyView view) {
        startTime = System.currentTimeMillis();
        actionTaken = false;
        view = view;
    }

    /**
     * Handles user input commands during the assembly phase.
     */
    @Override
    public void handleInput(String input, AssemblyPhase assemblyPhase) {
        if (actionTaken) return; // Ignore input after an action is taken
        view.printAssemblyMessage();
        switch (input.toLowerCase()) {
            case "place":
                actionTaken = true;
                assemblyPhase.setState(new ComponentPlacingState(view, protocol, player));
                break;
            case "draw":
                actionTaken = true;
                assemblyPhase.getAssemblyProtocol().newComponent(player);
                assemblyPhase.setState(new AssemblyState(view, protocol, player));
                break;
            case "choose":
                actionTaken = true;
                assemblyPhase.setState(new ComponentChoiceState(view, protocol, player));
                break;
            case "rotate":
                if(assemblyPhase.getAssemblyProtocol().getInHandMap().get(player) != null) {
                    assemblyPhase.getAssemblyProtocol().getInHandMap().get(player).rotate();
                    view.printRotateMessage(assemblyPhase.getAssemblyProtocol().getInHandMap().get(player));
                }else{
                    assemblyPhase.getAssemblyView().printEmptyHandErrorMessage();
                }
                assemblyPhase.setState(new AssemblyState(view, protocol, player));
                break;
            case "turn":
                actionTaken = true;
                view.printTurnMessage();
                assemblyPhase.getAssemblyProtocol().getHourGlass().twist();
                assemblyPhase.setState(new AssemblyState(view, protocol, player));
                break;
            case "show":
                actionTaken = true;
                assemblyPhase.setState(new ShowDeckState(view, protocol, player));
                break;
            case "book":
                actionTaken = true;
                if(assemblyPhase.getAssemblyProtocol().getInHandMap().get(player) != null) {
                    assemblyPhase.getAssemblyProtocol().bookComponent(player);
                }else{
                    assemblyPhase.getAssemblyView().printEmptyHandErrorMessage();
                }
                assemblyPhase.setState(new AssemblyState(view, protocol, player));
                break;
            case "place booked":
                actionTaken = true;
                assemblyPhase.setState(new PlaceBookedComponentState(view, protocol, player));
                break;
            default:
                view.printErrorInCommandMessage();
                assemblyPhase.setState(new AssemblyState(view, protocol, player));
        }
    }

    /**
     * Periodically called to check if the player has timed out.
     */
    public void update(AssemblyPhase assemblyPhase) {
        if (!actionTaken) {
            long now = System.currentTimeMillis();
            if (now - startTime >= 50000) { // 50 seconds timeout
                view.printAssemblyMessage();
                actionTaken = true;
                assemblyPhase.setState(new AssemblyState(view, protocol, player));
            }
        }
    }
}
