package org.tudor.Input;

public class Input {
    InputType key;
    long activationTime;
    boolean marked = false;

    Input(InputType key, long time) {
        this.key = key;
        this.activationTime = time;
    }
}
