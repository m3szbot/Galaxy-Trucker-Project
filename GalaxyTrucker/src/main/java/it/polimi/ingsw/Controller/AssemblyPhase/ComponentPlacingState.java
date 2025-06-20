package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Model.AssemblyModel.AssemblyProtocol;
import it.polimi.ingsw.Model.Components.ComponentRotatorVisitor;
import it.polimi.ingsw.Model.IllegalSelectionException;
import it.polimi.ingsw.Model.ShipBoard.NotPermittedPlacementException;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;

/**
 * ComponentPlacingState handles the logic for placing a component
 * onto the player's ship board based on user-provided coordinates.
 * After placing, a new component is drawn and the game returns to AssemblyState.
 *
 * @author Giacomo
 */
public class ComponentPlacingState extends GameState {
    // inherited attributes: assemblyProtocol, playerMessenger, player
    private Boolean booked;

    /**
     * Constructor inherited from GameState.
     */
    public ComponentPlacingState(AssemblyProtocol assemblyProtocol, PlayerMessenger playerMessenger, Player player, Boolean booked) {
        super(assemblyProtocol, playerMessenger, player);
        this.booked = booked;
    }


    /**
     * Called when this state becomes active. Displays a prompt to the player
     * asking where to place the component.
     *
     * @param assemblyThread the current game instance
     */
    @Override
    public void enter(AssemblyThread assemblyThread) {
        String message;
        if (!booked) {
            message = "Where do you want to place the component? Indicate coordinates Cols and Rows";
        } else {
            message = "Where do you want to place your booked component? Indicate coordinates Cols and Rows or write Rotate";
        }
        playerMessenger.printMessage(message);
    }

    /**
     * Parses the player's input for placement coordinates, adds the current component
     * to the ship board at the specified position, draws a new component, and returns
     * to the AssemblyState.
     *
     * @param input          the coordinates as a string (e.g., "3 4" or "3,4")
     * @param assemblyThread the current game instance
     */
    @Override
    public void handleInput(String input, AssemblyThread assemblyThread) {
        if (input.trim().toLowerCase().equals("rotate")) {
            if (booked) {
                assemblyProtocol.getPlayersInHandComponents().get(player).accept(new ComponentRotatorVisitor());
                playerMessenger.printComponent(assemblyProtocol.getPlayersInHandComponents().get(player));
                assemblyThread.setState(new ComponentPlacingState(assemblyProtocol, playerMessenger, player, booked));
            } else {
                String message = "Not valid format!";
                playerMessenger.printMessage(message);
                assemblyThread.setState(new AssemblyState(assemblyProtocol, playerMessenger, player));
            }
        } else {
            input.replaceAll("[^\\d]", " ");
            String[] parts = input.trim().split("[ ,]+"); //trim eliminates white spaces at the beginning and at the end

            // invalid coordinates
            if (parts.length != 2) {
                String message = "Not valid format!";
                playerMessenger.printMessage(message);
                assemblyThread.setState(new AssemblyState(assemblyProtocol, playerMessenger, player));
            }

            // valid coordinates
            try {
                int num1 = Integer.parseInt(parts[0]);
                int num2 = Integer.parseInt(parts[1]);

                // coordinates out of bounds
                if (!ShipBoard.checkCoordinatesInBounds(num1, num2)) {
                    String message = "Placing position out of bounds!";
                    playerMessenger.printMessage(message);
                    if (booked) {
                        try {
                            assemblyProtocol.bookComponent(player);
                        } catch (IllegalSelectionException e) {
                            playerMessenger.printMessage("Something strange is happening. How is it possible that you haven't placed your booked component and now you dont't have space to take it back??? Are you trying to cheat?");
                        }
                    }
                    assemblyThread.setState(new AssemblyState(assemblyProtocol, playerMessenger, player));
                }

                // coordinates in bounds
                else {
                    // component in hand
                    if (assemblyProtocol.getPlayersInHandComponents().get(player) != null) {
                        ShipBoard playerShipboard = player.getShipBoard();
                        // TODO delete checks, NotPermittedPlacement checked by shipboard
                        // coordinate selected has neighbours
                        if (playerShipboard.checkNotEmptyNeighbors(ShipBoard.getRealIndex(num1), ShipBoard.getRealIndex(num2))) {
                            try {
                                playerShipboard.addComponent(assemblyProtocol.getPlayersInHandComponents().get(player), num1, num2);
                                // component is removed from hand (not put back into lists or booked)
                                assemblyProtocol.removePlacedComponentFromHand(player);
                                // get new component
                                try {
                                    assemblyProtocol.newComponent(player);
                                } catch (IllegalSelectionException e) {
                                    playerMessenger.printMessage("Sorry brother, we have finished all components! This situation can't happen so you must be very lucky to be here. I want to reward you. Listen carefully to my words. The Answer to the Great Question... Of Life, the Universe and Everything... Is... Forty-two.");
                                    assemblyThread.setState(new AssemblyState(assemblyProtocol, playerMessenger, player));
                                }
                            }
                            // not permitted placement
                            catch (NotPermittedPlacementException e) {
                                String message = "Your are not allowed to place your component here";
                                playerMessenger.printMessage(message);
                                if (booked) {
                                    try {
                                        assemblyProtocol.bookComponent(player);
                                    } catch (IllegalSelectionException er) {
                                        playerMessenger.printMessage("Something strange is happening. How is it possible that you haven't placed your booked component and now you dont't have space to take it back??? Are you trying to cheat?");
                                    }
                                }
                            }
                            // coordinates out of bounds (already checked)
                            catch (IllegalSelectionException e) {
                                playerMessenger.printMessage("I have seen a lot of strange things during my journey across the galaxy, but it's the first time that i see a ship taking off without a crew");
                            }
                        }
                        // coordinate selected doesn't have neighbours
                        else {
                            String message = "You can't place your component here, it would float in the air";
                            playerMessenger.printMessage(message);
                            if (booked) {
                                try {
                                    assemblyProtocol.bookComponent(player);
                                } catch (IllegalSelectionException e) {
                                    playerMessenger.printMessage("Something strange is happening. How is it possible that you haven't placed your booked component and now you dont't have space to take it back??? Are you trying to cheat?");
                                }
                            }
                        }
                        assemblyThread.setState(new AssemblyState(assemblyProtocol, playerMessenger, player));
                    }
                    // no component in hand
                    else {
                        String message = "Your hand is empty";
                        playerMessenger.printMessage(message);
                        assemblyThread.setState(new AssemblyState(assemblyProtocol, playerMessenger, player));
                    }
                }
            }
            // cannot parse coordinates
            catch (NumberFormatException e) {
                String message = "Not valid format!";
                playerMessenger.printMessage(message);
                assemblyThread.setState(new AssemblyState(assemblyProtocol, playerMessenger, player));
            }
        }
    }
}
