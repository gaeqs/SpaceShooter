package io.github.spaceshooter.space.enemy;

import java.util.Random;

import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.component.BasicComponent;
import io.github.spaceshooter.engine.component.TickableComponent;
import io.github.spaceshooter.engine.math.Vector2f;
import io.github.spaceshooter.space.util.GenerationUtils;

public class AsteroidGenerator extends BasicComponent implements TickableComponent {

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
            nextAsteroidTime = getRandom().nextFloat() * 0.5f + 0.5f;
        }
    }

    private void generateAsteroid() {
        Random random = getRandom();
        GameObject asteroidGameObject = getScene().newGameObject("Asteroid");
        Asteroid asteroid = asteroidGameObject.addComponent(Asteroid.class);

        Vector2f from = GenerationUtils.generateOrigin(random);
        Vector2f to = new Vector2f(random.nextFloat() - 0.5f, random.nextFloat() - 0.5f);
        asteroidGameObject.getTransform().setPosition(from);
        asteroid.setDirection(to.sub(from).normalized());
    }
}
