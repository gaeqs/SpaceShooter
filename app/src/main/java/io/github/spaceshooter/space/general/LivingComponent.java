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

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = Math.max(0, maxHealth);
        this.health = Math.max(maxHealth, health);
        if (health == 0) {
            onDeath();
        }
    }

    public void damage(int damage) {
        heal(-damage);
    }

    public void heal(int heal) {
        health = Math.max(0, Math.min(health + heal, maxHealth));
        if (health <= 0) onDeath();
    }

    protected abstract void onDeath();

}
