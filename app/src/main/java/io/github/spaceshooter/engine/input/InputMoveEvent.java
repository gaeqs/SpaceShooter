package io.github.spaceshooter.engine.input;

import io.github.spaceshooter.engine.GameEngine;
import io.github.spaceshooter.engine.Scene;
import io.github.spaceshooter.engine.camera.Camera;
import io.github.spaceshooter.engine.math.Vector2f;

public class InputMoveEvent extends InputEvent {

    private final GameEngine engine;
    private final int[] pointers;
    private final Vector2f[] screenPositions;
    private final Vector2f[] worldPositions;

    public InputMoveEvent(GameEngine engine, int[] pointers, Vector2f[] screenPositions) {
        this.engine = engine;
        this.pointers = pointers;
        this.screenPositions = screenPositions;
        this.worldPositions = new Vector2f[pointers.length];
    }

    public GameEngine getEngine() {
        return engine;
    }

    public int getSize() {
        return pointers.length;
    }

    public int getPointer(int index) {
        return pointers[index];
    }

    public Vector2f getScreenPosition(int index) {
        return screenPositions[index];
    }

    public Vector2f getWorldPosition(int index) {
        if (worldPositions[index] == null) {
            Scene scene = engine.getScene();
            if (scene == null) return null;
            Camera camera = scene.getCamera();
            worldPositions[index] = camera.screenToWorld(screenPositions[index]);
        }
        return worldPositions[index];
    }
}
