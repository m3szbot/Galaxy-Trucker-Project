package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Model.AssemblyModel.AssemblyProtocol;
import it.polimi.ingsw.Model.ShipBoard.NotPermittedPlacementException;
import it.polimi.ingsw.Model.ShipBoard.Player;

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
    private Boolean booked;
    /**
     * Constructs a ComponentPlacingState for the current player.
     *
     * @param protocol the game logic handler
     * @param player   the current player placing the component
     */
    public ComponentPlacingState(AssemblyProtocol protocol, Player player, Boolean booked) {
        this.assemblyProtocol = protocol;
        this.player = player;
        this.booked = booked;
    }

    /**
     * Called when this state becomes active. Displays a prompt to the player
     * asking where to place the component.
     *
     * @param assemblyPhase the current game instance
     */
    @Override
    public void enter(AssemblyThread assemblyPhase) {
        String message = "Where do you want to place the component? Indicate coordinates Cols and Rows";
        ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);
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
        input.replaceAll("[^\\d]", " ");
        String[] parts = input.trim().split("[ ,]+"); //trim eliminates white spaces at the beginning and at the end
        if (parts.length != 2) {
            String message = "Not valid format!";
            ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);
            assemblyPhase.setState(new AssemblyState(assemblyProtocol, player));
        }
        try {
            int num1 = Integer.parseInt(parts[0]); int num2 = Integer.parseInt(parts[1]);
            if (num1 < 4 || num2 < 4 || num1 > 10 || num2 > 10) {
                String message = "Placing position out of bounds!";
                ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);
                if (booked){
                    assemblyPhase.getAssemblyProtocol().bookComponent(player);
                }
                assemblyPhase.setState(new AssemblyState(assemblyProtocol, player));
            }else {
                if (assemblyPhase.getAssemblyProtocol().getInHandMap().get(player) != null) {
                    if (assemblyPhase.getGameInformation().getPlayerList().get(assemblyPhase.getGameInformation().getPlayerList().indexOf(player)).getShipBoard().getRealComponent(num1 - 2, num2 - 1) != null ||
                            assemblyPhase.getGameInformation().getPlayerList().get(assemblyPhase.getGameInformation().getPlayerList().indexOf(player)).getShipBoard().getRealComponent(num1, num2 - 1) != null ||
                            assemblyPhase.getGameInformation().getPlayerList().get(assemblyPhase.getGameInformation().getPlayerList().indexOf(player)).getShipBoard().getRealComponent(num1 - 1, num2 - 2) != null ||
                            assemblyPhase.getGameInformation().getPlayerList().get(assemblyPhase.getGameInformation().getPlayerList().indexOf(player)).getShipBoard().getRealComponent(num1 - 1, num2) != null) {
                        try {
                            assemblyPhase.getGameInformation().getPlayerList().get(assemblyPhase.getGameInformation().getPlayerList().indexOf(player)).getShipBoard().addComponent(assemblyPhase.getAssemblyProtocol().getInHandMap().get(player), num1, num2);
                            synchronized (assemblyProtocol.lockCoveredList) {
                                assemblyPhase.getAssemblyProtocol().newComponent(player);
                            }
                        } catch (NotPermittedPlacementException e) {
                            String message = "Your are not allowed to place your component here";
                            ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);
                            if (booked){
                                assemblyPhase.getAssemblyProtocol().bookComponent(player);
                            }
                        }
                    } else {
                        String message = "You can't place your component here, it would float in the air";
                        ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);
                        if (booked){
                            assemblyPhase.getAssemblyProtocol().bookComponent(player);
                        }
                    }
                    assemblyPhase.setState(new AssemblyState(assemblyProtocol, player));
                } else {
                    String message = "Your hand is empty";
                    ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);
                    assemblyPhase.setState(new AssemblyState(assemblyProtocol, player));
                }
            }
        } catch (NumberFormatException e) {
            String message = "Not valid format!";
            ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);
            assemblyPhase.setState(new AssemblyState(assemblyProtocol, player));
        }
    }
}
