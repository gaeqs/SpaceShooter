package io.github.spaceshooter.space.player;

import io.github.spaceshooter.R;
import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.collision.Collision;
import io.github.spaceshooter.engine.component.CollisionListenerComponent;
import io.github.spaceshooter.engine.component.basic.Button;
import io.github.spaceshooter.engine.component.basic.Image;
import io.github.spaceshooter.engine.component.basic.Joystick;
import io.github.spaceshooter.engine.component.basic.Sprite;
import io.github.spaceshooter.engine.component.basic.Text;
import io.github.spaceshooter.engine.component.collision.SphereCollider;
import io.github.spaceshooter.engine.gui.GUIComponentArea;
import io.github.spaceshooter.engine.math.Vector2f;
import io.github.spaceshooter.space.gameover.GameOverScreen;
import io.github.spaceshooter.space.general.LivingComponent;
import io.github.spaceshooter.space.general.Team;
import io.github.spaceshooter.space.general.Teamable;
import io.github.spaceshooter.space.round.RoundManager;
import io.github.spaceshooter.space.util.DamageInflicter;
import io.github.spaceshooter.space.util.ScoreGiver;

public class PlayerData extends LivingComponent implements
        CollisionListenerComponent, DamageInflicter, Teamable {

    public static final int MAX_HEALTH = 100;

    private final PlayerStats stats = new PlayerStats();
    private final Sprite sprite;

    private Team team = Team.TEAM_1;

    public PlayerData(GameObject gameObject) {
        super(gameObject, MAX_HEALTH);

        Joystick movementJoystick = gameObject.addComponent(Joystick.class);
        Joystick shootJoystick = gameObject.addComponent(Joystick.class);
        shootJoystick.getArea().setAttachToRight(true);

        GUIComponentArea swapArea = new GUIComponentArea(new Vector2f(0.15f, 0.25f),
                new Vector2f(0.2f, 0.2f), false, true);
        Button swapButton = gameObject.addComponent(Button.class);
        swapButton.setArea(swapArea);
        swapButton.setOnPress(() -> setTeam(team == Team.TEAM_1 ? Team.TEAM_2 : Team.TEAM_1));

        Image image = gameObject.addComponent(Image.class);
        image.setBitmap(R.drawable.icon_swap);
        image.setArea(swapArea);

        PlayerMovementBehaviour movement = gameObject.addComponent(PlayerMovementBehaviour.class);
        movement.setMovementJoystick(movementJoystick);
        movement.setShootJoystick(shootJoystick);

        PlayerShootBehaviour shoot = gameObject.addComponent(PlayerShootBehaviour.class);
        shoot.setJoystick(shootJoystick);
        shoot.setPlayer(this);

        Text scoreText = gameObject.addComponent(Text.class);
        scoreText.setPosition(new Vector2f(0.2f, 0.2f));
        scoreText.getPaint().setColor(0xFFFFFFFF);
        ScoreDisplay scoreDisplay = gameObject.addComponent(ScoreDisplay.class);
        scoreDisplay.setStats(stats);

        sprite = gameObject.addComponent(Sprite.class);
        sprite.setBitmap(R.drawable.ship_black);
        sprite.setSpriteScale(new Vector2f(0.15f, 0.15f));
        SphereCollider collider = gameObject.addComponent(SphereCollider.class);
        collider.setRadius(0.07f);

    }

    public PlayerStats getStats() {
        return stats;
    }

    @Override
    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
        sprite.setBitmap(team == Team.TEAM_1 ? R.drawable.ship_black : R.drawable.ship_red);
    }

    @Override
    protected void onDeath() {
        getScene().destroyGameObject(gameObject);

        GameOverScreen screen = getScene().newGameObject("Game over")
                .addComponent(GameOverScreen.class);
        screen.setStats(stats);


        GameObject pause = getScene().findGameObject("Pause button");
        if (pause != null) {
            getScene().destroyGameObject(pause);
        }

        GameObject roundManager = getScene().findGameObject("Round Manager");
        if (roundManager != null) {
            RoundManager manager = roundManager.getComponent(RoundManager.class);
            if (manager != null) {
                screen.setRound(manager.getRound());
            }
            getScene().destroyGameObject(roundManager);
        }
    }

    @Override
    protected void onHealthChange(int health) {

    }

    @Override
    public void onCollision(Collision collision) {
        GameObject o = collision.getOtherCollider().getGameObject();
        LivingComponent living = o.getComponent(LivingComponent.class);
        if (living != null) {
            if (living instanceof Teamable && ((Teamable) living).getTeam() == getTeam()) return;
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
