package org.tudor.Entities;

import org.tudor.Graphics.Animations.*;
import org.tudor.Graphics.Primitives.CorePoint;
import org.tudor.Graphics.Skeletons.HumanSkeleton;

import java.awt.*;
import java.util.Vector;

public class PlayerEntity extends Entity {
    private Vector<SubAnimation<Animatable<Point>, Point>> punchRightSubAnimations = new Vector<>();
    private Vector<SubAnimation<Animatable<Point>, Point>> punchLeftSubAnimations = new Vector<>();
    private Vector<SubAnimation<Animatable<Point>, Point>> defendSubAnimations = new Vector<>();

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

    public void punchRight() {
        sendAnimationToManager(punchRightSubAnimations);
        returnSkeletonToBase();
    }

    public void punchLeft() {
        sendAnimationToManager(punchLeftSubAnimations);
        returnSkeletonToBase();
    }

    public void defend() {
        sendAnimationToManager(defendSubAnimations);
    }

    private void sendAnimationToManager(Vector<SubAnimation<Animatable<Point>, Point>> animations) {
        AnimationManager m = AnimationManager.shared();
        for ( SubAnimation<Animatable<Point>, Point> a: animations ) {
            m.addAnimation(
                    a.entity.getAnimationIdentifier(),
                    new PointAnimationHandler(a)
            );
        }
    }

    public void freeze() {
        AnimationManager.shared().clearQueue(skeleton.getJoint("elbowRight").getAnimationIdentifier());
        AnimationManager.shared().clearQueue(skeleton.getJoint("fistRight").getAnimationIdentifier());
        AnimationManager.shared().clearQueue(skeleton.getJoint("elbowLeft").getAnimationIdentifier());
        AnimationManager.shared().clearQueue(skeleton.getJoint("fistLeft").getAnimationIdentifier());
        AnimationManager.shared().clearQueue(skeleton.getJoint("hipLeft").getAnimationIdentifier());
        AnimationManager.shared().clearQueue(skeleton.getJoint("hipRight").getAnimationIdentifier());
        AnimationManager.shared().clearQueue(skeleton.getJoint("neck").getAnimationIdentifier());
    }

    public void returnSkeletonToBase() {
        returnJointToBase("elbowRight");
        returnJointToBase("fistRight");
        returnJointToBase("elbowLeft");
        returnJointToBase("fistLeft");
        returnJointToBase("hipLeft");
        returnJointToBase("hipRight");
    }

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

    public boolean isAnimating() {
        for ( CorePoint joint: skeleton.getJoints() ) {
            if ( joint.isAnimating() )
                return true;
        }

        return false;
    }

    public CorePoint getJoint(String name) { return skeleton.getJoint(name); }

    @Override
    public void update() {
        super.update();
    }
}
