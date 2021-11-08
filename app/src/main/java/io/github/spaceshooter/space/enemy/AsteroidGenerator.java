package io.github.spaceshooter.space.enemy;

import java.util.Random;

import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.component.BasicComponent;
import io.github.spaceshooter.engine.component.TickableComponent;
import io.github.spaceshooter.engine.math.Vector2f;

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

        Vector2f from = new Vector2f(1.2f, (random.nextFloat() * 2) - 1);
        float toY = -from.y() * 0.5f + ((random.nextFloat() * 2) - 1) * 0.5f;
        Vector2f to = new Vector2f(-1, toY);

        asteroidGameObject.getTransform().setPosition(from);
        asteroid.setDirection(to.sub(from).normalized());
    }
}
