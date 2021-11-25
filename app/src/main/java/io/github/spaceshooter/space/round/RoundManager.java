package io.github.spaceshooter.space.round;

import java.util.Random;

import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.component.BasicComponent;
import io.github.spaceshooter.engine.component.basic.Text;
import io.github.spaceshooter.engine.math.Vector2f;
import io.github.spaceshooter.space.enemy.EnemySpaceship;
import io.github.spaceshooter.space.general.Team;
import io.github.spaceshooter.space.util.GenerationUtils;
import io.github.spaceshooter.space.util.MovingObject;

public class RoundManager extends BasicComponent {

    private int round = 0;
    private int enemiesLeft = 0;

    private final Text text;

    public RoundManager(GameObject gameObject) {
        super(gameObject);

        text = gameObject.addComponent(Text.class);
        text.setPosition(new Vector2f(getEngine().getGameView().getGUIWidth() / 2, 0.2f));
        text.setCentered(true);
        text.getPaint().setColor(0xFFFFFFFF);
        text.getPaint().setTextSize(0.2f);

        runLater(5, this::startRound);
    }

    public int getRound() {
        return round;
    }

    private void startRound() {
        round++;
        enemiesLeft = round * 7;

        text.setText("Round " + round);
        runLater(3, () -> text.setText(""));

        entityLoop();
    }

    private void entityLoop() {
        spawnEntity();
        enemiesLeft--;
        if (enemiesLeft == 0) {
            runLater(5, this::startRound);
        } else {
            runLater(timeEntity(), this::entityLoop);
        }
    }

    private float timeEntity() {
        float func = 5.0f / round;
        return getRandom().nextFloat() * func;
    }

    private void spawnEntity() {
        Random random = new Random();
        Vector2f from = GenerationUtils.generateOrigin(random);
        Vector2f to = new Vector2f(random.nextFloat() - 0.5f, random.nextFloat() - 0.5f);
        Vector2f direction = to.sub(from).normalized();

        GameObject enemy = getScene().newGameObject("Enemy Spaceship");
        enemy.getTransform().setPosition(from);
        EnemySpaceship spaceship = enemy.addComponent(EnemySpaceship.class);
        spaceship.setTeam(random.nextBoolean() ? Team.TEAM_1 : Team.TEAM_2);

        MovingObject moving = enemy.addComponent(MovingObject.class);
        moving.setDirection(direction);
        moving.setVelocity(0.75f);
    }


}
