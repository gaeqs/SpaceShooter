package io.github.spaceshooter.engine.component.basic;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Locale;

import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.GameView;
import io.github.spaceshooter.engine.component.BasicComponent;
import io.github.spaceshooter.engine.component.GUIComponent;
import io.github.spaceshooter.util.TextUtils;

public class FPSViewer extends BasicComponent implements GUIComponent {

    private final Paint paint;
    private long lastTick = System.nanoTime();

    public FPSViewer(GameObject gameObject) {
        super(gameObject);
        paint = new Paint();
        paint.setColor(0);
        paint.setAlpha(255);
        paint.setTextSize(0.05f);
    }

    @Override
    public void draw(Canvas canvas, GameView view) {
        long now = System.nanoTime();
        long delay = now - lastTick;

        double fps = 1_000_000_000.0 / delay;
        String text =  String.format(Locale.getDefault(), "%.2f", fps);
        TextUtils.drawKernedText(canvas, text, 0.2f, 0.9f, paint);

        lastTick = now;
    }

    @Override
    public int getDrawPriority() {
        return Integer.MAX_VALUE;
    }


}
