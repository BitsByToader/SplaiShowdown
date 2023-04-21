package org.tudor.Timer;

import java.util.function.Consumer;

//TODO: Maybe createa a Timeable interface so we can encapsulate the duration and repatedness of
// the timer within the target member?

/**
 * Basic Timer class that is different from the Java Timer class, since it does everything
 * synchronously.
 * The updates will be called every frame and the timer will finish with the first change it gets.
 * This avoids concurrency issues, at the cost of less flexibility.
 */
public class SyncTimer<T> {
    public boolean running = false;
    private long duration;
    private long remaining;
    private boolean repeating;

    Consumer<T> finishCallback;
    Consumer<T> updateCallback;
    T targetObject;

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
