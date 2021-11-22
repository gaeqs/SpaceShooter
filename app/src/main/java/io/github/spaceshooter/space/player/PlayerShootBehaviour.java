package io.github.spaceshooter.space.player;

import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.component.BasicComponent;
import io.github.spaceshooter.engine.component.TickableComponent;
import io.github.spaceshooter.engine.component.basic.Joystick;
import io.github.spaceshooter.engine.math.Vector2f;
import io.github.spaceshooter.space.general.Bullet;
import io.github.spaceshooter.space.util.MovingObject;

public class PlayerShootBehaviour extends BasicComponent implements TickableComponent {

    private Joystick joystick;
    private float secondsPerShoot = 0.1f;
    private float time = 0.0f;

    public PlayerShootBehaviour(GameObject gameObject) {
        super(gameObject);
    }


    @Override
    public void tick(float deltaSeconds) {
        if (joystick.isExecuting()) {
            time += deltaSeconds;
            while (time >= secondsPerShoot) {
                shoot();
                time -= secondsPerShoot;
            }
        } else {
            time = Math.min(time + deltaSeconds, secondsPerShoot);
        }
    }

    public Joystick getJoystick() {
        return joystick;
    }

    public void setJoystick(Joystick joystick) {
        this.joystick = joystick;
    }

    private void shoot() {

        Vector2f factor = joystick.getFactor();
        float magnitude = factor.magnitudeSquared();
        if(magnitude < 0.1) return;
        factor = factor.div((float) Math.sqrt(magnitude));


        GameObject object = getScene().newGameObject("Bullet");

        MovingObject moving = object.addComponent(MovingObject.class);
        moving.setDirection(factor);
        moving.setVelocity(1);

        Bullet bullet = object.addComponent(Bullet.class);
        bullet.setOrigin(gameObject);
        bullet.getCollider().setRadius(0.05f);
        object.getTransform().setPosition(gameObject.getTransform().getPosition());
        playSound("shoot");
    }
}
