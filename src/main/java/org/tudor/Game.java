package org.tudor;

import org.tudor.GameStates.GameState;
import org.tudor.GameStates.MainMenuState;
import org.tudor.GameWindow.GameWindow;
import org.tudor.Graphics.Animations.AnimationManager;
import org.tudor.Graphics.Assets.Assets;
import org.tudor.Graphics.GameRenderer;
import org.tudor.Input.KeyManager;
import org.tudor.Timer.TimerManager;

import java.awt.*;
import java.awt.image.BufferStrategy;

/**
 * Main game engine class.
 */
public class Game implements Runnable {
    /** Game window. */
    private GameWindow          wnd;
    /** Indicates if the game is running. */
    private boolean             runState;
    /** Main render/engine thread. */
    private Thread              gameThread;
    /** Buffer strategy used when rendering. */
    private BufferStrategy      bs;
    /** Shared instance used for rendering. */
    private GameRenderer        rendererInstance;
    /**  Shared instance used for key handling. */
    private KeyManager          keyManager;
    private AnimationManager    animationManager;
    private TimerManager        timerManager;

    private GameState currentState;
    private GameState previousState = null;

    /** Previous time for the old frame. */
    private long            oldTime;

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
            wnd.getCanvas().createBufferStrategy(3);
            bs = wnd.getCanvas().getBufferStrategy();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Load the assets
        Assets.init();

        // Create the animation manager
        animationManager = AnimationManager.shared();

        // Create the timer manager
        timerManager = TimerManager.shared();

        // Create the key manager and register it
        keyManager = KeyManager.shared();
        wnd.getFrame().addKeyListener(keyManager);

        // Create the render instance singleton
        rendererInstance = GameRenderer.shared();

        // Initialize the first state of the game
        currentState = new MainMenuState(this);
        currentState.begin();
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
                update();
                draw();

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

    public void transition(GameState nextState) {
        previousState = currentState;
        previousState.cleanup();

        currentState = nextState;
        currentState.begin();
    }

    public void transitionToPreviousState() {
        if ( previousState != null ) {
            currentState.cleanup();

            GameState temp = currentState;
            currentState = previousState;
            previousState = temp;

            currentState.begin();
        }
    }

    /**
     * Handles all the game logic, thus updating the game state and all elements.
     */
    private void update() {
        long elapsed = ( System.nanoTime() - oldTime ) / 1000000;

        keyManager.update();

        currentState.update(elapsed);

        animationManager.update(elapsed);

        timerManager.update(elapsed);
    }

    /**
     * Draws the elements to the screen according to the game state built in the Update() method.
     */
    private void draw() {
        Graphics2D g2d = (Graphics2D) bs.getDrawGraphics();
        g2d.clearRect(0, 0, wnd.getWndWidth(), wnd.getWndHeight());

        rendererInstance.drawQueue(g2d);

        bs.show();
        g2d.dispose();
    }
}


