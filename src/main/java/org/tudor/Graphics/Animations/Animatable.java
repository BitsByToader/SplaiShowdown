package org.tudor.Graphics.Animations;

import java.util.UUID;

/**
 * The Animatable interface is very light, it forces implementing classes to only provide the
 * <i>animate</i> method. This is because an animatable object only needs to do one thing from the
 * animation perspective: change its state or one of its states to a new state.
 * For example, a <i>CorePoint's</i> main state is its 2D position, a background's state is its color, etc.
 * @param <T> The type of state that the implementing class will want to animate.
 */
public interface Animatable<T> {
    /**
     * Utility method for registering an Animatable for animations and making it responsible
     * for its UUID.
     */
    void registerForAnimations();

    /**
     * Utility method for unregistering an Animatable from animations.
     */
    void unregisterFromAnimations();

    /**
     * Getter for the animations UUID.
     * @return The UUID tied to this Animatable.
     */
    UUID getAnimationIdentifier();

    /**
     * Checks if this Animatable is currently performing an Animation.
     * @return The animation state.
     */
    boolean isAnimating();

    /**
     * Notifies this animatable that the animation has stopped.
     */
    void animationHasStopped();

    /**
     * Changes the animatable state of the object to the new state.
     * @param newState The new state of the object.
     */
    void animate(T newState);
}
