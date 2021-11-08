package io.github.spaceshooter.space.general;

import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.component.BasicComponent;
import io.github.spaceshooter.util.Validate;

public abstract class LivingComponent extends BasicComponent {

    private int maxHealth;
    private int health;

    public LivingComponent(GameObject gameObject, int maxHealth) {
        super(gameObject);
        Validate.isTrue(maxHealth > 0, "Max health must be bigger than 0.");
        this.maxHealth = maxHealth;
        this.health = maxHealth;
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

    public void setMaxHealth(int maxHealth) {
        maxHealth = Math.max(0, maxHealth);
        int nextHealth = Math.max(maxHealth, health);
        if (nextHealth == health) return;
        health = nextHealth;
        onHealthChange(nextHealth);
        if (health == 0) {
            onDeath();
        }
    }

    public void damage(int damage) {
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
