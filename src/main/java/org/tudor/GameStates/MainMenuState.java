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

/**
 * Extends the GameState class for handling main menu logic.
 */
public class MainMenuState extends GameState implements InputObserver {
    private final CoreRectangle logoRect;
    private final CoreRectangle playButtonRect;
    private final CoreRectangle quitButtonRect;
    private final CoreRectangle scoresButtonRect;
    private final CoreRectangle selectorRect;
    private final CorePoint selectorCp;

    /**Holds the currently selected menu item. */
    private int selectorIndex = 0;

    /**
     * Constructor that sets up the logo and buttons of the menu.
     * @param context Game context.
     */
    public MainMenuState(Game context) {
        super(context);

        CorePoint logoCp = new CorePoint(new Point(400, 50));
        CorePoint logo2 = new CorePoint(new Point(0, 100));
        logoCp.addChild(logo2);
        logoRect = new CoreRectangle(
                Assets.logo,
                logoCp,
                logo2
        );

        CorePoint playButtonCp = new CorePoint(new Point(400, 300));
        CorePoint pb2 = new CorePoint(new Point(0, 25));
        playButtonCp.addChild(pb2);
        playButtonRect = new CoreRectangle(
                Assets.multiplayerButton,
                playButtonCp,
                pb2
        );
        playButtonRect.setZIndex(1);

        CorePoint scoresButtonCp = new CorePoint(new Point(400, 400));
        CorePoint sb2 = new CorePoint(new Point(0, 25));
        scoresButtonCp.addChild(sb2);
        scoresButtonRect = new CoreRectangle(
                Assets.scoresButton,
                scoresButtonCp,
                sb2
        );
        scoresButtonRect.setZIndex(1);

        CorePoint quitButtonCp = new CorePoint(new Point(400, 500));
        CorePoint qb2 = new CorePoint(new Point(0, 25));
        quitButtonCp.addChild(qb2);
        quitButtonRect = new CoreRectangle(
                Assets.quitButton,
                quitButtonCp,
                qb2
        );
        quitButtonRect.setZIndex(1);

        selectorCp = new CorePoint(new Point(400, 300));
        CorePoint s2 = new CorePoint(new Point(0, 75));
        selectorCp.addChild(s2);
        selectorRect = new CoreRectangle(
                Assets.menuSelector,
                selectorCp,
                s2
        );
        selectorRect.setZIndex(2);
        selectorCp.registerForAnimations();
    }

    @Override
    public void update(long elapsedMs) {/**/}

    /**
     * Starts listening to input from the keyboard and begins rendering the menu.
     */
    @Override
    public void begin() {
        KeyManager.shared().register(this);
        GameRenderer.shared().addToQueue(logoRect);
        GameRenderer.shared().addToQueue(playButtonRect);
        GameRenderer.shared().addToQueue(scoresButtonRect);
        GameRenderer.shared().addToQueue(quitButtonRect);
        GameRenderer.shared().addToQueue(selectorRect);
    }

    /**
     * Unregisters from the KeyManager and stops rendering the menu.
     */
    @Override
    public void cleanup() {
        GameRenderer.shared().removeFromQueue(logoRect);
        GameRenderer.shared().removeFromQueue(playButtonRect);
        GameRenderer.shared().removeFromQueue(scoresButtonRect);
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
                    AnimationManager.shared().clearQueue(selectorCp.getAnimationIdentifier());
                    AnimationManager.shared().addAnimation(
                            selectorCp.getAnimationIdentifier(),
                            new PointAnimationHandler(new SubAnimation<>(
                                    selectorCp,
                                    new Point(0, 100*selectorIndex),
                                    250,
                                    null
                            ))
                    );
                }
            }
            case DOWNL, DOWNR -> {
                if ( selectorIndex < 2 ) {
                    selectorIndex++;
                    AnimationManager.shared().clearQueue(selectorCp.getAnimationIdentifier());
                    AnimationManager.shared().addAnimation(
                            selectorCp.getAnimationIdentifier(),
                            new PointAnimationHandler(new SubAnimation<>(
                                    selectorCp,
                                    new Point(0, 100*selectorIndex),
                                    250,
                                    null
                            ))
                    );
                }
            }
            case ENTER -> {
                switch (selectorIndex) {
                    case 0 -> parentContext.transition(new StageSelectionStage(parentContext));
                    case 1 -> parentContext.transition(new ScoresState(parentContext));
                    case 2 -> System.exit(0);
                }
            }
        }
    }
}
