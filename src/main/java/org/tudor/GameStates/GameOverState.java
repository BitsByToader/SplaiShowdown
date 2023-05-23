package org.tudor.GameStates;

import org.tudor.Game;
import org.tudor.Graphics.GameRenderer;
import org.tudor.Graphics.Primitives.CoreText;
import org.tudor.Timer.SyncTimer;
import org.tudor.Timer.TimerManager;

import java.awt.*;
import java.util.function.Consumer;

public class GameOverState extends GameState {
    CoreText gameOverText;

    protected GameOverState(String message, Game context) {
        super(context);

        gameOverText = new CoreText(message, new Point(380, 280));
        gameOverText.setZIndex(1);
    }

    @Override
    public void update(long elapsedMs) {/*stub*/}

    @Override
    public void begin() {
        GameRenderer.shared().addToQueue(gameOverText);

        Consumer<Game> timerFinish = g -> g.transition(new MainMenuState(parentContext));
        SyncTimer<Game> timer = new SyncTimer<Game>(
                5000,
                false,
                parentContext,
                null,
                timerFinish
        );
        timer.start();
        TimerManager.shared().add(timer);
    }

    @Override
    public void cleanup() {
        GameRenderer.shared().removeFromQueue(gameOverText);
    }
}