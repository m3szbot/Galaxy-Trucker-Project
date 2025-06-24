package it.polimi.ingsw.Connection.ClientSide.utils;

import it.polimi.ingsw.Connection.ConnectionType;
import it.polimi.ingsw.Connection.ViewType;
import it.polimi.ingsw.Model.GameInformation.GamePhase;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.View.GUI.GUIView;
import it.polimi.ingsw.View.GUI.GeneralGUIController;
import it.polimi.ingsw.View.GUI.utils.FXUtil;
import it.polimi.ingsw.View.GeneralView;
import it.polimi.ingsw.View.TUI.TUIView;

/**
 * Class which contains the information about the player's view, and which
 * offers set up methods.
 *
 * @author carlo
 */

public class ViewCommunicator {

    private ViewType viewType;
    private GeneralView view;
    private GeneralGUIController generalGUIController;
    private ConnectionType connectionType;

    public ViewCommunicator(ViewType viewType) {

        this.viewType = viewType;

        if (viewType == ViewType.TUI) {

            this.view = new TUIView();

        } else {

            this.view = new GUIView();

        }

    }

    public void setConnectionType(ConnectionType connectionType){
        this.connectionType = connectionType;
    }

    public ConnectionType getConnectionType(){
        return connectionType;
    }

    public ViewType getViewType(){
        return viewType;
    }

    public void setGameType(GameType gameType){

        if(viewType == ViewType.GUI) {
            ((GUIView) view).setGameType(gameType);
        }
    }

    public void setGeneralGUIController(GeneralGUIController generalGUIController){
        this.generalGUIController = generalGUIController;
    }

    /**
     * method which changes the GUI interface accordingly to the gamePhase passed as
     * parameter.
     * @param gamePhase
     */

    public void setGamePhase(GamePhase gamePhase) {

        if (viewType == ViewType.GUI) {

            FXUtil.runOnFXThread(() -> {
                generalGUIController.setPhaseGUI(gamePhase, view);
            });

        }
    }

    public GeneralView getView() {

        return view;

    }
}
