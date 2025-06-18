package it.polimi.ingsw.Model.AssemblyModel;

import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.IllegalSelectionException;
import it.polimi.ingsw.Model.ShipBoard.Color;
import it.polimi.ingsw.Model.ShipBoard.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Single thread tests + multithread tests (sync)
 * In the game, 1 thread for each player - important for player maps (inHand, booked
 *
 * @author Boti
 */

class AssemblyProtocolTest {
    GameInformation gameInformation;
    AssemblyProtocol assemblyProtocol;
    Player playerA, playerB, playerC, playerD;

    @BeforeEach
    void setUp() {
        // set up gameInformation
        gameInformation = new GameInformation();
        gameInformation.setUpGameInformation(GameType.NORMALGAME, 4);
        // set up players
        playerA = new Player("A", Color.BLUE, gameInformation);
        playerB = new Player("B", Color.RED, gameInformation);
        playerC = new Player("C", Color.YELLOW, gameInformation);
        playerD = new Player("D", Color.GREEN, gameInformation);

        gameInformation.setMaxNumberOfPlayers(4);
        gameInformation.addPlayer(playerA);
        gameInformation.addPlayer(playerB);
        gameInformation.addPlayer(playerC);
        gameInformation.addPlayer(playerD);

        // set up assemblyProtocol
        assemblyProtocol = new AssemblyProtocol(gameInformation);
    }

    // decks remove cards from a copy of cardsList, but not cardsList itself
    // because it cancels flightBoard cards
    @Test
    void testDeckCardsNotRemovedFromGameInformation() {
        gameInformation = new GameInformation();
        gameInformation.setUpGameInformation(GameType.NORMALGAME, 4);
        int cardCount = gameInformation.getCardsList().size();
        assemblyProtocol = new AssemblyProtocol(gameInformation);
        assertEquals(cardCount, gameInformation.getCardsList().size());
    }

    @Test
    void checkSetup() throws IllegalSelectionException {
        // check initial values
        assertNotNull(assemblyProtocol.getHourGlass());
        assertThrows(IllegalSelectionException.class, () -> {
            assemblyProtocol.showDeck(0);
        });
        assertEquals(3, assemblyProtocol.showDeck(1).getNumCards());
        assertEquals(3, assemblyProtocol.showDeck(2).getNumCards());
        assertEquals(3, assemblyProtocol.showDeck(3).getNumCards());
        assertEquals(152, assemblyProtocol.getCoveredComponentsList().size());
        assertEquals(0, assemblyProtocol.getUncoveredComponentsList().size());
        assertNull(assemblyProtocol.getPlayersInHandMap().get(playerA));
        assertEquals(0, assemblyProtocol.getPlayersBookedMap().get(playerA).size());
        assertEquals(4, assemblyProtocol.getPlayersInHandMap().size());
        assertEquals(4, assemblyProtocol.getPlayersBookedMap().size());
        assertEquals(4, assemblyProtocol.getPlayersBookedFlag().size());

    }

    @Test
    void drawTwoNewComponents() throws IllegalSelectionException {
        // draw first component
        assemblyProtocol.newComponent(playerA);
        assertNotNull(assemblyProtocol.getPlayersInHandMap().get(playerA));
        Component inHand = assemblyProtocol.getPlayersInHandMap().get(playerA);
        // draw second component
        assemblyProtocol.newComponent(playerA);
        assertNotNull(assemblyProtocol.getPlayersInHandMap().get(playerA));
        assertNotEquals(inHand, assemblyProtocol.getPlayersInHandMap().get(playerA));
        assertEquals(inHand, assemblyProtocol.getUncoveredComponentsList().getFirst());
    }

    @Test
    void chooseOneComponentFromUncoveredList() throws IllegalSelectionException {
        // put 1 component in uncoveredList
        assemblyProtocol.newComponent(playerA);
        assemblyProtocol.newComponent(playerA);
        Component inHand = assemblyProtocol.getPlayersInHandMap().get(playerA);
        // choose from uncoveredList
        assertThrows(IllegalSelectionException.class, () -> {
            assemblyProtocol.chooseUncoveredComponent(playerA, 1);
        });
        assemblyProtocol.chooseUncoveredComponent(playerA, 0);
        assertNotEquals(inHand, assemblyProtocol.getPlayersInHandMap().get(playerA));
        assertEquals(inHand, assemblyProtocol.getUncoveredComponentsList().getFirst());
    }

    @Test
    void testShowDeckDeckInUse() throws IllegalSelectionException {
        // use decks
        assemblyProtocol.showDeck(1);
        assertThrows(IllegalSelectionException.class, () -> {
            assemblyProtocol.showDeck(1);
        });
        assemblyProtocol.showDeck(2);
        assertThrows(IllegalSelectionException.class, () -> {
            assemblyProtocol.showDeck(2);
        });
        assemblyProtocol.showDeck(3);
        assertThrows(IllegalSelectionException.class, () -> {
            assemblyProtocol.showDeck(3);
        });
        // free decks
        assemblyProtocol.getDeck(0).setInUse(false);
        assemblyProtocol.getDeck(1).setInUse(false);
        assemblyProtocol.getDeck(2).setInUse(false);
        // reuse decks
        assemblyProtocol.showDeck(1);
        assemblyProtocol.showDeck(2);
        assemblyProtocol.showDeck(3);
    }

    @Test
    void testShowDeckIndexes() throws IllegalSelectionException {
        assemblyProtocol.showDeck(1);
        assemblyProtocol.showDeck(2);
        assemblyProtocol.showDeck(3);
        assertThrows(IllegalSelectionException.class, () -> {
            assemblyProtocol.showDeck(0);
        });
        assertThrows(IllegalSelectionException.class, () -> {
            assemblyProtocol.showDeck(4);
        });
    }


    @Test
    void testNewComponentExhaustCoveredListExhaustUncoveredList() throws IllegalSelectionException {
        int componentsCount = assemblyProtocol.getCoveredComponentsList().size();
        assertEquals(componentsCount, assemblyProtocol.getCoveredComponentsList().size());
        assertEquals(0, assemblyProtocol.getUncoveredComponentsList().size());

        while (!assemblyProtocol.getCoveredComponentsList().isEmpty()) {
            assemblyProtocol.newComponent(playerA);
        }

        assertEquals(0, assemblyProtocol.getCoveredComponentsList().size());
        // 1 component left in hand
        assertEquals(componentsCount - 1, assemblyProtocol.getUncoveredComponentsList().size());

        // empty uncovered list
        // discard components from hand
        while (!assemblyProtocol.getUncoveredComponentsList().isEmpty()) {
            assemblyProtocol.newComponent(playerA);
            // discard component in hand
            assemblyProtocol.getPlayersInHandMap().remove(playerA);
        }

        assertEquals(0, assemblyProtocol.getCoveredComponentsList().size());
        assertEquals(0, assemblyProtocol.getUncoveredComponentsList().size());

        assertThrows(IllegalSelectionException.class, () -> {
            assemblyProtocol.newComponent(playerA);
        });
    }

    @Test
    void bookOneComponent() throws IllegalSelectionException {
        assemblyProtocol.newComponent(playerA);
        Component inHand = assemblyProtocol.getPlayersInHandMap().get(playerA);
        assemblyProtocol.bookComponent(playerA);
        assertNull(assemblyProtocol.getPlayersInHandMap().get(playerA));
        assertEquals(inHand, assemblyProtocol.getPlayersBookedMap().get(playerA).getFirst());
    }


    @Test
    void testBookTooManyComponents() throws IllegalSelectionException {
        // book 3 components
        assemblyProtocol.newComponent(playerA);
        assemblyProtocol.bookComponent(playerA);
        assemblyProtocol.newComponent(playerA);
        assemblyProtocol.bookComponent(playerA);
        assemblyProtocol.newComponent(playerA);
        // booked full
        assertThrows(IllegalSelectionException.class, () -> {
            assemblyProtocol.bookComponent(playerA);
        });

        assemblyProtocol.chooseBookedComponent(playerA, 0);
    }

    @Test
    void chooseBookedComponent() throws IllegalSelectionException {
        assemblyProtocol.newComponent(playerA);
        Component inHand = assemblyProtocol.getPlayersInHandMap().get(playerA);
        assemblyProtocol.bookComponent(playerA);
        assertNull(assemblyProtocol.getPlayersInHandMap().get(playerA));
        assemblyProtocol.chooseBookedComponent(playerA, 0);
        assertEquals(inHand, assemblyProtocol.getPlayersInHandMap().get(playerA));
        assertEquals(0, assemblyProtocol.getPlayersBookedMap().get(playerA).size());
    }

    @Test
    void chooseBookedComponentFromEmptyList() {
        assertThrows(IllegalSelectionException.class, () -> {
            assemblyProtocol.chooseBookedComponent(playerA, 0);
        });
    }


    // TEST SYNCHRONIZATION
    // TODO

    @RepeatedTest(500)
    void testConcurrentShowDeck() throws InterruptedException {
        // ADD Thread.sleep() to the source code to delay operations and check concurrent access

        // setup tests
        assertEquals(4, gameInformation.getPlayerList().size());
        for (Deck deck : assemblyProtocol.getAllDecks())
            assertFalse(deck.getInUse());

        // create threads
        ExecutorService executor = Executors.newFixedThreadPool(100);
        // save the returned decks of showDeck in a list
        // shouldn't have duplicates! - check with streams
        // the return list modified by the threads must be synchronized too!
        List<Deck> returnedDecks = Collections.synchronizedList(new ArrayList<>());


        // create adder threads
        for (int i = 0; i < 100; i++) {
            executor.submit((() -> {
                // thread task
                try {
                    returnedDecks.add(assemblyProtocol.showDeck(ThreadLocalRandom.current().nextInt(1, 4)));
                } catch (IllegalSelectionException e) {
                }
            }));
        }

        executor.shutdown();
        executor.awaitTermination(15, TimeUnit.SECONDS);

        // result tests
        // all decks in use
        for (Deck deck : assemblyProtocol.getAllDecks())
            assertTrue(deck.getInUse());

        // all decks only returned once
        assertEquals(3, returnedDecks.size());
        for (Deck deck : returnedDecks) {
            assertEquals(1, returnedDecks.stream().filter(deck::equals).count());
        }

    }

    // TODO
    @RepeatedTest(500)
    void testConcurrentNewComponentFromCoveredList() throws InterruptedException {
        // ADD Thread.sleep() to the source code to delay operations and check concurrent access

        // setup tests
        assertEquals(4, gameInformation.getPlayerList().size());
        assertEquals(152, assemblyProtocol.getCoveredComponentsList().size());
        assertEquals(0, assemblyProtocol.getUncoveredComponentsList().size());

        // create threads
        ExecutorService executor = Executors.newFixedThreadPool(100);
        // save the returned decks of showDeck in a list
        // shouldn't have duplicates! - check with streams
        // the return list modified by the threads must be synchronized too!
        List<Component> resultList = Collections.synchronizedList(new ArrayList<>());
        Object lock = new Object();

        // create adder threads
        for (int i = 0; i < 100; i++) {

            executor.submit((() -> {
                // thread task
                while (!assemblyProtocol.getCoveredComponentsList().isEmpty()) {
                    try {
                        synchronized (lock) {
                            assemblyProtocol.newComponent(playerA);
                            // add component in hand to result list
                            resultList.add(assemblyProtocol.getPlayersInHandMap().get(playerA));
                        }
                    } catch (IllegalSelectionException e) {
                    }
                }
            }));
        }

        executor.shutdown();
        executor.awaitTermination(15, TimeUnit.SECONDS);

        // result tests
        // each component added only once
        for (Component component : resultList) {
            assertEquals(1, resultList.stream().filter(component::equals).count());
        }
        assertEquals(152, resultList.size());
        assertEquals(0, assemblyProtocol.getCoveredComponentsList().size());
        // 1 component left in hand
        assertEquals(151, assemblyProtocol.getUncoveredComponentsList().size());


    }

}