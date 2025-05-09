package it.polimi.ingsw.Connection.ClientSide.View.AssemblyView;

import it.polimi.ingsw.Controller.AssemblyPhase.AssemblyPhase;
import it.polimi.ingsw.Model.Components.Component;

import java.util.List;

public class AssemblyView {
    public String printAssemblyMessage() {
        return ("👾AssemblyPhase (place (current component) / draw (a new component) / Choose (a component) / Rotate (current component) / turn (the hourglass) / book (current component and have a new one) / place booked (component)");
    }

    public String printComponentChoice() {
        return ("Print the number of the component you would like:");
    }

    public String printGameOverMessage() {
        return("Game Over");
    }

    public String printNewComponentMessage(Component component) {
        return("New component:" + component.getComponentName() + "Front:" + component.getFront() + "Right:" + component.getRight() + "Back:" + component.getBack() + "Left:" + component.getLeft());
    }

    public String printTurnMessage() {
        return("Turn the hourglass");
    }

    public String printRotateMessage(Component component) {
        return("Component rotated:" + component.getComponentName() + "Front:" + component.getFront() + "Right:" + component.getRight() + "Back:" + component.getBack() + "Left:" + component.getLeft());
    }

    public String printUncoveredComponentsMessage(AssemblyPhase assemblyPhase) {
        for (int i = 0; i < assemblyPhase.getAssemblyProtocol().getUncoveredList().size(); i++) {
            Component component = assemblyPhase.getAssemblyProtocol().getUncoveredList().get(i);
            return ("Component " + i + ": Name:" + component.getComponentName() + " Front: " + component.getFront() + " Right: " + component.getRight() + " Back: " + component.getBack() + " Left: " + component.getLeft());
        }
        return(".");
    }

    public String printErrorComponentChoiceMessage() {
        return("Error in component choice");
    }

    public String printErrorInCommandMessage() {
        return("Invalid command");
    }

    public String printComponentPlacingMessage() {
        return("Where do you want to place the component? Indicate coordinates X and Y");
    }

    public String printChooseDeckMessage() {
        return("Choose a deck from 1 to 3 writing the number:");
    }

    public String printNotValidDeckNumberMessage() {
        return("Invalid deck number");
    }

    public String printChooseBookedComponentMessage(List<Component> bookedComponent) {
        if (bookedComponent != null) {
            for (int i = 0; i < bookedComponent.size(); i++) {
                Component component = bookedComponent.get(i);
                return("Component " + i + ": Name:" + component.getComponentName() + " Front: " + component.getFront() + " Right: " + component.getRight() + " Back: " + component.getBack() + " Left: " + component.getLeft());
            }
            return("Write the number associated to the booked component you want to place:");
        }
        else{
            return ("You don't have any booked component");
        }
    }

    public String printErrorChoosingBookedComponentMessage() {
        return("The Booked Component chose doesn't exist");
    }

    public String printEmptyHandErrorMessage() {
        return("Your hand is empty");
    }
}
