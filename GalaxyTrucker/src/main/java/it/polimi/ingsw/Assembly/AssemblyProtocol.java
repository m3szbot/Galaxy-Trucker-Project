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
    private Deck blockedDeck;
    private Deck[] decksList;
    private HourGlass hourGlass;
    private List<Component> coveredList;
    private List<Component> uncoveredList;
    private Map<Player, Optional<List<Component>>> bookedMap;
    private Map<Player, Component> viewMap;
    private boolean deck[];


    /**
     * Initializes the assembly protocol with the game setup.
     * Prepares decks, covered components, and player maps.
     *
     * @param gameInformation information about players, cards, and components
     */
    public AssemblyProtocol(GameInformation gameInformation) {
        coveredList = new ArrayList<>();
        coveredList.addAll(gameInformation.getComponentList());
        deck = new boolean[4];
        blockedDeck = new Deck(gameInformation.getCardsList(), gameInformation.getGameType());
        decksList = new Deck[3];
        for (int i = 0; i < 3; i++) {
            //qui c'è da verificare che le carte già utlizzate vengano rimosse
            decksList[i] = new Deck(gameInformation.getCardsList(), gameInformation.getGameType());
        }
        for (int i = 0; i < 4; i++) {
            deck[i] = false;
        }
        hourGlass = new HourGlass();
        uncoveredList = new ArrayList<>();
        bookedMap = new HashMap<>();
        for(int i = 0; i < gameInformation.getPlayerList().size(); i++) {
            Player player = gameInformation.getPlayerList().get(i);
            bookedMap.put(player, Optional.empty());
        }
        viewMap = new HashMap<>();
    }

    /**
     * Marks a deck as revealed and returns it.
     *
     * @param num the deck index (1 to 3), 0 is blocked
     * @return the selected deck, or null if blocked
     */
    public Deck showDeck(int num){

        if(num == 1){
            deck[num] = true;
            return decksList[0];
        }
        else if(num == 2){
            deck[num] = true;
            return decksList[1];
        }
        else if(num == 3){
            deck[num] = true;
            return decksList[2];
        }
        else {
            System.out.println("The Deck in 0 position is blocked");
            return null;
        }
    }

    /**
     * Draws a new component for the player and updates the current view.
     * Moves the previous component (if any) to the uncovered list.
     *
     * @param player the player drawing a component
     */
    public void newComponent(Player player){
        if(viewMap.get(player) != null){
            uncoveredList.add(viewMap.get(player));
        }
        viewMap.put(player, coveredList.get(0));
        coveredList.remove(0);
    }

    /**
     * Allows the player to choose a component from the uncovered list.
     * The player's current component is returned to the uncovered list,
     * and the selected component replaces it in the player's view.
     *
     * @param player the player making the selection
     * @param num the index of the component in the uncovered list
     */
    public void chooseComponent(Player player, int num){
        Component component = viewMap.get(player);
        viewMap.put(player, uncoveredList.remove(num));
        uncoveredList.add(component);
        return;
    }

    /**
     * Books the player's current component if they haven't reached the max (3).
     * Replaces it with a new one.
     *
     * @param player the player booking the component
     */
    public void bookComponent(Player player){
        if (bookedMap.get(player).map(List::size).orElse(0) < 3 && viewMap.containsKey(player)) {
            bookedMap.get(player).ifPresent(list -> list.add(viewMap.get(player)));
            newComponent(player);
        }
        else{
            System.out.println("Too many booked components");
        }
    }

    /**
     * Checks whether the specified deck is already occupied.
     *
     * @param num the deck index (1 to 3)
     * @return true if occupied or invalid index, false otherwise
     */
    public boolean checkOccupation(int num){
        if(num > 0 && num < 3){
            num--;
            return deck[num];
        }
        else{
            return true;
        }
    }

    /**
     * Shuffles all cards from the blocked deck and the three side decks.
     *
     * @return a shuffled list of all cards
     */
    public List<Card> shuffleDeck(){
        List<Card> mainDeck = new ArrayList<Card>();
        mainDeck.addAll(blockedDeck.getCards());
        mainDeck.addAll(decksList[0].getCards());
        mainDeck.addAll(decksList[1].getCards());
        mainDeck.addAll(decksList[2].getCards());
        Collections.shuffle(mainDeck);
        return mainDeck;
    }

    /**
     * Returns the list of uncovered components on the table.
     */
    public List<Component> getUncoveredList(){
        return uncoveredList;
    }

    /**
     * Returns the map of booked components per player.
     */
    public Map<Player, Optional<List<Component>>> getBookedMap(){
        return bookedMap;
    }

    /**
     * Returns the current visible component for each player.
     */
    public Map<Player, Component> getViewMap(){
        return viewMap;
    }


    /**
     * Returns the HourGlass object for managing time mechanics.
     */
    public HourGlass getHourGlass() {
        return hourGlass;
    }

    /**
     * Takes and removes a booked component from the player's list based on index.
     *
     * @param player the player retrieving a booked component
     * @param num the index of the component to take
     * @return the component removed, or null if not found
     */
    public Component takeBookedComponentToPlace(Player player, int num){
        Component component = bookedMap.get(player).filter(list -> num >= 0 && num < list.size())
                .map(list -> list.remove(num))
                .orElse(null);
        return component;
    }
}
