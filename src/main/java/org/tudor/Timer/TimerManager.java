package org.tudor.Timer;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Manages all the timers in the game in an organized fashion.
 */
public class TimerManager {
    /** Singleton instance of the manager. */
    private static TimerManager singleton = null;

    /** List of actively running timers. Any finished/stopped timers are removed from the list. */
    private ArrayList<SyncTimer<?>> timers = new ArrayList<>();

    /** Private contructor for the Singleton pattern. */
    private TimerManager() {}

    /**
     * Getter for the shared instance.
     * @return The shared instance.
     */
    public static synchronized TimerManager shared() {
        if ( singleton == null )
            singleton = new TimerManager();

        return singleton;
    }

    /**
     * Adds a timer to the list to update every frame.
     * @param t The timer to add.
     */
    public void add(SyncTimer<?> t) {
        timers.add(t);
    }

    /**
     * Updates the manager, which in turns updates all the tracked timers.
     * @param elapsedTime The elapsed time from the previous update.
     */
    public void update(long elapsedTime) {
        Iterator<SyncTimer<?>> it = timers.iterator();
        while ( it.hasNext() ) {
            SyncTimer<?> t = it.next();
            t.update(elapsedTime);

            if ( !t.running )
                it.remove();
        }
    }
}
