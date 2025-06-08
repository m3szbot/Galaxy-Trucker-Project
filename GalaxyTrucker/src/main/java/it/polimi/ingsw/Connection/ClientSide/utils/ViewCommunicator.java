package it.polimi.ingsw.Connection.ClientSide.utils;

import it.polimi.ingsw.Connection.ViewType;
import it.polimi.ingsw.Model.GameInformation.GamePhase;
import it.polimi.ingsw.View.GUI.GeneralGUIController;
import it.polimi.ingsw.View.GUI.LobbyControllers.LobbyGUIController;
import it.polimi.ingsw.View.GeneralView;
import it.polimi.ingsw.View.TUI.TUIView;

public class ViewCommunicator {

    private ViewType viewType;
    private LobbyGUIController lobbyGUIController;
    private GeneralView[] views;
    private GeneralGUIController generalGUIController;

    public ViewCommunicator(ViewType viewType){

        this.viewType = viewType;
        this.views = new GeneralView[4];
        this.generalGUIController = new GeneralGUIController();
        this.lobbyGUIController = new LobbyGUIController();

        if(viewType == ViewType.TUI){

            this.views[0] = new TUIView();
            this.views[1] = new TUIView();
            this.views[2] = new TUIView();
            this.views[3] = new TUIView();

        }
        else{

           /*
            this.views[0] = new AssemblyGUIController();
            this.views[1] = new CorrectionGUIController();
            this.views[2] = new FlightGUIController();
            this.views[3] = new EvaluationGUIController();

            */


        }

    }

    public void setGamePhase(GamePhase gamePhase){

        if(viewType == ViewType.GUI) {

            generalGUIController.setPhaseGUI(gamePhase);
        }
    }

    public GeneralView getView(int index){
        return views[index];
    }

    public void showData(String message, boolean isError){

        if(viewType == ViewType.TUI){
            if(isError) {
                System.err.println(message);
            }
            else{
                System.out.println(message);
            }
        }
        else{

            lobbyGUIController.showData(message, isError);

        }

    }

}
