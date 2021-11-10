package io.github.spaceshooter.space.background;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.GameView;
import io.github.spaceshooter.engine.component.BasicComponent;
import io.github.spaceshooter.engine.component.DrawableComponent;

public class PlayArea extends BasicComponent implements DrawableComponent {

    public static final RectF PLAY_AREA = new RectF(-1, -1, 1, 1);
    public static final RectF DESPAWN_AREA = new RectF(-2, -2, 2, 2);
    private static final Paint PAINT = new Paint();

    static {
        PAINT.setStyle(Paint.Style.STROKE);
        PAINT.setColor(0xFF777777);
    }

    public PlayArea(GameObject gameObject) {
        super(gameObject);
    }

    @Override
    public void draw(Canvas canvas, GameView view) {
        canvas.drawRect(PLAY_AREA, PAINT);
    }

    @Override
    public int getDrawPriority() {
        return -100;
    }
}
