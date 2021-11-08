package io.github.spaceshooter.engine;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import io.github.spaceshooter.engine.input.InputDownEvent;
import io.github.spaceshooter.engine.input.InputMoveEvent;
import io.github.spaceshooter.engine.input.InputUpEvent;
import io.github.spaceshooter.engine.math.Vector2f;

public class StandardGameView extends View implements GameView {

    private GameEngine engine;

    public StandardGameView(Context context) {
        super(context);
        setOnTouchListener((v, event) -> {
            onMotionEvent(event);
            return true;
        });
    }

    public StandardGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener((v, event) -> {
            onMotionEvent(event);
            return true;
        });
    }

    public StandardGameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnTouchListener((v, event) -> {
            onMotionEvent(event);
            return true;
        });
    }

    @Override
    public void draw() {
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (engine != null) {
            engine.draw(canvas);
        }
    }

    @Override
    public void setEngine(GameEngine engine) {
        this.engine = engine;
    }

    private void onMotionEvent(MotionEvent ev) {
        if (engine == null) return;

        float fh = getFinalHeight();

        if (ev.getActionMasked() == MotionEvent.ACTION_MOVE) {
            int pointerCount = ev.getPointerCount();
            int[] pointers = new int[pointerCount];
            Vector2f[] screenPositions = new Vector2f[pointerCount];

            for (int p = 0; p < pointerCount; p++) {
                pointers[p] = ev.getPointerId(p);
                screenPositions[p] = new Vector2f(ev.getX(p) / fh, ev.getY(p) / fh);
            }

            engine.queueInputEvent(new InputMoveEvent(engine, pointers, screenPositions));
            return;
        }

        int index = ev.getActionIndex();
        int id = ev.getPointerId(index);
        Vector2f position = new Vector2f(ev.getX(index) / fh, ev.getY(index) / fh);
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                engine.queueInputEvent(new InputDownEvent(engine, id, position));
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                engine.queueInputEvent(new InputUpEvent(engine, id, position));
                break;
        }
    }
}
