package io.github.spaceshooter.space.player;

import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.component.BasicComponent;
import io.github.spaceshooter.engine.component.TickableComponent;
import io.github.spaceshooter.engine.component.basic.Joystick;
import io.github.spaceshooter.engine.math.Vector2f;
import io.github.spaceshooter.space.general.Bullet;
import io.github.spaceshooter.space.general.Team;
import io.github.spaceshooter.space.util.MovingObject;

public class PlayerShootBehaviour extends BasicComponent implements TickableComponent {

    private Joystick joystick;
    private float time = 0.0f;

    private float lastShoot = 0.0f;

    private PlayerData player;

    public PlayerShootBehaviour(GameObject gameObject) {
        super(gameObject);
    }


    @Override
    public void tick(float deltaSeconds) {
        time += deltaSeconds;
        float charge = time - lastShoot;
        if (joystick.isExecuting()) {
            if (charge >= player.getShipType().getSecondsPerShoot()) {
                shoot();
            }
        }
    }

    public Joystick getJoystick() {
        return joystick;
    }

    public void setJoystick(Joystick joystick) {
        this.joystick = joystick;
    }

    public PlayerData getPlayer() {
        return player;
    }

    public void setPlayer(PlayerData player) {
        this.player = player;
    }

    private void shoot() {
        Vector2f factor = joystick.getFactor();
        float magnitude = factor.magnitudeSquared();
        if (magnitude < 0.1) return;
        factor = factor.div((float) Math.sqrt(magnitude));

        GameObject object = getScene().newGameObject("Bullet");
        MovingObject moving = object.addComponent(MovingObject.class);
        moving.setDirection(factor);
        Bullet bullet = object.addComponent(Bullet.class);
        bullet.setOrigin(gameObject);
        bullet.setTeam(player == null ? Team.TEAM_1 : player.getTeam());
        bullet.setInfringedDamage(player.getShipType().getBulletsDamage());

        object.getTransform().setPosition(gameObject.getTransform().getPosition());
        object.getTransform().lookAt(factor);

        float charge = time - lastShoot;
        if (charge < player.getShipType().getSecondsPerShoot() * 2) {
            moving.setVelocity(1);

            bullet.setOrigin(gameObject);
            bullet.getCollider().setRadius(0.05f);
        } else {
            charge = Math.min(charge - player.getShipType().getSecondsPerShoot() * 2, 5) + 1;
            moving.setVelocity(1 / charge);

            bullet.getCollider().setRadius(0.05f * charge);
            bullet.setSpriteScale(bullet.getSpriteScale().mul(charge));
            bullet.setInfringedDamage((int) (5 * charge * charge));
            bullet.setMaximumDamageableElements((int) (charge * charge));
        }

        playSound("shoot");
        lastShoot = time;
    }
}
