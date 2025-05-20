package it.polimi.ingsw.Connection.ClientSide;

import it.polimi.ingsw.Connection.ServerSide.DataExchanger;
import it.polimi.ingsw.Connection.ViewType;
import it.polimi.ingsw.View.AssemblyView.AssemblyViewTUI;
import it.polimi.ingsw.View.CorrectionView.CorrectionViewTUI;
import it.polimi.ingsw.View.EvaluationView.EvaluationViewTUI;
import it.polimi.ingsw.View.FlightView.FlightViewTUI;
import it.polimi.ingsw.View.GeneralView;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class that handles the client playing the game.
 *
 * @author carlo
 */

public class ClientGameHandler {

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

        start(views);

    }

    /**
     * Sets the player views in function of the view type he chose
     *
     * @param viewType
     * @param views
     */

    private void setViews(ViewType viewType, GeneralView[] views) {

        if (viewType == ViewType.TUI) {

            views[0] = new AssemblyViewTUI();
            views[1] = new CorrectionViewTUI();
            views[2] = new FlightViewTUI();
            views[3] = new EvaluationViewTUI();

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

    private void start(GeneralView[] views) {

        System.out.println("The game is starting!");

        DataExchanger dataExchanger = clientInfo.getDataExchanger();

        AtomicBoolean running = new AtomicBoolean(true);

        Thread messageReceiver = new Thread(new GameMessageReceiver(views, dataExchanger, running));
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
