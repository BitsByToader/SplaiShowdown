package org.tudor;

import  org.tudor.GameWindow.GameWindow;
import org.tudor.Graphics.Animations.AnimationHandler;
import org.tudor.Graphics.Animations.AnimationManager;
import org.tudor.Graphics.Assets.Assets;
import org.tudor.Graphics.GameRenderer;
import org.tudor.Graphics.Skeletons.HumanSkeleton;
import org.tudor.Input.KeyManager;
import org.tudor.Timer.TimerManager;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.UUID;

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
    /** Previous time for the old frame. */
    private long            oldTime;

    // Driver code animation system demo
    private HumanSkeleton testPlayer = null;
    private UUID u;
    private UUID uu;
    private AnimationHandler<Point> a;
    private AnimationHandler<Point> a1;
    private AnimationHandler<Point> a2;
    private AnimationHandler<Point> aa;
    private boolean performedHello = false;

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

        // Driver code to game demo
        testPlayer = new HumanSkeleton(new Point(300, 400));
        testPlayer.beginRendering();

        u = AnimationManager.shared().registerForAnimations();
        uu = AnimationManager.shared().registerForAnimations();

        a = new AnimationHandler<>(
                Point.class,
                testPlayer.getJoint("fistRight"),
                new Point(15, -25),
                1000
        );

        a1 = new AnimationHandler<>(
                Point.class,
                testPlayer.getJoint("fistRight"),
                new Point(-25, 5),
                1000
        );

        a2 = new AnimationHandler<>(
                Point.class,
                testPlayer.getJoint("fistRight"),
                new Point(25, 0),
                1000
        );

        aa = new AnimationHandler<>(
                Point.class,
                testPlayer.getJoint("elbowRight"),
                new Point(15, -15),
                1000
        );
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
        long elapsed = ( System.nanoTime() - oldTime ) / 1000000;

        keyManager.update();

        // Driver code for game demo.
        if ( keyManager.right ) {
            testPlayer.translatePosition(2, 0);
        }
        if ( keyManager.left ) {
            testPlayer.translatePosition(-2, 0);
        }
        if ( keyManager.space && !performedHello ) {
            AnimationManager.shared().addAnimation(u, a);
            AnimationManager.shared().addAnimation(u, a1);
            AnimationManager.shared().addAnimation(u, a2);
            AnimationManager.shared().addAnimation(uu, aa);
            performedHello = true;
        }

        animationManager.update(elapsed);

        timerManager.update(elapsed);
    }

    /**
     * Draws the elements to the screen according to the game state built in the Update() method.
     */
    private void Draw() {
        bs = wnd.getCanvas().getBufferStrategy();

        Graphics2D g2d = (Graphics2D) bs.getDrawGraphics();
        g2d.clearRect(0, 0, wnd.getWndWidth(), wnd.getWndHeight());

        rendererInstance.drawQueue(g2d);

        bs.show();
        g2d.dispose();
    }
}


