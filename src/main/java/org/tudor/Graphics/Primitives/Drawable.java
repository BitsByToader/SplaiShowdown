package org.tudor.Graphics.Primitives;

import java.awt.*;

/**
 * Describes the behaviour that an object should have if it wants to be drawn to the screen, based on a z index.
 */
public interface Drawable {
    /**
     * Draws the object to the screen.
     * @param g The graphics context used to draw.
     */
    void draw(Graphics2D g);

    /**
     * Getter for the z-index.
     * @return Z-Index of the drawable.
     */
    Integer getZIndex();

    /**
     * Setter for the z-index.
     * @param idx The new z-index of the drawable.
     */
    void setZIndex(Integer idx);
}
