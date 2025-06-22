package it.polimi.ingsw.Connection.ClientSide.utils;

import it.polimi.ingsw.Connection.ViewType;
import it.polimi.ingsw.Model.GameInformation.GamePhase;
import it.polimi.ingsw.View.GUI.GUIView;
import it.polimi.ingsw.View.GUI.GeneralGUIController;
import it.polimi.ingsw.View.GeneralView;
import it.polimi.ingsw.View.TUI.TUIView;

/**
 * Class which contains the information about the player's view.
 *
 * @author carlo
 */

public class ViewCommunicator {

    private ViewType viewType;
    private GeneralView view;
    private GeneralGUIController generalGUIController;

    public ViewCommunicator(ViewType viewType) {

        this.viewType = viewType;

        if (viewType == ViewType.TUI) {

            this.view = new TUIView();

        } else {

            this.view = new GUIView();

        }

    }

    public void setGeneralGUIController(GeneralGUIController generalGUIController){
        this.generalGUIController = generalGUIController;
    }

    public void setGamePhase(GamePhase gamePhase) {

        if (viewType == ViewType.GUI) {

            generalGUIController.setPhaseGUI(gamePhase, view);
        }
    }

    public GeneralView getView() {

        return view;

    }
}
