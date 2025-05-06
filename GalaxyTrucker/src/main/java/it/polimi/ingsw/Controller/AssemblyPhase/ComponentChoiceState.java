package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Model.AssemblyModel.AssemblyProtocol;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.Connection.ClientSide.View.AssemblyView.AssemblyView;

/**
 * ComponentChoice handles the user input when a player chooses
 * a component from the uncovered list.
 *
 * @author Giacomo
 */
public class ComponentChoiceState implements GameState {
    private AssemblyProtocol assemblyProtocol;
    private Player player;
    private AssemblyView view;
    private String message;

    /**
     * Constructs a ComponentChoice state.
     *
     * @param view     the game view for showing messages
     * @param protocol the game logic handler
     * @param player   the current player
     */
    public ComponentChoiceState(AssemblyView view, AssemblyProtocol protocol, Player player) {
        this.assemblyProtocol = protocol;
        this.view = view;
        this.player = player;
    }

    @Override
    public void update(AssemblyThread assemblyPhase) {
        GameState.super.update(assemblyPhase);
    }

    /**
     * Displays the message prompting the player to choose a component.
     *
     * @param assemblyPhase the current game instance
     * @param view          the view used to show the message
     */
    @Override
    public void enter(AssemblyThread assemblyPhase, AssemblyView view) {
        /*
        message = "Enter the number of the component you would like:";
        view.sendMessageToPlayer(message, player);
        view.printUncoveredComponentsMessage(assemblyPhase);

         */
    }

    /**
     * Handles the input used to choose a component from the uncovered list.
     * If the choice is valid, the component is assigned to the player.
     *
     * @param input         the index of the component
     * @param assemblyPhase the current game instance
     */
    @Override
    public void handleInput(String input, AssemblyThread assemblyPhase) {
        Component component;
        String imput = input.toLowerCase();
        int caseManagement = -1;
        try {
            caseManagement = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (caseManagement >= 0 && caseManagement < assemblyPhase.getAssemblyProtocol().getUncoveredList().size()) {
            caseManagement = 1;
        } else {
            caseManagement = 0;
        }
        switch (caseManagement) {
            case 1:
                /*
                assemblyPhase.getAssemblyProtocol().chooseUncoveredComponent(player, Integer.parseInt(input));
                component = assemblyPhase.getAssemblyProtocol().getInHandMap().get(player);
                message ="New component:" + component.getComponentName() + "Front:" + component.getFront() + "Right:" + component.getRight() + "Back:" + component.getBack()  + "Left:" + component.getLeft();
                view.sendComponentMessageToPlayer(message, player, component);
                assemblyPhase.getAssemblyProtocol().chooseUncoveredComponent(player, Integer.parseInt(input));
                component = assemblyPhase.getAssemblyProtocol().getInHandMap().get(player);
                message ="New component:" + component.getComponentName() + "Front:" + component.getFront() + "Right:" + component.getRight() + "Back:" + component.getBack()  + "Left:" + component.getLeft();
                view.sendComponentMessageToPlayer(message, player, component);
                break;
            case 2:
                view.printErrorComponentChoiceMessage();
                break;

                 */
        }
        assemblyPhase.setState(new AssemblyState(view, assemblyProtocol, player));
    }

}
