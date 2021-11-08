package io.github.spaceshooter.engine;

import android.app.Activity;
import android.graphics.Canvas;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import io.github.spaceshooter.engine.input.InputEvent;
import io.github.spaceshooter.engine.sound.SoundManager;
import io.github.spaceshooter.engine.thread.DrawThread;
import io.github.spaceshooter.engine.thread.UpdateThread;
import io.github.spaceshooter.util.Validate;

public class GameEngine {

    private final Activity activity;
    private final GameView gameView;

    private final UpdateThread updateThread;
    private final DrawThread drawThread;

    private final SoundManager soundManager;

    private final Queue<InputEvent> inputQueue = new ConcurrentLinkedQueue<>();

    private volatile Scene scene;
    private boolean running, destroyed;

    public GameEngine(Activity activity, GameView gameView) {
        this.activity = activity;
        this.gameView = gameView;

        this.soundManager = new SoundManager(activity);

        this.updateThread = new UpdateThread(this);
        this.drawThread = new DrawThread(gameView, 60);
        updateThread.start();
        drawThread.start();

        this.running = false;
        this.destroyed = false;

        gameView.setEngine(this);
    }

    public Activity getActivity() {
        return activity;
    }

    public GameView getGameView() {
        return gameView;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        if (destroyed) return;
        if (this.scene != null) {
            this.scene.detach();
        }
        this.scene = scene;
        if (this.scene != null) {
            this.scene.attach();
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void resume() {
        if (destroyed) return;
        this.running = true;
        updateThread.notifyResume();
        if (scene != null) {
            scene.resume();
        }
    }

    public void pause() {
        this.running = false;
        if (scene != null) {
            scene.pause();
        }
    }

    public void destroy() {
        this.destroyed = true;
        this.running = false;
        updateThread.interrupt();
        drawThread.interrupt();
    }

    public void tick(float deltaSeconds) {
        flushInputEventToScene();
        if (scene != null) {
            scene.tick(deltaSeconds);
        }
    }

    public void draw(Canvas canvas) {
        if (scene != null) {
            scene.draw(canvas);
        }
    }

    public void queueInputEvent(InputEvent event) {
        if (!running) return;
        Validate.notNull(event, "Event cannot be null!");
        inputQueue.add(event);
    }

    void flushInputEventToScene() {
        if (scene != null) {
            while (!inputQueue.isEmpty()) {
                scene.inputEvent(inputQueue.poll());
            }
        } else {
            inputQueue.clear();
        }
    }
}
