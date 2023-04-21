package org.tudor.Graphics.Animations;

import org.tudor.Timer.SyncTimer;
import org.tudor.Timer.TimerManager;

import java.util.*;
import java.util.function.Consumer;

public class AnimationManager {
    private static AnimationManager singleton = null;

    private HashMap<UUID, Queue<AnimationHandler<?>>> animatables = new HashMap<>();
    private HashMap<UUID, SyncTimer<AnimationHandler<?>>> timers = new HashMap<>();

    private Consumer<AnimationHandler<?>> animationUpdate = new Consumer<AnimationHandler<?>>() {
        @Override
        public void accept(AnimationHandler<?> animationHandler) {
            System.out.println("ANIMATION UPDATE:");
            animationHandler.calculateNewState();
        }
    };

    private Consumer<AnimationHandler<?>> animationFinish = new Consumer<AnimationHandler<?>>() {
        @Override
        public void accept(AnimationHandler<?> animationHandler) {
            System.out.println("ANIMATION FINISH: " + animationHandler);
        }
    };

    private AnimationManager() {}

    public static synchronized AnimationManager shared() {
        if ( singleton == null ) {
            singleton = new AnimationManager();
        }

        return singleton;
    }

    public UUID registerForAnimations() {
        UUID uuid = UUID.randomUUID();

        // Create the queue where we'll put the animations we'll have to perform
        animatables.put(uuid, new LinkedList<>());
        // Prepare for the timer that will eventually execute the current animation.
        timers.put(uuid, null);

        return uuid;
    }

    public void deRegister(UUID u) {
        animatables.remove(u);
        timers.remove(u);
    }

    public void addAnimation(UUID uuid, AnimationHandler<?> animation) {
        animatables.get(uuid).offer(animation);
    }

    public void clearQueue(UUID u) {
        animatables.put(u, new LinkedList<>());
    }

    public void update(long elapsedTimeMs) {
        animatables.forEach((uuid, queue) -> {
            // Get the possible timer for this animatable
            SyncTimer<AnimationHandler<?>> timer = timers.get(uuid);

            if ( timer != null && timer.running) {
                // Timer exists and is runnnig
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
