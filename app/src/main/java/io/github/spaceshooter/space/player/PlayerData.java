package io.github.spaceshooter.space.player;

import io.github.spaceshooter.R;
import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.component.basic.CollisionDebugger;
import io.github.spaceshooter.engine.component.basic.Joystick;
import io.github.spaceshooter.engine.component.basic.Sprite;
import io.github.spaceshooter.engine.component.collision.SphereCollider;
import io.github.spaceshooter.space.general.HealthBar;
import io.github.spaceshooter.space.general.LivingComponent;

public class PlayerData extends LivingComponent {

    public static final int MAX_HEALTH = 100;

    public PlayerData(GameObject gameObject) {
        super(gameObject, MAX_HEALTH);

        Joystick movementJoystick = gameObject.addComponent(Joystick.class);
        Joystick shootJoystick = gameObject.addComponent(Joystick.class);
        shootJoystick.getArea().setAttachToRight(true);

        PlayerMovementBehaviour movement = gameObject.addComponent(PlayerMovementBehaviour.class);
        movement.setJoystick(movementJoystick);

        PlayerShootBehaviour shoot = gameObject.addComponent(PlayerShootBehaviour.class);
        shoot.setJoystick(shootJoystick);

        HealthBar bar = gameObject.addComponent(HealthBar.class);
        bar.setLivingComponent(this);

        Sprite sprite = gameObject.addComponent(Sprite.class);
        sprite.setBitmap(R.drawable.ship);
        SphereCollider collider = gameObject.addComponent(SphereCollider.class);
        collider.setRadius(0.05f);
        gameObject.addComponent(CollisionDebugger.class);
    }

    @Override
    protected void onDeath() {
        getScene().destroyGameObject(gameObject);
    }

    @Override
    protected void onHealthChange(int health) {

    }
}
