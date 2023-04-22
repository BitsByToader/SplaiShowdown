package org.tudor.Timer;

import java.util.function.Consumer;

//TODO: Maybe createa a Timeable interface so we can encapsulate the duration and repatedness of
// the timer within the target member?

//TODO: Add a finished member to the timer. Remove the timer from the manager based on that member
// and not the running member so we can keep track of paused timers as well. Or when adding/starting a
// timer also start/add it.

/**
 * Basic Timer class that is different from the Java Timer class, since it does everything
 * synchronously.
 * The updates will be called every frame and the timer will finish with the first change it gets.
 * This avoids concurrency issues, at the cost of less flexibility.
 * The SyncTimer class heavily relies on the Producer-Consumer design pattern, requiring that the timeable
 * object produces the data, and the timer consumes it every frame until it expires.
 * @param <T> The object type that is being consumed every frame.
 */
public class SyncTimer<T> {
    /** Indicates if the timer is running. */
    public boolean running = false;
    /** The duration of the timer. */
    private long duration;
    /** How much is left from the timer. */
    private long remaining;
    /** Indicates if the timer is repeating. */
    private boolean repeating;

    /** Callback that gets called when the timer finishes. */
    private Consumer<T> finishCallback;
    /** Callback that gets called every frame. */
    private Consumer<T> updateCallback;
    /** The target object that is being consumed in the callbacks. */
    private T targetObject;

    /**
     * Initializes the timer with a duration, whether it repeats, update and finish callbacks and
     * the target object on which the callbacks make changes to.
     * @param durationMs The duration of the timer.
     * @param repeating If the timer repeats after it fired the first time.
     * @param target The target object on which the callbacks will act on.
     * @param update Update callback called every frame (or update call). Can be null if not needed.
     * @param finish Finish callback called when the timer fires. Can be null if not needed.
     */
    public SyncTimer(long durationMs, boolean repeating, T target, Consumer<T> update, Consumer<T> finish) {
        this.duration = durationMs;
        this.repeating = repeating;
        this.targetObject = target;
        this.finishCallback = finish;
        this.updateCallback = update;
    }

    /**
     * Starts the timer with the duration and callbacks set in the constructor.
     */
    public void start() {
        remaining = duration;
        running = true;
    }

    /**
     * Stops the timer, but doesn't reset the remaining time or any target objects.
     */
    public void stop() {
        running = false;
    }

    /**
     * Resumes the timer after it has been stopped.
     */
    public void resume() { running = true; }

    /**
     * Returns the target that the timer is consuming every frame.
     * @return Target object of timer.
     */
    public T target() { return targetObject; }

    /**
     * Updates the timer with how much time passed from the previous frame.
     * Will call the update callback until the duration elapsed and finish callback last.
     * @param elapsedTimeMs Time elapsed from the previous frame to the current frame.
     */
    public void update(long elapsedTimeMs) {
        if ( running ) {
            remaining -= elapsedTimeMs;

            if ( updateCallback != null )
                updateCallback.accept(targetObject);

            if ( remaining <= 0 ) { // Timer finished
                if ( repeating ) {
                    this.start();
                } else {
                    running = false;
                }

                if ( finishCallback != null )
                    finishCallback.accept(targetObject);
            }
        }
    }
}
