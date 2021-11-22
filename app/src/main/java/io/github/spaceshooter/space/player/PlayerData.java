package io.github.spaceshooter.space.player;

import io.github.spaceshooter.R;
import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.collision.Collision;
import io.github.spaceshooter.engine.component.CollisionListenerComponent;
import io.github.spaceshooter.engine.component.basic.Joystick;
import io.github.spaceshooter.engine.component.basic.Sprite;
import io.github.spaceshooter.engine.component.basic.Text;
import io.github.spaceshooter.engine.component.collision.SphereCollider;
import io.github.spaceshooter.engine.math.Vector2f;
import io.github.spaceshooter.space.general.HealthBar;
import io.github.spaceshooter.space.general.LivingComponent;
import io.github.spaceshooter.space.util.DamageInflicter;
import io.github.spaceshooter.space.util.ScoreGiver;

public class PlayerData extends LivingComponent implements CollisionListenerComponent, DamageInflicter {

    public static final int MAX_HEALTH = 100;

    private final PlayerStats stats = new PlayerStats();

    public PlayerData(GameObject gameObject) {
        super(gameObject, MAX_HEALTH);

        Joystick movementJoystick = gameObject.addComponent(Joystick.class);
        Joystick shootJoystick = gameObject.addComponent(Joystick.class);
        shootJoystick.getArea().setAttachToRight(true);

        PlayerMovementBehaviour movement = gameObject.addComponent(PlayerMovementBehaviour.class);
        movement.setMovementJoystick(movementJoystick);
        movement.setShootJoystick(shootJoystick);

        PlayerShootBehaviour shoot = gameObject.addComponent(PlayerShootBehaviour.class);
        shoot.setJoystick(shootJoystick);

        HealthBar bar = gameObject.addComponent(HealthBar.class);
        bar.setLivingComponent(this);

        gameObject.addComponent(Text.class).setPosition(new Vector2f(0.2f, 0.9f));
        ScoreDisplay scoreDisplay = gameObject.addComponent(ScoreDisplay.class);
        scoreDisplay.setStats(stats);

        Sprite sprite = gameObject.addComponent(Sprite.class);
        sprite.setBitmap(R.drawable.ship);
        SphereCollider collider = gameObject.addComponent(SphereCollider.class);
        collider.setRadius(0.05f);
    }

    public PlayerStats getStats() {
        return stats;
    }

    @Override
    protected void onDeath() {
        getScene().destroyGameObject(gameObject);
    }

    @Override
    protected void onHealthChange(int health) {

    }

    @Override
    public void onCollision(Collision collision) {
        GameObject o = collision.getOtherCollider().getGameObject();
        LivingComponent living = o.getComponent(LivingComponent.class);
        if (living != null) {
            living.damage(getInflictedDamage(living));
            if (living.isDead()) {
                stats.enemiesDestroyed++;
                o.getAllComponents(ScoreGiver.class).forEach(giver ->
                        stats.score += giver.getScoreToGive());
            }
        }
    }

    @Override
    public int getInflictedDamage(LivingComponent livingComponent) {
        return livingComponent.getMaxHealth();
    }
}
