package it.polimi.ingsw.Model.Components;

import it.polimi.ingsw.Model.ShipBoard.ComponentVisitor;

/**
 * Component accepts any kind of visitor implementing the ComponentVisitor interface.
 */
public interface Visitable {
    <T> T accept(ComponentVisitor<T> visitor);
}
