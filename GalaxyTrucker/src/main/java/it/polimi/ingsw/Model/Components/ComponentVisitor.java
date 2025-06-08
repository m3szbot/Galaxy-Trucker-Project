package it.polimi.ingsw.Model.Components;

/**
 * @param <T> return type
 * @param <E> thrown exception type. Extend dummy RuntimeException for unchecked exception (no handling enforced).
 */
public interface ComponentVisitor<T, E extends Exception> {
    T visitAlienSupport(AlienSupport alienSupport) throws E;

    T visitBattery(Battery battery) throws E;

    T visitCabin(Cabin cabin) throws E;

    T visitCannon(Cannon cannon) throws E;

    T visitComponent(Component component) throws E;

    T visitEngine(Engine engine) throws E;


    T visitShield(Shield shield) throws E;


    T visitStorage(Storage storage) throws E;
}