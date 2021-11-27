package io.github.spaceshooter.engine.component;

import java.util.Random;

import io.github.spaceshooter.engine.GameEngine;
import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.Scene;

/**
 * Represents a component inside a {@link GameObject}.
 * <p>
 * Components implement the logic of the game.
 * <p>
 * To be created, components should have a construction
 * asking only for a {@link GameObject} as a parameter!
 */
public interface Component {

    /**
     * The global identifier of the component.
     *
     * @return the global identifier.
     */
    int getId();

    /**
     * The {@link GameObject} this component is inside of.
     *
     * @return the {@link GameObject}.
     */
    GameObject getGameObject();

    /**
     * Returns whether this component has been destroyed and, therefore, is not valid.
     *
     * @return whether this component has been destroyed.
     */
    boolean isDestroyed();

    /**
     * Marks this component as a destroyed component.
     * You should not use this method!
     */
    void markAsDestroyed();

    /**
     * This method is invoked when this component is destroyed. Override it to implement
     * your logic.
     */
    default void onDestroy() {
    }

    // region utils

    /**
     * Returns the {@link Scene} this component is inside of.
     *
     * @return the {@link Scene}.
     */
    default Scene getScene() {
        return getGameObject().getScene();
    }

    /**
     * Returns the {@link GameEngine}.
     *
     * @return the {@link GameEngine}.
     */
    default GameEngine getEngine() {
        return getGameObject().getScene().getEngine();
    }

    /**
     * Plays a sound.
     *
     * @param sound the sound identifier.
     */
    default void playSound(String sound) {
        getGameObject().getScene().getEngine().getSoundManager().play(sound);
    }

    /**
     * Plays a sound.
     *
     * @param sound the sound identifier.
     * @param rate  the pitch.
     */
    default void playSound(String sound, float rate) {
        getGameObject().getScene().getEngine().getSoundManager().play(sound, rate);
    }

    /**
     * Plays a sound.
     *
     * @param sound  the sound identifier.
     * @param volume the volume.
     * @param rate   the pitch.
     */
    default void playSound(String sound, float volume, float rate) {
        getGameObject().getScene().getEngine().getSoundManager().play(sound, volume, rate);
    }

    /**
     * Runs the given lambda after the given seconds have passed.
     *
     * The given code won't be executed if this component is destroyed before
     * the given time.
     *
     * @param seconds  the seconds.
     * @param runnable the code to execute.
     */
    default void runLater(float seconds, Runnable runnable) {
        getGameObject().getScene().runAfter(this, seconds, runnable);
    }

    /**
     * Returns the random linked to this {@link Scene}.
     * Use this instead of a new random.
     *
     * @return the random.
     */
    default Random getRandom() {
        return getGameObject().getScene().getRandom();
    }

    // endregion

}
