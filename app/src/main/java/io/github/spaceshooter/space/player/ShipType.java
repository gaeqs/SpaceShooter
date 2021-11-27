package io.github.spaceshooter.space.player;

import io.github.spaceshooter.R;
import io.github.spaceshooter.space.general.Team;

public enum ShipType {

    NORMAL(1, 100, 0.1f, 5, R.drawable.ship_black, R.drawable.ship_red),
    FAST(1.5f, 70, 0.05f, 3, R.drawable.ship_light_black, R.drawable.ship_light_red),
    TANK(0.7f, 200, 0.25f, 15, R.drawable.ship_heavy_black, R.drawable.ship_heavy_red);

    private final float speed;
    private final int maxHealth;
    private final float secondsPerShoot;
    private final int bulletsDamage;
    private final int team1Sprite;
    private final int team2Sprite;

    ShipType(float speed, int maxHealth,
             float secondsPerShoot, int bulletsDamage,
             int team1Sprite, int team2Sprite) {
        this.speed = speed;
        this.maxHealth = maxHealth;
        this.secondsPerShoot = secondsPerShoot;
        this.bulletsDamage = bulletsDamage;
        this.team1Sprite = team1Sprite;
        this.team2Sprite = team2Sprite;
    }

    public float getSpeed() {
        return speed;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public float getSecondsPerShoot() {
        return secondsPerShoot;
    }

    public int getBulletsDamage() {
        return bulletsDamage;
    }

    public int getSprite(Team team) {
        return team == Team.TEAM_1 ? team1Sprite : team2Sprite;
    }
}
