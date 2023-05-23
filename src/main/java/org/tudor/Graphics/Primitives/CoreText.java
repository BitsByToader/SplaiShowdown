package org.tudor.Graphics.Primitives;

import java.awt.*;

/**
 * A graphics primitive that renders text via the Drawable interface.
 * A possible addition to this class can be adding a CorePoint to make its position animatable, or
 * even implementing the Animatable interface to animate text changing or other features.
 */
public class CoreText implements Drawable {
    /**The Z-Index of the Drawable. */
    private int zIndex = 1;
    /** The text being rendered. */
    private String text;
    /** The position of the text being rendered. */
    private Point position;

    /**
     * Sets up the CoreText.
     * @param text What to render.
     * @param pos Where to render.
     */
    public CoreText(String text, Point pos) {
        this.text = text;
        this.position = new Point(pos);
    }

    /**
     * Setter for a new String.
     * @param text New text to be rendered.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * The new position of the text.
     * @param pos New position of the CoreText in absolute coordinates.
     */
    public void setPosition(Point pos) {
        this.position = pos;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(new Color(0,0,0));
        g.drawString(text, position.x, position.y);
    }

    @Override
    public Integer getZIndex() {
        return zIndex;
    }

    @Override
    public void setZIndex(Integer idx) {
        zIndex = idx;
    }
}
