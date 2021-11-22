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
import io.github.spaceshooter.space.general.Bullet;
import io.github.spaceshooter.space.general.LivingComponent;
import io.github.spaceshooter.space.util.DamageInflicter;
import io.github.spaceshooter.space.player.PlayerData;
import io.github.spaceshooter.space.util.MovingObject;
import io.github.spaceshooter.space.util.ScoreGiver;
import io.github.spaceshooter.util.Validate;

public class EnemySpaceship extends LivingComponent implements
        TickableComponent, CollisionListenerComponent, DamageInflicter, ScoreGiver {

    public static final int MAX_HEALTH = 60;

    private final Sprite sprite;
    private final PlayerData player;

    public EnemySpaceship(GameObject gameObject) {
        super(gameObject, MAX_HEALTH);

        sprite = gameObject.addComponent(Sprite.class);
        sprite.setBitmap(R.drawable.ship);

        SphereCollider collider = gameObject.addComponent(SphereCollider.class);
        collider.setRadius(0.05f);

        player = gameObject.findComponent(PlayerData.class);
        shoot();
    }

    @Override
    public int getInflictedDamage(LivingComponent livingComponent) {
        return 20;
    }

    @Override
    public int getScoreToGive() {
        return 500;
    }

    @Override
    public void tick(float deltaSeconds) {
        Vector2f dir = player.gameObject.getTransform().getPosition()
                .sub(gameObject.getTransform().getPosition());
        gameObject.getTransform().lookAt(dir);
    }

    @Override
    public void onCollision(Collision collision) {
        if (isDead()) return;
        GameObject o = collision.getOtherCollider().getGameObject();
        LivingComponent living = o.getComponent(LivingComponent.class);
        if (living != null) {
            living.damage(getInflictedDamage(living));
        }
    }

    @Override
    protected void onDeath() {
        getScene().destroyGameObject(gameObject);
    }

    @Override
    protected void onHealthChange(int health) {
    }

    private void shoot() {

        Vector2f dir = player.gameObject.getTransform().getPosition()
                .sub(gameObject.getTransform().getPosition()).normalized();

        GameObject object = getScene().newGameObject("Bullet");

        MovingObject moving = object.addComponent(MovingObject.class);
        moving.setDirection(dir);
        moving.setVelocity(1);

        Bullet bullet = object.addComponent(Bullet.class);
        bullet.setOrigin(gameObject);
        bullet.getCollider().setRadius(0.05f);
        object.getTransform().setPosition(gameObject.getTransform().getPosition());

        runLater(1.0f, this::shoot);
    }
}
