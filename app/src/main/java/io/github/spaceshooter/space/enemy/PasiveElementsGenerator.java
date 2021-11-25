package io.github.spaceshooter.space.enemy;

import java.util.Random;

import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.component.BasicComponent;
import io.github.spaceshooter.engine.component.TickableComponent;
import io.github.spaceshooter.engine.math.Vector2f;
import io.github.spaceshooter.space.item.HealItem;
import io.github.spaceshooter.space.item.ShieldItem;
import io.github.spaceshooter.space.util.GenerationUtils;
import io.github.spaceshooter.space.util.MovingObject;

public class PasiveElementsGenerator extends BasicComponent implements TickableComponent {

    private float seconds;
    private float nextAsteroidTime;

    public PasiveElementsGenerator(GameObject gameObject) {
        super(gameObject);
    }

    @Override
    public void tick(float deltaSeconds) {
        seconds += deltaSeconds;

        if (seconds >= nextAsteroidTime) {

            if (getRandom().nextDouble() > 0.9) {
                generateHealItem();
            } else if (getRandom().nextDouble() > 0.9) {
                generateShieldItem();
            } else {
                generateAsteroid();
            }

            seconds = 0;
            nextAsteroidTime = getRandom().nextFloat() * 0.5f + 0.5f;
        }
    }

    private void generateAsteroid() {
        Random random = getRandom();
        GameObject asteroidGameObject = getScene().newGameObject("Asteroid");

        MovingObject moving = asteroidGameObject.addComponent(MovingObject.class);
        Vector2f from = GenerationUtils.generateOrigin(random);
        Vector2f to = new Vector2f(random.nextFloat() - 0.5f, random.nextFloat() - 0.5f);
        asteroidGameObject.getTransform().setPosition(from);

        moving.setDirection(to.sub(from).normalized());
        moving.setVelocity(0.5f);

        asteroidGameObject.addComponent(Asteroid.class);
    }

    private void generateHealItem() {
        Random random = getRandom();
        GameObject asteroidGameObject = getScene().newGameObject("Heal Item");

        MovingObject moving = asteroidGameObject.addComponent(MovingObject.class);
        Vector2f from = GenerationUtils.generateOrigin(random);
        Vector2f to = new Vector2f(random.nextFloat() - 0.5f, random.nextFloat() - 0.5f);
        asteroidGameObject.getTransform().setPosition(from);

        moving.setDirection(to.sub(from).normalized());
        moving.setVelocity(0.5f);

        asteroidGameObject.addComponent(HealItem.class);
    }

    private void generateShieldItem() {
        Random random = getRandom();
        GameObject asteroidGameObject = getScene().newGameObject("Shiel Item");

        MovingObject moving = asteroidGameObject.addComponent(MovingObject.class);
        Vector2f from = GenerationUtils.generateOrigin(random);
        Vector2f to = new Vector2f(random.nextFloat() - 0.5f, random.nextFloat() - 0.5f);
        asteroidGameObject.getTransform().setPosition(from);

        moving.setDirection(to.sub(from).normalized());
        moving.setVelocity(0.5f);

        asteroidGameObject.addComponent(ShieldItem.class);
    }
}
