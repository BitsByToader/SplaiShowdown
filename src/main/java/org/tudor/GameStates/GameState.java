package org.tudor.GameStates;

import org.tudor.Game;

public abstract class GameState {
    protected Game parentContext;

    protected GameState(Game context) {
        this.parentContext = context;
    }

    abstract public void update(long elapsedMs);

    abstract public void begin();

    abstract public void cleanup();
}
