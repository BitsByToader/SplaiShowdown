package org.tudor.Input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener  {
    private static KeyManager singleton = null;

    private boolean[] keys = new boolean[256];
    public boolean up;
    public boolean down;
    public boolean left;
    public boolean right;

    private KeyManager() {}

    public static KeyManager shared() {
        if ( singleton == null ) {
            singleton = new KeyManager();
        }

        return singleton;
    }

    public void update() {
        up    = keys[KeyEvent.VK_W];
        down  = keys[KeyEvent.VK_S];
        left  = keys[KeyEvent.VK_A];
        right = keys[KeyEvent.VK_D];
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
