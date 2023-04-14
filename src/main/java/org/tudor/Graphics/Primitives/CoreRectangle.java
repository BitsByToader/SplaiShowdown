package org.tudor.Graphics.Primitives;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class CoreRectangle implements Drawable {
    private Integer zIndex = 1;

    private CorePoint parent = null;
    private CorePoint child = null;

    private BufferedImage texture = null;

    public CoreRectangle(BufferedImage texture, CorePoint parent, CorePoint child) {
        this.texture = texture;
        this.parent = parent;
        this.child = child;
    }

    @Override
    public void draw(Graphics2D g) {
        AffineTransform t = new AffineTransform();

        Point parentAbsPos = parent.getAbsolutePos();

        Point imgPos = new Point(parentAbsPos);
        imgPos.x -= texture.getWidth() / 2;

        t.rotate(Math.atan2(child.getRelativePos().y, child.getRelativePos().x) - Math.PI/2,
                parentAbsPos.x, parentAbsPos.y);
        t.translate(imgPos.x, imgPos.y);

        g.drawRenderedImage(texture, t);

        // Debug rendering of CorePoints
//        g.setColor(new Color(255, 0, 0));
//        g.fillRect(parentAbsPos.x, parentAbsPos.y, 1, 1);
//        Point childAbsPos = child.getAbsolutePos();
//        g.fillRect(childAbsPos.x, childAbsPos.y, 1, 1);
    }

    public void setZIndex(Integer idx) {
        zIndex = idx;
    }

    public Integer getZIndex() {
        // TODO: Stub implementation
        return zIndex;
    }
}
