package io.github.spaceshooter.space.player;

import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.component.BasicComponent;

public class PlayerData extends BasicComponent {

    public static final int MAX_HEALTH = 100;
    private int health = MAX_HEALTH;

    public PlayerData(GameObject gameObject) {
        super(gameObject);
    }

    public int getHealth() {
        return health;
    }

    public void damage(int damage) {
        health = Math.max(health - damage, 0);
        if (health < 0) {
            // DEATH
        }
    }

    public void heal(int heal) {
        health = Math.min(heal + health, MAX_HEALTH);
    }
}
