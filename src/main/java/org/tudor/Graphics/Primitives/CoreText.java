package org.tudor.Graphics.Primitives;

import java.awt.*;

public class CoreText implements Drawable {
    private int zIndex = 1;
    private String text;
    private Point position;

    public CoreText(String text, Point pos) {
        this.text = text;
        this.position = new Point(pos);
    }

    public void setText(String text) {
        this.text = text;
    }

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
