package io.github.spaceshooter.space.pause;

import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.Scene;
import io.github.spaceshooter.engine.component.BasicComponent;
import io.github.spaceshooter.engine.component.InputListenerComponent;
import io.github.spaceshooter.engine.input.InputDownEvent;
import io.github.spaceshooter.engine.input.InputMoveEvent;
import io.github.spaceshooter.engine.input.InputUpEvent;

public class BackToGameButton extends BasicComponent implements InputListenerComponent {

    private Scene gameScene;

    public BackToGameButton(GameObject gameObject) {
        super(gameObject);
    }

    public void setGameScene(Scene gameScene) {
        this.gameScene = gameScene;
    }

    @Override
    public void onInputDown(InputDownEvent event) {
        if (gameScene != null) {
            getEngine().setScene(gameScene);
        }
    }

    @Override
    public void onInputMove(InputMoveEvent event) {

    }

    @Override
    public void onInputUp(InputUpEvent event) {

    }
}
