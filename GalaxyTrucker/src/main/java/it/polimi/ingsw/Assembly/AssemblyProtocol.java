package it.polimi.ingsw.Assembly;

import java.util.*;

import it.polimi.ingsw.Cards.*;
import it.polimi.ingsw.Components.*;
import it.polimi.ingsw.Shipboard.Player;
import it.polimi.ingsw.Application.*;

//tutto ancora da implementare
public class AssemblyProtocol {
    private Deck blockedDeck;
    private Deck[] decksList;
    private HourGlass hourGlass;
    private List<Component> coveredList;
    private List<Component> uncoveredList;
    private Map<Player, List<Component>> bookedMap;
    private Map<Player, Component> viewMap;
    private boolean deck[];

    public AssemblyProtocol(List<Card> cards, List<Component> components, GameType gameType) {
        coveredList = new ArrayList<>();
        coveredList.addAll(components);
        deck = new boolean[4];
        blockedDeck = new Deck(cards, gameType);
        decksList = new Deck[3];
        for (int i = 0; i < 3; i++) {
            decksList[i] = new Deck(cards, gameType);
        }
        for (int i = 0; i < 4; i++) {
            deck[i] = false;
        }
        hourGlass = new HourGlass();
        uncoveredList = new ArrayList<>();
        bookedMap = new HashMap<>();
        viewMap = new HashMap<>();
    }

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

    public void newComponent(Player player){
        if(viewMap.get(player) != null){
            uncoveredList.add(viewMap.get(player));
        }
        viewMap.put(player, coveredList.get(0));
        coveredList.remove(0);
    }

    public void chooseComponent(Player player, int num){
        //da capire come fare la scelta
    }

    public void bookComponent(Player player){
        if(bookedMap.get(player) == null){
            bookedMap.put(player, new ArrayList<>());
            newComponent(player);
        }
        if (bookedMap.get(player).size() < 3 && viewMap.containsKey(player)) {
            bookedMap.get(player).add(viewMap.get(player));
            newComponent(player);
        }
        else{
            System.out.println("Too many booked components");
        }
    }

    public boolean checkOccupation(int num){
        if(num > 0 && num < 3){
            num--;
            return deck[num];
        }
        else{
            return true;
        }
    }

    public List<Card> shuffleDeck(){
        List<Card> mainDeck = new ArrayList<Card>();
        mainDeck.addAll(blockedDeck.getCards());
        mainDeck.addAll(decksList[0].getCards());
        mainDeck.addAll(decksList[1].getCards());
        mainDeck.addAll(decksList[2].getCards());
        Collections.shuffle(mainDeck);
        return mainDeck;
    }

    public List<Component> getUncoveredList(){
        return uncoveredList;
    }

    public Map<Player, List<Component>> getBookedMap(){
        return bookedMap;
    }
    public Map<Player, Component> getViewMap(){
        return viewMap;
    }

    public HourGlass getHourGlass() {
        return hourGlass;
    }

}
