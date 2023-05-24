package org.tudor.GameStates;

import org.tudor.Database.MatchLog;
import org.tudor.Game;
import org.tudor.Graphics.GameRenderer;
import org.tudor.Graphics.Primitives.CorePoint;
import org.tudor.Graphics.Primitives.CoreRectangle;
import org.tudor.Graphics.Primitives.CoreText;
import org.tudor.Input.InputObserver;
import org.tudor.Input.InputType;
import org.tudor.Input.KeyManager;
import org.tudor.Players.KeyboardPlayer;
import org.tudor.Players.KeyboardPlayerType;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Extends the GameState class to provide logic for a multiplayer game mode between two keyboard
 * players.
 */
public class PlayState extends GameState implements InputObserver {
    private final KeyboardPlayer player1;
    private final KeyboardPlayer player2;

    private final CoreRectangle background;

    private final CoreText player1Text;
    private final CoreText player1HpText;
    private final CoreText player2Text;
    private final CoreText player2HpText;

    private boolean prevHitPlayer1 = false;
    private boolean prevHitPlayer2 = false;

    private final long matchStartTime;

    /**
     * Constructor that sets up the two players and the UI.
     * @param context The Game context.
     */
    public PlayState(BufferedImage backgroundTexture, Game context) {
        super(context);

        matchStartTime = System.currentTimeMillis();

        player1 = new KeyboardPlayer(KeyboardPlayerType.PLAYER_1);
        player2 = new KeyboardPlayer(KeyboardPlayerType.PLAYER_2);
        player1Text = new CoreText("PLAYER1", new Point(10, 20));
        player2Text = new CoreText("PLAYER2", new Point(700, 20));
        player1HpText = new CoreText("HP: ", new Point(10, 40));
        player2HpText = new CoreText("HP: ", new Point(700, 40));

        CorePoint cp1 = new CorePoint(new Point(400,0));
        CorePoint cp2 = new CorePoint(new Point(0, 10));
        cp1.addChild(cp2);
        background = new CoreRectangle(backgroundTexture, cp1, cp2);
        background.setZIndex(0);
    }

    /**
     * Checks for collisions between players and end-game conditions.
     * @param elapsedMs The elapsed time between the previous frame and this one in milliseconds.
     */
    @Override
    public void update(long elapsedMs) {
        player1.update();
        player2.update();

        if ( player1.getHealth() == 0 ) {
            MatchLog l = new MatchLog("Player2",
                    player2.getHealth(),
                    System.currentTimeMillis() - matchStartTime
            );
            parentContext.transition(new GameOverState(l,"PLAYER2 WON!", parentContext));
        }

        if ( player2.getHealth() == 0 ) {
            MatchLog l = new MatchLog("Player1",
                    player1.getHealth(),
                    System.currentTimeMillis() - matchStartTime
            );
            parentContext.transition(new GameOverState(l, "PLAYER1 WON!", parentContext));
        }

        boolean hit = player1.checkHitOn(player2);
        if ( hit && !prevHitPlayer1 )
            player2.inflictDamage(10);
        prevHitPlayer1 = hit;

        hit = player2.checkHitOn(player1);
        if ( hit && !prevHitPlayer2 )
            player1.inflictDamage(10);
        prevHitPlayer2 = hit;

        player1HpText.setText("HP: " + player1.getHealth());
        player2HpText.setText("HP: " + player2.getHealth());
    }

    /**
     * Starts rendering the players, UI and listening for keyboard inputs.
     */
    public void begin() {
        KeyManager.shared().register(this);
        player1.getEntity().startDrawing();
        player2.getEntity().startDrawing();
        GameRenderer.shared().addToQueue(player1Text);
        GameRenderer.shared().addToQueue(player2Text);
        GameRenderer.shared().addToQueue(player1HpText);
        GameRenderer.shared().addToQueue(player2HpText);
        GameRenderer.shared().addToQueue(background);
    }

    /**
     * Stops rendering the players, UI and listening for keyboard inputs.
     */
    public void cleanup() {
        GameRenderer.shared().removeFromQueue(player1Text);
        GameRenderer.shared().removeFromQueue(player2Text);
        GameRenderer.shared().removeFromQueue(player1HpText);
        GameRenderer.shared().removeFromQueue(player2HpText);
        GameRenderer.shared().removeFromQueue(background);
        player1.cleanup();
        player2.cleanup();
    }

    @Override
    public void releasedInput(InputType i) {/*stub*/}

    @Override
    public void newInput(InputType i) {
        if (i == InputType.ESCAPE) {
            parentContext.transition(new MainMenuState(parentContext));
        }
    }
}
