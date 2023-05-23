package org.tudor.Graphics.Animations;

/**
 * While currently linear, this class will be made abstract so that it can be extended, so it can support
 * linear, exponential, ease-in, ease-out, etc animations.
 * AnimationHandlers also require an entity that will be animated, which conforms to the <i>Animatable</i>
 * interface. As such, every frame, the handler will first be provided the time that has passed from the
 * previous animation frame. Afterward, the handler can be asked to calculate the new state for the object.
 * While the handler could automatically calculate it's time between calls, this method is more inline with
 * <i>MainLoop design pattern</i> and can potentially allow more freedom with timing the animations.
 * @param <T> The type of the animatable state of the object we're animating.
 */
abstract class AnimationHandler<T> {
    /** Current animation time. */
     int time = 0; // ms
    /** Current value of the state we're animating. */
    T current = null;
    /** The animation that is animated by this handler. */
    SubAnimation<Animatable<T>, T> animation;

    /**
     * Constructs a handler that will animate a specific animation.
     * @param animation The animation this AnimationHandler will handle.
     */
    public AnimationHandler(SubAnimation<Animatable<T>, T> animation) {
        this.animation = new SubAnimation<>(animation);
    }

    /**
     * Informs the handler that some time has passed, and we will be calculating the new state in the animation.
     * @param ms The time that has passed in milliseconds.
     */
    void timePassed(long ms) {
        time += ms;
    }

    /**
     * (Convenience) Getter for the duration of the animation.
     * @return The duration of the animation.
     */
    public long duration() {
        return animation.durationMs;
    }

    /**
     * Getter for the animation we're animating.
     * @return The animation being animated by this handler.
     */
    public SubAnimation<Animatable<T>, T> animation() {
        return animation;
    }

    /**
     * Creates and returns an AnimationHandler for the next sub-animation in the chain.
     * @return The next handler to continue the animation.
     */
    public abstract AnimationHandler<T> getNextHandler();

    /**
     * Makes any necessary calculations to start the animation.
     */
    public abstract void startAnimation();

    /**
     * Resets this animation handler to perform this animation one more time.
     */
    public void reset() {
        // Reset the current time
        time = 0;
    }

    /**
     * Calculates the new state of the animation for the current time.
     */
    public abstract void calculateNewState();
}
