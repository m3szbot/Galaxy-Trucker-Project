package it.polimi.ingsw.components;

import it.polimi.ingsw.shipboard.Visitor;

public interface Visitable {
    <T> T accept(Visitor<T> visitor);
}
