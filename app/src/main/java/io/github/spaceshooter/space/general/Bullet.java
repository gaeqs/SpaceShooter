package io.github.spaceshooter.space.general;

import io.github.spaceshooter.R;
import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.collision.Collision;
import io.github.spaceshooter.engine.component.CollisionListenerComponent;
import io.github.spaceshooter.engine.component.TickableComponent;
import io.github.spaceshooter.engine.component.basic.CollisionDebugger;
import io.github.spaceshooter.engine.component.basic.Sprite;
import io.github.spaceshooter.engine.component.collision.SphereCollider;
import io.github.spaceshooter.engine.math.Vector2f;
import io.github.spaceshooter.util.Validate;

public class Bullet extends Sprite implements TickableComponent, CollisionListenerComponent {

    private final SphereCollider collider;
    private GameObject origin;

    private Vector2f direction = new Vector2f(1, 0);
    private float velocity = 1;
    private int damage = 5;


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

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    @Override
    public void tick(float deltaSeconds) {
        gameObject.getTransform().move(direction.mul(velocity * deltaSeconds));
        Vector2f pos = gameObject.getTransform().getPosition();
        if (pos.x() < -1.2 || pos.x() > 1.5 || pos.y() > 2 || pos.y() < -2) {
            getScene().destroyGameObject(gameObject);
        }
    }

    @Override
    public void onCollision(Collision collision) {
        if (collision.getOtherCollider().getGameObject().equals(origin)) return;
        if (collision.getOtherCollider().getGameObject().getComponent(Bullet.class) != null) return;
        getScene().destroyGameObject(gameObject);
        collision.getOtherCollider().getGameObject().getAllComponents(LivingComponent.class)
                .forEach(it -> it.damage(damage));
    }
}
