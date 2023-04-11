package org.tudor;

import  org.tudor.GameWindow.GameWindow;
import org.tudor.Graphics.Skeletons.HumanSkeleton;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game implements Runnable {
    private GameWindow      wnd;
    private boolean         runState;
    private Thread          gameThread;
    private BufferStrategy  bs;

    long oldTime;

    private HumanSkeleton testPlayer = null;

    /** Creates the game window for the game.
     * @param title Window title
     * @param width Window width.
     * @param height Window height.
     */
    public Game(String title, int width, int height) {
        // Creates the GameWindow object, but doesn't build the actual window
        wnd = new GameWindow(title, width, height);

        // Build the game window
        wnd.buildGameWindow();
        runState = false;

        // Create buffer strategy
        try {
            wnd.GetCanvas().createBufferStrategy(3);
        } catch (Exception e) {
            e.printStackTrace();
        }

        testPlayer = new HumanSkeleton(new Point(200, 200));
    }

    /**
     * Main run loop of the game that handles input, logic and drawing.
     */
    public void run() {
        oldTime = System.nanoTime();   // previous frame time
        long curentTime; // current frame time

        final int framesPerSecond   = 60; // target frames per second of the game
        final double timeFrame      = 1000000000 / framesPerSecond; // frame time in nano seconds

        while ( runState ) {
            curentTime = System.nanoTime();

            if( (curentTime - oldTime) > timeFrame ) {
                Update();
                Draw();

                oldTime = curentTime;
            }
        }

    }

    /**
     * Starts the thread where the game logic will happen.
     */
    public synchronized void startGame() {
        if( !runState ) {
            runState = true;
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    /**
     * Attempts to stop the thread with the game logic, i.e. stopping the game.
     */
    public synchronized void stopGame() {
        if( runState ) {
            runState = false;

            try {
                gameThread.join();
            } catch(InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Handles all the game logic, thus updating the game state and all elements.
     */
    private void Update() {
        // TODO: Stub here
    }

    /**
     * Draws the elements to the screen according to the game state built in the Update() method.
     */
    private void Draw() {
        bs = wnd.GetCanvas().getBufferStrategy();

        Graphics2D g2d = (Graphics2D) bs.getDrawGraphics();
        g2d.clearRect(0, 0, wnd.getWndWidth(), wnd.getWndHeight());

        // Stub drawing
        testPlayer.draw(g2d);

        bs.show();
        g2d.dispose();
    }
}


