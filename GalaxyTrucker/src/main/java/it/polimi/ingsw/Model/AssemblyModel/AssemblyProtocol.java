package it.polimi.ingsw.Model.AssemblyModel;

import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.IllegalSelectionException;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * AssemblyProtocol handles the logic behind deck selection, component
 * booking and drawing, and other player interactions.
 * <p>
 * Synchronization: synchronized each method!
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
    // Component in hand for each player:
    // Not synchronized: each thread accesses only its own player's entry, map structure is not modified.
    // If no component in hand, null value is used (but do not remove entry).
    private final Map<Player, Component> playersInHandMap;

    // Booked components for each player:
    // Not synchronized: each thread accesses only its own player's entry, map structure is not modified.
    // If no component in hand, null value is used (but do not remove entry).
    private final Map<Player, List<Component>> playersBookedMap;
    // Signals if a component currently in hand has been booked before
    // so it cannot be returned to uncoveredList.
    private final Map<Player, Boolean> playersBookedFlag;
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
        playersInHandMap = new HashMap<>();
        playersBookedMap = new HashMap<>();
        playersBookedFlag = new HashMap<>();
        for (Player player : gameInformation.getPlayerList()) {
            playersInHandMap.put(player, null);
            playersBookedMap.put(player, new ArrayList<>());
            playersBookedFlag.put(player, false);
        }

    }

    public Map<Player, Boolean> getPlayersBookedFlag() {
        return playersBookedFlag;
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

    public Deck[] getAllDecks() {
        // TODO deadlock?
        synchronized (showableDecksList[0]) {
            synchronized (showableDecksList[1]) {
                synchronized (showableDecksList[2]) {
                    return showableDecksList;
                }
            }
        }
    }

    /**
     * Returns the list of covered components on the table.
     */
    public List<Component> getCoveredComponentsList() {
        synchronized (coveredComponentsList) {
            return coveredComponentsList;
        }
    }

    /**
     * Returns the list of uncovered components on the table.
     */
    public List<Component> getUncoveredComponentsList() {
        synchronized (uncoveredComponentsList) {
            return uncoveredComponentsList;
        }
    }

    /**
     * Returns the map of booked components per player.
     */
    public Map<Player, List<Component>> getPlayersBookedMap() {
        return playersBookedMap;
    }

    /**
     * Returns the current visible component for each player.
     */
    public Map<Player, Component> getPlayersInHandMap() {
        return playersInHandMap;
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
                /*
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
                */
                showableDecksList[index].setInUse(true);
                return showableDecksList[index];
            } else
                throw new IllegalSelectionException("Deck is in use by others");
        }
    }

    /**
     * Draws a new random component for the player and puts in the player's hand.
     * Moves the previous component in hand (if any) to the uncovered list.
     * Draws from coveredList until it is exhausted, then it draws from uncoveredList.
     * Synchronized.
     *
     * @param player the player drawing a component
     * @throws IllegalSelectionException if no more components are available
     */
    public void newComponent(Player player) throws IllegalSelectionException {
        // add previous component to uncovered list from hand
        addComponentInHandToUncoveredList(player);

        // add new random component to player's hand
        // from coveredComponentsList
        synchronized (coveredComponentsList) {
            if (!coveredComponentsList.isEmpty()) {
                int randomIndex = ThreadLocalRandom.current().nextInt(coveredComponentsList.size());
                playersInHandMap.put(player, coveredComponentsList.remove(randomIndex));
                return;
            }
        }

        // from uncoveredComponentsList
        synchronized (uncoveredComponentsList) {
            if (!uncoveredComponentsList.isEmpty()) {
                int randomIndex = ThreadLocalRandom.current().nextInt(uncoveredComponentsList.size());
                playersInHandMap.put(player, uncoveredComponentsList.remove(randomIndex));
                return;
            }
        }

        // coveredComponentsList and uncoveredComponentsList empty
        throw new IllegalSelectionException("Component lists are empty");
    }

    /**
     * If a component is present in the player's hand, return it to the uncovered list and empty the player's hand.
     * Synchronized.
     *
     * @author Boti
     */
    private void addComponentInHandToUncoveredList(Player player) {
        // if no component in hand, value == null
        // only return component in hand to uncovered list if there is a component in hand (!=null)
        if (playersInHandMap.get(player) != null) {
            synchronized (uncoveredComponentsList) {
                uncoveredComponentsList.add(playersInHandMap.get(player));
            }
            // remove returned component from hand
            playersInHandMap.put(player, null);
        }
    }

    /**
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
            if (index >= 0 && index < uncoveredComponentsList.size()) {
                addComponentInHandToUncoveredList(player);
                playersInHandMap.put(player, uncoveredComponentsList.remove(index));
            } else {
                throw new IllegalSelectionException("Uncovered component list is empty");
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
        if (playersBookedMap.get(player).size() < 2) {
            // book component
            playersBookedMap.get(player).add(playersInHandMap.get(player));
            // remove component from hand (newComponent places component in hand in uncovered list)
            playersInHandMap.put(player, null);

        } else {
            // booked map already full
            throw new IllegalSelectionException("Cannot book any more components, limit is reached (2).");
        }

    }

    /**
     * Takes and removes a booked component from the player's booked components based on index.
     *
     * @param player the player retrieving a booked component
     * @param index  the index of the component to take
     * @author Boti
     */
    public void chooseBookedComponent(Player player, int index) throws IllegalSelectionException {
        if (index >= 0 && index < playersBookedMap.get(player).size()) {
            addComponentInHandToUncoveredList(player);
            playersInHandMap.put(player, playersBookedMap.get(player).remove(index));
        } else {
            throw new IllegalSelectionException("Not enough booked components");
        }
    }


}
