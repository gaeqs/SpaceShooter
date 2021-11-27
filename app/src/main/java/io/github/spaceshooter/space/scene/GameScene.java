package io.github.spaceshooter.space.scene;

import io.github.spaceshooter.R;
import io.github.spaceshooter.engine.GameEngine;
import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.Scene;
import io.github.spaceshooter.engine.camera.CameraScreenMode;
import io.github.spaceshooter.engine.component.basic.Button;
import io.github.spaceshooter.engine.component.basic.FPSViewer;
import io.github.spaceshooter.engine.component.basic.Image;
import io.github.spaceshooter.engine.component.basic.Text;
import io.github.spaceshooter.engine.gui.GUIComponentArea;
import io.github.spaceshooter.engine.math.Vector2f;
import io.github.spaceshooter.engine.sound.SoundManager;
import io.github.spaceshooter.space.background.Background;
import io.github.spaceshooter.space.background.PlayArea;
import io.github.spaceshooter.space.enemy.PasiveElementsGenerator;
import io.github.spaceshooter.space.player.PlayerData;
import io.github.spaceshooter.space.player.ShipType;
import io.github.spaceshooter.space.round.RoundManager;

public class GameScene extends Scene {

    public GameScene(GameEngine engine, ShipType type) {
        super(engine);

        registerSounds();

        getCamera().setScreenMode(CameraScreenMode.FIT_WIDTH);
        getCamera().setSize(new Vector2f(2, 2));

        //
        // GameObject fpsViewer = newGameObject("FPS");
        //
        // fpsViewer.addComponent(Text.class).setPosition(new Vector2f(1, 0.9f));
        //
        // fpsViewer.addComponent(FPSViewer.class);

        GameObject roundManager = newGameObject("Round Manager");
        roundManager.addComponent(RoundManager.class);

        initPlayer(type);
        initAsteroidGenerator();
        initBackground();
        initPauseButton();

        if(getRandom().nextFloat() > 0.9) {
            getEngine().getSoundManager().playMusic("battle_2.mp3");
        } else {
            getEngine().getSoundManager().playMusic("battle_1.mp3");
        }

    }

    private void registerSounds() {
        SoundManager m = getEngine().getSoundManager();
        m.registerSound("shoot", "shoot.wav");
        m.registerSound("explosion", "explosion.wav");
        m.registerSound("button", "button.wav");
        m.registerSound("shield", "shield.wav");
        m.registerSound("health", "health.wav");
    }

    private void initPlayer(ShipType type) {
        GameObject player = newGameObject("Player");
        player.getTransform().rotate(90);
        player.addComponent(PlayerData.class).setShipType(type);
    }

    private void initAsteroidGenerator() {
        GameObject asteroidGenerator = newGameObject("Asteroid generator");
        asteroidGenerator.addComponent(PasiveElementsGenerator.class);
    }

    private void initBackground() {
        GameObject background = newGameObject("Background");
        background.addComponent(PlayArea.class);
        background.addComponent(Background.class);
    }

    private void initPauseButton() {
        GameObject pause = newGameObject("Pause button");

        GUIComponentArea area = new GUIComponentArea(Vector2f.ZERO,
                new Vector2f(0.2f, 0.2f), false, true);

        Button button = pause.addComponent(Button.class);
        button.setArea(area);
        button.setOnPress(() -> getEngine().setScene(new PauseScene(getEngine(), this)));

        Image image = pause.addComponent(Image.class);
        image.setArea(area);
        image.setBitmap(R.drawable.icon_pause);
    }
}
