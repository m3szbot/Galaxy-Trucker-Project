package it.polimi.ingsw.Connection.ClientSide;

import it.polimi.ingsw.Model.GameInformation.ConnectionType;
import it.polimi.ingsw.Model.GameInformation.ViewType;
import it.polimi.ingsw.View.AssemblyView.AssemblyViewTUI;
import it.polimi.ingsw.View.CorrectionView.CorrectionViewTUI;
import it.polimi.ingsw.View.EvaluationView.EvaluationViewTUI;
import it.polimi.ingsw.View.FlightView.FlightViewTUI;
import it.polimi.ingsw.View.GeneralView;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
     * @param clientInfo
     */

    public void start(ClientInfo clientInfo) {

        this.clientInfo = clientInfo;

        setViews(clientInfo.getViewType(), views);

        if (clientInfo.getConnectionType() == ConnectionType.Socket) {
            startSCK(views);


        } else {
            startRMI(views);
        }

    }

    /**
     * Sets the player views in function of the view type he chose
     * @param viewType
     * @param views
     */

    private void setViews(ViewType viewType, GeneralView[] views) {

        if (viewType == ViewType.CLI) {

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
     * @param views
     */

    private void startSCK(GeneralView[] views) {

        ObjectInputStream in;
        DataOutputStream out;
        AtomicBoolean running = new AtomicBoolean(true);

        try {
            in = new ObjectInputStream(clientInfo.getServerSocket().getInputStream());
            out = new DataOutputStream(clientInfo.getServerSocket().getOutputStream());
        } catch (IOException e) {
            System.err.println("A critical error occurred while opening streams");
            return;
        }

        Thread messageReceiver = new Thread(new GameMessageReceiver(views, in, running));
        Thread messageSender = new Thread(new GameMessageSender(out, running));

        messageReceiver.start();
        messageSender.start();

        try {

            messageReceiver.join();

            try {
                in.close();
                out.close();
            } catch (IOException e) {
               System.err.println("Error while closing sockets");
            }

        } catch (InterruptedException e) {
            System.err.println("Receiver thread was interrupted abnormally");

            try{

                in.close();
                out.close();

            }catch (IOException ex){

                System.err.println("Error while closing sockets");

            }
        }

    }


    /**
     * Starts the player threads that communicate with the server with
     * RMI protocol
     * @param views
     */

    private void startRMI(GeneralView[] views) {

        //TODO

    }
}
