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
import io.github.spaceshooter.space.general.Team;
import io.github.spaceshooter.space.general.Teamable;
import io.github.spaceshooter.space.util.DamageInflicter;
import io.github.spaceshooter.space.util.MovingObject;
import io.github.spaceshooter.space.util.ScoreGiver;

public class Snake extends LivingComponent implements
        TickableComponent, CollisionListenerComponent, DamageInflicter, ScoreGiver, Teamable {

    public static final int MAX_HEALTH = 20;

    private final Sprite sprite;
    private final MovingObject moving;

    private Team team = Team.TEAM_1;

    private Vector2f last = Vector2f.ZERO;
    private float time = 0;

    public Snake(GameObject gameObject) {
        super(gameObject, MAX_HEALTH);

        moving = gameObject.getComponent(MovingObject.class);

        sprite = gameObject.addComponent(Sprite.class);
        sprite.setBitmap(R.drawable.snake_black);
        sprite.setSpriteScale(new Vector2f(0.1f, 0.1f));

        SphereCollider collider = gameObject.addComponent(SphereCollider.class);
        collider.setRadius(0.06f);
    }

    @Override
    public int getInflictedDamage(LivingComponent livingComponent) {
        return 10;
    }

    @Override
    public int getScoreToGive() {
        return 200;
    }

    @Override
    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
        sprite.setBitmap(team == Team.TEAM_1 ? R.drawable.snake_black : R.drawable.snake_red);
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    @Override
    public void tick(float deltaSeconds) {
        time += deltaSeconds;
        float sin = (float) Math.sin(time * 5) * 0.05f;

        Vector2f dir = moving.getDirection();
        Vector2f per = new Vector2f(dir.y(), -dir.x()).mul(sin);
        gameObject.getTransform().move(last.neg().add(per));
        last = per;
    }

    @Override
    public void onCollision(Collision collision) {
        GameObject o = collision.getOtherCollider().getGameObject();
        if (o.getComponent(Snake.class) != null) return;

        LivingComponent living = o.getComponent(LivingComponent.class);
        if (living != null) {
            if (living instanceof Teamable && ((Teamable) living).getTeam() == getTeam()) return;
            living.damage(getInflictedDamage(living));
        }
    }

    @Override
    protected void onDeath() {
        getScene().destroyGameObject(gameObject);
        playSound("explosion", getRandom().nextFloat() * 0.75f + 0.75f);
    }

    @Override
    protected void onHealthChange(int health) {
    }
}
