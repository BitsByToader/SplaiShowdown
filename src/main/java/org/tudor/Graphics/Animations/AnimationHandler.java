package org.tudor.Graphics.Animations;

import java.awt.*;
import java.lang.reflect.ParameterizedType;

/**
 * While it's called a handler, it also defines the actual animation that is performing. Since animations are
 * relative to a previous state, the traits of an animation are as follows: duration and a target state.
 * While currently linear, this class will be made abstract so that it can be extended, so it can support
 * linear, exponential, ease-in, ease-out, etc animations.
 * AnimationHandlers also require an entity that will be animated, which conforms to the <i>Animatable</i>
 * interface. As such, every frame, the handler will first be provided the time that has passed from the
 * previous animation frame. Afterward, the handler can be asked to calculate the new state for the object.
 * While the handler could automatically calculate it's time between calls, this method is more inline with
 * <i>MainLoop design pattern</i> and can potentially allow more freedom with timing the animations.
 * @param <T> The type of the animatable state of the object we're animating.
 */
public class AnimationHandler<T> {
    /** The class of the state we're animating. */
    private Class targetClass;

    /** Current animation time. */
    private int time = 0; // ms
    /** The duration of the animation */
    private int duration = 0; // ms
    /** Current value of the state we're animating. */
    private T current = null;
    /** The target state we're animating towards. */
    private T target;
    /** The entity we're animating. */
    private Animatable<T> animatableEntity = null;

    /**
     * Constructs a handler that will animate an entity's state.
     * @param targetClass The class of the state we're animating.
     * @param entity The entity we're animating.
     * @param target The target state we're animating towards.
     * @param durationMs The duration of the animation in milliseconds.
     */
    public AnimationHandler(Class targetClass, Animatable<T> entity, T target, int durationMs) {
        this.animatableEntity = entity;
        this.target = target;
        this.duration = durationMs;
        this.targetClass = targetClass;

        if ( this.targetClass == Point.class ) {
            current = (T) new Point(0,0);
        }
    }

    /**
     * Informs the handler that some time has passed, and we will be calculating the new state in the animation.
     * @param ms The time that has passed in milliseconds.
     */
    void timePassed(long ms) {
        time += ms;
    }

    /**
     * Getter for the duration of the animation.
     * @return The duration of the animation.
     */
    long duration() { return duration; }

    /**
     * Calculates the new state of the animation for the current time. Currently only Point targets are supported.
     */
    @SuppressWarnings("unchecked")
    public void calculateNewState() {
        if ( targetClass == Point.class ) {
            int targetX = ((Point) target).x;
            int targetY = ((Point) target).y;

            double newX = ( (double)targetX / duration ) * time;
            double newY = ( (double)targetY / duration ) * time;

            Point delta = new Point((int)newX - ((Point)current).x, (int)newY - ((Point)current).y);

            animatableEntity.animate((T) delta);

            current = (T) new Point((int)newX, (int)newY);
        }
    }
}
