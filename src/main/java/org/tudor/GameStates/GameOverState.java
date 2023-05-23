package org.tudor.GameStates;

import org.tudor.Database.Database;
import org.tudor.Database.DatabaseNotOpenException;
import org.tudor.Database.MatchLog;
import org.tudor.Game;
import org.tudor.Graphics.GameRenderer;
import org.tudor.Graphics.Primitives.CoreText;
import org.tudor.Timer.SyncTimer;
import org.tudor.Timer.TimerManager;

import java.awt.*;
import java.util.function.Consumer;

/**
 * Extends the GameState class to provide game-over logic.
 */
public class GameOverState extends GameState {
    CoreText gameOverText;

    /**
     * Constructor that sets up the UI for game-over screen.
     * @param matchStats The stats for the previous match.
     * @param message The game-over message.
     * @param context The Game context.
     */
    protected GameOverState(MatchLog matchStats, String message, Game context) {
        super(context);

        gameOverText = new CoreText(message, new Point(380, 280));
        gameOverText.setZIndex(1);

        try {
            Database.shared().insertMatch(matchStats);
        } catch(DatabaseNotOpenException e) {
            System.out.println("Database connection not open? How?");
        }
    }

    @Override
    public void update(long elapsedMs) {/*stub*/}

    /**
     * Renders the UI and starts a 5-second timer to automatically dismiss this view.
     */
    @Override
    public void begin() {
        GameRenderer.shared().addToQueue(gameOverText);

        Consumer<Game> timerFinish = g -> g.transition(new MainMenuState(parentContext));
        SyncTimer<Game> timer = new SyncTimer<>(
                5000,
                false,
                parentContext,
                null,
                timerFinish
        );
        timer.start();
        TimerManager.shared().add(timer);
    }

    /**
     * Stops rendering the UI.
     */
    @Override
    public void cleanup() {
        GameRenderer.shared().removeFromQueue(gameOverText);
    }
}
