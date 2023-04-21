package org.tudor.Graphics.Primitives;

import org.tudor.Graphics.Animations.Animatable;

import java.awt.*;
import java.util.ArrayList;

public class CorePoint implements Animatable<Point> {
    private CorePoint parent = null;
    private ArrayList<CorePoint> children = new ArrayList<>();

    public CorePoint(Point initialPosition) {
        relativePos = initialPosition;
    }

    /** A CorePoint's position will always be relative to its parent. However, the root of the CorePoint
     * tree has the <i>relativePos</i> field actually be the absolute position in the world.
     * We do this because we don't need to impose any world positioning to the CorePoint tree,
     * since animations will always be referenced from a default state, thus working with relative positions.
     * Making the root keep the absolute position, instead of relative to some other component, is also
     * really helpful because we can recursively determine absolute positions for the other CorePoints very
     * easily, e.g. when drawing textures.
     */
    private Point relativePos;

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

    public void addChild(CorePoint cp) {
        cp.parent = this;
        children.add(cp);
    }

    public void setParent(CorePoint cp) {
        parent = cp;
    }

    public Point getRelativePos() { return relativePos; }

    public Point getAbsolutePos() {
        if ( parent == null ) {
            return new Point(relativePos);
        }

        Point p = parent.getAbsolutePos();
        p.translate(relativePos.x, relativePos.y);
        return p;
    }

    public void move(int deltaX, int deltaY) {
        relativePos.translate(deltaX, deltaY);

        System.out.println("RELATIVE POSITION: " + relativePos);

        // TODO: Change the parent's position as well for a chain-like movement (kinda hard)
    }


    @Override
    public void animate(Point newState) {
        move(newState.x, newState.y);
    }
}
