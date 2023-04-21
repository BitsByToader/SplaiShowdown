package org.tudor.Graphics.Skeletons;

import org.tudor.Graphics.Assets.Assets;
import org.tudor.Graphics.Assets.ImageLoader;
import org.tudor.Graphics.Primitives.CorePoint;
import org.tudor.Graphics.Primitives.CoreRectangle;
import org.tudor.Graphics.GameRenderer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class HumanSkeleton extends BaseSkeleton {
    public HashMap<String, CoreRectangle> bodyParts = new HashMap<>();
    public HashMap<String, CorePoint> joints = new HashMap<>();

    public HumanSkeleton(Point initialPosition) {
        // Create the CP tree aka skeleton
        CorePoint elbowLeft = new CorePoint(new Point(-5, 10));
        CorePoint elbowRight = new CorePoint(new Point(5, 10));
        CorePoint shoulderLeft = new CorePoint(new Point(-5, 5));
        CorePoint shoulderRight = new CorePoint(new Point(5, 5));
        shoulderLeft.addChild(elbowLeft);
        shoulderRight.addChild(elbowRight);

        CorePoint fistLeft = new CorePoint(new Point(0, 10));
        CorePoint fistRight = new CorePoint(new Point(0, 10));
        elbowLeft.addChild(fistLeft);
        elbowRight.addChild(fistRight);

        CorePoint kneeLeft = new CorePoint(new Point(-5, 10));
        CorePoint kneeRight = new CorePoint(new Point(5, 10));
        CorePoint hipLeft = new CorePoint(new Point(-5, 0));
        CorePoint hipRight = new CorePoint(new Point(5, 0));
        hipLeft.addChild(kneeLeft);
        hipRight.addChild(kneeRight);

        CorePoint footLeft = new CorePoint(new Point(0, 10));
        CorePoint footRight = new CorePoint(new Point(0, 10));
        kneeLeft.addChild(footLeft);
        kneeLeft.addChild(footRight);

        CorePoint neck = new CorePoint(new Point(0,0));
        CorePoint hips = new CorePoint(new Point(0,40));
        hips.addChild(hipLeft);
        hips.addChild(hipRight);
        neck.addChild(hips);
        neck.addChild(shoulderLeft);
        neck.addChild(shoulderRight);

        // Save important joints separately
        joints.put("fistLeft", fistLeft);
        joints.put("fistRight", fistRight);
        joints.put("footLeft", footLeft);
        joints.put("footRight", footRight);
        joints.put("elbowRight", elbowRight);

        // Create the body parts
        BufferedImage partLarge = Assets.partLarge;
        BufferedImage partSmallImg = Assets.partSmall;

        bodyParts.put("torso", new CoreRectangle(partLarge, neck, hips));
        bodyParts.put("armLeft", new CoreRectangle(partSmallImg, shoulderLeft, elbowLeft));
        bodyParts.put("armRight", new CoreRectangle(partSmallImg, shoulderRight, elbowRight));
        bodyParts.put("foreArmLeft", new CoreRectangle(partSmallImg, elbowLeft, fistLeft));
        bodyParts.put("foreArmRight", new CoreRectangle(partSmallImg, elbowRight, fistRight));
        bodyParts.put("thighLeft", new CoreRectangle(partSmallImg, hipLeft, kneeLeft));
        bodyParts.put("thighRight", new CoreRectangle(partSmallImg, hipRight, kneeRight));
        bodyParts.put("legLeft", new CoreRectangle(partSmallImg, kneeLeft, footLeft));
        bodyParts.put("legRight", new CoreRectangle(partSmallImg, kneeRight, footRight));

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

    public CoreRectangle getBodyPart(String name) {
        return bodyParts.get(name);
    };

    public CorePoint getJoint(String name) {
        return joints.get(name);
    };
}
