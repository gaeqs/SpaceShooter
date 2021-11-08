package io.github.spaceshooter.space.scene;

import io.github.spaceshooter.engine.GameEngine;
import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.Scene;
import io.github.spaceshooter.engine.camera.CameraScreenMode;
import io.github.spaceshooter.engine.component.basic.FPSViewer;
import io.github.spaceshooter.engine.math.Vector2f;
import io.github.spaceshooter.engine.sound.SoundManager;
import io.github.spaceshooter.space.enemy.AsteroidGenerator;
import io.github.spaceshooter.space.player.PlayerData;

public class GameScene extends Scene {

    public GameScene(GameEngine engine) {
        super(engine);

        registerSounds();

        getCamera().setScreenMode(CameraScreenMode.FIT_WIDTH);
        getCamera().setSize(new Vector2f(2, 2));

        GameObject fpsViewer = newGameObject("FPS");
        fpsViewer.addComponent(FPSViewer.class);

        initPlayer();
        initAsteroidGenerator();
    }

    private void registerSounds() {
        SoundManager m = getEngine().getSoundManager();
        m.registerSound("shoot", "shoot.wav");
    }

    private void initPlayer() {
        GameObject player = newGameObject("Player");
        player.getTransform().rotate(90);
        player.addComponent(PlayerData.class);
    }

    private void initAsteroidGenerator() {
        GameObject asteroidGenerator = newGameObject("Asteroid generator");
        asteroidGenerator.addComponent(AsteroidGenerator.class);
    }
}