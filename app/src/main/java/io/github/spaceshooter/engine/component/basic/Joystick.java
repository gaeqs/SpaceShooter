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

public class Joystick extends BasicComponent
        implements InputListenerComponent, GameStatusListenerComponent, GUIComponent {

    private GUIComponentArea area = new GUIComponentArea(
            new Vector2f(0.1f, 0.1f),
            new Vector2f(0.5f, 0.5f),
            true,
            false
    );

    private Vector2f factor = Vector2f.ZERO;
    private boolean executing;
    private int targetJoystick;
    private Vector2f startPosition;

    private final Paint paint;

    public Joystick(GameObject gameObject) {
        super(gameObject);
        paint = new Paint();
        paint.setColor(0x77777777);
    }

    public Vector2f getFactor() {
        return factor;
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

    public void setColor(int color) {
        paint.setColor(color);
    }

    @Override
    public void onInputDown(InputDownEvent event) {
        if (executing) return;

        Area a = area.getArea(getEngine().getGameView());
        if (!a.isInside(event.getScreenPosition())) return;

        startPosition = event.getScreenPosition();
        targetJoystick = event.getPointer();
        executing = true;
        factor = Vector2f.ZERO;
    }

    @Override
    public void onInputMove(InputMoveEvent event) {
        if (!executing || event.getSize() <= targetJoystick) return;

        factor = event.getScreenPosition(targetJoystick)
                .sub(startPosition)
                .div(area.getSize().div(2));

        float mag = factor.magnitudeSquared();
        if (mag > 1) {
            factor = factor.div((float) Math.sqrt(mag));
        }
    }

    @Override
    public void onInputUp(InputUpEvent event) {
        if (!executing || event.getPointer() != targetJoystick) return;
        executing = false;
        factor = Vector2f.ZERO;
    }

    @Override
    public void draw(Canvas canvas, GameView view) {
        Area a = area.getArea(view);
        Vector2f pos = a.getMin();


        Vector2f size2 = area.getSize().div(2);
        Vector2f centerPos = pos.add(size2);
        Vector2f joyPos = centerPos.add(size2.mul(factor));

        canvas.drawCircle(centerPos.x(), centerPos.y(), area.getSize().x() / 8, paint);
        canvas.drawCircle(joyPos.x(), joyPos.y(), area.getSize().x() / 4, paint);
    }

    @Override
    public void onSceneDetach() {
        executing = false;
        factor = Vector2f.ZERO;
    }

    @Override
    public void onPause() {
        executing = false;
        factor = Vector2f.ZERO;
    }

    @Override
    public int getDrawPriority() {
        return Integer.MAX_VALUE;
    }

}
