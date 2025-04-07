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
    public void enter(AssemblyGame assemblyGame, AssemblyView view) {
        startTime = System.currentTimeMillis();
        actionTaken = false;
        view = view;
    }

    /**
     * Handles user input commands during the assembly phase.
     */
    @Override
    public void handleInput(String input, AssemblyGame assemblyGame) {
        if (actionTaken) return; // Ignore input after an action is taken
        view.printAssemblyMessage();
        switch (input.toLowerCase()) {
            case "place":
                actionTaken = true;
                assemblyGame.setState(new ComponentPlacingState(view, protocol,player));
                break;
            case "draw":
                actionTaken = true;
                assemblyGame.getAssemblyProtocol().newComponent(player);
                assemblyGame.setState(new AssemblyState(view, protocol, player));
                break;
            case "choose":
                actionTaken = true;
                assemblyGame.setState(new ComponentChoice(view, protocol, player));
                break;
            case "rotate":
                assemblyGame.getAssemblyProtocol().getViewMap().get(player).rotate();
                view.printRotateMessage(assemblyGame.getAssemblyProtocol().getViewMap().get(player));
                assemblyGame.setState(new AssemblyState(view, protocol, player));
                break;
            case "turn":
                actionTaken = true;
                view.printTurnMessage();
                assemblyGame.getAssemblyProtocol().getHourGlass().twist();
                assemblyGame.setState(new AssemblyState(view, protocol, player));
                break;
            case "show":
                actionTaken = true;
                assemblyGame.setState(new ShowDeckState(view, protocol, player));
                break;
            case "book":
                actionTaken = true;
                assemblyGame.getAssemblyProtocol().bookComponent(player);
                assemblyGame.setState(new AssemblyState(view, protocol, player));
                break;
            case "place booked":
                actionTaken = true;
                assemblyGame.setState(new PlaceBookedComponentState(view, protocol, player));
                break;
            default:
                view.printErrorInCommandMessage();
                assemblyGame.setState(new AssemblyState(view, protocol, player));
        }
    }

    /**
     * Periodically called to check if the player has timed out.
     */
    public void update(AssemblyGame assemblyGame) {
        if (!actionTaken) {
            long now = System.currentTimeMillis();
            if (now - startTime >= 50000) { // 50 seconds timeout
                view.printAssemblyMessage();
                actionTaken = true;
                assemblyGame.setState(new AssemblyState(view, protocol, player));
            }
        }
    }
}
