package io.github.spaceshooter.space.item;

import io.github.spaceshooter.R;
import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.collision.Collision;
import io.github.spaceshooter.engine.component.BasicComponent;
import io.github.spaceshooter.engine.component.CollisionListenerComponent;
import io.github.spaceshooter.engine.component.basic.Sprite;
import io.github.spaceshooter.engine.component.collision.SphereCollider;
import io.github.spaceshooter.engine.math.Vector2f;
import io.github.spaceshooter.space.player.PlayerData;

public class HealItem extends BasicComponent implements CollisionListenerComponent {

    public HealItem(GameObject gameObject) {
        super(gameObject);

        Sprite sprite = gameObject.addComponent(Sprite.class);
        sprite.setBitmap(R.drawable.heal_item);
        sprite.setSpriteScale(new Vector2f(0.244f, 0.10f));

        SphereCollider collider = gameObject.addComponent(SphereCollider.class);
        collider.setRadius(0.08f);
    }

    @Override
    public void onCollision(Collision collision) {
        PlayerData data = collision.getOtherCollider()
                .getGameObject().getComponent(PlayerData.class);

        if (data != null) {
            data.heal(20);
            getScene().destroyGameObject(gameObject);
        }
    }
}
