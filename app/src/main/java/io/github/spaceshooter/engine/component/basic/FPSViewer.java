package io.github.spaceshooter.engine.component.basic;

import android.graphics.Canvas;

import java.util.Locale;

import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.GameView;
import io.github.spaceshooter.engine.component.BasicComponent;
import io.github.spaceshooter.engine.component.GUIComponent;
import io.github.spaceshooter.engine.component.TickableComponent;

public class FPSViewer extends BasicComponent implements TickableComponent, GUIComponent {

    private final Text text;

    private long lastTick = System.nanoTime();
    private int size;

    public FPSViewer(GameObject gameObject) {
        super(gameObject);
        text = gameObject.getComponent(Text.class);
    }

    @Override
    public void tick(float deltaSeconds) {
        size = getScene().getGameObjectsAmount();
    }

    @Override
    public void draw(Canvas canvas, GameView view) {
        long now = System.nanoTime();
        long delay = now - lastTick;
        double fps = 1_000_000_000.0 / delay;
        text.setText(String.format(Locale.getDefault(), "%.2f", fps) + " (" + size + ")");
        lastTick = now;
    }

    @Override
    public int getDrawPriority() {
        return Integer.MAX_VALUE;
    }
}
