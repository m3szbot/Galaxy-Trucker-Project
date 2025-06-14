package it.polimi.ingsw.Connection.ClientSide.utils;

import it.polimi.ingsw.Connection.ViewType;
import it.polimi.ingsw.Model.GameInformation.GamePhase;
import it.polimi.ingsw.View.GUI.GeneralGUIController;
import it.polimi.ingsw.View.GUI.LobbyControllers.LobbyGUIController;
import it.polimi.ingsw.View.GeneralView;
import it.polimi.ingsw.View.TUI.TUIView;

/**
 * Class which contains the information about the player's view.
 *
 * @author carlo
 */

public class ViewCommunicator {

    private ViewType viewType;
    private LobbyGUIController lobbyGUIController;
    private GeneralView[] views;
    private GeneralGUIController generalGUIController;
    private GeneralView lobbyView;
    private int index = -1;

    public ViewCommunicator(ViewType viewType) {

        this.viewType = viewType;
        this.views = new GeneralView[4];
        this.generalGUIController = new GeneralGUIController();
        this.lobbyGUIController = new LobbyGUIController();

        if (viewType == ViewType.TUI) {

            this.lobbyView = new TUIView();
            this.views[0] = new TUIView();
            this.views[1] = new TUIView();
            this.views[2] = new TUIView();
            this.views[3] = new TUIView();

        } else {
            /*
            this.lobbyView = new LobbyGUIController();
            this.views[0] = new AssemblyGUIController();
            this.views[1] = new CorrectionGUIController();
            this.views[2] = new FlightGUIController();
            this.views[3] = new EvaluationGUIController();

             */


        }

    }

    public void setGamePhase(GamePhase gamePhase) {

        switch (gamePhase) {
            case Assembly -> index = 0;
            case Correction -> index = 1;
            case Flight -> index = 2;
            case Evaluation -> index = 3;
        }

        if (viewType == ViewType.GUI) {

            generalGUIController.setPhaseGUI(gamePhase);
        }
    }

    public GeneralView getView() {

        if (index == -1) {
            return lobbyView;
        }

        return views[index];
    }

    public void showData(String message, boolean isError) {

        if (viewType == ViewType.TUI) {
            if (isError) {
                System.err.println(message);
            } else {
                System.out.println(message);
            }
        }
        /*else{

            lobbyGUIController.showData(message, isError);

        }

         */

    }

}
