package it.polimi.ingsw.Connection.ClientSide.Socket;

import it.polimi.ingsw.Connection.ClientSide.utils.ClientInfo;
import it.polimi.ingsw.Connection.ServerSide.Socket.SocketDataExchanger;
import it.polimi.ingsw.Connection.ViewType;
import it.polimi.ingsw.View.GeneralView;
import it.polimi.ingsw.View.TUI.TUIView;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class that handles the client playing the game.
 *
 * @author carlo
 */

public class GameStarter {

    GeneralView[] views = new GeneralView[4];
    ClientInfo clientInfo;

    /**
     * Starts the handler that handles the player communication with
     * the server during the game
     *
     * @param clientInfo
     */

    public void start(ClientInfo clientInfo) {

        this.clientInfo = clientInfo;

        setViews(clientInfo.getViewType(), views);

        run(views);

    }

    /**
     * Sets the player views in function of the view type he chose
     *
     * @param viewType
     * @param views
     */

    private void setViews(ViewType viewType, GeneralView[] views) {

        if (viewType == ViewType.TUI) {

            views[0] = new TUIView();
            views[1] = new TUIView();
            views[2] = new TUIView();
            views[3] = new TUIView();

        } else {/*

            views[0] = new AssemblyViewGUI();
            views[1] = new CorrectionViewGUI();
            views[2] = new FlightViewGUI();
            views[3] = new EvaluationViewGUI();
            */
        }

    }

    /**
     * Starts the player threads that communicate with the server with
     * socket protocol
     *
     * @param views
     */

    private void run(GeneralView[] views) {

        System.out.println("The game is starting!");

        SocketDataExchanger dataExchanger = clientInfo.getDataExchanger();

        AtomicBoolean running = new AtomicBoolean(true);

        Thread messageReceiver = new Thread(new GameMessageReceiver(views, dataExchanger, running, clientInfo.getUserInput()));
        Thread messageSender = new Thread(new GameMessageSender(dataExchanger, running, clientInfo.getUserInput()));

        messageReceiver.start();
        messageSender.start();

        try {

            messageReceiver.join();
            messageSender.join();

        } catch (InterruptedException e) {
            System.err.println("Receiver or sender thread was interrupted abnormally");

        }

    }

}
