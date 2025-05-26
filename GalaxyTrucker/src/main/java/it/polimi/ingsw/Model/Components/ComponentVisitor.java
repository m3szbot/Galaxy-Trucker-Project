package it.polimi.ingsw.Model.Components;

public interface ComponentVisitor<T> {
    T visitAlienSupport(AlienSupport alienSupport);

    T visitBattery(Battery battery);

    T visitCabin(Cabin cabin);

    T visitCannon(Cannon cannon);

    T visitComponent(Component component);

    T visitEngine(Engine engine);


    T visitShield(Shield shield);


    T visitStorage(Storage storage);
}