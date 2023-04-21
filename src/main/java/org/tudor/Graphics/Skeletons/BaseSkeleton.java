package org.tudor.Graphics.Skeletons;

import org.tudor.Graphics.Primitives.CorePoint;
import org.tudor.Graphics.Primitives.CoreRectangle;

import java.awt.*;

abstract public class BaseSkeleton {
    private CorePoint cpTreeRoot = null;
    private Point absolutePos = new Point(0, 0);

    public void setCpTreeRoot(CorePoint cp) {
        cpTreeRoot = cp;
    }

    public void setPosition(int x, int y) {
        cpTreeRoot.move(x-absolutePos.x, y-absolutePos.y);
        absolutePos.setLocation(x, y);
    }

    public void translatePosition(int deltaX, int deltaY) {
        absolutePos.translate(deltaX, deltaY);
        cpTreeRoot.move(deltaX, deltaY);
    }

    public abstract CoreRectangle getBodyPart(String name);

    public abstract CorePoint getJoint(String name);
}
