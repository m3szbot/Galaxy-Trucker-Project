package it.polimi.ingsw;

import it.polimi.ingsw.Connection.ClientSide.RMI.VirtualClient;
import it.polimi.ingsw.Connection.ClientSide.utils.ClientInfo;
import it.polimi.ingsw.Connection.ClientSide.utils.ClientInputManager;
import it.polimi.ingsw.Connection.ServerSide.Server;
import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.GameMessenger;
import it.polimi.ingsw.Connection.ViewType;
import it.polimi.ingsw.Controller.Game.Game;
import it.polimi.ingsw.Controller.Sleeper;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Color;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.rmi.RemoteException;

public abstract class Mocker {
    private static Thread inputThread;
    private static Game game;


    public static Game getGame() {
        return game;
    }

    public static GameInformation getGameInformation() {
        return game.getGameInformation();
    }

    public static Player getFirstPlayer() {
        return game.getGameInformation().getPlayerList().getFirst();
    }

    public static GameMessenger getGameMessenger() {
        return ClientMessenger.getGameMessenger(game.getGameCode());
    }

    public static void simulateClientInput(String input) {
        inputThread = new Thread(() -> {
            ClientInputManager.setTestInput(input);
        });
        inputThread.start();
    }


    /**
     * Set up a Game with gameType NORMALGAME, 1 player, GameMessenger and PlayerMessenger.
     * Reset simulatedInput.
     *
     * @return
     */
    public static void mockNormalGame1Player() {
        resetInputSimulation();

        // find and keep correct setup order
        // setup Game
        int gameCode = 0;
        Game game = new Game(gameCode);
        game.setGameType(GameType.NORMALGAME);
        game.setNumberOfPlayers(1);

        // add 1 player
        Player player1 = new Player("Player1", Color.BLUE, game.getGameInformation());
        game.addPlayer(player1, true);

        // server
        Server centralServer = new Server();
        ClientMessenger.setCentralServer(centralServer);
        centralServer.addNickName(player1.getNickName());


        // setup messengers
        ClientMessenger.addGame(gameCode, game.getGameInformation());
        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setViewType(ViewType.TUI);
        clientInfo.setGameCode(gameCode);
        try {
            VirtualClient virtualClient = new VirtualClient(clientInfo);
            ClientMessenger.getGameMessenger(gameCode).addPlayer(player1, virtualClient);
        } catch (RemoteException ex) {
        }

        game.setUpPhases();

        // set timeout to 0.25 sec
        ClientInputManager.setTimeOut(250);

        Mocker.game = game;
    }

    private static void resetInputSimulation() {
        int trials = 100;

        if (inputThread != null) {
            while (trials > 0 && (ClientInputManager.getTestRunning() || ClientInputManager.getSimulatedInput() != null)) {
                ClientInputManager.endTestInput();

                try {
                    // wait for previous input simulator thread to finish
                    inputThread.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                Sleeper.sleepXSeconds(0.5);
                trials--;
            }
        }

        if (trials == 0)
            throw new IllegalStateException("Previous test couldn't finish");
    }

    public static void mockNormalGame2Players() {
        resetInputSimulation();

        // find and keep correct setup order
        // setup Game
        int gameCode = 1;
        Game game = new Game(gameCode);
        game.setGameType(GameType.NORMALGAME);
        game.setNumberOfPlayers(2);

        // add 1 player
        Player player1 = new Player("Player1", Color.BLUE, game.getGameInformation());
        Player player2 = new Player("Player2", Color.RED, game.getGameInformation());
        game.addPlayer(player1, true);
        game.addPlayer(player2, false);


        // setup messengers
        ClientMessenger.addGame(gameCode, game.getGameInformation());
        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setViewType(ViewType.TUI);
        clientInfo.setGameCode(gameCode);
        try {
            VirtualClient virtualClient = new VirtualClient(clientInfo);
            ClientMessenger.getGameMessenger(gameCode).addPlayer(player1, virtualClient);
            ClientMessenger.getGameMessenger(gameCode).addPlayer(player2, virtualClient);
        } catch (RemoteException ex) {
        }

        game.setUpPhases();

        // set timeout to 1 sec
        ClientInputManager.setTimeOut(1000);

        Mocker.game = game;
    }
}
