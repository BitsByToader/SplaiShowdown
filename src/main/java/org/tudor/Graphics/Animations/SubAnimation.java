package org.tudor.Graphics.Animations;

/**
 * The SubAnimation data class is similar to a LinkedList node, because the concept and implementation
 * of sub-animation chaining is similar to how a linked list works. What everyone knows of as an
 * animation is composed of multiple chained sub-animations that will perform one after the other.
 * As such, each sub-animation can also be though of as a key-frame in any video-editing software.
 * @param <K> The type of the entity that will be animated, usually Animatable<T>
 * @param <T> The type of state that will be animated in the animation.
 */
public class SubAnimation<K, T> {
    /** The animated entity. */
    public K entity;
    /** The target that is being animated towards. */
    public T target;
    /** The duration of the sub-animation. */
    public int durationMs;
    /** The next sub-animation in the animation chain. */
    public SubAnimation<K, T> next;

    /**
     * Basic constructor for the data class.
     * @param entity The entity of the sub-animation.
     * @param target The target of the sub-animation.
     * @param durationMs The duration of the sub-animation.
     * @param next The next sub-animation.
     */
    public SubAnimation(K entity, T target, int durationMs, SubAnimation<K, T> next) {
        this.entity = entity;
        this.target = target;
        this.durationMs = durationMs;
        this.next = next;
    }

    /**
     * Copy constructor for the sub-animation.
     * @param copy The sub-animation being copied.
     */
    public SubAnimation(SubAnimation<K, T> copy) {
        this.entity = copy.entity;
        this.target = copy.target;
        this.durationMs = copy.durationMs;
        this.next = copy.next;
    }
}
