package io.github.spaceshooter.space.general;

import java.util.HashSet;
import java.util.Set;

import io.github.spaceshooter.R;
import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.collision.Collision;
import io.github.spaceshooter.engine.component.CollisionListenerComponent;
import io.github.spaceshooter.engine.component.basic.Sprite;
import io.github.spaceshooter.engine.component.collision.SphereCollider;
import io.github.spaceshooter.engine.math.Vector2f;
import io.github.spaceshooter.space.player.PlayerData;
import io.github.spaceshooter.space.util.DamageInflicter;
import io.github.spaceshooter.space.util.ScoreGiver;
import io.github.spaceshooter.util.Validate;

public class Bullet extends Sprite implements CollisionListenerComponent, DamageInflicter, Teamable {

    private final SphereCollider collider;
    private final Set<GameObject> collided = new HashSet<>();
    private GameObject origin;

    private int infringedDamage = 5;

    private int maximumDamageableElements = 1;
    private int damagedEntities = 0;

    private Team team = Team.TEAM_1;

    public Bullet(GameObject gameObject) {
        super(gameObject);
        setBitmap(R.drawable.bullet_black);
        setSpriteScale(new Vector2f(0.08f, 0.08f));

        collider = gameObject.addComponent(SphereCollider.class);
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
    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
        setBitmap(team == Team.TEAM_1 ? R.drawable.bullet_black : R.drawable.bullet_red);
    }

    public int getMaximumDamageableElements() {
        return maximumDamageableElements;
    }

    public void setMaximumDamageableElements(int maximumDamageableElements) {
        this.maximumDamageableElements = maximumDamageableElements;
    }

    @Override
    public void onCollision(Collision collision) {
        if (collision.getOtherCollider().getGameObject().equals(origin)) return;
        if (collision.getOtherCollider().getGameObject().getComponent(Bullet.class) != null) return;
        if (collided.contains(collision.getOtherCollider().getGameObject())) return;
        collided.add(collision.getOtherCollider().getGameObject());

        GameObject o = collision.getOtherCollider().getGameObject();
        o.getAllComponents(LivingComponent.class).forEach(it -> {
            if (it instanceof Teamable && ((Teamable) it).getTeam() == getTeam()) return;
            it.damage(getInflictedDamage(it));

            damagedEntities++;

            if (damagedEntities >= maximumDamageableElements) {
                getScene().destroyGameObject(gameObject);
            }


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

    @Override
    public int getDrawPriority() {
        return -1;
    }
}
