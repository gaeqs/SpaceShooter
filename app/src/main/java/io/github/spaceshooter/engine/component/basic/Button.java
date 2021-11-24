package io.github.spaceshooter.engine.component.basic;

import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.component.BasicComponent;
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
        implements InputListenerComponent, GameStatusListenerComponent {

    private GUIComponentArea area = new GUIComponentArea(
            new Vector2f(0.1f, 0.1f),
            new Vector2f(0.5f, 0.5f),
            true,
            false
    );

    private boolean executing;
    private int targetPoint;

    private Runnable onPress, onRelease;

    public Button(GameObject gameObject) {
        super(gameObject);
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

}
