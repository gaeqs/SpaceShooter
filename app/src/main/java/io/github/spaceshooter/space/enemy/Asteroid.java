package io.github.spaceshooter.space.enemy;

import io.github.spaceshooter.R;
import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.collision.Collision;
import io.github.spaceshooter.engine.component.CollisionListenerComponent;
import io.github.spaceshooter.engine.component.TickableComponent;
import io.github.spaceshooter.engine.component.basic.CollisionDebugger;
import io.github.spaceshooter.engine.component.basic.Sprite;
import io.github.spaceshooter.engine.component.collision.SphereCollider;
import io.github.spaceshooter.engine.math.Vector2f;
import io.github.spaceshooter.space.general.LivingComponent;
import io.github.spaceshooter.space.player.Bullet;
import io.github.spaceshooter.space.player.PlayerData;
import io.github.spaceshooter.util.Validate;

public class Asteroid extends LivingComponent implements TickableComponent, CollisionListenerComponent {

    public static final int MAX_HEALTH = 20;

    private Vector2f direction = new Vector2f(-1, 0);
    private float velocity = 0.5f;
    private int inflictedDamage = 20;

    public Asteroid(GameObject gameObject) {
        super(gameObject, MAX_HEALTH);

        Sprite sprite = gameObject.addComponent(Sprite.class);
        sprite.setBitmap(R.drawable.a10000);
        sprite.setSpriteScale(new Vector2f(0.2f, 0.2f));

        SphereCollider collider = gameObject.addComponent(SphereCollider.class);
        collider.setRadius(0.05f);
        gameObject.addComponent(CollisionDebugger.class);
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

    public int getInflictedDamage() {
        return inflictedDamage;
    }

    public void setInflictedDamage(int inflictedDamage) {
        this.inflictedDamage = inflictedDamage;
    }

    @Override
    public void tick(float deltaSeconds) {
        gameObject.getTransform().move(direction.mul(velocity * deltaSeconds));

        Vector2f pos = gameObject.getTransform().getPosition();
        if (pos.x() < -5 || pos.y() > 2 || pos.y() < -2) {
            gameObject.getScene().destroyGameObject(gameObject);
        }
    }

    @Override
    public void onCollision(Collision collision) {
        GameObject o = collision.getOtherCollider().getGameObject();

        if (o.getComponent(Bullet.class) != null) {
            damage(10);
        }

        if (o.getComponent(Asteroid.class) != null) {
            damage(getMaxHealth());
            return;
        }

        PlayerData data = o.getComponent(PlayerData.class);
        if (data != null) {
            getScene().destroyGameObject(gameObject);
            damage(getMaxHealth());
            data.damage(20);
        }

    }

    @Override
    protected void onDeath() {
        getScene().destroyGameObject(gameObject);
    }
}
