package io.github.spaceshooter.space.util;

import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.component.BasicComponent;
import io.github.spaceshooter.engine.component.TickableComponent;
import io.github.spaceshooter.engine.math.Vector2f;
import io.github.spaceshooter.space.background.PlayArea;
import io.github.spaceshooter.util.Validate;

public class MovingObject extends BasicComponent implements TickableComponent {

    private Vector2f direction = new Vector2f(1, 0);
    private float velocity = 0.5f;

    public MovingObject(GameObject gameObject) {
        super(gameObject);
    }

    public Vector2f getDirection() {
        return direction;
    }

    public void setDirection(Vector2f direction) {
        Validate.notNull(direction, "Direction cannot be null!");
        this.direction = direction;
    }

    public float getVelocity() {
        return velocity;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    @Override
    public void tick(float deltaSeconds) {
        gameObject.getTransform().move(direction.mul(velocity * deltaSeconds));
        Vector2f pos = gameObject.getTransform().getPosition();
        if (!PlayArea.DESPAWN_AREA.contains(pos.x(), pos.y())) {
            gameObject.getScene().destroyGameObject(gameObject);
        }
    }
}
