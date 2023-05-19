package org.tudor.Players;

import org.tudor.Entities.PlayerEntity;
import org.tudor.Graphics.Skeletons.HumanSkeleton;
import org.tudor.Input.InputObserver;
import org.tudor.Input.InputType;
import org.tudor.Input.KeyManager;

import java.awt.*;

public class KeyboardPlayer implements InputObserver {
    private PlayerEntity entity = null;
    private KeyboardPlayerType type;

    private boolean movingLeft = false;
    private boolean movingRight = false;

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
                }
            }

            case PLAYER_2 -> {
                switch (i) {
                    case LEFTR -> movingLeft = false;
                    case RIGHTR -> movingRight = false;
                }
            }
        }
    }

    @Override
    public void newInput(InputType i) {
        switch (type) {
            case PLAYER_1 -> {
                switch (i) {
                    case LEFTL -> movingLeft = true;
                    case RIGHTL -> movingRight = true;
                    case UPL -> System.out.println("JUMP ANIMATION");
                    case DOWNL -> System.out.println("DUCK ANIMATION");
                    case PUNCHL -> entity.punch();
                    case KICKL -> entity.hello();
                    case DEFENDL -> System.out.println("DEFEND ANIMATION");
                    case COMBO1L -> System.out.println("COMBO ANIMATION");
                }
            }

            case PLAYER_2 -> {
                switch (i) {
                    case LEFTR -> movingLeft = true;
                    case RIGHTR -> movingRight = true;
                    case UPR -> System.out.println("JUMP ANIMATION");
                    case DOWNR -> System.out.println("DUCK ANIMATION");
                    case PUNCHR -> entity.punch();
                    case KICKR -> entity.hello();
                    case DEFENDR -> System.out.println("DEFEND ANIMATION");
                    case COMBO1R -> System.out.println("COMBO ANIMATION");
                }
            }
        }
    }

    public void update() {
        if ( movingLeft ) {
            entity.translatePosition(new Point(-2, 0));
        }

        if ( movingRight ) {
            entity.translatePosition(new Point(2, 0));
        }
    }
}
