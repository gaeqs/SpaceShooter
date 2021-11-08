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

public class Button extends BasicComponent
        implements InputListenerComponent, GameStatusListenerComponent, GUIComponent {

    private Vector2f size = new Vector2f(0.3f, 0.3f);
    private Vector2f offset = new Vector2f(-0.15f, -0.15f);

    private boolean pressed;
    private int targetJoystick;
    private final Paint paint;

    private Runnable onPress, onRelease;

    public Button(GameObject gameObject) {
        super(gameObject);
        paint = new Paint();
        paint.setColor(0x77777777);
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


    public void setOnPress(Runnable onPress) {
        this.onPress = onPress;
    }

    public void setOnRelease(Runnable onRelease) {
        this.onRelease = onRelease;
    }

    @Override
    public void onInputDown(InputDownEvent event) {
        if (pressed) return;

        float w = getScene().getEngine().getGameView().getGUIWidth();
        Vector2f pos = event.getScreenPosition().sub(w - size.x(), 1 - size.y()).sub(offset);

        if (!isInside(pos)) return;

        targetJoystick = event.getPointer();
        pressed = true;
        if (onPress != null) {
            onPress.run();
        }
    }

    @Override
    public void onInputMove(InputMoveEvent event) {
    }

    @Override
    public void onInputUp(InputUpEvent event) {
        if (!pressed || event.getPointer() != targetJoystick) return;
        pressed = false;
        if (onRelease != null) {
            onRelease.run();
        }
    }

    @Override
    public void draw(Canvas canvas, GameView view) {
        float w = view.getGUIWidth();
        Vector2f pos = new Vector2f(w - size.x(), 1 - size.y()).add(offset);
        Vector2f centerPos = pos.add(size.div(2));
        canvas.drawCircle(centerPos.x(), centerPos.y(), size.x() / 3, paint);
    }

    @Override
    public void onSceneDetach() {
        pressed = false;
    }

    @Override
    public void onPause() {
        pressed = false;
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
