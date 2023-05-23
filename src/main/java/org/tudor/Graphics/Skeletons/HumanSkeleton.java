package org.tudor.Graphics.Skeletons;

import org.tudor.Graphics.Assets.Assets;
import org.tudor.Graphics.Primitives.CorePoint;
import org.tudor.Graphics.Primitives.CoreRectangle;
import org.tudor.Graphics.GameRenderer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Implementation of <i>BaseSkeleton</i> for a Human skeleton, using <i>HashMaps</i> for storing the
 * body parts and the joints.
 */
public class HumanSkeleton extends BaseSkeleton {
    /** Maps body parts to their names. */
    public HashMap<String, CoreRectangle> bodyParts = new HashMap<>();
    /** Maps the joints to their names. */
    public HashMap<String, CorePoint> joints = new HashMap<>();

    /**
     * Constructor that builds the skeleton in memory.
     * @param initialPosition The initial, absolute, position of the skeleton.
     */
    public HumanSkeleton(Point initialPosition) {
        // Create the CP tree aka skeleton
        CorePoint elbowLeft = new CorePoint(new Point(-15, 35));
        CorePoint elbowRight = new CorePoint(new Point(8, 37));
        CorePoint shoulderLeft = new CorePoint(new Point(-10, 5));
        CorePoint shoulderRight = new CorePoint(new Point(10, 5));
        shoulderLeft.addChild(elbowLeft);
        shoulderRight.addChild(elbowRight);

        CorePoint fistLeft = new CorePoint(new Point(37, -10));
        CorePoint fistRight = new CorePoint(new Point(9, -39));
        elbowLeft.addChild(fistLeft);
        elbowRight.addChild(fistRight);

        CorePoint kneeLeft = new CorePoint(new Point(-15, 35));
        CorePoint kneeRight = new CorePoint(new Point(15, 35));
        CorePoint hipLeft = new CorePoint(new Point(-7, 0));
        CorePoint hipRight = new CorePoint(new Point(7, 0));
        hipLeft.addChild(kneeLeft);
        hipRight.addChild(kneeRight);

        CorePoint footLeft = new CorePoint(new Point(0, 40));
        CorePoint footRight = new CorePoint(new Point(0, 40));
        kneeLeft.addChild(footLeft);
        kneeRight.addChild(footRight);

        CorePoint neck = new CorePoint(new Point(0,0));
        CorePoint hips = new CorePoint(new Point(0,80));
        CorePoint head = new CorePoint(new Point(0, -30));
        hips.addChild(hipLeft);
        hips.addChild(hipRight);
        neck.addChild(hips);
        neck.addChild(shoulderLeft);
        neck.addChild(shoulderRight);
        neck.addChild(head);

        // Save important joints separately
        joints.put("fistLeft", fistLeft);
        joints.put("fistRight", fistRight);
        joints.put("footLeft", footLeft);
        joints.put("footRight", footRight);
        joints.put("elbowLeft", elbowLeft);
        joints.put("elbowRight", elbowRight);
        joints.put("hipLeft", hipLeft);
        joints.put("hipRight", hipRight);
        joints.put("neck", neck);

        // Create the body parts
        BufferedImage partLarge = Assets.partBig;
        BufferedImage partSmall = Assets.part;

        bodyParts.put("armRight", new CoreRectangle(partSmall, shoulderRight, elbowRight));
        bodyParts.put("armLeft", new CoreRectangle(partSmall, shoulderLeft, elbowLeft));
        bodyParts.put("foreArmLeft", new CoreRectangle(partSmall, elbowLeft, fistLeft));
        bodyParts.put("foreArmRight", new CoreRectangle(partSmall, elbowRight, fistRight));
        bodyParts.put("thighLeft", new CoreRectangle(partSmall, hipLeft, kneeLeft));
        bodyParts.put("thighRight", new CoreRectangle(partSmall, hipRight, kneeRight));
        bodyParts.put("legLeft", new CoreRectangle(partSmall, kneeLeft, footLeft));
        bodyParts.put("legRight", new CoreRectangle(partSmall, kneeRight, footRight));
        bodyParts.put("head", new CoreRectangle(Assets.head, neck, head));
        bodyParts.put("torso", new CoreRectangle(partLarge, neck, hips));

        super.setCpTreeRoot(neck);
        super.setPosition(initialPosition.x, initialPosition.y);
    }

    public void beginRendering() {
        GameRenderer r = GameRenderer.shared();
        bodyParts.forEach( (k, v) -> {
            v.setZIndex(1);
            r.addToQueue(v);
        });
    }

    public void stopRendering() {
        GameRenderer r = GameRenderer.shared();

        bodyParts.forEach( (k, v) -> {
            r.removeFromQueue(v);
        });
    }

    public CoreRectangle getBodyPart(String name) {
        return bodyParts.get(name);
    };

    public CorePoint getJoint(String name) {
        return joints.get(name);
    }

    @Override
    public Collection<CorePoint> getJoints() {
        return joints.values();
    }

    ;
}
