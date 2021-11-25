package io.github.spaceshooter.space.scene;

import io.github.spaceshooter.engine.GameEngine;
import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.GameView;
import io.github.spaceshooter.engine.Scene;
import io.github.spaceshooter.engine.component.BasicComponent;
import io.github.spaceshooter.engine.component.basic.Button;
import io.github.spaceshooter.engine.component.basic.Circle;
import io.github.spaceshooter.engine.component.basic.Image;
import io.github.spaceshooter.engine.component.basic.Text;
import io.github.spaceshooter.engine.gui.GUIComponentArea;
import io.github.spaceshooter.engine.math.Vector2f;
import io.github.spaceshooter.space.general.Team;
import io.github.spaceshooter.space.player.ShipType;

public class MainMenuScene extends Scene {

    private ShipType type = ShipType.NORMAL;
    private Circle selectedCircle;

    public MainMenuScene(GameEngine engine) {
        super(engine);

        GameObject timerO = newGameObject();
        BasicComponent timer = timerO.addComponent(BasicComponent.class);
        timer.runLater(0.2f, () -> {
            initTitle(engine.getGameView());
            initShips(engine.getGameView());
            initPlayButton(engine.getGameView());
            destroyGameObject(timerO);
        });
    }


    private void initTitle(GameView view) {
        GameObject o = newGameObject("Title");
        Text text = o.addComponent(Text.class);
        text.getPaint().setColor(0xFF000000);
        text.getPaint().setTextSize(0.2f);
        text.setCentered(true);
        text.setText("Candy wars");
        text.setPosition(new Vector2f(view.getGUIWidth() / 2, 0.25f));
    }

    private void initShips(GameView view) {
        ShipType[] types = ShipType.values();

        int size2 = types.length / 2;

        float gw = view.getGUIWidth();
        int i = 0;
        for (ShipType type : types) {
            GameObject object = newGameObject("Ship Type");
            Vector2f offset = new Vector2f(gw / 2 - (size2 - i) * 0.3f - 0.1f, 0.5f);
            Vector2f size = new Vector2f(0.2f, 0.2f);

            GUIComponentArea area = new GUIComponentArea(offset, size,
                    false, false);

            Image image = object.addComponent(Image.class);
            image.setBitmap(type.getSprite(Team.TEAM_1));
            image.setArea(area);

            Circle circle = object.addComponent(Circle.class);
            circle.setArea(area);

            if (type == ShipType.NORMAL) {
                circle.getPaint().setColor(0xFFFFFF00);
                selectedCircle = circle;
            } else {
                circle.getPaint().setColor(0);
            }

            Button button = object.addComponent(Button.class);
            button.setArea(area);
            button.setOnPress(() -> {
                this.type = type;
                selectedCircle.getPaint().setColor(0);
                circle.getPaint().setColor(0xFFFFFF00);
                selectedCircle = circle;
            });

            i++;
        }
    }

    private void initPlayButton(GameView view) {
        GameObject o = newGameObject("Button");

        GUIComponentArea area =
                new GUIComponentArea(new Vector2f(0.2f, 0.7f),
                        new Vector2f(view.getGUIWidth() - 0.4f, 0.3f),
                        false, false);

        Button button = o.addComponent(Button.class);
        button.setArea(area);
        button.setOnPress(() -> getEngine().setScene(new GameScene(getEngine(), type)));

        Text text = o.addComponent(Text.class);

        text.setText("Let's play!");
        text.getPaint().setColor(0xFF000000);
        text.getPaint().setTextSize(0.1f);
        text.setPosition(new Vector2f(view.getGUIWidth() / 2, 0.9f));
        text.setCentered(true);


    }
}
