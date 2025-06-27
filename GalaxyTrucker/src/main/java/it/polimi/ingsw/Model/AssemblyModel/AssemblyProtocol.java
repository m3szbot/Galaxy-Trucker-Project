package it.polimi.ingsw.Model.AssemblyModel;

import it.polimi.ingsw.Controller.FlightPhase.Cards.Card;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.IllegalSelectionException;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * AssemblyProtocol handles the logic behind deck selection, component
 * booking and drawing, and other player interactions.
 * <p>
 * Synchronization:
 * Shared: covered, uncovered component lists, individual Decks
 * Not shared: player maps - player thread only accesses own player's entry - structure is never modified
 *
 * @author Giacomo, Boti
 */
public class AssemblyProtocol {
    private final int gameCode;
    private final GameType gameType;
    private final FlightBoard flightBoard;
    private final HourGlass hourGlass;

    // DECKS:
    // Synchronize each deck, but not the deckList (list structure is not modified).
    private final Deck[] showableDecksList;

    // COMPONENTS:
    // Synchronize lists - adds/removes
    private final List<Component> coveredComponentsList;
    private final List<Component> uncoveredComponentsList;

    // PLAYER ASSOCIATED MAPS:
    // Use concurrentHashMap for extra safety:
    // (cannot have null values, so remove entry if no value)
    // (get return value or null if no value)
    // No need to be synchronized: each thread accesses only its own player's entry, map structure is not modified.

    // Component in hand for each player:
    private final Map<Player, Component> playersInHandComponents;

    // Booked components for each player:
    // player enty must not be removed, but list can be empty
    private final Map<Player, List<Component>> playersBookedComponents;

    // Stores a previously booked component currently in the hand of a player (to be placed):
    // Booked components cannot be returned to uncoveredList.
    private final Map<Player, Component> playersPlacingBookedComponentsCache;

    /**
     * Initializes the assembly protocol with the game setup.
     * Prepares decks, covered components, and player maps.
     *
     * @param gameInformation information about players, cards, and components
     */
    public AssemblyProtocol(GameInformation gameInformation) {
        gameType = gameInformation.getGameType();
        flightBoard = gameInformation.getFlightBoard();
        gameCode = gameInformation.getGameCode();
        hourGlass = new HourGlass();

        // DECKS:
        // copy cardList! - do not remove cards from original
        List<Card> tmpCardList = new ArrayList<>(gameInformation.getCardsList());
        showableDecksList = new Deck[3];
        for (int i = 0; i < 3; i++) {
            // used cards must be removed from cardsList
            showableDecksList[i] = new Deck(tmpCardList, gameInformation.getGameType());
        }

        // COMPONENTS:
        // synchronizedList: Thread-safe for individual add/remove/get operations, good for frequent add/remove
        coveredComponentsList = new ArrayList<>();
        coveredComponentsList.addAll(gameInformation.getComponentList());
        Collections.shuffle(coveredComponentsList);

        uncoveredComponentsList = new ArrayList<>();

        // player mapped structures
        playersInHandComponents = new ConcurrentHashMap<>();
        playersBookedComponents = new ConcurrentHashMap<>();
        playersPlacingBookedComponentsCache = new ConcurrentHashMap<>();
        for (Player player : gameInformation.getPlayerList()) {
            playersBookedComponents.put(player, new ArrayList<>());
        }

    }

    public Map<Player, Component> getPlayersPlacingBookedComponentsCache() {
        return playersPlacingBookedComponentsCache;
    }

    public GameType getGameType() {
        return gameType;
    }

    public FlightBoard getFlightBoard() {
        return flightBoard;
    }

    public int getGameCode() {
        return gameCode;
    }

    public Deck getDeck(int index) {
        synchronized (showableDecksList[index]) {
            return showableDecksList[index];
        }
    }

    /**
     * not synchronized! - use only for testing
     */
    public Deck[] getAllDecks() {
        return showableDecksList;
    }


    /**
     * Returns a copy of the list of covered components on the table (safe get).
     */
    public List<Component> getCoveredComponentsList() {
        synchronized (coveredComponentsList) {
            return new ArrayList<>(coveredComponentsList);
        }
    }

    /**
     * Returns a copy of the list of uncovered components on the table (safe get).
     */
    public List<Component> getUncoveredComponentsList() {
        synchronized (uncoveredComponentsList) {
            return new ArrayList<>(uncoveredComponentsList);
        }
    }

    /**
     * Returns the map of booked components per player.
     */
    public Map<Player, List<Component>> getPlayersBookedComponents() {
        return playersBookedComponents;
    }

    /**
     * Returns the current visible component for each player.
     */
    public Map<Player, Component> getPlayersInHandComponents() {
        return playersInHandComponents;
    }


    /**
     * Returns the HourGlass object for managing time mechanics.
     */
    public HourGlass getHourGlass() {
        return hourGlass;
    }

    /**
     * Marks the selected deck as inUse and returns it
     * (so it can be shown to the player).
     * Synchronized.
     *
     * @param num the deck index (1 to 3), 0 is blocked
     * @return the selected deck
     */
    public Deck showDeck(int num) throws IllegalSelectionException {
        if (num < 1 || num > 3)
            throw new IllegalSelectionException("Invalid deck number");

        // valid deck number
        int index = num - 1;

        synchronized (showableDecksList[index]) {
            if (!showableDecksList[index].getInUse()) {
                showableDecksList[index].setInUse(true);
                return showableDecksList[index];
            } else
                throw new IllegalSelectionException("Deck is in use by others");
        }
    }

    /**
     * Returns the previous component in hand.
     * Draws a new random component for the player and puts in the player's hand.
     * Draws from coveredList until it is exhausted, then it draws from uncoveredList.
     * Synchronized.
     *
     * @param player the player drawing a component
     * @throws IllegalSelectionException if no more components are available
     */
    public void newComponent(Player player) throws IllegalSelectionException {
        // add previous component to uncovered list from hand
        returnComponentInHand(player);

        // add new random component to player's hand
        // from coveredComponentsList
        synchronized (coveredComponentsList) {
            if (!coveredComponentsList.isEmpty()) {
                int randomIndex = ThreadLocalRandom.current().nextInt(coveredComponentsList.size());
                playersInHandComponents.put(player, coveredComponentsList.remove(randomIndex));
                return;
            }
        }

        // from uncoveredComponentsList
        synchronized (uncoveredComponentsList) {
            if (!uncoveredComponentsList.isEmpty()) {
                int randomIndex = ThreadLocalRandom.current().nextInt(uncoveredComponentsList.size());
                playersInHandComponents.put(player, uncoveredComponentsList.remove(randomIndex));
                return;
            }
        }

        // coveredComponentsList and uncoveredComponentsList empty
        throw new IllegalSelectionException("Component lists are empty");
    }

    /**
     * Return component currently in hand to the correct place.
     * If a previously booked component is in hand, return it to the booked components.
     * If a never booked component is in hand, return it to end of the uncovered list.
     * If nothing in hand, do nothing.
     * Synchronized.
     *
     * @author Boti
     */
    private void returnComponentInHand(Player player) {
        // get component or null if no component in hand
        Component current = playersInHandComponents.get(player);

        // only return component in hand to uncovered list if there is a component in hand
        if (current != null) {

            // current component was previously booked
            if (current.equals(playersPlacingBookedComponentsCache.get(player))) {
                // rebook current component
                try {
                    bookComponent(player);
                } catch (IllegalSelectionException e) {
                    throw new IllegalStateException("Error: cannot book the same component twice.");
                }
            }

            // current component was not booked previously
            else {
                synchronized (uncoveredComponentsList) {
                    // return component to uncovered list
                    uncoveredComponentsList.add(playersInHandComponents.get(player));
                }
                // remove returned component from hand
                playersInHandComponents.remove(player);
            }
        }
    }

    /**
     * If component is present in player's hand, books the component
     * if the number of booked components hasn't reached the max (2).
     * Empties the player's hand (null).
     *
     * @param player the player booking the component
     * @author Boti
     */
    public void bookComponent(Player player) throws IllegalSelectionException {
        Component current = playersInHandComponents.get(player);
        if (current != null) {
            // component not yet booked
            if (!playersBookedComponents.get(player).contains(current)) {
                // booked limit not reached
                if (playersBookedComponents.get(player).size() < 2) {
                    // book component
                    playersBookedComponents.get(player).add(current);
                    // remove component from hand (newComponent places component in hand in uncovered list)
                    playersInHandComponents.remove(player);

                } else {
                    // booked map already full
                    throw new IllegalSelectionException("Cannot book any more components, limit is reached (2).");
                }
            } else {
                // component already booked once
                throw new IllegalStateException("Error: trying to rebook an already booked component.");
            }
        } else
            throw new IllegalSelectionException("No component in hand to book.");

    }

    /**
     * Remove the placed component from the player's hand.
     *
     * @param player
     * @author Boti
     */
    public void removePlacedComponentFromHand(Player player) {
        if (playersInHandComponents.get(player) != null) {
            playersInHandComponents.remove(player);
        } else
            throw new IllegalStateException("Error: cannot remove component from empty hand.");
    }

    /**
     * Returns the previous component in hand.
     * Allows the player to choose a component from the uncovered list.
     * The player's current component is returned to the uncovered list,
     * and the selected component replaces it in the player's hand.
     * Synchronized.
     *
     * @param player the player making the selection
     * @param index  the index of the component in the uncovered list
     */
    public void chooseUncoveredComponent(Player player, int index) throws IllegalSelectionException {
        // size: 0-size
        // index: 0-(size-1)
        synchronized (uncoveredComponentsList) {
            if (uncoveredComponentsList.isEmpty())
                throw new IllegalSelectionException("Uncovered list is empty.");

            if (index >= 0 && index < uncoveredComponentsList.size()) {
                // returned component added to last place - does not disturb indexes
                returnComponentInHand(player);
                playersInHandComponents.put(player, uncoveredComponentsList.remove(index));
            } else {
                throw new IllegalSelectionException("Index out of range.");
            }
        }
    }

    /**
     * Returns the previous component in hand.
     * Takes and removes a booked component from the player's booked components based on index.
     *
     * @param player the player retrieving a booked component
     * @param index  the index of the component to take
     * @author Boti
     */
    public void chooseBookedComponent(Player player, int index) throws IllegalSelectionException {
        if (index >= 0 && index < playersBookedComponents.get(player).size()) {
            returnComponentInHand(player);

            Component tmp = playersBookedComponents.get(player).remove(index);
            playersPlacingBookedComponentsCache.put(player, tmp);
            playersInHandComponents.put(player, tmp);
        } else {
            throw new IllegalSelectionException("Invalid selection from booked components.");
        }
    }


}
