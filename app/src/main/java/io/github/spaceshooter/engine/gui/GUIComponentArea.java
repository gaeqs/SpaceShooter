package io.github.spaceshooter.engine.gui;

import android.graphics.RectF;

import io.github.spaceshooter.engine.GameView;
import io.github.spaceshooter.engine.math.Area;
import io.github.spaceshooter.engine.math.Vector2f;
import io.github.spaceshooter.util.Validate;

public class GUIComponentArea {

    private Vector2f offset;
    private Vector2f size;

    private boolean attachToBottom;
    private boolean attachToRight;

    public GUIComponentArea(Vector2f offset, Vector2f size,
                            boolean attachToBottom, boolean attachToRight) {
        this.offset = offset;
        this.size = size;
        this.attachToBottom = attachToBottom;
        this.attachToRight = attachToRight;
    }

    public Vector2f getOffset() {
        return offset;
    }

    public void setOffset(Vector2f offset) {
        Validate.notNull(offset, "Offset cannot be null!");
        this.offset = offset;
    }

    public Vector2f getSize() {
        return size;
    }

    public void setSize(Vector2f size) {
        Validate.notNull(size, "Size cannot be null!");
        this.size = size;
    }

    public boolean shouldAttachToBottom() {
        return attachToBottom;
    }

    public void setAttachToBottom(boolean attachToBottom) {
        this.attachToBottom = attachToBottom;
    }

    public boolean shouldAttachToRight() {
        return attachToRight;
    }

    public void setAttachToRight(boolean attachToRight) {
        this.attachToRight = attachToRight;
    }

    public Vector2f getStartPosition(GameView view) {
        float x = attachToRight ? view.getGUIWidth() - offset.x() - size.x() : offset.x();
        float y = attachToBottom ? 1 - offset.y() - size.y() : offset.y();
        return new Vector2f(x, y);
    }

    public Area getArea(GameView view) {
        Vector2f start = getStartPosition(view);
        return new Area(start, start.add(size));
    }

    public RectF getRect(GameView view) {
        Vector2f start = getStartPosition(view);
        return new RectF(start.x(), start.y(),
                start.x() + size.x(), start.y() + size.y());
    }
}
