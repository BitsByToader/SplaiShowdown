package org.tudor.Graphics.Primitives;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * The CoreRectangle is the only way other higher-level systems will interact with textures in the game.
 * Instead of directly manipulating the underlying images, it is expected to use the CoreRectangle and
 * its two CorePoints to display anything in the game. While this might seem like an unnecessary
 * abstraction, it plays into the animation system, as animating the two CPs will basically animate the
 * texture in the game. As such, it is necessary to implement the Drawable interface so a CoreRectangle
 * can be passed to the GameRenderer.
 * One thing to note, however, is that a CoreRectangle will not infer any sizing on the texture. The second
 * CorePoint is only used for calculating angles, and doesn't resize the BufferedImage used to store the
 * image.
 */
public class CoreRectangle implements Drawable {
    /** Z-index of the rectangle. */
    private Integer zIndex = 1;

    /** The parent CorePoint which will be used to calculate the position of the CoreRectangle. */
    private CorePoint parent = null;
    /** The child CorePoint which will be used to calculate the angle of the CoreRectangle. */
    private CorePoint child = null;

    /** The texture to display. */
    private BufferedImage texture = null;

    /**
     * Constructor of a CoreRectangle.
     * @param texture The texture that will be rendered.
     * @param parent Parent CorePoint.
     * @param child Child CorePoint.
     */
    public CoreRectangle(BufferedImage texture, CorePoint parent, CorePoint child) {
        this.texture = texture;
        this.parent = parent;
        this.child = child;
    }

    /**
     * Draws the CoreRectangle.
     * @param g The graphics context the CoreRectangle is drawn into.
     */
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
//        g.fillRect(parentAbsPos.x, parentAbsPos.y, 2, 2);
//        Point childAbsPos = child.getAbsolutePos();
//        g.fillRect(childAbsPos.x, childAbsPos.y, 2, 2);
    }

    public void setZIndex(Integer idx) {
        zIndex = idx;
    }

    public Integer getZIndex() {
        return zIndex;
    }
}
