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
        assertEquals(4, assemblyProtocol.getPlayersBookedComponents().size());
        assertEquals(0, assemblyProtocol.getPlayersBookedComponents().get(playerA).size());
        assertNull(assemblyProtocol.getPlayersInHandComponents().get(playerA));
        assertNull(assemblyProtocol.getPlayersPlacingBookedComponentsCache().get(playerA));
    }

    @Test
    void drawTwoNewComponents() throws IllegalSelectionException {
        // draw first component
        assemblyProtocol.newComponent(playerA);
        assertNotNull(assemblyProtocol.getPlayersInHandComponents().get(playerA));
        Component inHand = assemblyProtocol.getPlayersInHandComponents().get(playerA);
        // draw second component
        assemblyProtocol.newComponent(playerA);
        assertNotNull(assemblyProtocol.getPlayersInHandComponents().get(playerA));
        assertNotEquals(inHand, assemblyProtocol.getPlayersInHandComponents().get(playerA));
        assertEquals(inHand, assemblyProtocol.getUncoveredComponentsList().getFirst());
    }

    @Test
    void chooseOneComponentFromUncoveredList() throws IllegalSelectionException {
        // put 1 component in uncoveredList
        assemblyProtocol.newComponent(playerA);
        assemblyProtocol.newComponent(playerA);
        Component inHand = assemblyProtocol.getPlayersInHandComponents().get(playerA);
        // choose from uncoveredList
        assertThrows(IllegalSelectionException.class, () -> {
            assemblyProtocol.chooseUncoveredComponent(playerA, 1);
        });
        assemblyProtocol.chooseUncoveredComponent(playerA, 0);
        assertNotEquals(inHand, assemblyProtocol.getPlayersInHandComponents().get(playerA));
        assertEquals(inHand, assemblyProtocol.getUncoveredComponentsList().getFirst());
    }

    @Test
    void testUncoveredListIndexes() throws IllegalSelectionException {
        assertEquals(0, assemblyProtocol.getUncoveredComponentsList().size());

        assertThrows(IllegalSelectionException.class, () -> {
                    assemblyProtocol.chooseUncoveredComponent(playerA, 0);
                }
        );

        for (int i = 0; i < 10; i++)
            assemblyProtocol.newComponent(playerA);

        // indexes out of range
        // 1 element always left in hand
        assertEquals(9, assemblyProtocol.getUncoveredComponentsList().size());
        assertThrows(IllegalSelectionException.class, () -> {
                    assemblyProtocol.chooseUncoveredComponent(playerA, 9);
                }
        );
        assertThrows(IllegalSelectionException.class, () -> {
                    assemblyProtocol.chooseUncoveredComponent(playerA, -1);
                }
        );

        // indexes in range, component reput into uncovered list
        // 1 element always left in hand
        for (int i = 0; i < 9; i++)
            assemblyProtocol.chooseUncoveredComponent(playerA, i);

        assertEquals(9, assemblyProtocol.getUncoveredComponentsList().size());
    }

    @Test
    void testBookSameComponentTwice() throws IllegalSelectionException {
        assemblyProtocol.newComponent(playerA);
        Component comp = assemblyProtocol.getPlayersInHandComponents().get(playerA);
        assemblyProtocol.bookComponent(playerA);
        assemblyProtocol.getPlayersInHandComponents().put(playerA, comp);
        assertThrows(IllegalStateException.class, () -> {
            assemblyProtocol.bookComponent(playerA);
        });
    }

    @Test
    void testChooseBookedComponent() throws IllegalSelectionException {
        assertEquals(0, assemblyProtocol.getPlayersBookedComponents().get(playerA).size());

        // book component
        assemblyProtocol.newComponent(playerA);
        Component inHand = assemblyProtocol.getPlayersInHandComponents().get(playerA);
        assemblyProtocol.bookComponent(playerA);
        assertNull(assemblyProtocol.getPlayersInHandComponents().get(playerA));
        assertEquals(1, assemblyProtocol.getPlayersBookedComponents().get(playerA).size());
        assertEquals(inHand, assemblyProtocol.getPlayersBookedComponents().get(playerA).getFirst());

        // choose booked component
        assemblyProtocol.chooseBookedComponent(playerA, 0);
        assertEquals(inHand, assemblyProtocol.getPlayersInHandComponents().get(playerA));
        assertEquals(0, assemblyProtocol.getPlayersBookedComponents().get(playerA).size());
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
        // test with newComponent
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
            assemblyProtocol.getPlayersInHandComponents().remove(playerA);
        }

        assertEquals(0, assemblyProtocol.getCoveredComponentsList().size());
        assertEquals(0, assemblyProtocol.getUncoveredComponentsList().size());

        assertThrows(IllegalSelectionException.class, () -> {
            assemblyProtocol.newComponent(playerA);
        });
    }

    @Test
    void testChooseUncoveredComponentExhaustCoveredListExhaustUncoveredList() throws IllegalSelectionException {
        // test with newComponent + chooseUncoveredComponent
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
            assemblyProtocol.chooseUncoveredComponent(playerA,
                    ThreadLocalRandom.current().nextInt(0, assemblyProtocol.getUncoveredComponentsList().size()));
            // discard component in hand
            assemblyProtocol.getPlayersInHandComponents().remove(playerA);
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
        Component inHand = assemblyProtocol.getPlayersInHandComponents().get(playerA);
        assemblyProtocol.bookComponent(playerA);
        assertNull(assemblyProtocol.getPlayersInHandComponents().get(playerA));
        assertEquals(inHand, assemblyProtocol.getPlayersBookedComponents().get(playerA).getFirst());
    }


    @Test
    void testBookTooManyComponents() throws IllegalSelectionException {
        // book 3 components
        assemblyProtocol.newComponent(playerA);
        assemblyProtocol.bookComponent(playerA);
        assemblyProtocol.newComponent(playerA);
        assemblyProtocol.bookComponent(playerA);

        // book again - hand emptied
        assertThrows(IllegalSelectionException.class, () -> {
            assemblyProtocol.bookComponent(playerA);
        });

        // booked full
        assemblyProtocol.newComponent(playerA);
        assertThrows(IllegalSelectionException.class, () -> {
            assemblyProtocol.bookComponent(playerA);
        });

        assemblyProtocol.chooseBookedComponent(playerA, 0);
    }

    @Test
    void chooseBookedComponentFromEmptyList() {
        assertThrows(IllegalSelectionException.class, () -> {
            assemblyProtocol.chooseBookedComponent(playerA, 0);
        });
    }

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
                synchronized (lock) {
                    while (!assemblyProtocol.getCoveredComponentsList().isEmpty()) {
                        try {
                            Player player = gameInformation.getPlayerList().get(ThreadLocalRandom.current().nextInt(0,
                                    gameInformation.getPlayerList().size()));
                            assemblyProtocol.newComponent(player);
                            // add component in hand to result list
                            resultList.add(assemblyProtocol.getPlayersInHandComponents().get(player));
                        } catch (IllegalSelectionException e) {
                        }
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
        // 1 component left in hand, 4 players
        assertEquals(148, assemblyProtocol.getUncoveredComponentsList().size());


    }

    @Test
    void testPreviouslyBookedComponentInHand() throws IllegalSelectionException {
        // test booking and choosing and rebooking 2 components

        // TEST BOOK COMPONENT:

        // book from empty hand error
        assertEquals(0, assemblyProtocol.getPlayersBookedComponents().get(playerA).size());
        assertThrows(IllegalSelectionException.class, () -> {
            assemblyProtocol.bookComponent(playerA);
        });

        // book 2 components
        assemblyProtocol.newComponent(playerA);
        assemblyProtocol.bookComponent(playerA);
        assemblyProtocol.newComponent(playerA);
        assemblyProtocol.bookComponent(playerA);
        assertEquals(2, assemblyProtocol.getPlayersBookedComponents().get(playerA).size());

        // book again - hand emptied
        assertThrows(IllegalSelectionException.class, () -> {
            assemblyProtocol.bookComponent(playerA);
        });

        assemblyProtocol.newComponent(playerA);
        // fail to book new component
        assertThrows(IllegalSelectionException.class, () -> {
            assemblyProtocol.bookComponent(playerA);
        });


        // TEST PREVIOUSLY BOOKED IN HAND - MUST BE RETURNED TO BOOKED
        Component booked1 = assemblyProtocol.getPlayersBookedComponents().get(playerA).get(1);
        Component booked2 = assemblyProtocol.getPlayersBookedComponents().get(playerA).get(0);

        assemblyProtocol.chooseBookedComponent(playerA, 1);
        int uncoveredSize = assemblyProtocol.getUncoveredComponentsList().size();
        assertEquals(1, assemblyProtocol.getPlayersBookedComponents().get(playerA).size());
        assertFalse(assemblyProtocol.getPlayersBookedComponents().get(playerA).contains(booked1));
        assertEquals(booked1, assemblyProtocol.getPlayersPlacingBookedComponentsCache().get(playerA));
        assertEquals(booked1, assemblyProtocol.getPlayersInHandComponents().get(playerA));

        // get new component - prevBooked in hand gets rebooked
        assemblyProtocol.newComponent(playerA);
        assertEquals(2, assemblyProtocol.getPlayersBookedComponents().get(playerA).size());
        assertTrue(assemblyProtocol.getPlayersBookedComponents().get(playerA).contains(booked1));
        assertEquals(booked1, assemblyProtocol.getPlayersPlacingBookedComponentsCache().get(playerA));
        assertNotEquals(booked1, assemblyProtocol.getPlayersInHandComponents().get(playerA));
        assertEquals(uncoveredSize, assemblyProtocol.getUncoveredComponentsList().size());

        // put the other booked in hand (prevBooked still booked)
        assemblyProtocol.chooseBookedComponent(playerA, 0);
        assertEquals(1, assemblyProtocol.getPlayersBookedComponents().get(playerA).size());
        assertTrue(assemblyProtocol.getPlayersBookedComponents().get(playerA).contains(booked1));
        assertEquals(booked2, assemblyProtocol.getPlayersPlacingBookedComponentsCache().get(playerA));
        assertEquals(booked2, assemblyProtocol.getPlayersInHandComponents().get(playerA));
        assertEquals(uncoveredSize + 1, assemblyProtocol.getUncoveredComponentsList().size());
    }

    @RepeatedTest(3)
    void testSimulate4PlayerThreadsRandomMethods() throws InterruptedException {
        // simulate players and wait for unexpected exceptions

        // setup tests
        assertEquals(4, gameInformation.getPlayerList().size());

        // create threads
        ExecutorService executor = Executors.newFixedThreadPool(gameInformation.getPlayerList().size());


        // create adder threads
        for (Player player : gameInformation.getPlayerList()) {
            executor.submit((() -> {
                // thread task
                // execute random commands until shut down
                while (true) {
                    try {
                        randomCommand(player);
                    } catch (IllegalSelectionException e) {
                    }
                }
            }));
        }

        executor.shutdown();
        // execute until shut down
        executor.awaitTermination(15, TimeUnit.SECONDS);

        // result tests

    }

    private void randomCommand(Player player) throws IllegalSelectionException, InterruptedException {
        int command = ThreadLocalRandom.current().nextInt(0, 5);

        // indexes from -2 to simulate wrong selection input
        switch (command) {
            case 0 -> {
                // show deck (and return it)
                int index = ThreadLocalRandom.current().nextInt(-2, 3);
                assemblyProtocol.showDeck(index);
                // watch deck
                Thread.sleep(5);
                // return deck
                assemblyProtocol.getDeck(index).setInUse(false);
            }
            case 1 -> {
                // new component
                assemblyProtocol.newComponent(player);
            }
            case 2 -> {
                // book component
                assemblyProtocol.bookComponent(player);
            }
            case 3 -> {
                // choose uncovered component
                int index = ThreadLocalRandom.current().nextInt(-2, assemblyProtocol.getUncoveredComponentsList().size());
                assemblyProtocol.chooseUncoveredComponent(player, index);
            }
            case 4 -> {
                // choose booked component
                int index = ThreadLocalRandom.current().nextInt(-2, assemblyProtocol.getUncoveredComponentsList().size());
                assemblyProtocol.chooseBookedComponent(player, index);
            }
        }
    }


}