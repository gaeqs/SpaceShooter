package io.github.spaceshooter.space.player;

import android.graphics.Canvas;
import android.graphics.Paint;

import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.GameView;
import io.github.spaceshooter.engine.component.BasicComponent;
import io.github.spaceshooter.engine.component.GUIComponent;
import io.github.spaceshooter.engine.component.TickableComponent;
import io.github.spaceshooter.util.TextUtils;

public class ScoreDisplay extends BasicComponent implements GUIComponent, TickableComponent {

    private final Paint paint;
    private PlayerStats stats;
    private volatile int score;

    public ScoreDisplay(GameObject gameObject) {
        super(gameObject);
        paint = new Paint();
        paint.setColor(0);
        paint.setAlpha(255);
        paint.setTextSize(0.05f);
    }

    public PlayerStats getStats() {
        return stats;
    }

    public void setStats(PlayerStats stats) {
        this.stats = stats;
    }

    @Override
    public void tick(float deltaSeconds) {
        score = stats == null ? 0 : stats.score;
    }

    @Override
    public void draw(Canvas canvas, GameView view) {
        String text = String.valueOf(score);
        TextUtils.drawKernedText(canvas, text, 0.2f, 0.9f, paint);

    }

    @Override
    public int getDrawPriority() {
        return Integer.MAX_VALUE;
    }
}
