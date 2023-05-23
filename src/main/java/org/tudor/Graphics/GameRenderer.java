package org.tudor.Graphics;

import org.tudor.Graphics.Primitives.Drawable;

import java.awt.*;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Handles all rendering tasks required by the game engine.
 * Currently, it only supports the <i>Painter's Algorithm</i> when rendering, i.e. drawing everything
 * back-to-front based on a z-index. What this means is that the drawables with the lowest z-index will
 * be drawn first, and the ones with a higher z-index will be drawn on top of them.
 */
public class GameRenderer {
    /** Shared instance of the renderer. */
    private static GameRenderer singleton = null;

    //TODO: Maybe implement drawables as a list with an added insertSorted method?
    /**
     * List of drawables to render based on their z-index.
     */
    private final PriorityQueue<Drawable> drawables = new PriorityQueue<>(Comparator.comparing(Drawable::getZIndex));

    /**
     * Private constructor for the Singleton design pattern.
     */
    private GameRenderer() {}

    /**
     * Getter for the shared instance.
     * @return The shared instance.
     */
    public static synchronized GameRenderer shared() {
        if ( singleton == null )
            singleton = new GameRenderer();

        return singleton;
    }

    /**
     * Adds a drawable to the rendering queue
     * @param d The new drawable to render.
     */
    public void addToQueue(Drawable d) {
        drawables.add(d);
    }

    /**
     * Removes a drawable from the rendering queue.
     * @param d The drawable to stop rendering.
     */
    public void removeFromQueue(Drawable d) {
        //TODO: Check if the removal is done based on the comparator provided or the hashcode!
        drawables.remove(d);
    }

    /**
     * Draws the rendering queue, i.e. renders the game.
     * @param g The graphics context used to render.
     */
    public void drawQueue(Graphics2D g) {
        PriorityQueue<Drawable> copy = new PriorityQueue<>(drawables);

        while ( !copy.isEmpty() )
            copy.poll().draw(g);
    }

}
