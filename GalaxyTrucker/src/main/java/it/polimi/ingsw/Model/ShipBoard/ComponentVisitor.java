package it.polimi.ingsw.Model.ShipBoard;

import it.polimi.ingsw.Model.Components.*;

public interface ComponentVisitor<T> {
    T visit(AlienSupport alienSupport);

    T visit(Battery battery);

    T visit(Cabin cabin);

    T visit(Cannon cannon);

    T visit(Component component);

    T visit(Engine engine);


    T visit(Shield shield);


    T visit(Storage storage);
}