package it.polimi.ingsw.Model.AssemblyModel;

import it.polimi.ingsw.Controller.Cards.Card;
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
 *
 * @author Giacomo, Boti
 */
public class AssemblyProtocol {
    private final int gameCode;
    private final GameType gameType;
    private final FlightBoard flightBoard;
    private final HourGlass hourGlass;

    // CARDS
    private final Deck[] showableDecksList;

    // COMPONENTS:
    // synchronizedLists: Thread-safe for individual add/remove/get operations, good for frequent add/remove
    private final List<Component> coveredList;
    private final List<Component> uncoveredList;

    // ConcurrentHashMap:
    // Threads can safely call put, get, remove, etc., simultaneously. Does NOT allow null values!

    // Components in hand for each player:
    // Stores components currently in hand for each player.
    // Does not contain player entry if no component in hand (no nulls, only remove())!
    private final Map<Player, Component> inHandMap;

    // Booked components for each player:
    // List of player is empty if no components booked, but player Map entry is not removed.
    private final Map<Player, List<Component>> bookedMap;

    // TODO remove locks? - collections are already synchronized?
    // TODO synchronize manually instead of synchronized collections?
    public Object lockUncoveredList = new Object();
    public Object lockCoveredList = new Object();
    public Object lockDecksList = new Object();

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

        // cards
        // copy cardList! - do not remove cards from original
        List<Card> tmpCardList = new ArrayList<>(gameInformation.getCardsList());
        showableDecksList = new Deck[3];
        for (int i = 0; i < 3; i++) {
            // used cards must be removed from cardsList
            showableDecksList[i] = new Deck(tmpCardList, gameInformation.getGameType());
        }

        // component lists:
        // synchronizedList: Thread-safe for individual add/remove/get operations, good for frequent add/remove
        coveredList = Collections.synchronizedList(new ArrayList<>());
        coveredList.addAll(gameInformation.getComponentList());
        Collections.shuffle(coveredList);
        uncoveredList = Collections.synchronizedList(new ArrayList<>());

        // player mapped structures
        inHandMap = new ConcurrentHashMap<>();
        bookedMap = new ConcurrentHashMap<>();
        for (Player player : gameInformation.getPlayerList()) {
            bookedMap.put(player, new ArrayList<>());
        }

    }

    /**
     * Returns the list of covered components on the table.
     */
    public List<Component> getCoveredList() {
        return coveredList;
    }

    /**
     * Returns the list of uncovered components on the table.
     */
    public List<Component> getUncoveredList() {
        return uncoveredList;
    }

    /**
     * Returns the map of booked components per player.
     */
    public Map<Player, List<Component>> getBookedMap() {
        return bookedMap;
    }

    /**
     * Returns the current visible component for each player.
     */
    public Map<Player, Component> getInHandMap() {
        return inHandMap;
    }


    /**
     * Returns the HourGlass object for managing time mechanics.
     */
    public HourGlass getHourGlass() {
        return hourGlass;
    }

    /**
     * Marks a deck as revealed and returns it.
     *
     * @param num the deck index (1 to 3), 0 is blocked
     * @return the selected deck
     */
    public Deck showDeck(int num) throws IllegalSelectionException {
        if (num >= 1 && num <= 3) {
            if (!showableDecksList[num - 1].getInUse()) {
                showableDecksList[num - 1].setInUse(true);
                return showableDecksList[num - 1];
            } else {
                throw new IllegalSelectionException("Deck is in use by others");
            }
        } else {
            throw new IllegalSelectionException("Invalid deck number");
        }
    }

    /**
     * Draws a new random component for the player and updates the current view.
     * Moves the previous component in hand (if any) to the uncovered list.
     *
     * @param player the player drawing a component
     * @throws IllegalSelectionException if no more components are available
     */
    public void newComponent(Player player) throws IllegalSelectionException {
        addComponentInHandToUncoveredList(player);
        // add new random component to player's hand

        //from coveredList
        if (!coveredList.isEmpty()) {
            int randomIndex = ThreadLocalRandom.current().nextInt(coveredList.size());
            inHandMap.put(player, coveredList.remove(randomIndex));
        }

        // from uncoveredList
        else if (!uncoveredList.isEmpty()) {
            int randomIndex = ThreadLocalRandom.current().nextInt(uncoveredList.size());
            inHandMap.put(player, uncoveredList.remove(randomIndex));

        }

        // coveredList and uncoveredList empty
        else {
            throw new IllegalSelectionException("Component lists are empty");
        }
    }

    /**
     * If a component is present in the player's hand, return component to the uncovered list,
     * remove player entry from inHandMap.
     *
     * @author Boti
     */
    private void addComponentInHandToUncoveredList(Player player) {
        if (inHandMap.containsKey(player)) {
            uncoveredList.add(inHandMap.get(player));
            inHandMap.remove(player);
        }
    }

    /**
     * Allows the player to choose a component from the uncovered list.
     * The player's current component is returned to the uncovered list,
     * and the selected component replaces it in the player's view.
     *
     * @param player the player making the selection
     * @param index  the index of the component in the uncovered list
     */
    public void chooseUncoveredComponent(Player player, int index) throws IllegalSelectionException {
        // size: 0-size
        // index: 0-(size-1)
        if (uncoveredList.size() > index && index >= 0) {
            addComponentInHandToUncoveredList(player);
            inHandMap.put(player, uncoveredList.remove(index));
        } else {
            throw new IllegalSelectionException("Uncovered component list is empty");
        }
    }

    /**
     * If component is present in player's hand, books the component
     * if number of booked components hasn't reached the max (3).
     *
     * @param player the player booking the component
     * @author Boti
     */
    public void bookComponent(Player player) throws IllegalSelectionException {
        if (inHandMap.containsKey(player)) {
            if (bookedMap.get(player).size() < 3) {
                // book component
                bookedMap.get(player).add(inHandMap.get(player));
                // remove component from hand (newComponent places component in hand in uncovered list)
                inHandMap.remove(player);

            } else {
                // booked map already full
                throw new IllegalSelectionException("Cannot book component, booked map is already full.");
            }

        } else {
            // error: no booked map for player
            throw new IllegalStateException("Error: the player has no booked map");
        }
    }

    /**
     * Takes and removes a booked component from the player's list based on index.
     *
     * @param player the player retrieving a booked component
     * @param index  the index of the component to take
     * @author Boti
     */
    public void chooseBookedComponent(Player player, int index) throws IllegalSelectionException {
        if (bookedMap.get(player).size() > index) {
            addComponentInHandToUncoveredList(player);
            inHandMap.put(player, bookedMap.get(player).remove(index));
        } else {
            throw new IllegalSelectionException("Not enough booked components");
        }
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
        return showableDecksList[index];
    }
}
