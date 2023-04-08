package org.tudor.Animations;

import java.util.ArrayList;

public class CorePoint {
    private CorePoint parent = null;
    private ArrayList<CorePoint> children;

    /** A CorePoint's position will always be relative to its parent. The root CorePoint
     * is also supposed to have its position relative to something, like a Player's bounding box or
     * overall skeleton. We do this because we don't need to impose any world positioning to the CorePoint tree,
     * since animations will always be referenced from a default state, thus working with relative positions.
     */
    private int relativeX = 0;
    private int relativeY = 0;

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
        children.add(cp);
    }

    public void setParent(CorePoint cp) {
        parent = cp;
    }

    public void move(int deltaX, int deltaY) {
        relativeX += deltaX;
        relativeY += deltaY;

        // TODO: Change the parent's position as well (kinda hard).

        // When moving a parent, we execute a drag-along movement on the children.
        // (i.e. we move the children the same way as the parent)
        for ( CorePoint child: children ) {
            child.move(deltaX, deltaY);
        }
    }
}
