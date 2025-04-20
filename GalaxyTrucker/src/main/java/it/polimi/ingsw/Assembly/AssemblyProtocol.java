package it.polimi.ingsw.Assembly;

import java.util.*;

import it.polimi.ingsw.Cards.*;
import it.polimi.ingsw.Components.*;
import it.polimi.ingsw.Shipboard.Player;
import it.polimi.ingsw.Application.*;

/**
 * AssemblyProtocol handles the logic behind deck selection, component
 * booking and drawing, and other player interactions.
 *
 * @author Giacomo
 */
public class AssemblyProtocol {
    private HourGlass hourGlass;
    // cards
    private Deck blockedDeck;
    private Deck[] decksList;
    // components
    private List<Component> coveredList;
    private List<Component> uncoveredList;
    private Map<Player, Component> viewMap;
    private Map<Player, List<Component>> bookedMap;


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
        coveredList = new ArrayList<>();
        coveredList.addAll(gameInformation.getComponentList());
        uncoveredList = new ArrayList<>();
        viewMap = new HashMap<>();
        bookedMap = new HashMap<>();
        for (Player player : gameInformation.getPlayerList()) {
            bookedMap.put(player, new ArrayList<>());
        }
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
    public Map<Player, Component> getViewMap() {
        return viewMap;
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
     * @return the selected deck, or null if blocked
     */
    public List<Card> showDeck(int num) {
        if (num >= 1 && num <= 3) {
            if (!decksList[num - 1].getInUse()) {
                decksList[num - 1].setInUse(true);
                return decksList[num - 1].getCards();
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
     * Add current component in hand to the uncovered list
     * So that it can be replaced
     *
     * @param player player with the component in hand
     */
    private void addComponentInHandToUncoveredList(Player player) {
        // remove card from player's hand
        if (viewMap.get(player) != null) {
            uncoveredList.add(viewMap.get(player));
        }
    }

    /**
     * Draws a new component for the player and updates the current view.
     * Moves the previous component in hand (if any) to the uncovered list.
     *
     * @param player the player drawing a component
     */
    public void newComponent(Player player) {
        addComponentInHandToUncoveredList(player);
        // add new card to player's hand
        if (!coveredList.isEmpty()) {
            viewMap.put(player, coveredList.removeFirst());
        } else {
            throw new IndexOutOfBoundsException("Covered list empty");
        }
    }

    /**
     * Allows the player to choose a component from the uncovered list.
     * The player's current component is returned to the uncovered list,
     * and the selected component replaces it in the player's view.
     *
     * @param player the player making the selection
     * @param num    the index of the component in the uncovered list
     */
    public void chooseComponent(Player player, int num) {
        // size: 0-size
        // index: 0-(size-1)
        if (uncoveredList.size() > num) {
            addComponentInHandToUncoveredList(player);
            viewMap.put(player, uncoveredList.remove(num));
        } else {
            throw new IndexOutOfBoundsException("Uncovered list does not have enough elements");
        }
    }

    /**
     * Books the player's current component if they haven't reached the max (3).
     * Replaces it with a new one.
     *
     * @param player the player booking the component
     */
    public void bookComponent(Player player) {
        if (bookedMap.get(player).size() < 3) {
            bookedMap.get(player).add(viewMap.get(player));
            // remove component from hand (newComponent places component in hand in uncovered list)
            viewMap.put(player, null);
            newComponent(player);
        } else {
            throw new IllegalStateException("Too many components booked");
        }
    }

    /**
     * Takes and removes a booked component from the player's list based on index.
     *
     * @param player the player retrieving a booked component
     * @param num    the index of the component to take
     */
    public void takeBookedComponentToPlace(Player player, int num) {
        if (bookedMap.get(player).size() > num) {
            addComponentInHandToUncoveredList(player);
            viewMap.put(player, bookedMap.get(player).remove(num));
        } else {
            throw new IndexOutOfBoundsException("Not enough booked components");
        }
    }
}
