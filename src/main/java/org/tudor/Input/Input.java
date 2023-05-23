package org.tudor.Input;

/**
 * Data class for an input event (be it a press or a release).
 */
public class Input {
    /** The key that was pressed/released. */
    InputType key;
    /** The time the key was pressed. */
    long activationTime;
    /** Holds whether the key was marked for removal from the input buffer while running a combo
     * detection.*/
    boolean marked = false;

    /**
     * Basic constructor.
     * @param key The key.
     * @param time The time.
     */
    Input(InputType key, long time) {
        this.key = key;
        this.activationTime = time;
    }
}
