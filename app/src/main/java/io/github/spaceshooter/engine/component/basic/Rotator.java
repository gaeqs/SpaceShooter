package io.github.spaceshooter.engine.component.basic;

import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.component.BasicComponent;
import io.github.spaceshooter.engine.component.TickableComponent;
import io.github.spaceshooter.engine.math.Vector2f;
import io.github.spaceshooter.util.Validate;

public class Rotator extends BasicComponent implements TickableComponent {

    private float radius = 0.2f;
    private float angle = 0;
    private float velocity = 1;
    private Vector2f center = Vector2f.ZERO;

    public Rotator(GameObject gameObject) {
        super(gameObject);
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getVelocity() {
        return velocity;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    public Vector2f getCenter() {
        return center;
    }

    public void setCenter(Vector2f center) {
        Validate.notNull(center, "Center cannot be null!");
        this.center = center;
    }

    @Override
    public void tick(float deltaSeconds) {
        angle += deltaSeconds * velocity;
        angle %= Math.PI * 2;
        Vector2f offset = new Vector2f((float) Math.sin(angle), (float) Math.cos(angle)).mul(radius);
        gameObject.getTransform().setPosition(offset.add(center));
    }
}
