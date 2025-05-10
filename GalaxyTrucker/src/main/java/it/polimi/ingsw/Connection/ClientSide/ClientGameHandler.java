package it.polimi.ingsw.Connection.ClientSide;

import it.polimi.ingsw.Model.GameInformation.ConnectionType;
import it.polimi.ingsw.Model.GameInformation.ViewType;
import it.polimi.ingsw.View.AssemblyView.AssemblyViewGUI;
import it.polimi.ingsw.View.AssemblyView.AssemblyViewTUI;
import it.polimi.ingsw.View.CorrectionView.CorrectionViewGUI;
import it.polimi.ingsw.View.CorrectionView.CorrectionViewTUI;
import it.polimi.ingsw.View.EvaluationView.EvaluationViewGUI;
import it.polimi.ingsw.View.EvaluationView.EvaluationViewTUI;
import it.polimi.ingsw.View.FlightView.FlightViewGUI;
import it.polimi.ingsw.View.FlightView.FlightViewTUI;
import it.polimi.ingsw.View.GeneralView;
import it.polimi.ingsw.View.SetUpView.SetUpViewGUI;
import it.polimi.ingsw.View.SetUpView.SetUpViewTUI;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Class that handles the client playing the game.
 *
 * @author carlo
 */

public class ClientGameHandler {

    GeneralView[] views = new GeneralView[5];
    ClientInfo clientInfo;

    public void start(ClientInfo clientInfo){

        this.clientInfo = clientInfo;

        setViews(clientInfo.getViewType(), views);

        if(clientInfo.getConnectionType() == ConnectionType.Socket){
            int result = startSCK(views);
        }
        else{
            startRMI(views);
        }

    }

    private void setViews(ViewType viewType, GeneralView[] views){

        if(viewType == ViewType.CLI){

            views[0] = new SetUpViewTUI();
            views[1] = new AssemblyViewTUI();
            views[2] = new CorrectionViewTUI();
            views[3] = new FlightViewTUI();
            views[4] = new EvaluationViewTUI();

        }
        else{

            views[0] = new SetUpViewGUI();
            views[1] = new AssemblyViewGUI();
            views[2] = new CorrectionViewGUI();
            views[3] = new FlightViewGUI();
            views[4] = new EvaluationViewGUI();
        }

    }

    private int startSCK(GeneralView[] views){

        DataInputStream in;
        DataOutputStream out;

        try{
            in = new DataInputStream(clientInfo.getServerSocket().getInputStream());
            out = new DataOutputStream(clientInfo.getServerSocket().getOutputStream());
        }catch (IOException e){
           System.err.println("A critical error occured while opening streams");
           e.printStackTrace();
           return -1;
        }

        Thread messageReceiver = new Thread(new GameMessageReceiver(views, in));
        Thread messageSender = new Thread(new GameMessageSender(out));

        messageReceiver.start();
        messageSender.start();

        try {

            messageReceiver.join();
            messageSender.interrupt();

        } catch (InterruptedException e) {
           System.err.println("Receiver thread was interrupted abnormally");
        }
        return 0;

    }

    private void startRMI(GeneralView[] views){

    }
}
