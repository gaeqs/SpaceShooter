package io.github.spaceshooter.space.player;

import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.camera.Camera;
import io.github.spaceshooter.engine.component.BasicComponent;
import io.github.spaceshooter.engine.component.TickableComponent;
import io.github.spaceshooter.engine.component.basic.Joystick;
import io.github.spaceshooter.engine.math.Vector2f;

public class PlayerMovementBehaviour extends BasicComponent implements TickableComponent {

    private final Joystick joystick;
    private final Vector2f velocity = Vector2f.ONE;

    public PlayerMovementBehaviour(GameObject gameObject) {
        super(gameObject);
        joystick = gameObject.getComponent(Joystick.class);
    }

    @Override
    public void tick(float deltaSeconds) {
        Vector2f movement = joystick.getFactor().mul(velocity).mul(deltaSeconds);

        Vector2f start = gameObject.getTransform().getPosition();
        Vector2f toX = start.add(movement.x(), 0);
        Vector2f toY = start.add(0, movement.y());

        Camera camera = gameObject.getScene().getCamera();
        if (camera.isInside(toX)) {
            start = toX;
        }
        if(camera.isInside(toY)) {
            start = start.add(0, movement.y());
        }
        gameObject.getTransform().setPosition(start);

    }
}
