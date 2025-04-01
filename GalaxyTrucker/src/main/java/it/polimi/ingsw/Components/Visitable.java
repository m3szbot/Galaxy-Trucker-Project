package it.polimi.ingsw.Components;

import it.polimi.ingsw.Shipboard.Visitor;

public interface Visitable {
    <T> T accept(Visitor<T> visitor);
}
