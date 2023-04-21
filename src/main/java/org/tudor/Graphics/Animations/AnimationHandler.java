package org.tudor.Graphics.Animations;

import java.awt.*;
import java.lang.reflect.ParameterizedType;

public class AnimationHandler<T> {
    private Class targetClass;

    private int time = 0; // ms
    private int duration = 0; // ms
    private T current = null;
    private T target;
    private Animatable<T> animatableEntity = null;

    public AnimationHandler(Class targetClass, Animatable<T> entity, T target, int durationMs) {
        this.animatableEntity = entity;
        this.target = target;
        this.duration = durationMs;
        this.targetClass = targetClass;

        if ( this.targetClass == Point.class ) {
            current = (T) new Point(0,0);
        }
    }

    void timePassed(long ms) {
        time += ms;
    }

    long duration() { return duration; }

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
