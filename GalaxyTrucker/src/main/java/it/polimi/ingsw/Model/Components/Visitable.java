package it.polimi.ingsw.Model.Components;

import it.polimi.ingsw.Model.ShipBoard.ComponentVisitor;

public interface Visitable {
    <T> T accept(ComponentVisitor<T> visitor);
}
