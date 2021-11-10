package io.github.spaceshooter.space.background;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.GameView;
import io.github.spaceshooter.engine.component.BasicComponent;
import io.github.spaceshooter.engine.component.DrawableComponent;
import io.github.spaceshooter.engine.component.TickableComponent;
import io.github.spaceshooter.engine.math.Vector2f;

public class Background extends BasicComponent implements DrawableComponent, TickableComponent {

    private final Paint PAINT = new Paint();
    private final Paint PAINT_DEBUG = new Paint();
    private final RectF RECT = new RectF();
    private final RectF RECT_INNER_TEMP = new RectF();
    private final RectF RECT_INNER = new RectF();

    private Vector2f center;

    public Background(GameObject gameObject) {
        super(gameObject);
    }

    @Override
    public void tick(float deltaSeconds) {
        center = getScene().getCamera().getPosition();
    }

    @Override
    public void draw(Canvas canvas, GameView view) {
        PAINT.setColor(0xFF555555);
        RECT_INNER.set(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY,
                Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);

        int steps = 20;

        int red = 255 / steps;
        int green = 255 / steps;
        int blue = 150 / steps;

        Vector2f cen = center.div(steps);
        for (int i = 0; i < steps; i++) {
            float m = i / 2.0f;

            RECT.set(
                    cen.x() * m - 1.0f / m,
                    cen.y() * m - 1.0f / m,
                    cen.x() * m + 1.0f / m,
                    cen.y() * m + 1.0f / m
            );

            intersection(RECT, RECT_INNER, RECT);

            PAINT.setColor(toARGB(
                    255,
                    red * (steps - i),
                    green * (steps - i),
                    blue * (steps - i)
            ));

            canvas.drawRect(RECT, PAINT);

            RECT_INNER_TEMP.set(
                    cen.x() * m - 1.0f / (m + 1),
                    cen.y() * m - 1.0f / (m + 1),
                    cen.x() * m + 1.0f / (m + 1),
                    cen.y() * m + 1.0f / (m + 1)
            );

            intersection(RECT_INNER_TEMP, RECT_INNER, RECT_INNER);
        }

    }

    @Override
    public int getDrawPriority() {
        return -200;
    }


    private static int toARGB(int alpha, int red, int green, int blue) {
        return (alpha << 24) |
                (red << 16) |
                (green << 8) |
                blue;
    }


    private static void intersection(RectF a, RectF b, RectF set) {
        set.set(
                Math.max(a.left, b.left),
                Math.max(a.top, b.top),
                Math.min(a.right, b.right),
                Math.min(a.bottom, b.bottom)
        );
        if (set.right < set.left) set.right = set.left;
        if (set.bottom < set.top) set.bottom = set.top;
    }
}
