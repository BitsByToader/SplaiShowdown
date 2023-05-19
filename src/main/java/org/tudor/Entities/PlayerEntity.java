package org.tudor.Entities;

import org.tudor.Graphics.Animations.*;
import org.tudor.Graphics.Skeletons.HumanSkeleton;

import java.awt.*;
import java.util.Vector;

public class PlayerEntity extends Entity {
    private Vector<SubAnimation<Animatable<Point>, Point>> punchSubAnimations = new Vector<>();
    private Vector<SubAnimation<Animatable<Point>, Point>> helloSubAnimations = new Vector<>();

    public PlayerEntity(HumanSkeleton s) {
        super(s);

        skeleton.getJoint("fistRight").registerForAnimations();
        skeleton.getJoint("elbowRight").registerForAnimations();

        // Set-up hello
        helloSubAnimations.add(new SubAnimation<>(
                skeleton.getJoint("fistRight"),
                new Point(15, -25),
                1000,
                new SubAnimation<>(
                        skeleton.getJoint("fistRight"),
                        new Point(-10, -20),
                        1000,
                        new SubAnimation<>(
                                skeleton.getJoint("fistRight"),
                                new Point(15, -20),
                                1000,
                                null
                        )
                )
        ));

        helloSubAnimations.add(new SubAnimation<>(
                skeleton.getJoint("elbowRight"),
                new Point(15, -15),
                500,
                null
        ));

        // Set-up punch
        punchSubAnimations.add(new SubAnimation<>(
                skeleton.getJoint("elbowRight"),
                new Point(-3, 8),
                500,
                new SubAnimation<>(
                        skeleton.getJoint("elbowRight"),
                        new Point(14, -8),
                        500,
                        null
                )
        ));

        punchSubAnimations.add(new SubAnimation<>(
                skeleton.getJoint("fistRight"),
                new Point(8, -37),
                500,
                new SubAnimation<>(
                        skeleton.getJoint("fistRight"),
                        new Point(18, -22),
                        500,
                        null
                )
        ));
    }

    public void hello() {
        freeze();
        sendAnimationToManager(helloSubAnimations);
    }

    public void punch() {
        // Stop any current animations
        freeze();
        // Start the punch animation
        sendAnimationToManager(punchSubAnimations);
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
    }

    @Override
    public void update() {
        super.update();
    }
}
