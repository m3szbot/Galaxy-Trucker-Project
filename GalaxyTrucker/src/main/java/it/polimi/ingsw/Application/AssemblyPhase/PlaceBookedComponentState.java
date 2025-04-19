package it.polimi.ingsw.Application.AssemblyPhase;

import it.polimi.ingsw.Assembly.AssemblyProtocol;
import it.polimi.ingsw.Shipboard.Player;

/**
 * placeBookedComponentState allows the player to select one of their
 * booked components to place on the table.
 *
 * @author Giacomo
 */
public class PlaceBookedComponentState implements GameState{
    private AssemblyProtocol assemblyProtocol;
    private Player player;
    private AssemblyView view;

    /**
     * Constructs a new state for allowing the player to place one of their
     * previously booked components.
     *
     * @param view the game view used to display prompts and messages
     * @param protocol the logic handler that manages components and booking
     * @param player the player currently placing a booked component
     */
    public PlaceBookedComponentState(AssemblyView view, AssemblyProtocol protocol, Player player) {
        this.assemblyProtocol = protocol;
        this.view = view;
        this.player = player;
    }

    /**
     * Displays the prompt asking the player to choose a booked component.
     *
     * @param assemblyPhase the current game instance
     * @param assemblyView the view used for messages
     */
    @Override
    public void enter(AssemblyPhase assemblyPhase, AssemblyView assemblyView) {
        view.printChooseBookedComponentMessage(assemblyProtocol.getBookedMap().get(player).orElse(null));
    }

    /**
     * Handles the input index provided by the player, retrieves the corresponding
     * booked component (if valid), places the current component in the uncovered list,
     * and moves the selected one to the active slot.
     *
     * @param input the player's input (should be 1 or 2)
     * @param assemblyPhase the current game instance
     */
    @Override
    public void handleInput(String input, AssemblyPhase assemblyPhase) {
        int index = Integer.parseInt(input);
        index = index - 1;
        if(index == 0 || index == 1){
            assemblyPhase.getAssemblyProtocol().takeBookedComponentToPlace(player, index);
            assemblyPhase.setState(new ComponentPlacingState(view, assemblyProtocol, player));
        }
        else{
            view.printErrorChoosingBookedComponentMessage();
        }
    }

}
