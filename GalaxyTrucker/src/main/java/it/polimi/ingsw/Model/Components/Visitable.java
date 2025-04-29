package it.polimi.ingsw.Model.Components;

import it.polimi.ingsw.Model.ShipBoard.Visitor;

public interface Visitable {
    <T> T accept(Visitor<T> visitor);
}
