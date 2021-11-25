package io.github.spaceshooter.engine.scheduler;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeSet;

import io.github.spaceshooter.engine.component.Component;

public class Scheduler {

    private final Queue<RunAfter> tickAddition = new LinkedList<>();
    private final TreeSet<RunAfter> runs = new TreeSet<>
            ((o1, o2) -> Float.compare(o1.getTimestamp(), o2.getTimestamp()));

    private float currentTime = 0;

    public void runAfter(Component owner, float seconds, Runnable runnable) {
        tickAddition.add(new RunAfter(owner, seconds + currentTime, runnable));
    }

    public void flush() {
        runs.addAll(tickAddition);
        tickAddition.clear();
    }

    public void tick(float deltaSeconds) {
        currentTime += deltaSeconds;

        Iterator<RunAfter> iterator = runs.iterator();
        while (iterator.hasNext()) {
            RunAfter current = iterator.next();
            if (current.getTimestamp() > currentTime) return;
            iterator.remove();

            if (!current.getOwner().isDestroyed()) {
                try {
                    current.getOwner().getGameObject().getLock().lock();
                    current.getRunnable().run();
                    current.getOwner().getGameObject().getLock().unlock();
                } catch (Exception ex) {
                    System.err.println("Error executing RunAfter task.");
                    ex.printStackTrace();
                }

            }
        }
    }

}
