package io.github.spaceshooter.space.player;

import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.camera.Camera;
import io.github.spaceshooter.engine.component.BasicComponent;
import io.github.spaceshooter.engine.component.TickableComponent;
import io.github.spaceshooter.engine.component.basic.Joystick;
import io.github.spaceshooter.engine.math.Vector2f;
import io.github.spaceshooter.space.background.PlayArea;

public class PlayerMovementBehaviour extends BasicComponent implements TickableComponent {

    private Joystick movementJoystick;
    private Joystick shootJoystick;
    private PlayerData player;

    public PlayerMovementBehaviour(GameObject gameObject) {
        super(gameObject);
    }

    public Joystick getMovementJoystick() {
        return movementJoystick;
    }

    public void setMovementJoystick(Joystick movementJoystick) {
        this.movementJoystick = movementJoystick;
    }

    public Joystick getShootJoystick() {
        return shootJoystick;
    }

    public void setShootJoystick(Joystick shootJoystick) {
        this.shootJoystick = shootJoystick;
    }

    public PlayerData getPlayer() {
        return player;
    }

    public void setPlayer(PlayerData player) {
        this.player = player;
    }

    @Override
    public void tick(float deltaSeconds) {
        rotate();
        if (movementJoystick == null) return;

        Vector2f movement = movementJoystick.getFactor()
                .mul(player.getShipType().getSpeed()).mul(deltaSeconds);

        Vector2f start = gameObject.getTransform().getPosition();
        Vector2f toX = start.add(movement.x(), 0);
        Vector2f toY = start.add(0, movement.y());

        Camera camera = gameObject.getScene().getCamera();
        if (PlayArea.PLAY_AREA.contains(toX.x(), toX.y())) {
            start = toX;
        }
        if (PlayArea.PLAY_AREA.contains(toY.x(), toY.y())) {
            start = start.add(0, movement.y());
        }
        gameObject.getTransform().setPosition(start);
        camera.setPosition(start);
    }

    private void rotate() {
        if (shootJoystick == null) return;
        gameObject.getTransform().lookAt(shootJoystick.getFactor());
    }
}
