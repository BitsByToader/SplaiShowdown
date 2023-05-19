package org.tudor.GameStates;

import org.tudor.Game;
import org.tudor.Input.InputObserver;
import org.tudor.Input.InputType;
import org.tudor.Input.KeyManager;
import org.tudor.Players.KeyboardPlayer;
import org.tudor.Players.KeyboardPlayerType;

import java.util.Objects;

public class PlayState extends GameState implements InputObserver {
    private KeyboardPlayer player1;
    private KeyboardPlayer player2;

    public PlayState(Game context) {
        super(context);

        player1 = new KeyboardPlayer(KeyboardPlayerType.PLAYER_1);
        player2 = new KeyboardPlayer(KeyboardPlayerType.PLAYER_2);
    }
    @Override
    public void update(long elapsedMs) {
        player1.update();
        player2.update();
    }

    public void begin() {
        KeyManager.shared().register(this);
        player1.getEntity().startDrawing();
        player2.getEntity().startDrawing();
    }

    public void cleanup() {
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
