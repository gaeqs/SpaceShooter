package io.github.spaceshooter.engine.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import io.github.spaceshooter.engine.GameEngine;
import io.github.spaceshooter.util.Validate;

public class UpdateThread extends Thread {

    private final ReentrantLock pauseLock = new ReentrantLock();
    private final Condition pauseCondition = pauseLock.newCondition();

    private final GameEngine engine;

    public UpdateThread(GameEngine engine) {
        Validate.notNull(engine, "Engine cannot be null!");
        this.engine = engine;
    }


    @Override
    public void run() {
        super.run();
        long lastTick = System.nanoTime();
        long now;
        long nanoDelay;
        float deltaSeconds;
        while (!isInterrupted()) {

            pauseLock.lock();
            if (!engine.isRunning()) {
                if (!waitForResume()) return;
                lastTick = System.nanoTime();
            }
            pauseLock.unlock();

            now = System.nanoTime();
            nanoDelay = now - lastTick;
            deltaSeconds = nanoDelay / 1000_000_000.0f;
            engine.tick(deltaSeconds);
            lastTick = now;
        }
    }

    public void notifyResume() {
        pauseLock.lock();
        pauseCondition.signalAll();
        pauseLock.unlock();
    }

    private boolean waitForResume() {
        try {
            pauseCondition.await();
        } catch (InterruptedException e) {
            return false;
        }
        return true;
    }
}
