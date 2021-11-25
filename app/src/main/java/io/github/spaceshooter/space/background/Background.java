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
import io.github.spaceshooter.space.player.PlayerData;

public class Background extends BasicComponent implements DrawableComponent, TickableComponent {

    private final Paint PAINT = new Paint();
    private final RectF RECT = new RectF();
    private final RectF RECT_INNER_TEMP = new RectF();
    private final RectF RECT_INNER = new RectF();

    private final PlayerData player;

    private Vector2f center;
    private int red, green;

    private float multiplier = 0;
    private float seconds = 0;

    public Background(GameObject gameObject) {
        super(gameObject);
        player = gameObject.findComponent(PlayerData.class);
    }

    @Override
    public void tick(float deltaSeconds) {
        center = getScene().getCamera().getPosition();
        float health = player.getHealth() / (float) player.getMaxHealth();

        float blend = 0.02f;
        float inv = 1 - blend;

        if (health > 0.5f) {
            red = (int) (red * inv + ((1 - health) * 2 * 255) * blend);
            green = (int) (green * inv + 255 * blend);
        } else {
            red = (int) (red * inv + 127 * blend);
            green = (int) (green * inv + (health * 2 * 127) * blend);
        }

        seconds += deltaSeconds;
        multiplier = (float) (Math.sin(seconds * (5 - health * 5)) + 3) / 4;

    }

    @Override
    public void draw(Canvas canvas, GameView view) {
        if (center == null) return;
        RECT_INNER.set(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY,
                Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);

        int steps = 20;

        int red = this.red / steps;
        int green = this.green / steps;
        int blue = 50 / steps;

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
                    (int) (red * (steps + i) * multiplier),
                    (int) (green * (steps + i) * multiplier),
                    (int) (blue * (steps + i) * multiplier)
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
        alpha = Math.min(Math.max(alpha, 0), 255);
        red = Math.min(Math.max(red, 0), 255);
        green = Math.min(Math.max(green, 0), 255);
        blue = Math.min(Math.max(blue, 0), 255);
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
