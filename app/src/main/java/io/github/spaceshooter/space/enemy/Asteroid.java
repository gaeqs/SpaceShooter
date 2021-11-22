package io.github.spaceshooter.space.enemy;

import io.github.spaceshooter.R;
import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.collision.Collision;
import io.github.spaceshooter.engine.component.CollisionListenerComponent;
import io.github.spaceshooter.engine.component.TickableComponent;
import io.github.spaceshooter.engine.component.basic.Sprite;
import io.github.spaceshooter.engine.component.collision.SphereCollider;
import io.github.spaceshooter.engine.math.Vector2f;
import io.github.spaceshooter.space.background.PlayArea;
import io.github.spaceshooter.space.general.LivingComponent;
import io.github.spaceshooter.space.player.DamageInflicter;
import io.github.spaceshooter.util.Validate;

public class Asteroid extends LivingComponent implements
        TickableComponent, CollisionListenerComponent, DamageInflicter {

    public static final int MAX_HEALTH = 20;

    private final Sprite sprite;

    private Vector2f direction = new Vector2f(-1, 0);
    private float velocity = 0.5f;

    private float angularVelocity = getRandom().nextFloat() * 360 - 180;

    public Asteroid(GameObject gameObject) {
        super(gameObject, MAX_HEALTH);

        sprite = gameObject.addComponent(Sprite.class);
        sprite.setBitmap(R.drawable.asteroid);
        sprite.setSpriteScale(new Vector2f(0.2f, 0.2f));

        SphereCollider collider = gameObject.addComponent(SphereCollider.class);
        collider.setRadius(0.05f);
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

    public float getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    @Override
    public int getInflictedDamage(LivingComponent livingComponent) {
        return 20;
    }

    @Override
    public void tick(float deltaSeconds) {
        gameObject.getTransform().move(direction.mul(velocity * deltaSeconds));
        gameObject.getTransform().rotate(angularVelocity * deltaSeconds);

        Vector2f pos = gameObject.getTransform().getPosition();
        if (!PlayArea.DESPAWN_AREA.contains(pos.x(), pos.y())) {
            gameObject.getScene().destroyGameObject(gameObject);
        }
    }

    @Override
    public void onCollision(Collision collision) {
        GameObject o = collision.getOtherCollider().getGameObject();

        Asteroid asteroid = o.getComponent(Asteroid.class);
        if (asteroid != null) {
            damage(getInflictedDamage(asteroid) / 2);
            if (!isDead()) {
                direction = collision.getOtherNormal();
            }
            return;
        }

        LivingComponent living = o.getComponent(LivingComponent.class);
        if (living != null) {
            living.damage(getInflictedDamage(living));
            damage(getMaxHealth());
        }

    }

    @Override
    protected void onDeath() {
        getScene().destroyGameObject(gameObject);
    }

    @Override
    protected void onHealthChange(int health) {
        sprite.setBitmap(health > 10 ? R.drawable.asteroid : R.drawable.damaged_asteroid);
    }
}
