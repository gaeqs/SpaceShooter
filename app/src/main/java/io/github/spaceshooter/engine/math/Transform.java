package io.github.spaceshooter.engine.math;

import io.github.spaceshooter.util.Validate;

public class Transform {

    private Vector2f position, scale;
    private float rotation;

    public Transform() {
        this.position = Vector2f.ZERO;
        this.scale = Vector2f.ONE;
        this.rotation = 0;
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        Validate.notNull(position, "Position cannot be null!");
        this.position = position;
    }

    public Vector2f getScale() {
        return scale;
    }

    public void setScale(Vector2f scale) {
        Validate.notNull(scale, "Scale cannot be null!");
        this.scale = scale;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void move(Vector2f offset) {
        Validate.notNull(offset, "Offset cannot be null!");
        this.position = position.add(offset);
    }

    public void move(float x, float y) {
        this.position = position.add(x, y);
    }

    public void rotate(float rotation) {
        this.rotation += rotation;
    }

}
