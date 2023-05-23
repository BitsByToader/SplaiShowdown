package org.tudor.GameStates;

import org.tudor.Game;
import org.tudor.Graphics.GameRenderer;
import org.tudor.Graphics.Primitives.CoreText;
import org.tudor.Input.InputObserver;
import org.tudor.Input.InputType;
import org.tudor.Input.KeyManager;
import org.tudor.Players.KeyboardPlayer;
import org.tudor.Players.KeyboardPlayerType;

import java.awt.*;

public class PlayState extends GameState implements InputObserver {
    private KeyboardPlayer player1;
    private KeyboardPlayer player2;

    private CoreText player1Text;
    private CoreText player1HpText;
    private CoreText player2Text;
    private CoreText player2HpText;

    private boolean prevHitPlayer1 = false;
    private boolean prevHitPlayer2 = false;

    public PlayState(Game context) {
        super(context);

        player1 = new KeyboardPlayer(KeyboardPlayerType.PLAYER_1);
        player2 = new KeyboardPlayer(KeyboardPlayerType.PLAYER_2);
        player1Text = new CoreText("PLAYER1", new Point(10, 20));
        player2Text = new CoreText("PLAYER2", new Point(700, 20));
        player1HpText = new CoreText("HP: ", new Point(10, 40));
        player2HpText = new CoreText("HP: ", new Point(700, 40));

    }
    @Override
    public void update(long elapsedMs) {
        player1.update();
        player2.update();

        if ( player1.getHealth() == 0 ) {
            parentContext.transition(new GameOverState("PLAYER2 WON!", parentContext));
        }

        if ( player2.getHealth() == 0 ) {
            parentContext.transition(new GameOverState("PLAYER1 WON!", parentContext));
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

    public void begin() {
        KeyManager.shared().register(this);
        player1.getEntity().startDrawing();
        player2.getEntity().startDrawing();
        GameRenderer.shared().addToQueue(player1Text);
        GameRenderer.shared().addToQueue(player2Text);
        GameRenderer.shared().addToQueue(player1HpText);
        GameRenderer.shared().addToQueue(player2HpText);
    }

    public void cleanup() {
        GameRenderer.shared().removeFromQueue(player1Text);
        GameRenderer.shared().removeFromQueue(player2Text);
        GameRenderer.shared().removeFromQueue(player1HpText);
        GameRenderer.shared().removeFromQueue(player2HpText);
        player1.cleanup();
        player2.cleanup();
    }

    @Override
    public void releasedInput(InputType i) {/*stub*/}

    @Override
    public void newInput(InputType i) {
        if (i == InputType.ESCAPE) {
            parentContext.transitionToPreviousState();
        }
    }
}
