package org.tudor.Input;

public interface InputObserver {
    void releasedInput(InputType i);

    void newInput(InputType i);
}
