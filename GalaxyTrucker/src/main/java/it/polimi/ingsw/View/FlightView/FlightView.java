package it.polimi.ingsw.View.FlightView;

import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.View.GeneralView;

public abstract class FlightView extends GeneralView {
    public abstract void printFlightBoard(DataContainer dataContainer);

}
