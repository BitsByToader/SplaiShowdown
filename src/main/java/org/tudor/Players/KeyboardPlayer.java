package org.tudor.Players;

import org.tudor.Entities.PlayerEntity;
import org.tudor.Graphics.Primitives.CorePoint;
import org.tudor.Graphics.Skeletons.HumanSkeleton;
import org.tudor.Input.InputObserver;
import org.tudor.Input.InputType;
import org.tudor.Input.KeyManager;

import java.awt.*;
import java.util.ArrayList;

public class KeyboardPlayer implements InputObserver {
    private PlayerEntity entity = null;
    private KeyboardPlayerType type;

    private Integer health = 100;

    private boolean movingLeft = false;
    private boolean movingRight = false;
    private PlayerOrientationState orientation = PlayerOrientationState.RIGHT;
    private PlayerState state = PlayerState.IDLE;

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
                    case LEFTL -> {
                        movingLeft = false;
                    }
                    case RIGHTL -> {
                        movingRight = false;
                    }
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

    public PlayerEntity getEntity() {
        return entity;
    }

    public void cleanup() {
        entity.freeze();
        entity.stopDrawing();
        KeyManager.shared().unregister(this);
    }

    public Integer getHealth() {
        return health;
    }

    public CorePoint getJoint(String name) { return entity.getJoint(name); }

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

    public void inflictDamage(int dmg) {
        health -= dmg;
    }
}
