package io.github.spaceshooter.engine.component;

import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.Scene;

public interface Component {

    int getId();

    GameObject getGameObject();

    default Scene getScene() {
        return getGameObject().getScene();
    }

    default void onDestroy() {
    }

}
