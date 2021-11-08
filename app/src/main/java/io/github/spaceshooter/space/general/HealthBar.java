package io.github.spaceshooter.space.general;

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

    private LivingComponent livingComponent;

    public HealthBar(GameObject gameObject) {
        super(gameObject);
    }

    public LivingComponent getLivingComponent() {
        return livingComponent;
    }

    public void setLivingComponent(LivingComponent livingComponent) {
        this.livingComponent = livingComponent;
    }

    @Override
    public void draw(Canvas canvas, GameView view) {
        if (livingComponent == null) return;

        float max = view.getGUIWidth() - SPACING;

        canvas.drawRect(
                SPACING,
                SPACING,
                max,
                SPACING + HEIGHT,
                background
        );

        float percentage = livingComponent.getHealth() / (float) livingComponent.getMaxHealth();

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
