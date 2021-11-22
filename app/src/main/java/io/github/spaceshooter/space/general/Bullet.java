package io.github.spaceshooter.space.general;

import io.github.spaceshooter.R;
import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.collision.Collision;
import io.github.spaceshooter.engine.component.CollisionListenerComponent;
import io.github.spaceshooter.engine.component.basic.Sprite;
import io.github.spaceshooter.engine.component.collision.SphereCollider;
import io.github.spaceshooter.space.player.PlayerData;
import io.github.spaceshooter.space.util.DamageInflicter;
import io.github.spaceshooter.space.util.ScoreGiver;
import io.github.spaceshooter.util.Validate;

public class Bullet extends Sprite implements CollisionListenerComponent, DamageInflicter {

    private final SphereCollider collider;
    private GameObject origin;

    public int infringedDamage = 5;

    public Bullet(GameObject gameObject) {
        super(gameObject);
        setBitmap(R.drawable.bullet);
        collider = gameObject.addComponent(SphereCollider.class);
        collider.setRadius(0.025f);
    }

    public SphereCollider getCollider() {
        return collider;
    }

    public GameObject getOrigin() {
        return origin;
    }

    public void setOrigin(GameObject origin) {
        Validate.notNull(origin, "Origin cannot be null!");
        this.origin = origin;
    }

    @Override
    public int getInflictedDamage(LivingComponent livingComponent) {
        return infringedDamage;
    }

    public void setInfringedDamage(int infringedDamage) {
        this.infringedDamage = infringedDamage;
    }

    @Override
    public void onCollision(Collision collision) {
        if (collision.getOtherCollider().getGameObject().equals(origin)) return;
        if (collision.getOtherCollider().getGameObject().getComponent(Bullet.class) != null) return;
        getScene().destroyGameObject(gameObject);

        GameObject o = collision.getOtherCollider().getGameObject();
        o.getAllComponents(LivingComponent.class).forEach(it -> {
            it.damage(getInflictedDamage(it));
            if (it.isDead()) {
                PlayerData player = origin.getComponent(PlayerData.class);
                if (player != null) {
                    player.getStats().enemiesDestroyed++;
                    o.getAllComponents(ScoreGiver.class).forEach(giver ->
                            player.getStats().score += giver.getScoreToGive());
                }
            }
        });
    }
}
