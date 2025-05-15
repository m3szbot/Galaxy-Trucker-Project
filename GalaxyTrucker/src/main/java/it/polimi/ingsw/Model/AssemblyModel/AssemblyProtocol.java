package it.polimi.ingsw.Model.AssemblyModel;

import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
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
    public Object lockFlightBoard = new Object();

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
        blockedDeck = new Deck(gameInformation.getCardsList(), gameInformation.getGameType());
        decksList = new Deck[3];
        for (int i = 0; i < 3; i++) {
            // used cards must be removed from cardsList
            decksList[i] = new Deck(gameInformation.getCardsList(), gameInformation.getGameType());
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
    public Deck showDeck(int num) {
        if (num >= 1 && num <= 3) {
            if (!decksList[num - 1].getInUse()) {
                decksList[num - 1].setInUse(true);
                return decksList[num - 1];
            } else {
                throw new IllegalStateException("Deck is in use by others");
            }
        } else {
            throw new IllegalArgumentException("Invalid deck number");
        }
    }

    /**
     * Shuffles all cards from the blocked deck and the three side decks,
     * and returns them as a single deck
     *
     * @return a shuffled list of all cards
     */
    public List<Card> mergeDecks() {
        List<Card> mainDeck = new ArrayList<>();
        mainDeck.addAll(blockedDeck.getCards());
        mainDeck.addAll(decksList[0].getCards());
        mainDeck.addAll(decksList[1].getCards());
        mainDeck.addAll(decksList[2].getCards());
        Collections.shuffle(mainDeck);
        return mainDeck;
    }

    /**
     * Draws a new random component for the player and updates the current view.
     * Moves the previous component in hand (if any) to the uncovered list.
     *
     * @param player the player drawing a component
     */
    public void newComponent(Player player) {
        addComponentInHandToUncoveredList(player);
        // add new random component to player's hand
        if (!coveredList.isEmpty()) {
            int randomIndex = randomizer.nextInt(coveredList.size());
            inHandMap.put(player, coveredList.remove(randomIndex));
        } else {
            throw new IndexOutOfBoundsException("Covered list empty");
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
    public void chooseUncoveredComponent(Player player, int index) {
        // size: 0-size
        // index: 0-(size-1)
        if (uncoveredList.size() > index) {
            addComponentInHandToUncoveredList(player);
            inHandMap.put(player, uncoveredList.remove(index));
        } else {
            throw new IndexOutOfBoundsException("Uncovered list does not have enough elements");
        }
    }

    /**
     * If component is present in player's hand, books the component
     * if number of booked components hasn't reached the max (3).
     *
     * @param player the player booking the component
     * @author Boti
     */
    public void bookComponent(Player player) {
        if (inHandMap.containsKey(player)) {
            if (bookedMap.get(player).size() < 3) {
                bookedMap.get(player).add(inHandMap.get(player));
                // remove component from hand (newComponent places component in hand in uncovered list)
                inHandMap.remove(player);
            } else {
                throw new IllegalStateException("Too many components booked");
            }
        } else {
            throw new NoSuchElementException("No component in hand");
        }
    }

    /**
     * Takes and removes a booked component from the player's list based on index.
     *
     * @param player the player retrieving a booked component
     * @param index  the index of the component to take
     * @author Boti
     */
    public void chooseBookedComponent(Player player, int index) {
        if (bookedMap.get(player).size() > index) {
            addComponentInHandToUncoveredList(player);
            inHandMap.put(player, bookedMap.get(player).remove(index));
        } else {
            throw new IndexOutOfBoundsException("Not enough booked components");
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
}
