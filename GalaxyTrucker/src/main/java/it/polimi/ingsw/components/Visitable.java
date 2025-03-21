package it.polimi.ingsw.components;

import it.polimi.ingsw.shipboard.Visitor;

public interface Visitable {
    void accept(Visitor visitor);
}
