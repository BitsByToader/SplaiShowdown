package org.tudor.GameStates;

import org.tudor.Game;

/**
 * Abstract class that defines the basic behaviour of a GameState. A GameState can be seen as an
 * abstraction of game logic. A main-menu, a stage-selection screen, a play-screen, etc. all have
 * different logic bound to them and are only tied together by a few simple operations: update every
 * frame, begin and stop (clean-up), pause and resume (not yet implemented).
 * A keen observer, however, may notice that a basic GameState doesn't define any transition logic.
 * That is because, there is no need to couple that logic to a GameState, it can, and it has been left
 * to the Game class (or maybe a dedicated one in the future?), since a state is just a state and
 * nothing more, it has very few connections to the outside world, which are mainly through the parent
 * context either way.
 */
public abstract class GameState {
    /** The parent context of this (and every other) GameState. While currently it's directly the
     * Game class, it is planned for it to be a class that follows the Adapter pattern to channel
     * logic to any class that needs it. */
    protected Game parentContext;

    /**
     * Base constructor for every GameState
     * @param context The context of the Game.
     */
    protected GameState(Game context) {
        this.parentContext = context;
    }

    /**
     * Method that is called every frame.
     * @param elapsedMs The elapsed time between the previous frame and this one in milliseconds.
     */
    abstract public void update(long elapsedMs);

    /**
     * Notifies the GameState that it has ownership over the game logic and can begin setting up
     * operations.
     */
    abstract public void begin();

    /**
     * Notifies the GameState that it has lost ownership and it needs to perform clean up operations
     * for the next GameState.
     */
    abstract public void cleanup();
}
