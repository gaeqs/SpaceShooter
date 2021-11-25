package io.github.spaceshooter.space.gameover;

import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.component.BasicComponent;
import io.github.spaceshooter.engine.component.basic.Button;
import io.github.spaceshooter.engine.component.basic.Text;
import io.github.spaceshooter.engine.gui.GUIComponentArea;
import io.github.spaceshooter.engine.math.Vector2f;
import io.github.spaceshooter.space.player.PlayerStats;
import io.github.spaceshooter.space.player.ShipType;
import io.github.spaceshooter.space.scene.GameScene;
import io.github.spaceshooter.space.scene.MainMenuScene;

public class GameOverScreen extends BasicComponent {

    private final Text killedEnemiesText;
    private final Text scoreText;
    private final Text roundText;

    private ShipType shipType = ShipType.NORMAL;

    public GameOverScreen(GameObject gameObject) {
        super(gameObject);

        float gw = getEngine().getGameView().getGUIWidth();

        Text gameOverText = gameObject.addComponent(Text.class);
        gameOverText.setText("GAME OVER");
        gameOverText.getPaint().setColor(0xFFFFFFFF);
        gameOverText.getPaint().setTextSize(0.2f);
        gameOverText.setPosition(new Vector2f(gw / 2, 0.2f));
        gameOverText.setCentered(true);


        killedEnemiesText = gameObject.addComponent(Text.class);
        killedEnemiesText.getPaint().setColor(0xFFFFFFFF);
        killedEnemiesText.getPaint().setTextSize(0.07f);
        killedEnemiesText.setPosition(new Vector2f(gw / 2, 0.3f));
        killedEnemiesText.setCentered(true);

        scoreText = gameObject.addComponent(Text.class);
        scoreText.getPaint().setColor(0xFFFFFFFF);
        scoreText.getPaint().setTextSize(0.07f);
        scoreText.setPosition(new Vector2f(gw / 2, 0.5f));
        scoreText.setCentered(true);

        roundText = gameObject.addComponent(Text.class);
        roundText.getPaint().setColor(0xFFFFFFFF);
        roundText.getPaint().setTextSize(0.07f);
        roundText.setPosition(new Vector2f(gw / 2, 0.7f));
        roundText.setCentered(true);


        Text playAgain = gameObject.addComponent(Text.class);
        playAgain.setText("Play again");
        playAgain.getPaint().setColor(0xFFFFFFFF);
        playAgain.getPaint().setTextSize(0.07f);
        playAgain.setPosition(new Vector2f(gw / 4, 0.8f));
        playAgain.setCentered(true);

        Text mainMenu = gameObject.addComponent(Text.class);
        mainMenu.setText("Main menu");
        mainMenu.getPaint().setColor(0xFFFFFFFF);
        mainMenu.getPaint().setTextSize(0.07f);
        mainMenu.setPosition(new Vector2f(gw * 3 / 4, 0.8f));
        mainMenu.setCentered(true);

        Button playAgainButton = gameObject.addComponent(Button.class);
        playAgainButton.setArea(new GUIComponentArea(
                new Vector2f(0, 0.7f),
                new Vector2f(gw / 2, 0.3f),
                false,
                false
        ));

        playAgainButton.setOnPress(() ->
                getEngine().setScene(new GameScene(getEngine(), shipType)));

        Button mainMenuButton = gameObject.addComponent(Button.class);
        mainMenuButton.setArea(new GUIComponentArea(
                new Vector2f(gw / 2, 0.7f),
                new Vector2f(gw / 2, 0.3f),
                false,
                false
        ));

        mainMenuButton.setOnPress(() -> getEngine().setScene(new MainMenuScene(getEngine())));
    }

    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    public void setStats(PlayerStats stats) {
        killedEnemiesText.setText("Enemies destroyed: " + stats.enemiesDestroyed);
        scoreText.setText("Score: " + stats.score);
    }

    public void setRound(int round) {
        roundText.setText("Round: " + round);
    }
}
