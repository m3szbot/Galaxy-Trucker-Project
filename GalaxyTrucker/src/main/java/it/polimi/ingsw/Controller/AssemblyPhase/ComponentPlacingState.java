package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Model.AssemblyModel.AssemblyProtocol;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.View.AssemblyView.AssemblyView;

/**
 * ComponentPlacingState handles the logic for placing a component
 * onto the player's ship board based on user-provided coordinates.
 * After placing, a new component is drawn and the game returns to AssemblyState.
 *
 * @author Giacomo
 */
public class ComponentPlacingState implements GameState {
    private AssemblyProtocol assemblyProtocol;
    private Player player;
    private AssemblyView view;

    /**
     * Constructs a ComponentPlacingState for the current player.
     *
     * @param view     the game view used for displaying messages
     * @param protocol the game logic handler
     * @param player   the current player placing the component
     */
    public ComponentPlacingState(AssemblyView view, AssemblyProtocol protocol, Player player) {
        this.assemblyProtocol = protocol;
        this.view = view;
        this.player = player;
    }

    /**
     * Called when this state becomes active. Displays a prompt to the player
     * asking where to place the component.
     *
     * @param assemblyPhase the current game instance
     * @param view          the view used for messaging
     */
    @Override
    public void enter(AssemblyThread assemblyPhase, AssemblyView view) {
        view.printComponentPlacingMessage();
    }

    /**
     * Parses the player's input for placement coordinates, adds the current component
     * to the ship board at the specified position, draws a new component, and returns
     * to the AssemblyState.
     *
     * @param input         the coordinates as a string (e.g., "3 4" or "3,4")
     * @param assemblyPhase the current game instance
     */
    @Override
    public void handleInput(String input, AssemblyThread assemblyPhase) {
        String[] parts = input.split("[ ,]");
        int num1 = Integer.parseInt(parts[0]);
        int num2 = Integer.parseInt(parts[1]);


        if (assemblyPhase.getAssemblyProtocol().getInHandMap().get(player) != null) {
            assemblyPhase.getGameInformation().getPlayerList().get(assemblyPhase.getGameInformation().getPlayerList().indexOf(player)).getShipBoard().addComponent(assemblyPhase.getAssemblyProtocol().getInHandMap().get(player), num1, num2);
            assemblyPhase.getAssemblyProtocol().newComponent(player);
            assemblyPhase.setState(new AssemblyState(view, assemblyProtocol, player));
        } else {
            assemblyPhase.getAssemblyView().printEmptyHandErrorMessage();
            assemblyPhase.setState(new AssemblyState(view, assemblyProtocol, player));
        }
    }
}
