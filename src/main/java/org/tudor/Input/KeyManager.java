package org.tudor.Input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Singleton class that handles the keyboard inputs for the game. The sole purpose of this class is only
 * to gather the inputs every frame, nothing more. A future class will deal with the logic of processing
 * what was received from the keyboard.
 */
public class KeyManager implements KeyListener  {
    /** Singleton instance for the manager. */
    private static KeyManager singleton = null;

    /** Maps all the keyboard keys by their code. */
    private final boolean[] keys = new boolean[256];
    /** Indicates if W is pressed.*/
    public boolean up;
    /** Indicates if S is pressed.*/
    public boolean down;
    /** Indicates if A is pressed.*/
    public boolean left;
    /** Indicates if D is pressed.*/
    public boolean right;
    /** Indicates if SPACE is pressed.*/
    public boolean space;

    /**
     * Private constructor for the singleton design pattern.
     */
    private KeyManager() {}

    /**
     * Getter for the shared instance.
     * @return The shared instance.
     */
    public static KeyManager shared() {
        if ( singleton == null ) {
            singleton = new KeyManager();
        }

        return singleton;
    }

    /**
     * Update the keys vector with the current state of the keys.
     */
    public void update() {
        up    = keys[KeyEvent.VK_W];
        down  = keys[KeyEvent.VK_S];
        left  = keys[KeyEvent.VK_A];
        right = keys[KeyEvent.VK_D];
        space = keys[KeyEvent.VK_SPACE];
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
