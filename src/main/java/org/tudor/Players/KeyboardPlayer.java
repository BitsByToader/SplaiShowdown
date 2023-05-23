package org.tudor.Players;

import org.tudor.Entities.PlayerEntity;
import org.tudor.Graphics.Primitives.CorePoint;
import org.tudor.Graphics.Skeletons.HumanSkeleton;
import org.tudor.Input.InputObserver;
import org.tudor.Input.InputType;
import org.tudor.Input.KeyManager;

import java.awt.*;
import java.util.ArrayList;

/**
 * This needs to be abstracted into a base Player interface/abstract class!
 * This class manages the behaviour of a Player controlled by a Keyboard, using a micro-state-machine.
 */
public class KeyboardPlayer implements InputObserver {
    /** The player entity this class controls. */
    private final PlayerEntity entity;
    /** The type of player this instance represents. */
    private final KeyboardPlayerType type;

    /** Health of the player. */
    private Integer health = 100;

    /** Whether the player is moving left. */
    private boolean movingLeft = false;
    /** Whether the player is moving right. */
    private boolean movingRight = false;
    /** The orientation of the player. */
    private PlayerOrientationState orientation = PlayerOrientationState.RIGHT;
    /** The current state of the player. */
    private PlayerState state = PlayerState.IDLE;

    /**
     * Constructor that sets up the player entity and registers to the KeyManager.
     * @param t The type of keyboard player.
     */
    public KeyboardPlayer(KeyboardPlayerType t) {
        HumanSkeleton s = null;
        if ( t == KeyboardPlayerType.PLAYER_1) {
            s = new HumanSkeleton(new Point(300, 400));
        } else if ( t == KeyboardPlayerType.PLAYER_2 ) {
            s = new HumanSkeleton(new Point(600, 400));
        }

        entity = new PlayerEntity(s);
        this.type = t;
        KeyManager.shared().register(this);
    }

    @Override
    public void releasedInput(InputType i) {
        switch (type) {
            case PLAYER_1 -> {
                switch (i) {
                    case LEFTL -> movingLeft = false;
                    case RIGHTL -> movingRight = false;
                    case DEFENDL -> {
                        entity.freeze();
                        entity.returnSkeletonToBase();
                        state = PlayerState.IDLE;
                    }
                }
            }

            case PLAYER_2 -> {
                switch (i) {
                    case LEFTR -> {
                        movingLeft = false;
                        state = PlayerState.IDLE;
                    }
                    case RIGHTR -> {
                        movingRight = false;
                        state = PlayerState.IDLE;
                    }
                    case DEFENDR -> {
                        entity.freeze();
                        entity.returnSkeletonToBase();
                        state = PlayerState.IDLE;
                    }
                }
            }
        }
    }

    @Override
    public void newInput(InputType i) {
        switch (type) {
            case PLAYER_1 -> {
                switch (i) {
                    case LEFTL -> {
                        movingLeft = true;
                        orientation = PlayerOrientationState.LEFT;
                    }
                    case RIGHTL -> {
                        movingRight = true;
                        orientation = PlayerOrientationState.RIGHT;
                    }
                    case UPL -> System.out.println("JUMP ANIMATION");
                    case DOWNL -> System.out.println("DUCK ANIMATION");
                    case PUNCHL -> {
                        if ( orientation == PlayerOrientationState.LEFT)
                            entity.punchLeft();
                        else if ( orientation == PlayerOrientationState.RIGHT )
                            entity.punchRight();
                    }
                    case KICKL -> System.out.println("KICK ANIMATION");
                    case DEFENDL -> {
                        entity.defend();
                        state = PlayerState.DEFENDING;
                    }
                    case COMBO1L -> System.out.println("COMBO ANIMATION");
                }
            }

            case PLAYER_2 -> {
                switch (i) {
                    case LEFTR -> {
                        movingLeft = true;
                        orientation = PlayerOrientationState.LEFT;
                    }
                    case RIGHTR -> {
                        movingRight = true;
                        orientation = PlayerOrientationState.RIGHT;
                    }
                    case UPR -> System.out.println("JUMP ANIMATION");
                    case DOWNR -> System.out.println("DUCK ANIMATION");
                    case PUNCHR -> {
                        if ( orientation == PlayerOrientationState.LEFT)
                            entity.punchLeft();
                        else if ( orientation == PlayerOrientationState.RIGHT )
                            entity.punchRight();
                    }
                    case KICKR -> System.out.println("KICK ANIMATION");
                    case DEFENDR -> {
                        entity.defend();
                        state = PlayerState.DEFENDING;
                    }
                    case COMBO1R -> System.out.println("COMBO ANIMATION");
                }
            }
        }
    }

    /**
     * Updates the state machine.
     */
    public void update() {
        if ( state != PlayerState.DEFENDING ) {
            state = entity.isAnimating() ? PlayerState.ANIMATING : PlayerState.IDLE;

            if ( movingLeft ) {
                state = PlayerState.MOVING;
                entity.translatePosition(new Point(-3, 0));
            }

            if ( movingRight ) {
                state = PlayerState.MOVING;
                entity.translatePosition(new Point(3, 0));
            }
        }
    }

    /**
     * Getter for the entity of this player.
     * @return The entity in question.
     */
    public PlayerEntity getEntity() {
        return entity;
    }

    /**
     * Performs clean-up operations: freeze the entity, removes it from the render queue and
     * unregisters from the KeyManager.
     */
    public void cleanup() {
        entity.freeze();
        entity.stopDrawing();
        KeyManager.shared().unregister(this);
    }

    /**
     * Getter for the health.
     * @return Player health.
     */
    public Integer getHealth() {
        return health;
    }

    /**
     * Hit checker between this player and another player.
     * @param p The other player to check hits on.
     * @return If there was a hit caused by this player.
     */
    public boolean checkHitOn(KeyboardPlayer p) {
        if ( this.state != PlayerState.ANIMATING || p.state == PlayerState.DEFENDING )
            return false;

        ArrayList<CorePoint> damageInflector = new ArrayList<>();
        damageInflector.add(entity.getJoint("fistLeft"));
        damageInflector.add(entity.getJoint("fistRight"));
        damageInflector.add(entity.getJoint("footLeft"));
        damageInflector.add(entity.getJoint("footRight"));

        Rectangle boundingBox = p.getEntity().getBoundingBox();

        for (CorePoint di: damageInflector ) {
            if (boundingBox.contains(di.getAbsolutePos()) )
                return true;
        }

        return false;
    }

    /**
     * Informs the player that it was hit.
     * @param dmg The damage it took.
     */
    public void inflictDamage(int dmg) {
        health -= dmg;
    }
}
