package org.tudor.Timer;

public class Timer {
    private long duration;
    private long startTime;

    Timer(long durationMs) {
        this.duration = durationMs;
        this.startTime = System.currentTimeMillis();
    }

    public void update() {
        
    }
}
