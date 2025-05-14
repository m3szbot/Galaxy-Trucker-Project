package it.polimi.ingsw.Connection.ClientSide;

import it.polimi.ingsw.Model.GameInformation.ConnectionType;
import it.polimi.ingsw.Model.GameInformation.ViewType;
import it.polimi.ingsw.View.AssemblyView.AssemblyViewTUI;
import it.polimi.ingsw.View.CorrectionView.CorrectionViewTUI;
import it.polimi.ingsw.View.EvaluationView.EvaluationViewTUI;
import it.polimi.ingsw.View.FlightView.FlightViewTUI;
import it.polimi.ingsw.View.GeneralView;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

        if (clientInfo.getConnectionType() == ConnectionType.SOCKET) {
            startSCK(views);


        } else {
            startRMI(views);
        }

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

    private void startSCK(GeneralView[] views) {

        System.out.println("The game is starting!");

        ObjectOutputStream out = clientInfo.getOutputStream();
        ObjectInputStream in = clientInfo.getInputStream();

        AtomicBoolean running = new AtomicBoolean(true);

        Thread messageReceiver = new Thread(new GameMessageReceiver(views, in, running));
        Thread messageSender = new Thread(new GameMessageSender(out, running));

        messageReceiver.start();
        messageSender.start();

        try {

            messageReceiver.join();
            messageSender.join();

        } catch (InterruptedException e) {
            System.err.println("Receiver or sender thread was interrupted abnormally");

        }

    }


    /**
     * Starts the player threads that communicate with the server with
     * RMI protocol
     *
     * @param views
     */

    private void startRMI(GeneralView[] views) {

        //TODO

    }
}
