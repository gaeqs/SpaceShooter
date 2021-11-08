package io.github.spaceshooter.space.room;

import io.github.spaceshooter.R;
import io.github.spaceshooter.engine.GameEngine;
import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.Scene;
import io.github.spaceshooter.engine.camera.CameraScreenMode;
import io.github.spaceshooter.engine.component.basic.CollisionDebugger;
import io.github.spaceshooter.engine.component.basic.FPSViewer;
import io.github.spaceshooter.engine.component.basic.Joystick;
import io.github.spaceshooter.engine.component.basic.Sprite;
import io.github.spaceshooter.engine.component.collision.SphereCollider;
import io.github.spaceshooter.engine.math.Vector2f;
import io.github.spaceshooter.space.enemy.AsteroidGenerator;
import io.github.spaceshooter.space.player.HealthBar;
import io.github.spaceshooter.space.player.PlayerData;
import io.github.spaceshooter.space.player.PlayerMovementBehaviour;

public class GameScene extends Scene {

    public GameScene(GameEngine engine) {
        super(engine);

        getCamera().setScreenMode(CameraScreenMode.FIT_WIDTH);
        getCamera().setSize(new Vector2f(2, 2));

        GameObject fpsViewer = newGameObject("FPS");
        fpsViewer.addComponent(FPSViewer.class);

        initPlayer();
        initAsteroidGenerator();
    }


    private void initPlayer() {
        GameObject player = newGameObject("Player");
        player.getTransform().rotate(90);
        player.addComponent(Joystick.class);
        player.addComponent(PlayerData.class);
        player.addComponent(PlayerMovementBehaviour.class);
        player.addComponent(HealthBar.class);

        Sprite sprite = player.addComponent(Sprite.class);
        sprite.setBitmap(R.drawable.ship);
        SphereCollider collider = player.addComponent(SphereCollider.class);
        collider.setRadius(0.05f);
        player.addComponent(CollisionDebugger.class);
    }

    private void initAsteroidGenerator() {
        GameObject asteroidGenerator = newGameObject("Asteroid generator");
        asteroidGenerator.addComponent(AsteroidGenerator.class);
    }
}
