package org.tudor.Graphics.Animations;

import org.tudor.Graphics.Primitives.CorePoint;

import java.awt.*;

public class PointAnimationHandler extends AnimationHandler<Point> {
    public PointAnimationHandler(SubAnimation<Animatable<Point>, Point> animation) {
        super(animation);
        current = new Point(0,0);
        this.animation.target = ((CorePoint) this.animation.entity).getDeltaUsingBase(this.animation.target);
    }

    @Override
    public AnimationHandler<Point> getNextHandler() {
        if ( animation.next != null ) {
            return new PointAnimationHandler(animation.next);
        } else {
            return null;
        }
    }

    @Override
    public void reset() {
        super.reset();
        current = new Point(0,0);
    }

    @Override
    public void calculateNewState() {
        int targetX = animation.target.x;
        int targetY = animation.target.y;

        double newX = ( (double)targetX / animation.durationMs ) * time;
        double newY = ( (double)targetY / animation.durationMs ) * time;

        Point delta = new Point((int)newX - current.x, (int)newY - current.y);

        animation.entity.animate(delta);

        current = new Point((int)newX, (int)newY);
    }
}
