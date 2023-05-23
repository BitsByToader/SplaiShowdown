package org.tudor.Graphics.Skeletons;

import org.tudor.Graphics.Primitives.CorePoint;
import org.tudor.Graphics.Primitives.CoreRectangle;

import java.awt.*;
import java.util.Collection;

/**
 * Abstract class that defines basic functionality for a CorePoint tree. It leaves it up to the implementing
 * classes to decide how to store the body parts, other CorePoints and how the actual tree looks like.
 */
abstract public class BaseSkeleton {
    /** The roof of the CorePoint tree. */
    private CorePoint cpTreeRoot = null;
    /** The absolute position in the world of the skeleton. */
    private final Point absolutePos = new Point(0, 0);

    /**
     * Default stub constructor for implementing classes to use when constructing their specific skeleton.
     */
    public BaseSkeleton() {}

    /**
     * Setter for the CorePoint tree root.
     * @param cp The root of the tree.
     */
    public void setCpTreeRoot(CorePoint cp) {
        cpTreeRoot = cp;
    }

    /**
     * Sets the absolute position of the skeleton in the world.
     * @param x X coordinate of the new position.
     * @param y Y coordinate of the new position.
     */
    public void setPosition(int x, int y) {
        cpTreeRoot.move(x-absolutePos.x, y-absolutePos.y);
        absolutePos.setLocation(x, y);
    }

    /**
     * Translate the position of the skeleton in the world.
     * @param deltaX The offset of the new position on the X axis.
     * @param deltaY The offset of the new position on the Y axis.
     */
    public void translatePosition(int deltaX, int deltaY) {
        absolutePos.translate(deltaX, deltaY);
        cpTreeRoot.move(deltaX, deltaY);
    }

    /**
     * Getter for the absolute position of the tree.
     * @return The absolute position of tree.
     */
    public Point getAbsolutePosition() { return new Point(absolutePos); }

    /**
     * Notifies the <i>GameRenderer</i> that this skeleton will need to be rendered.
     */
    public abstract void beginRendering();

    /**
     * Notifies the <i>GameRenderer</i> that this skeleton can be removed from the render queue.
     */
    public abstract void stopRendering();

    /**
     * Getter for a body part of the skeleton.
     * @param name Name of the body part to retrieve.
     * @return The CoreRectangle associated with the body part.
     */
    public abstract CoreRectangle getBodyPart(String name);

    /**
     * Getter for a joint of the skeleton.
     * @param name Name of the joint to retrieve.
     * @return The CorePoint associated with the joint.
     */
    public abstract CorePoint getJoint(String name);

    /**
     * Getter for the joints of the skeleton.
     * @return A Collection of the joints.
     */
    public abstract Collection<CorePoint> getJoints();
}
