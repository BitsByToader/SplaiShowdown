package org.tudor.Graphics.Primitives;

import org.jetbrains.annotations.NotNull;
import org.tudor.Graphics.Animations.Animatable;
import org.tudor.Graphics.Animations.AnimationManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.UUID;

/**
 * The CorePoint class is the absolute base for the Skeleton and Animation systems. It defines the
 * lowest abstraction for the graphics system, along with the <i>CoreRectangle</i> class. A CorePoint
 * defines a child in the CorePoint tree, and one of the two CorePoints for a CoreRectangle.
 * The CorePoint tree is supposed to organize CorePoints into a logical manner to imitate a body, but
 * the main feature is how the CorePoints can be moved in the tree. Moving a CorePoint with children,
 * will determine said children to move exactly the same, preserving the relative positions of the
 * CorePoints in the tree. However, such a movement will also influence the parents, recursively, in
 * a <i>chain-like movement</i>. While this added logic isn't necessary, it greatly simplifies describing
 * and creating an animation, since we can only dictate the movement of a child and get a natural movement
 * out of the parents, as well.
 * By implementing the Animatable interface, a CorePoint states that it can have its position changed by
 * an animation. This is the foundation for the animation system and, by definition, every single animatable
 * object in the game, from a player's arm to any particles and to the backgrounds.
 */
public class CorePoint implements Animatable<Point> {
    /** Parent of the CorePoint. */
    private CorePoint parent = null;
    /** Children of this CorePoint. */
    private ArrayList<CorePoint> children = new ArrayList<>();

    /**
     * The identifier used when interacting with the Animation Manager. While identifiers can
     * be kept separately if the user wants more control, a CorePoint can also keep track of its
     * own identifier when it is registered for animations.
     * This can prove useful when setting up animations directly through the CorePoint.
     */
    private UUID animationIdentifier = null;

    /**
     * A CorePoint's position will always be relative to its parent. However, the root of the CorePoint
     * tree has the <i>relativePos</i> field actually be the absolute position in the world.
     * We do this because we don't need to impose any world positioning to the CorePoint tree,
     * since animations will always be referenced from a default state, thus working with relative positions.
     * Making the root keep the absolute position, instead of relative to some other component, is also
     * really helpful because we can recursively determine absolute positions for the other CorePoints very
     * easily, e.g. when drawing textures.
     */
    private Point relativePos;

    /**
     * The relative position of a CorePoint when it is in its base state. The base point of a
     * CorePoint refers to its position when the whole CorePoint tree is in its base state,
     * animation-wise. This is really helpful for interrupting animations, since we can always
     * reference animations from the base state, and then calculate delta positions from one
     * state of an animation to another easily.
     */
    private Point basePos;

    /**
     * A fixedToParent CorePoint will not allow its children to modify its location during a
     * chain-like movement. For example, this can be a shoulder or a hip joint.
     */
    public boolean fixedToParent = false;

    /**
     * A detached CorePoint will not update its movement based on the parent and won't update a non-fixed
     * parent's position in this state. For example, this can be useful when performing a dismembering
     * animation, and we don't want to permanently break the CorePoint tree.
     */
    public boolean detached = false;

    /**
     * Initialises this CorePoint with a position. This position is two-fold, as it is the
     * relative position the CorePoint takes in the tree, and also the base state of the CorePoint.
     * If it's the root of the tree, this is the absolute position in the world. Otherwise,
     * it's the relative position to its parent.
     * @param initialPosition The initial and base position of the CorePoint.
     */
    public CorePoint(Point initialPosition) {
        relativePos = new Point(initialPosition);
        basePos = new Point(initialPosition);
    }

    /**
     * Adds a child to this CorePoint.
     * @param cp The child to be added.
     */
    public void addChild(CorePoint cp) {
        cp.parent = this;
        children.add(cp);
    }

    /**
     * Sets the parent of this CorePoint.
     * @param cp The new parent.
     */
    public void setParent(CorePoint cp) {
        parent = cp;
    }

    /**
     * Getter for the relative position of the CorePoint in relation to its parent.
     * @return The relative position.
     */
    public Point getRelativePos() { return relativePos; }

    /**
     * Getter for the absolute position of the CorePoint in world coordinates.
     * @return The absolute position.
     */
    public Point getAbsolutePos() {
        if ( parent == null ) {
            return new Point(relativePos);
        }

        Point p = parent.getAbsolutePos();
        p.translate(relativePos.x, relativePos.y);
        return p;
    }

    /**
     * Moves the CorePoint by the specified offset
     * @param deltaX Offset on the X axis.
     * @param deltaY Offset on the Y axis
     */
    public void move(int deltaX, int deltaY) {
        relativePos.translate(deltaX, deltaY);

        // TODO: Change the parent's position as well for a chain-like movement (kinda hard)
    }

    /**
     * Utility method for registering a CorePoint for animations and making the CorePoint responsible
     * for its UUID.
     */
    public void registerForAnimations() {
        animationIdentifier = AnimationManager.shared().registerForAnimations();
    }

    /**
     * Utility method for unregistering a CorePoint from animations.
     */
    public void unregisterFromAnimations() {
        AnimationManager.shared().unregister(animationIdentifier);
        animationIdentifier = null;
    }

    /**
     * Getter for the animations UUID.
     * @return The UUID tied to this Animatable.
     */
    public UUID getAnimationIdentifier() {
        return animationIdentifier;
    }

    /**
     * Calculates the delta between the current position and the target position.
     * @param t Target position, as a delta between this position and the base position.
     * @return Delta between target and current position.
     */
    public Point getDeltaUsingBase(@NotNull Point t) {
        return new Point(
                t.x + basePos.x - relativePos.x,
                t.y + basePos.y - relativePos.y
        );
    }

    @Override
    public void animate(Point newState) {
        move(newState.x, newState.y);
    }
}
