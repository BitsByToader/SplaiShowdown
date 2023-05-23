package org.tudor.Input;

/**
 * Interface for the <i>Observer Pattern</i>.
 */
public interface InputObserver {
    /**
     * Notifies the observer a key was released.
     * @param i The key in question.
     */
    void releasedInput(InputType i);

    /**
     * Notifies the observer a key was pressed.
     * @param i The key in question.
     */
    void newInput(InputType i);
}
