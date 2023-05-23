package org.tudor.GameStates;

import org.tudor.Database.Database;
import org.tudor.Database.DatabaseNotOpenException;
import org.tudor.Database.MatchLog;
import org.tudor.Game;
import org.tudor.Graphics.GameRenderer;
import org.tudor.Graphics.Primitives.CoreText;
import org.tudor.Input.InputObserver;
import org.tudor.Input.InputType;
import org.tudor.Input.KeyManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Extends the GameState class to handle scoreboard logic.
 */
public class ScoresState extends GameState implements InputObserver {
    /** List of previous matches. */
    private List<CoreText> scoresList;

    /**
     * Constructor the sets up the scoreboard.
     * @param context The Game context.
     */
    public ScoresState(Game context) {
        super(context);

        try {
            List<MatchLog> matches = Database.shared().getMatches();

            scoresList = new ArrayList<>();

            scoresList.add(new CoreText("Name  | HP Left   | Duration", new Point(300, 100)));

            for ( int i = 0; i < matches.size(); i++ ) {
                MatchLog m = matches.get(i);
                scoresList.add(new CoreText(
                        String.format("%s | %d | %d seconds", m.winnerName, m.winnerHP, (int)m.matchTime/1000),
                        new Point(300, (i+1)*20+100)
                ));
            }
            scoresList.add(new CoreText("Press ESC to go back!", new Point(300, (matches.size()+2)*20 + 100)));
        } catch (DatabaseNotOpenException e ) {
            System.out.println("Database not open?");
            e.printStackTrace();
        }
    }

    @Override
    public void update(long elapsedMs) {/*stub*/}

    /**
     * Begins rendering the scoreboard lines.
     */
    @Override
    public void begin() {
        for (CoreText t: scoresList ) {
            GameRenderer.shared().addToQueue(t);
        }
        KeyManager.shared().register(this);
    }

    /**
     * Stops rendering the scoreboard lines.
     */
    @Override
    public void cleanup() {
        for (CoreText t: scoresList ) {
            GameRenderer.shared().removeFromQueue(t);
        }
        KeyManager.shared().unregister(this);
    }

    @Override
    public void releasedInput(InputType i) {
        if ( i == InputType.ESCAPE ) {
            parentContext.transitionToPreviousState();
        }
    }

    @Override
    public void newInput(InputType i) {/*stub*/}
}
