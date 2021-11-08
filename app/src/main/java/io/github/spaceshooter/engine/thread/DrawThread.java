package io.github.spaceshooter.engine.thread;

import io.github.spaceshooter.engine.GameView;
import io.github.spaceshooter.util.Validate;

public class DrawThread extends Thread {

    private final GameView gameView;
    private final long nanosPerFrame;

    public DrawThread(GameView gameView, int fps) {
        Validate.notNull(gameView, "Game view cannot be null!");
        this.gameView = gameView;
        nanosPerFrame = 1_000_000_000 / fps;
    }

    @Override
    public void run() {
        super.run();
        long now = System.nanoTime();
        long nextFrame;
        while (!isInterrupted()) {
            nextFrame = now + nanosPerFrame;

            gameView.draw();

            // Active wait is more accurate!
            //noinspection StatementWithEmptyBody
            while (nextFrame >= System.nanoTime()) {
            }

            now = nextFrame;
        }
    }
}
