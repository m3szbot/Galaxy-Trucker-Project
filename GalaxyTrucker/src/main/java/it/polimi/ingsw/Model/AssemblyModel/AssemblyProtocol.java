package it.polimi.ingsw.Model.AssemblyModel;

import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.IllegalSelectionException;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AssemblyProtocol handles the logic behind deck selection, component
 * booking and drawing, and other player interactions.
 *
 * @author Giacomo, Boti
 */
public class AssemblyProtocol {
    public Object lockUncoveredList = new Object();
    public Object lockCoveredList = new Object();
    public Object lockDecksList = new Object();

    private HourGlass hourGlass;
    // cards
    private Deck blockedDeck;
    private Deck[] decksList;
    // components - synchronized lists! - concurrent access by multiple players
    private List<Component> coveredList;
    private List<Component> uncoveredList;
    // ConcurrentMap does NOT allow null values!
    // Components currently in hand (viewMap).
    // Does not contain player entry if no component in hand (no nulls).
    private Map<Player, Component> inHandMap;
    // Booked components.
    // List has no elements if no components booked, but entry is not removed.
    private Map<Player, List<Component>> bookedMap;
    private GameType gameType;
    private FlightBoard flightBoard;
    private int gameCode;
    private Random randomizer;

    /**
     * Initializes the assembly protocol with the game setup.
     * Prepares decks, covered components, and player maps.
     *
     * @param gameInformation information about players, cards, and components
     */
    public AssemblyProtocol(GameInformation gameInformation) {
        hourGlass = new HourGlass();

        // copy cardList! - do not remove cards from original
        List<Card> tmpCardList = new ArrayList<>(gameInformation.getCardsList());
        blockedDeck = new Deck(tmpCardList, gameInformation.getGameType());
        decksList = new Deck[3];
        for (int i = 0; i < 3; i++) {
            // used cards must be removed from cardsList
            decksList[i] = new Deck(tmpCardList, gameInformation.getGameType());
        }
        // concurrently accessed lists
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
        gameType = gameInformation.getGameType();
        flightBoard = gameInformation.getFlightBoard();
        gameCode = gameInformation.getGameCode();
        randomizer = new Random();
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
            if (!decksList[num - 1].getInUse()) {
                decksList[num - 1].setInUse(true);
                return decksList[num - 1];
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
        if (!coveredList.isEmpty()) {
            // from covered list
            int randomIndex = randomizer.nextInt(coveredList.size());
            inHandMap.put(player, coveredList.remove(randomIndex));
        } else if (!uncoveredList.isEmpty()) {
            // from uncovered list
            int randomIndex = randomizer.nextInt(coveredList.size());
            inHandMap.put(player, uncoveredList.remove(randomIndex));
        } else {
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
                bookedMap.get(player).add(inHandMap.get(player));
                // remove component from hand (newComponent places component in hand in uncovered list)
                inHandMap.remove(player);
            } else {
                throw new IllegalSelectionException("Too many components booked");
            }
        } else {
            throw new IllegalSelectionException("No component in hand");
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
        return decksList[index];
    }
}
