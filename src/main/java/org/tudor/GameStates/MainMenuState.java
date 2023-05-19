package org.tudor.GameStates;

import org.tudor.Game;
import org.tudor.Graphics.Animations.AnimationManager;
import org.tudor.Graphics.Animations.PointAnimationHandler;
import org.tudor.Graphics.Animations.SubAnimation;
import org.tudor.Graphics.Assets.Assets;
import org.tudor.Graphics.GameRenderer;
import org.tudor.Graphics.Primitives.CorePoint;
import org.tudor.Graphics.Primitives.CoreRectangle;
import org.tudor.Input.InputObserver;
import org.tudor.Input.InputType;
import org.tudor.Input.KeyManager;

import java.awt.*;

public class MainMenuState extends GameState implements InputObserver {
    private CoreRectangle logoRect;
    private CorePoint logoCp;

    private CoreRectangle playButtonRect;
    private CorePoint playButtonCp;

    private CoreRectangle quitButtonRect;
    private CorePoint quitButtonCp;

    private CoreRectangle selectorRect;
    private CorePoint selectorCp;

    private int selectorIndex = 0;

    public MainMenuState(Game context) {
        super(context);

        logoCp = new CorePoint(new Point(400, 50));
        CorePoint logo2 = new CorePoint(new Point(0, 100));
        logoCp.addChild(logo2);
        logoRect = new CoreRectangle(
                Assets.logo,
                logoCp,
                logo2
        );

        playButtonCp = new CorePoint(new Point(400, 300));
        CorePoint pb2 = new CorePoint(new Point(0, 25));
        playButtonCp.addChild(pb2);
        playButtonRect = new CoreRectangle(
                Assets.multiplayer_btn,
                playButtonCp,
                pb2
        );
        playButtonRect.setZIndex(1);

        quitButtonCp = new CorePoint(new Point(400, 400));
        CorePoint qb2 = new CorePoint(new Point(0, 25));
        quitButtonCp.addChild(qb2);
        quitButtonRect = new CoreRectangle(
                Assets.quit_btn,
                quitButtonCp,
                qb2
        );
        quitButtonRect.setZIndex(1);

        selectorCp = new CorePoint(new Point(400, 300));
        CorePoint s2 = new CorePoint(new Point(0, 75));
        selectorCp.addChild(s2);
        selectorRect = new CoreRectangle(
                Assets.menu_selector,
                selectorCp,
                s2
        );
        selectorRect.setZIndex(2);
        selectorCp.registerForAnimations();
    }

    @Override
    public void update(long elapsedMs) {
    }

    @Override
    public void begin() {
        KeyManager.shared().register(this);
        GameRenderer.shared().addToQueue(logoRect);
        GameRenderer.shared().addToQueue(playButtonRect);
        GameRenderer.shared().addToQueue(quitButtonRect);
        GameRenderer.shared().addToQueue(selectorRect);
    }

    @Override
    public void cleanup() {
        GameRenderer.shared().removeFromQueue(logoRect);
        GameRenderer.shared().removeFromQueue(playButtonRect);
        GameRenderer.shared().removeFromQueue(quitButtonRect);
        GameRenderer.shared().removeFromQueue(selectorRect);

        KeyManager.shared().unregister(this);
    }

    @Override
    public void releasedInput(InputType i) { /*stub*/}

    @Override
    public void newInput(InputType i) {
        switch (i) {
            case UPL, UPR -> {
                if ( selectorIndex > 0 ) {
                    selectorIndex--;
                    AnimationManager.shared().addAnimation(
                            selectorCp.getAnimationIdentifier(),
                            new PointAnimationHandler(new SubAnimation<>(
                                    selectorCp,
                                    new Point(0, 0),
                                    250,
                                    null
                            ))
                    );
                }
            }
            case DOWNL, DOWNR -> {
                if ( selectorIndex < 1 ) {
                    selectorIndex++;
                    AnimationManager.shared().addAnimation(
                            selectorCp.getAnimationIdentifier(),
                            new PointAnimationHandler(new SubAnimation<>(
                                    selectorCp,
                                    new Point(0, 100),
                                    250,
                                    null
                            ))
                    );
                }
            }
            case ENTER -> {
                if ( selectorIndex == 0 ) {
                    parentContext.transition(new PlayState(parentContext));
                } else {
                    System.exit(0);
                }
            }
        }
    }
}
