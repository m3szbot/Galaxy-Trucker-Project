package it.polimi.ingsw.Controller.Cards;

/**
 * @author carlo
 */
/*
class EnginePowerChoiceTest {

    class Operator implements EnginePowerChoice {
    }

    ;

    Operator operator = new Operator();

    GameInformation gameInformation;

    Player player;

    FlightView flightView;

    @BeforeEach
    void initialize() {

        gameInformation = new GameInformation();
        gameInformation.setGameType(GameType.TESTGAME);

        player = new Player("player", Color.BLUE, gameInformation);
    }

    @AfterEach
    void reestablishStdin() {
        System.setIn(System.in);
    }


    @Test
    void threeEnginesActivatedWithIncorrectValues() throws NotPermittedPlacementException {

        player.getShipBoard().addComponent(new Battery(new SideType[]{SideType.Double, SideType.Universal, SideType.Single, SideType.Double}, 3), 6, 6);
        player.getShipBoard().addComponent(new Component(new SideType[]{SideType.Double, SideType.Universal, SideType.Double, SideType.Single}), 4, 4);

        String playerResponses = "true\n0\n4\n5\n3\n5 5\n3 3\n5 5\n3 3\n5 5\n";
        ByteArrayInputStream in = new ByteArrayInputStream(playerResponses.getBytes());
        System.setIn(in);

        flightView = new FlightViewTUI();

        assertTrue(operator.chooseEnginePower(player, gameInformation) == 11);

    }

}

 */