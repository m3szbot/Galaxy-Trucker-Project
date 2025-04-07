package it.polimi.ingsw.Application.AssemblyPhase;

import it.polimi.ingsw.Components.Component;

import java.util.List;

public class AssemblyView {
    public void printAssemblyMessage(){
        System.out.println("👾AssemblyPhase (place (current component) / draw (a new component) / Choose (a component) / Rotate (current component) / turn (the hourglass) / book (current component and have a new one) / place booked (component)");
    }
    public void printComponentChoice(){
        System.out.println("Print the number of the component you would like:");
    }
    public void printGameOverMessage(){System.out.println("Game Over");}
    public void printNewComponentMessage(Component component){
        System.out.println("New component:" + component.getComponentName() + "Front:" + component.getFront() + "Right:" + component.getRight() + "Back:" + component.getBack()  + "Left:" + component.getLeft());
    }
    public void printTurnMessage(){
        System.out.println("Turn the hourglass");
    }
    public void printRotateMessage(Component component){
        System.out.println("Component rotated:" + component.getComponentName() + "Front:" + component.getFront() + "Right:" + component.getRight() + "Back:" + component.getBack()  + "Left:" + component.getLeft());
    }
    public void printUncoveredComponentsMessage(AssemblyGame assemblyGame){
        for(int i = 0; i < assemblyGame.getAssemblyProtocol().getUncoveredList().size(); i++){
            Component component = assemblyGame.getAssemblyProtocol().getUncoveredList().get(i);
            System.out.println("Component " + i + ": Name:" + component.getComponentName() + " Front: " + component.getFront() + " Right: " + component.getRight() + " Back: " + component.getBack() + " Left: " + component.getLeft());
        }
    }
    public void printErrorComponentChoiceMessage(){
        System.out.println("Error in component choice");
    }
    public void printErrorInCommandMessage(){
        System.out.println("Invalid command");
    }
    public void printComponentPlacingMessage(){
        System.out.println("Where do you want to place the component? Indicate coordinates X and Y");
    }
    public void printChooseDeckMessage(){
        System.out.println("Choose a deck from 1 to 3 writing the number:");
    }
    public void printNotValidDeckNumberMessage(){
        System.out.println("Invalid deck number");
    }
    public void printChooseBookedComponentMessage(List<Component> bookedComponent){
        if(bookedComponent != null){
            for(int i = 0; i < bookedComponent.size(); i++){
                Component component = bookedComponent.get(i);
                System.out.println("Component " + i + ": Name:" + component.getComponentName() + " Front: " + component.getFront() + " Right: " + component.getRight() + " Back: " + component.getBack() + " Left: " + component.getLeft());
            }
            System.out.println("Write the number associated to the booked component you want to place:");
        }
    }
    public void printErrorChoosingBookedComponentMessage(){
        System.out.println("The Booked Component chose doesn't exist");
    }
}
