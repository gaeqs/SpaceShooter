package io.github.spaceshooter.space.enemy;

import java.util.Random;

import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.component.BasicComponent;
import io.github.spaceshooter.engine.component.TickableComponent;
import io.github.spaceshooter.engine.math.Vector2f;
import io.github.spaceshooter.space.background.PlayArea;

public class AsteroidGenerator extends BasicComponent implements TickableComponent {

    private final Random random = new Random();
    private float seconds;
    private float nextAsteroidTime;

    public AsteroidGenerator(GameObject gameObject) {
        super(gameObject);
    }

    @Override
    public void tick(float deltaSeconds) {
        seconds += deltaSeconds;

        if (seconds >= nextAsteroidTime) {

            generateAsteroid();

            seconds = 0;
            nextAsteroidTime = random.nextFloat() * 0.5f + 0.5f;
        }
    }

    private void generateAsteroid() {
        GameObject asteroidGameObject = getScene().newGameObject("Asteroid");
        Asteroid asteroid = asteroidGameObject.addComponent(Asteroid.class);

        Vector2f from = generateOrigin();
        Vector2f to = new Vector2f(random.nextFloat() - 0.5f, random.nextFloat() - 0.5f);
        asteroidGameObject.getTransform().setPosition(from);
        asteroid.setDirection(to.sub(from).normalized());
    }

    private Vector2f generateOrigin() {
        float x, y;
        if (random.nextBoolean()) {
            x = random.nextBoolean() ? PlayArea.DESPAWN_AREA.left : PlayArea.DESPAWN_AREA.right;
            float p = random.nextFloat();
            y = p * PlayArea.DESPAWN_AREA.top + p * PlayArea.DESPAWN_AREA.bottom;
        } else {
            y = random.nextBoolean() ? PlayArea.DESPAWN_AREA.bottom : PlayArea.DESPAWN_AREA.top;
            float p = random.nextFloat();
            x = p * PlayArea.DESPAWN_AREA.bottom + p * PlayArea.DESPAWN_AREA.top;
        }
        return new Vector2f(x, y);
    }
}
