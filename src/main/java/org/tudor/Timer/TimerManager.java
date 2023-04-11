package org.tudor.Timer;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;

// TODO: Write the doc for TimerManager after further testing.

public class TimerManager {
    private static TimerManager singleton = null;

    private ArrayList<SyncTimer<?>> timers = new ArrayList<>();

    private TimerManager() {}

    public static synchronized TimerManager shared() {
        if ( singleton == null )
            singleton = new TimerManager();

        return singleton;
    }

    public void add(SyncTimer<?> t) {
        timers.add(t);
    }

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
