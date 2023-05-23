package org.tudor.Graphics.Animations;

import org.tudor.Timer.SyncTimer;
import org.tudor.Timer.TimerManager;

import java.util.*;
import java.util.function.Consumer;

/**
 * Manages all the animations in the game, in a <i>Singleton</i> fashion. The <i>AnimationManager</i>
 * is capable of handling animations for multiple entities in "parallel". This is done via a registering
 * system, in which every entity that wants to animate a state will register to the manager. The manager
 * will return a UUID for registrant to use when wanting to perform an animation. Each registrant has
 * a <i>AnimationHandler</i> queue and a timer available to them. As such, animations can be queued up
 * and displayed sequentially using the <i>Consumer-Producer design pattern</i> of the <i>SyncTimer</i>
 * class.
 * The basic flow for using the AnimationManager class is as follows: register and get a UUID; using that
 * UUID, you can begin putting animations in a queue that will be executed every frame; the manager will
 * poll the queue and give it to the timer to perform update and finish operations on the animation.
 */
public class AnimationManager {
    /** The singleton instance of the manager. */
    private static AnimationManager singleton = null;

    /** Maps every registrant's animation queue to their UUID. */
    private final HashMap<UUID, Deque<AnimationHandler<?>>> animatables = new HashMap<>();
    /** Maps every registrant's timer to their UUID. */
    private final HashMap<UUID, SyncTimer<AnimationHandler<?>>> timers = new HashMap<>();

    /** A general update consumer for the animation. */
    private final Consumer<AnimationHandler<?>> animationUpdate = AnimationHandler::calculateNewState;

    /** A general finish consumer for the animation. */
    private final Consumer<AnimationHandler<?>> animationFinish = animationHandler -> {
        animationHandler.animation().entity.animationHasStopped();
        animationHandler.reset();

        AnimationHandler<?> next = animationHandler.getNextHandler();
        if (next != null) {
            AnimationManager.shared().forceAnimationInFront(
                    animationHandler.animation().entity.getAnimationIdentifier(),
                    next
            );
        }
    };

    /**
     * Stub private constructor for the singleton design pattern.
     */
    private AnimationManager() {}

    /**
     * Getter for the singleton instance.
     * @return The singleton for the AnimationManager.
     */
    public static synchronized AnimationManager shared() {
        if ( singleton == null ) {
            singleton = new AnimationManager();
        }

        return singleton;
    }

    /**
     * Registers a new entity for animation.
     * @return The UUID used to acces the animation queue for the new entity.
     */
    public UUID registerForAnimations() {
        UUID uuid = UUID.randomUUID();

        // Create the queue where we'll put the animations we'll have to perform
        animatables.put(uuid, new LinkedList<>());
        // Prepare for the timer that will eventually execute the current animation.
        timers.put(uuid, null);

        return uuid;
    }

    /**
     * Unregisters the UUID.
     * @param u The UUID to unregister.
     */
    public void unregister(UUID u) {
        animatables.remove(u);
        timers.remove(u);
    }

    /**
     * Adds an animation to the queue.
     * @param uuid The UUID of the queue that's being added to.
     * @param animation The animation that is added.
     */
    public void addAnimation(UUID uuid, AnimationHandler<?> animation) {
        animatables.get(uuid).offer(animation);
    }

    /**
     * Forces an animation in front of the queue. This is useful for chained sub-animations because we need
     * a following sub-animation to pe performed next, not after other queued animations.
     * @param uuid The UUID of the queue that's being added to.
     * @param animation The animation that is added.
     */
    public void forceAnimationInFront(UUID uuid, AnimationHandler<?> animation) {
        animatables.get(uuid).offerFirst(animation);
    }

    /**
     * Clears the queue.
     * @param u The UUID of the queue that's being cleared.
     */
    public void clearQueue(UUID u) {
        if ( timers.get(u) != null ) {
            // Stop any timers that are associated with this queue
            timers.get(u).stop();
            timers.remove(u);
        }
        animatables.put(u, new LinkedList<>());
    }

    /**
     * Updates the timers for all the registrants.
     * @param elapsedTimeMs The time that passed between the current and previous update.
     */
    public void update(long elapsedTimeMs) {
        animatables.forEach((uuid, queue) -> {
            // Get the possible timer for this animatable
            SyncTimer<AnimationHandler<?>> timer = timers.get(uuid);

            if ( timer != null && timer.running) {
                // Timer exists and is running
                timer.target().timePassed(elapsedTimeMs);

                // Skip because we need to wait for the current animation to finish
                return;
            }

            // Timer doesn't exist, or it isn't running, so we finished the previous animation
            // Get a new handler for the next animation
            AnimationHandler<?> handler = queue.poll();

            if ( handler == null) {
                // No more animations to perform, skip
                // Clear the previous timer
                timers.put(uuid, null);
                return;
            }

            // Tell the next handler to do all calculations necessary for an animation
            // start because we're planning on doing it now.
            handler.startAnimation();

            // Set up the timer that will perform the animation every frame
            SyncTimer<AnimationHandler<?>> t = new SyncTimer<>(handler.duration(),
                    false,
                    handler,
                    animationUpdate,
                    animationFinish
            );
            t.start();

            // The animation will begin next frame
            TimerManager.shared().add(t);

            // Save the timer for bookkeeping
            timers.put(uuid, t);
        });
    }
}
