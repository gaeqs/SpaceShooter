package io.github.spaceshooter.space.general;

import io.github.spaceshooter.R;
import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.component.BasicComponent;
import io.github.spaceshooter.engine.component.basic.Sprite;
import io.github.spaceshooter.engine.math.Vector2f;
import io.github.spaceshooter.util.Validate;

public abstract class LivingComponent extends BasicComponent {

    private final Sprite shieldSprite;

    private int maxHealth;
    private int health;
    private int shield;

    public LivingComponent(GameObject gameObject, int maxHealth) {
        super(gameObject);
        Validate.isTrue(maxHealth > 0, "Max health must be bigger than 0.");
        this.maxHealth = maxHealth;
        this.health = maxHealth;

        shieldSprite = gameObject.addComponent(Sprite.class);
        shieldSprite.setSpriteScale(new Vector2f(0.2f, 0.2f));
        shieldSprite.setBitmap(null);
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public boolean isDead() {
        return health == 0;
    }

    public int getShield() {
        return shield;
    }

    public void setShield(int shield) {
        this.shield = Math.max(shield, 0);
        if (shield == 0) {
            shieldSprite.setBitmap(null);
        } else {
            shieldSprite.setBitmap(R.drawable.shield);
        }
    }

    public void setHealth(int health) {
        this.health = Math.min(Math.max(health, 0), maxHealth);
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = Math.max(0, maxHealth);
        int nextHealth = Math.max(this.maxHealth, health);
        if (nextHealth == health) return;
        health = nextHealth;
        onHealthChange(nextHealth);
        if (health == 0) {
            onDeath();
        }
    }

    public void damage(int damage) {
        if (shield > damage) {
            shield -= damage;
            return;
        } else {
            damage -= shield;
            shield = 0;
            shieldSprite.setBitmap(null);
        }

        heal(-damage);
    }

    public void heal(int heal) {
        if (heal == 0 || isDead()) return;
        health = Math.max(0, Math.min(health + heal, maxHealth));
        onHealthChange(health);
        if (health == 0) onDeath();
    }

    protected abstract void onDeath();

    protected abstract void onHealthChange(int health);

}
