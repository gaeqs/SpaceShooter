package io.github.spaceshooter.space.player;

import android.graphics.Canvas;
import android.graphics.Paint;

import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.GameView;
import io.github.spaceshooter.engine.component.BasicComponent;
import io.github.spaceshooter.engine.component.GUIComponent;

public class HealthBar extends BasicComponent implements GUIComponent {

    private static final float HEIGHT = 0.1f;
    private static final float SPACING = 0.05f;

    private static final Paint background = new Paint();
    private static final Paint paint = new Paint();

    static {
        background.setColor(0xFF000000);
        paint.setColor(0xFFFF0000);
    }

    private final PlayerData playerData;

    public HealthBar(GameObject gameObject) {
        super(gameObject);
        playerData = gameObject.getComponent(PlayerData.class);
    }

    @Override
    public void draw(Canvas canvas, GameView view) {
        float max = view.getGUIWidth() - SPACING;

        canvas.drawRect(
                SPACING,
                SPACING,
                max,
                SPACING + HEIGHT,
                background
        );

        float percentage = playerData.getHealth() / (float) PlayerData.MAX_HEALTH;

        canvas.drawRect(
                SPACING,
                SPACING,
                SPACING * (1 - percentage) + max * percentage,
                SPACING + HEIGHT,
                paint
        );
    }

    @Override
    public int getDrawPriority() {
        return Integer.MAX_VALUE;
    }
}
