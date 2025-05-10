package it.polimi.ingsw.View;

import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Model.Components.Component;

/**
 * General view class defining the most basic methods reused by others classes.
 * public methods: no parameters!, model extracted from DataContainer
 * private methods: model passed as parameter, always invoked inside public print methods
 */
public abstract class GeneralView {
    public void printMessage(DataContainer dataContainer) {
        System.out.println(dataContainer.getMessage());
    }

    public void printComponent() {
    }

    private void printComponent(Component component) {
    }

    public void printShipboard() {
    }

    public void printCard() {
    }

    private void printCard(Card card) {
    }


}
