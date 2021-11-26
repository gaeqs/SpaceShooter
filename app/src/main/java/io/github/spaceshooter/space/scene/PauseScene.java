package io.github.spaceshooter.space.scene;

import io.github.spaceshooter.R;
import io.github.spaceshooter.engine.GameEngine;
import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.Scene;
import io.github.spaceshooter.engine.component.basic.BackgroundImage;
import io.github.spaceshooter.engine.component.basic.Image;
import io.github.spaceshooter.engine.component.basic.Text;
import io.github.spaceshooter.engine.gui.GUIComponentArea;
import io.github.spaceshooter.engine.math.Vector2f;
import io.github.spaceshooter.space.pause.BackToGameButton;

public class PauseScene extends Scene {

    public PauseScene(GameEngine engine, Scene gameScene) {
        super(engine);
        GameObject textObject = newGameObject("Text");
        Text text = textObject.addComponent(Text.class);
        text.setText("Pause");
        text.setCentered(true);
        text.getPaint().setTextSize(0.2f);
        text.setPosition(new Vector2f(getEngine().getGameView().getGUIWidth() / 2, 0.5f));

        BackToGameButton button = textObject.addComponent(BackToGameButton.class);
        button.setGameScene(gameScene);

        Image image = textObject.addComponent(BackgroundImage.class);
        image.setBitmap(R.drawable.background);
        image.setArea(new GUIComponentArea(Vector2f.ZERO,
                new Vector2f(engine.getGameView().getGUIWidth(), 1),
                false, false));
    }
}
