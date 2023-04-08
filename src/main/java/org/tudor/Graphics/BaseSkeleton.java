package org.tudor.Animations;

import java.awt.*;

public class BaseSkeleton {
    private CorePoint cpTreeRoot = null;
    private Point absolutePos = new Point(0, 0);

    public void setCpTreeRoot(CorePoint cp) {
        cpTreeRoot = cp;
    }

    public void setPosition(int x, int y) {
        absolutePos.setLocation(x, y);
        cpTreeRoot.move(absolutePos.x-x, absolutePos.y-y);
    }

    public void translatePosition(int deltaX, int deltaY) {
        absolutePos.translate(deltaX, deltaY);
        cpTreeRoot.move(deltaX, deltaY);
    }
}
