package org.tudor.GameStates;

import org.tudor.Game;
import org.tudor.Graphics.Assets.Assets;
import org.tudor.Graphics.GameRenderer;
import org.tudor.Graphics.Primitives.CorePoint;
import org.tudor.Graphics.Primitives.CoreRectangle;
import org.tudor.Graphics.Primitives.CoreText;
import org.tudor.Input.InputObserver;
import org.tudor.Input.InputType;
import org.tudor.Input.KeyManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Extends the base GameState class to add stage selection logic.
 */
public class StageSelectionStage extends GameState implements InputObserver {
    /** The CoreRectangle which will render the background. */
    private final CoreRectangle backgroundRect;
    /** The first UI text. */
    private final CoreText text1;
    /** The second UI text. */
    private final CoreText text2;

    /** The index of the selected stage. */
    private int selectedStage = 0;
    /** The array of possible stages. */
    private final ArrayList<BufferedImage> stages = new ArrayList<>();

    /**
     * Constructor that sets up the UI.
     *
     * @param context The context of the Game.
     */
    protected StageSelectionStage(Game context) {
        super(context);

        // Set up stages array
        stages.add(0, Assets.stage1Background);
        stages.add(1, Assets.stage2Background);
        stages.add(2, Assets.stage3Background);

        // Set up background
        CorePoint backgroundCp = new CorePoint(new Point(400,0));
        CorePoint secondCp = new CorePoint(new Point(0, 10));
        backgroundCp.addChild(secondCp);
        backgroundRect = new CoreRectangle(Assets.partBig, backgroundCp, secondCp);
        backgroundRect.setZIndex(1);

        // Set up UI text
        text1 = new CoreText("STAGE SELECTION", new Point(330, 100));
        text1.setZIndex(2);
        text2 = new CoreText("Use the ARROWS to select a stage and ENTER to proceed.", new Point(220, 120));
        text2.setZIndex(2);
    }


    @Override
    public void update(long elapsedMs) {
        backgroundRect.setTexture(stages.get(selectedStage));
    }

    @Override
    public void begin() {
        GameRenderer.shared().addToQueue(backgroundRect);
        GameRenderer.shared().addToQueue(text1);
        GameRenderer.shared().addToQueue(text2);
        KeyManager.shared().register(this);
    }

    @Override
    public void cleanup() {
        GameRenderer.shared().removeFromQueue(backgroundRect);
        GameRenderer.shared().removeFromQueue(text1);
        GameRenderer.shared().removeFromQueue(text2);
        KeyManager.shared().unregister(this);
    }

    @Override
    public void releasedInput(InputType i) {/*stub*/}

    @Override
    public void newInput(InputType i) {
        switch (i) {
            case LEFTL, LEFTR -> {
                if ( selectedStage > 0 )
                    selectedStage--;
            }
            case RIGHTL, RIGHTR -> {
                if ( selectedStage < 2 )
                    selectedStage++;
            }
            case ENTER -> parentContext.transition(new PlayState(stages.get(selectedStage), parentContext));
            case ESCAPE -> parentContext.transitionToPreviousState();
        }
    }
}
