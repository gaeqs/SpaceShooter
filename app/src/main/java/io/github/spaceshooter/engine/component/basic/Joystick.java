package io.github.spaceshooter.engine.component.basic;

import android.graphics.Canvas;
import android.graphics.Paint;

import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.GameView;
import io.github.spaceshooter.engine.component.BasicComponent;
import io.github.spaceshooter.engine.component.GUIComponent;
import io.github.spaceshooter.engine.component.GameStatusListenerComponent;
import io.github.spaceshooter.engine.component.InputListenerComponent;
import io.github.spaceshooter.engine.input.InputDownEvent;
import io.github.spaceshooter.engine.input.InputMoveEvent;
import io.github.spaceshooter.engine.input.InputUpEvent;
import io.github.spaceshooter.engine.math.Vector2f;
import io.github.spaceshooter.util.Validate;

public class Joystick extends BasicComponent
        implements InputListenerComponent, GameStatusListenerComponent, GUIComponent {

    private Vector2f size = new Vector2f(0.3f, 0.3f);
    private Vector2f offset = new Vector2f(0.1f, -0.35f);

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

    public Vector2f getSize() {
        return size;
    }

    public void setSize(Vector2f size) {
        Validate.notNull("Size cannot be null!");
        this.size = size;
    }

    public Vector2f getOffset() {
        return offset;
    }

    public void setOffset(Vector2f offset) {
        Validate.notNull("Offset cannot be null!");
        this.offset = offset;
    }

    public void setColor(int color) {
        paint.setColor(color);
    }

    @Override
    public void onInputDown(InputDownEvent event) {
        if (executing) return;

        Vector2f pos = event.getScreenPosition().sub(0, 1 - size.y()).sub(offset);

        if (!isInside(pos)) return;

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
                .div(size.div(2));

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
        Vector2f pos = new Vector2f(0, 1 - size.y()).add(offset);

        Vector2f size2 = size.div(2);
        Vector2f centerPos = pos.add(size2);
        Vector2f joyPos = centerPos.add(size2.mul(factor));

        canvas.drawCircle(centerPos.x(), centerPos.y(), size.x() / 6, paint);
        canvas.drawCircle(joyPos.x(), joyPos.y(), size.x() / 3, paint);
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

    private boolean isInside(Vector2f vec) {
        return vec.x() >= 0 && vec.y() >= 0 &&
                vec.x() <= size.x() && vec.y() <= size.y();
    }

}
