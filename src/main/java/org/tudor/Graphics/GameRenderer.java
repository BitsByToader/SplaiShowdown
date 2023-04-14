package org.tudor.Graphics;

import org.tudor.Graphics.Primitives.Drawable;

import java.awt.*;
import java.util.Comparator;
import java.util.PriorityQueue;

public class GameRenderer {
    private static GameRenderer singleton = null;

    // Maybe implement drawables as a list with an added instertSorted method?
    private PriorityQueue<Drawable> drawables = new PriorityQueue<>(new Comparator<Drawable>() {
        @Override
        public int compare(Drawable o1, Drawable o2) {
            return o1.getZIndex().compareTo(o2.getZIndex());
        }
    });

    private GameRenderer() {}

    public static synchronized GameRenderer shared() {
        if ( singleton == null )
            singleton = new GameRenderer();

        return singleton;
    }

    public void addToQueue(Drawable d) {
        drawables.add(d);
    }

    public void removeFromQueue(Drawable d) {
        drawables.remove(d);
    }

    public void drawQueue(Graphics2D g) {
        PriorityQueue<Drawable> copy = new PriorityQueue<>(drawables);

        while ( !copy.isEmpty() )
            copy.poll().draw(g);
    }

}
