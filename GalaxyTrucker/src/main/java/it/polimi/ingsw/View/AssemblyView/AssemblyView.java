package it.polimi.ingsw.View.AssemblyView;

import it.polimi.ingsw.Controller.AssemblyPhase.AssemblyPhase;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.View.GeneralView;

import java.util.List;

public abstract class AssemblyView extends GeneralView {

    public void printHourglass() {
    }

    public void printDeck() {
        // calls printCard(card)
    }

    // TODO: remove unnecessary methods:
    public abstract String printAssemblyMessage();

    public abstract String printComponentChoice();

    public abstract String printGameOverMessage();

    public abstract String printNewComponentMessage(Component component);

    public abstract String printTurnMessage();

    public abstract String printRotateMessage(Component component);

    public abstract String printUncoveredComponentsMessage(AssemblyPhase assemblyPhase);

    public abstract String printErrorComponentChoiceMessage();

    public abstract String printErrorInCommandMessage();

    public abstract String printComponentPlacingMessage();

    public abstract String printChooseDeckMessage();

    public abstract String printNotValidDeckNumberMessage();

    public abstract String printChooseBookedComponentMessage(List<Component> bookedComponent);

    public abstract String printErrorChoosingBookedComponentMessage();

    public abstract String printEmptyHandErrorMessage();


}
