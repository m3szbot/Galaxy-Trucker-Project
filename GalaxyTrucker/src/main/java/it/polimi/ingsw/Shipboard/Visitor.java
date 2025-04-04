package it.polimi.ingsw.Shipboard;

import it.polimi.ingsw.Components.*;

public interface Visitor<T> {
    T visit(Cannon cannon);

    T visit(Shield shield);

    T visit(Component component);

    T visit(AlienSupport alienSupport);

    T visit(Battery battery);

    T visit(Cabin cabin);

    T visit(Engine engine);

    T visit(Storage storage);
}