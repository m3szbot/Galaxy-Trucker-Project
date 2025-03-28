package it.polimi.ingsw.assembly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import it.polimi.ingsw.cards.*;

//tutto ancora da implementare
public class AssemblyProtocol {
    private Deck blockedDeck;
    private Deck[] decksList;


    public List<Card> shuffleDeck(){
        List<Card> mainDeck = new ArrayList<Card>();
        mainDeck.addAll(blockedDeck.getCards());
        mainDeck.addAll(decksList[0].getCards());
        mainDeck.addAll(decksList[1].getCards());
        mainDeck.addAll(decksList[2].getCards());
        Collections.shuffle(mainDeck);
        return mainDeck;
    }
}
