package org.tudor.GameWindow;

import javax.swing.*;
import java.awt.*;

/**
 * Implements the main window of the Game.
 */
public class GameWindow {
    private JFrame wndFrame;
    private String wndTitle;
    private int wndWidth;
    private int wndHeight;
    private Canvas canvas;

    /**
     * Creates the GameWindow object by initializing all the members needed while the game is running.
     * The constructor is supposed to be followed by BuildGameWindow() to actually construct the window.
     * @param title Window title.
     * @param width Window width.
     * @param height Window height.
     */
    public GameWindow(String title, int width, int height) {
        wndTitle = title;
        wndWidth = width;
        wndHeight = height;
        wndFrame = null;
    }

    /**
     * Builds the Game Window
     */
    public void buildGameWindow() {
        if(wndFrame != null) {
            return;
        }

        // Make window
        wndFrame = new JFrame(wndTitle);
        wndFrame.setSize(wndWidth, wndHeight);
        wndFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        wndFrame.setResizable(false);
        wndFrame.setLocationRelativeTo(null);
        wndFrame.setVisible(true);

        // Make canvas
        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(wndWidth, wndHeight));
        canvas.setMaximumSize(new Dimension(wndWidth, wndHeight));
        canvas.setMinimumSize(new Dimension(wndWidth, wndHeight));

        // Add canvas to window
        wndFrame.add(canvas);
        wndFrame.pack();
    }

    /**
     * Window width getter.
     * @return Width of the window.
     */
    public int getWndWidth() {
        return wndWidth;
    }

    /**
     * Window height getter.
     * @return Height of the window.
     */
    public int getWndHeight() {
        return wndHeight;
    }

    /**
     * Game canvas getter.
     * @return Canvas of the game.
     */
    public Canvas GetCanvas() {
        return canvas;
    }
}

