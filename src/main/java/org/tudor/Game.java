package org.tudor;

import  org.tudor.GameWindow.GameWindow;
import org.tudor.Graphics.Animations.AnimationHandler;
import org.tudor.Graphics.Animations.AnimationManager;
import org.tudor.Graphics.Assets.Assets;
import org.tudor.Graphics.GameRenderer;
import org.tudor.Graphics.Skeletons.HumanSkeleton;
import org.tudor.Timer.TimerManager;

import javax.sound.midi.Soundbank;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.UUID;

/**
 * Main game engine class.
 */
public class Game implements Runnable {
    /** Game window. */
    private GameWindow      wnd;
    /** Indicates if the game is running. */
    private boolean         runState;
    /** The main render/engine thread. */
    private Thread          gameThread;
    /** The buffer strategy used when rendering. */
    private BufferStrategy  bs;
    /** The shared instance when rendering. */
    private GameRenderer    rendererInstance = null;
    /** The previous time for the old time. */
    private long            oldTime;

    /** A test human skeleton for testing. */
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

        Assets.init();

        // Create the render instance singleton
        rendererInstance = GameRenderer.shared();

        testPlayer = new HumanSkeleton(new Point(200, 200));
        testPlayer.beginRendering();

        UUID u = AnimationManager.shared().registerForAnimations();
        UUID uu = AnimationManager.shared().registerForAnimations();

        AnimationHandler<Point> a = new AnimationHandler<>(
                Point.class,
                testPlayer.getJoint("fistRight"),
                new Point(15, -25),
                1000
        );

        AnimationHandler<Point> a1 = new AnimationHandler<>(
                Point.class,
                testPlayer.getJoint("fistRight"),
                new Point(-25, 5),
                1000
        );

        AnimationHandler<Point> a2 = new AnimationHandler<>(
                Point.class,
                testPlayer.getJoint("fistRight"),
                new Point(25, 0),
                1000
        );

        AnimationHandler<Point> aa = new AnimationHandler<>(
                Point.class,
                testPlayer.getJoint("elbowRight"),
                new Point(15, -15),
                1000
        );

        AnimationManager.shared().addAnimation(u, a);
        AnimationManager.shared().addAnimation(u, a1);
        AnimationManager.shared().addAnimation(u, a2 );
        AnimationManager.shared().addAnimation(uu, aa);
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
        long elapsed = ( System.nanoTime() - oldTime ) / 1000000;

        AnimationManager.shared().update(elapsed);
        TimerManager.shared().update(elapsed);
    }

    /**
     * Draws the elements to the screen according to the game state built in the Update() method.
     */
    private void Draw() {
        bs = wnd.GetCanvas().getBufferStrategy();

        Graphics2D g2d = (Graphics2D) bs.getDrawGraphics();
        g2d.clearRect(0, 0, wnd.getWndWidth(), wnd.getWndHeight());

        rendererInstance.drawQueue(g2d);

        bs.show();
        g2d.dispose();
    }
}


