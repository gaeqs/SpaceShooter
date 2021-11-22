package io.github.spaceshooter.engine.component.basic;

import android.graphics.Canvas;
import android.graphics.Paint;

import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.GameView;
import io.github.spaceshooter.engine.component.BasicComponent;
import io.github.spaceshooter.engine.component.GUIComponent;
import io.github.spaceshooter.engine.component.GameStatusListenerComponent;
import io.github.spaceshooter.engine.component.InputListenerComponent;
import io.github.spaceshooter.engine.gui.GUIComponentArea;
import io.github.spaceshooter.engine.input.InputDownEvent;
import io.github.spaceshooter.engine.input.InputMoveEvent;
import io.github.spaceshooter.engine.input.InputUpEvent;
import io.github.spaceshooter.engine.math.Area;
import io.github.spaceshooter.engine.math.Vector2f;
import io.github.spaceshooter.util.Validate;

public class Button extends BasicComponent
        implements InputListenerComponent, GameStatusListenerComponent, GUIComponent {

    private GUIComponentArea area = new GUIComponentArea(
            new Vector2f(0.1f, 0.1f),
            new Vector2f(0.5f, 0.5f),
            true,
            false
    );

    private boolean executing;
    private int targetPoint;

    private final Paint paint;

    private Runnable onPress, onRelease;

    public Button(GameObject gameObject) {
        super(gameObject);
        paint = new Paint();
        paint.setColor(0x77777777);
    }

    public boolean isExecuting() {
        return executing;
    }

    public GUIComponentArea getArea() {
        return area;
    }

    public void setArea(GUIComponentArea area) {
        Validate.notNull(area, "Area cannot be null!");
        this.area = area;
    }


    public Paint getPaint() {
        return paint;
    }

    public Runnable getOnPress() {
        return onPress;
    }

    public void setOnPress(Runnable onPress) {
        this.onPress = onPress;
    }

    public Runnable getOnRelease() {
        return onRelease;
    }

    public void setOnRelease(Runnable onRelease) {
        this.onRelease = onRelease;
    }

    @Override
    public void onInputDown(InputDownEvent event) {
        if (executing) return;

        Area a = area.getArea(getEngine().getGameView());
        if (!a.isInside(event.getScreenPosition())) return;

        targetPoint = event.getPointer();


        if (onPress != null) {
            onPress.run();
        }

        executing = true;
    }

    @Override
    public void onInputMove(InputMoveEvent event) {
    }

    @Override
    public void onInputUp(InputUpEvent event) {
        if (!executing || event.getPointer() != targetPoint) return;
        if (onRelease != null) {
            onRelease.run();
        }
        executing = false;
    }

    @Override
    public void draw(Canvas canvas, GameView view) {
        Area a = area.getArea(view);
        Vector2f pos = a.getMin();
        Vector2f size2 = area.getSize().div(2);
        Vector2f centerPos = pos.add(size2);

        float radius = Math.min(area.getSize().x(), area.getSize().y()) / 2;

        canvas.drawCircle(centerPos.x(), centerPos.y(), radius, paint);
    }

    @Override
    public void onSceneDetach() {
        if (executing) {
            if (onRelease != null) {
                onRelease.run();
            }
        }
        executing = false;
    }

    @Override
    public void onPause() {
        if (executing) {
            if (onRelease != null) {
                onRelease.run();
            }
        }
        executing = false;
    }

    @Override
    public int getDrawPriority() {
        return Integer.MAX_VALUE;
    }

}
