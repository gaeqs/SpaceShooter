package io.github.spaceshooter.engine.component;

import java.util.Random;

import io.github.spaceshooter.engine.GameEngine;
import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.Scene;
import io.github.spaceshooter.engine.sound.SoundManager;

public interface Component {

    int getId();

    GameObject getGameObject();

    boolean isDestroyed();

    void markAsDestroyed();

    default void onDestroy() {
    }

    // region utils

    default Scene getScene() {
        return getGameObject().getScene();
    }

    default GameEngine getEngine() {
        return getGameObject().getScene().getEngine();
    }

    default SoundManager getSoundManager() {
        return getGameObject().getScene().getEngine().getSoundManager();
    }

    default void playSound(String sound) {
        getGameObject().getScene().getEngine().getSoundManager().play(sound);
    }

    default void runLater(float seconds, Runnable runnable) {
        getGameObject().getScene().runAfter(this, seconds, runnable);
    }

    default Random getRandom() {
        return getGameObject().getScene().getRandom();
    }

    // endregion

}
