package io.github.spaceshooter.engine.input;

import io.github.spaceshooter.engine.GameEngine;
import io.github.spaceshooter.engine.Scene;
import io.github.spaceshooter.engine.camera.Camera;
import io.github.spaceshooter.engine.math.Vector2f;

public class InputDownEvent extends InputEvent {

    private final GameEngine engine;
    private final int pointer;
    private final Vector2f screenPosition;
    private Vector2f worldPosition;

    public InputDownEvent(GameEngine engine, int pointer, Vector2f screenPosition) {
        this.engine = engine;
        this.pointer = pointer;
        this.screenPosition = screenPosition;
    }

    public GameEngine getEngine() {
        return engine;
    }

    public int getPointer() {
        return pointer;
    }

    public Vector2f getScreenPosition() {
        return screenPosition;
    }

    public Vector2f getWorldPosition() {
        if (worldPosition == null) {
            Scene scene = engine.getScene();
            if (scene == null) return null;
            Camera camera = scene.getCamera();
            worldPosition = camera.screenToWorld(screenPosition);
        }
        return worldPosition;
    }
}
