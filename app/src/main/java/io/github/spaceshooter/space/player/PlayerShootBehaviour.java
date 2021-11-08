package io.github.spaceshooter.space.player;

import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.component.BasicComponent;
import io.github.spaceshooter.engine.component.basic.Button;
import io.github.spaceshooter.engine.math.Vector2f;

public class PlayerShootBehaviour extends BasicComponent {

    public PlayerShootBehaviour(GameObject gameObject) {
        super(gameObject);
        Button button = gameObject.getComponent(Button.class);
        //button.setOnRelease(this::shoot);
    }

    private void shoot() {
        GameObject object = getScene().newGameObject("Bullet");
        Bullet bullet = object.addComponent(Bullet.class);
        bullet.setOrigin(gameObject);
        bullet.getCollider().setRadius(0.05f);
        object.getTransform().setPosition(gameObject.getTransform().getPosition());
        playSound("shoot");
    }
}
