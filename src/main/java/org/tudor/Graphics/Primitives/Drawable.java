package org.tudor.Graphics.Primitives;

import java.awt.*;

public interface Drawable {
    void draw(Graphics2D g);
    Integer getZIndex();
    void setZIndex(Integer idx);
}
