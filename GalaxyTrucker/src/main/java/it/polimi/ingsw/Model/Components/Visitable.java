package it.polimi.ingsw.Model.Components;

/**
 * Component accepts any kind of visitor implementing the ComponentVisitor interface.
 */
public interface Visitable {
    <T, E extends Exception> T accept(ComponentVisitor<T, E> visitor) throws E;
}
