package org.tudor.Entities;

import org.tudor.Graphics.Skeletons.BaseSkeleton;

import java.awt.*;

public class Entity {
    BaseSkeleton skeleton;

    public Entity(BaseSkeleton s) {
        this.skeleton = s;
    }

    public void translatePosition(Point p) {
        this.skeleton.translatePosition(p.x, p.y);
    }

    public void setPosition(Point p) {
        this.skeleton.setPosition(p.x, p.y);
    }

    public void startDrawing() {
        skeleton.beginRendering();
    }

    public void stopDrawing() {
        skeleton.stopRendering();
    }

    public void update() {};
}
