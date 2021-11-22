package io.github.spaceshooter.engine.scheduler;

import io.github.spaceshooter.engine.component.Component;

class RunAfter {

    private final Component owner;
    private final float timestamp;
    private final Runnable runnable;

    public RunAfter(Component owner, float timestamp, Runnable runnable) {
        this.owner = owner;
        this.timestamp = timestamp;
        this.runnable = runnable;
    }

    public Component getOwner() {
        return owner;
    }

    public float getTimestamp() {
        return timestamp;
    }

    public Runnable getRunnable() {
        return runnable;
    }
}
