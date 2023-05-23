package org.tudor.Entities;

import org.tudor.Graphics.Animations.*;
import org.tudor.Graphics.Primitives.CorePoint;
import org.tudor.Graphics.Skeletons.HumanSkeleton;

import java.awt.*;
import java.util.Vector;

/**
 * An extension of the Entity clase, the PlayerEntity defines the possible behaviours of a player entity
 * in the game. At this point in time, a player can punch to the left and to the right, defend, and move.
 */
public class PlayerEntity extends Entity {
    /** Sub-animations needed to perform a punch to the right animation. */
    private final Vector<SubAnimation<Animatable<Point>, Point>> punchRightSubAnimations = new Vector<>();
    /** Sub-animations needed to perform a punch to the left animation. */
    private final Vector<SubAnimation<Animatable<Point>, Point>> punchLeftSubAnimations = new Vector<>();
    /** Sub-animations needed to perform a defend animation. */
    private final Vector<SubAnimation<Animatable<Point>, Point>> defendSubAnimations = new Vector<>();

    /**
     * Constructor for the PlayerEntity class. It sets up the animations and the bounding box of the
     * entity.
     * @param s The skeleton used for the entity.
     */
    public PlayerEntity(HumanSkeleton s) {
        super(s);

        // Set-up bounding box
        boundingBoxHeight = 180;
        boundingBoxWidth = 30;
        boundingBoxRelativePosition = new Point(-30, -30);

        // Register points for animation
        skeleton.getJoint("fistRight").registerForAnimations();
        skeleton.getJoint("elbowRight").registerForAnimations();
        skeleton.getJoint("fistLeft").registerForAnimations();
        skeleton.getJoint("elbowLeft").registerForAnimations();
        skeleton.getJoint("hipRight").registerForAnimations();
        skeleton.getJoint("hipLeft").registerForAnimations();
        skeleton.getJoint("neck").registerForAnimations();

        // Set-up punch for right arm
        punchRightSubAnimations.add(new SubAnimation<>(
                skeleton.getJoint("elbowRight"),
                new Point(25, -20),
                100,
                null
        ));

        punchRightSubAnimations.add(new SubAnimation<>(
                skeleton.getJoint("fistRight"),
                new Point(30, 30),
                100,
                null
        ));

        // Set-up punch for left arm
        punchLeftSubAnimations.add(new SubAnimation<>(
                skeleton.getJoint("elbowLeft"),
                new Point(-20, -20),
                100,
                null
        ));

        punchLeftSubAnimations.add(new SubAnimation<>(
                skeleton.getJoint("fistLeft"),
                new Point(-80, 10),
                100,
                null
        ));

        // Set-up defend animation
        defendSubAnimations.add(new SubAnimation<>(
                skeleton.getJoint("fistLeft"),
                new Point(-35, -10),
                100,
                null
        ));

        defendSubAnimations.add(new SubAnimation<>(
                skeleton.getJoint("fistRight"),
                new Point(-15, 10),
                100,
                null
        ));

        defendSubAnimations.add(new SubAnimation<>(
                skeleton.getJoint("elbowLeft"),
                new Point(10, -15),
                100,
                null
        ));

        defendSubAnimations.add(new SubAnimation<>(
                skeleton.getJoint("elbowRight"),
                new Point(-5, -15),
                100,
                null
        ));
    }

    /**
     * Performs a punch to the right animation.
     */
    public void punchRight() {
        sendAnimationToManager(punchRightSubAnimations);
        returnSkeletonToBase();
    }

    /**
     * Performs a punch to the left animation.
     */
    public void punchLeft() {
        sendAnimationToManager(punchLeftSubAnimations);
        returnSkeletonToBase();
    }

    /**
     * Performs a defend animation.
     */
    public void defend() {
        sendAnimationToManager(defendSubAnimations);
    }

    /**
     * Sends a sub-animation to the AnimationManager.
     * @param animations A list of sub animations to perform.
     */
    private void sendAnimationToManager(Vector<SubAnimation<Animatable<Point>, Point>> animations) {
        AnimationManager m = AnimationManager.shared();
        for ( SubAnimation<Animatable<Point>, Point> a: animations ) {
            m.addAnimation(
                    a.entity.getAnimationIdentifier(),
                    new PointAnimationHandler(a)
            );
        }
    }

    /**
     * Stops any current animations this entity is performing.
     */
    public void freeze() {
        AnimationManager.shared().clearQueue(skeleton.getJoint("elbowRight").getAnimationIdentifier());
        AnimationManager.shared().clearQueue(skeleton.getJoint("fistRight").getAnimationIdentifier());
        AnimationManager.shared().clearQueue(skeleton.getJoint("elbowLeft").getAnimationIdentifier());
        AnimationManager.shared().clearQueue(skeleton.getJoint("fistLeft").getAnimationIdentifier());
        AnimationManager.shared().clearQueue(skeleton.getJoint("hipLeft").getAnimationIdentifier());
        AnimationManager.shared().clearQueue(skeleton.getJoint("hipRight").getAnimationIdentifier());
        AnimationManager.shared().clearQueue(skeleton.getJoint("neck").getAnimationIdentifier());
    }

    /**
     * Returns the entity to its base position.
     */
    public void returnSkeletonToBase() {
        returnJointToBase("elbowRight");
        returnJointToBase("fistRight");
        returnJointToBase("elbowLeft");
        returnJointToBase("fistLeft");
        returnJointToBase("hipLeft");
        returnJointToBase("hipRight");
    }

    /**
     * Returns the joint to its base position via an animation.
     * @param jointName The joint to return.
     */
    private void returnJointToBase(String jointName) {
        AnimationManager.shared().addAnimation(
                skeleton.getJoint(jointName).getAnimationIdentifier(),
                new PointAnimationHandler(new SubAnimation<>(
                        skeleton.getJoint(jointName),
                        new Point(0, 0),
                        100,
                        null
                ))
        );
    }

    /**
     * Checks if the entity is currently performing an animation.
     * @return Whether or not an animation is performed.
     */
    public boolean isAnimating() {
        for ( CorePoint joint: skeleton.getJoints() ) {
            if ( joint.isAnimating() )
                return true;
        }

        return false;
    }

    /**
     * Gets a joint from the skeleton
     * @param name The joint name.
     * @return The requested joint.
     */
    public CorePoint getJoint(String name) { return skeleton.getJoint(name); }

    @Override
    public void update() {
        super.update();
    }
}
