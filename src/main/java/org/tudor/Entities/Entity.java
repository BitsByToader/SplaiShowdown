package org.tudor.Entities;

import org.tudor.Graphics.Skeletons.BaseSkeleton;

import java.awt.*;

/**
 * Base class for all the entities in the game. It stores the skeleton of the entity, necessary for the
 * entity to be rendered, and the bounding box of the entity, necessary for collisions with other
 * entities.
 */
public class Entity {
    /** The skeleton of the entity. */
    protected BaseSkeleton skeleton;

    /** The height of the bounding box. */
    protected Integer boundingBoxHeight;
    /** The width of the bounding box. */
    protected Integer boundingBoxWidth;
    /** The position of the bounding box relative to the root position of the skeleton. */
    protected Point boundingBoxRelativePosition;

    /**
     * Basic constructor for an entity.
     * @param s The skeleton of the entitiy.
     */
    public Entity(BaseSkeleton s) {
        this.skeleton = s;
    }

    /**
     * Translates the entity's position in the world.
     * @param p The amount of pixels to translate the skeleton by.
     */
    public void translatePosition(Point p) {
        this.skeleton.translatePosition(p.x, p.y);
    }

    /**
     * Sets the absolute position of the entity in the world.
     * @param p The new position of the entity.
     */
    public void setPosition(Point p) {
        this.skeleton.setPosition(p.x, p.y);
    }

    /**
     * Gets the position of the entity in the world.
     * @return The position of the entity.
     */
    public Point getPosition() { return this.skeleton.getAbsolutePosition(); }

    /**
     * Gets the current bounding box of the entity.
     * @return A Rectangle object positioned absolutely in the world.
     */
    public Rectangle getBoundingBox() {
        Point p = getPosition();
        p.translate(boundingBoxRelativePosition.x, boundingBoxRelativePosition.y);
        return new Rectangle(p, new Dimension(boundingBoxWidth, boundingBoxHeight));
    }

    /**
     * Notifies the GameRenderer to begin drawing the entity.
     */
    public void startDrawing() {
        skeleton.beginRendering();
    }

    /**
     * Notifies the GameRenderer to stop drawing the entity.
     */
    public void stopDrawing() {
        skeleton.stopRendering();
    }

    /**
     * Update method that is called every frame.
     */
    public void update() {};
}
