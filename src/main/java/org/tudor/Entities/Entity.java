package org.tudor.Entities;

import org.tudor.Graphics.Skeletons.BaseSkeleton;

import java.awt.*;

public class Entity {
    protected BaseSkeleton skeleton;

    protected Integer boundingBoxHeight;
    protected Integer boundingBoxWidth;
    protected Point boundingBoxRelativePosition;

    public Entity(BaseSkeleton s) {
        this.skeleton = s;
    }

    public void translatePosition(Point p) {
        this.skeleton.translatePosition(p.x, p.y);
    }

    public void setPosition(Point p) {
        this.skeleton.setPosition(p.x, p.y);
    }

    public Point getPosition() { return this.skeleton.getAbsolutePosition(); }

    public Rectangle getBoundingBox() {
        Point p = getPosition();
        p.translate(boundingBoxRelativePosition.x, boundingBoxRelativePosition.y);
        return new Rectangle(p, new Dimension(boundingBoxWidth, boundingBoxHeight));
    }

    public void startDrawing() {
        skeleton.beginRendering();
    }

    public void stopDrawing() {
        skeleton.stopRendering();
    }

    public void update() {};
}
