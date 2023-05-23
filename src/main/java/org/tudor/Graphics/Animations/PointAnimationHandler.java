package org.tudor.Graphics.Animations;

import org.tudor.Graphics.Primitives.CorePoint;

import java.awt.*;

/**
 * Extends the AnimationHandler class to provide implementations for specific calculations that are done
 * on Point animatables and differ from other animatable types.
 */
public class PointAnimationHandler extends AnimationHandler<Point> {
    /** The actual, absolute, target we're animating towards calculated from the relative value
     * found in the sub-animation. */
    private Point actualTarget = null;

    public PointAnimationHandler(SubAnimation<Animatable<Point>, Point> animation) {
        super(animation);
        current = new Point(0,0);
    }

    @Override
    public AnimationHandler<Point> getNextHandler() {
        if ( animation.next != null ) {
            return new PointAnimationHandler(animation.next);
        } else {
            return null;
        }
    }

    public void startAnimation() {
        this.reset();
        // Calculate the actual target we're animating towards using the delta target from the
        // base position.
        this.actualTarget = ((CorePoint) this.animation.entity).getDeltaUsingBase(this.animation.target);
    }

    @Override
    public void reset() {
        super.reset();
        current = new Point(0,0);
        actualTarget = new Point(0, 0);
    }

    @Override
    public void calculateNewState() {
        int targetX = actualTarget.x;
        int targetY = actualTarget.y;

        double newX = ( (double)targetX / animation.durationMs ) * time;
        double newY = ( (double)targetY / animation.durationMs ) * time;

        Point delta = new Point((int)newX - current.x, (int)newY - current.y);

        animation.entity.animate(delta);

        current = new Point((int)newX, (int)newY);
    }
}
