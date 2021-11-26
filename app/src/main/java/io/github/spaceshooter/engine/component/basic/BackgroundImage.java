package io.github.spaceshooter.engine.component.basic;

import io.github.spaceshooter.engine.GameObject;

public class BackgroundImage extends Image {

    public BackgroundImage(GameObject gameObject) {
        super(gameObject);
    }


    @Override
    public int getDrawPriority() {
        return Integer.MIN_VALUE;
    }
}
