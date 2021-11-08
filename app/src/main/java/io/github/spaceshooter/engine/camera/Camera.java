package io.github.spaceshooter.engine.camera;

import android.graphics.Matrix;

import io.github.spaceshooter.engine.GameView;
import io.github.spaceshooter.engine.math.Vector2f;
import io.github.spaceshooter.util.Validate;

public class Camera {

    private final GameView gameView;

    private Vector2f position;
    private CameraScreenMode screenMode;
    private CameraOriginMode originMode;
    private Vector2f size;
    private int dirtyCount = 0;

    private Vector2f minCameraArea, maxCameraArea;
    private boolean areaDirty = true;

    public Camera(GameView gameView) {
        this.gameView = gameView;
        this.position = Vector2f.ZERO;
        this.screenMode = CameraScreenMode.FIT_HEIGHT;
        this.originMode = CameraOriginMode.CENTER;
        this.size = Vector2f.ONE;
        this.dirtyCount++;
    }

    public int getDirtyCount() {
        return dirtyCount;
    }

    public float getWidth() {
        if (screenMode == CameraScreenMode.FIT_HEIGHT) {
            return size.y() * gameView.getFinalWidth() / gameView.getFinalHeight();
        }
        return size.x();
    }

    public float getHeight() {
        if (screenMode == CameraScreenMode.FIT_WIDTH) {
            return size.x() * gameView.getFinalHeight() / gameView.getFinalWidth();
        }
        return size.y();
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        Validate.notNull(position, "Position cannot be null!");
        if (this.position.equals(position)) return;
        this.position = position;
        this.dirtyCount++;
        this.areaDirty = true;
    }

    public CameraScreenMode getScreenMode() {
        return screenMode;
    }

    public void setScreenMode(CameraScreenMode screenMode) {
        Validate.notNull(screenMode, "Screen mode cannot be null!");
        if (this.screenMode == screenMode) return;
        this.screenMode = screenMode;
        this.dirtyCount++;
        this.areaDirty = true;
    }

    public CameraOriginMode getOriginMode() {
        return originMode;
    }

    public void setOriginMode(CameraOriginMode originMode) {
        Validate.notNull(originMode, "Origin mode cannot be null!");
        if (this.originMode == originMode) return;
        this.originMode = originMode;
        this.dirtyCount++;
        this.areaDirty = true;
    }

    public Vector2f getSize() {
        return size;
    }

    public void setSize(Vector2f size) {
        Validate.notNull(size, "Size cannot be null!");
        if (this.size.equals(size)) return;
        this.size = size;
        this.dirtyCount++;
        this.areaDirty = true;
    }

    public Vector2f screenToWorld(Vector2f screenPos) {
        float x = screenPos.x();
        float y = screenPos.y();

        float w = gameView.getFinalWidth();
        float h = gameView.getFinalHeight();

        if (originMode == CameraOriginMode.CENTER) {
            x -= w / 2.0f;
            y -= h / 2.0f;
        }

        x *= size.x();
        y *= size.y();

        switch (screenMode) {
            case STRETCH:
                x /= w;
                y /= h;
                break;
            case FIT_HEIGHT:
                x /= h;
                y /= h;
                break;
            case FIT_WIDTH:
                x /= w;
                y /= w;
                break;
        }
        return new Vector2f(x + position.x(), y + position.y());
    }

    public Vector2f worldToScreen(Vector2f worldPos) {
        float x = worldPos.x() - position.x();
        float y = worldPos.y() - position.y();
        float w = gameView.getFinalWidth();
        float h = gameView.getFinalHeight();

        switch (screenMode) {
            case STRETCH:
                x *= w;
                y *= h;
                break;
            case FIT_HEIGHT:
                x *= h;
                y *= h;
                break;
            case FIT_WIDTH:
                x *= w;
                y *= w;
                break;
        }

        x /= size.x();
        y /= size.y();

        if (originMode == CameraOriginMode.CENTER) {
            x += w / 2.0f;
            y += h / 2.0f;
        }

        return new Vector2f(x, y);
    }

    public boolean isInside(Vector2f point) {
        recalculateArea();
        if (point.x() < minCameraArea.x() || point.y() < minCameraArea.y()) return false;
        return point.x() < maxCameraArea.x() && point.y() < maxCameraArea.y();
    }

    public void toViewMatrix(Matrix matrix, int dirtyCount) {
        if (this.dirtyCount == dirtyCount) return;

        float w = gameView.getFinalWidth();
        float h = gameView.getFinalHeight();

        matrix.reset();

        // position to 0, 0
        matrix.postTranslate(-position.x(), -position.y());

        // Scale
        switch (screenMode) {
            case STRETCH:
                matrix.postScale(w, h);
                break;
            case FIT_HEIGHT:
                matrix.postScale(h, h);
                break;
            case FIT_WIDTH:
                matrix.postScale(w, w);
                break;
        }

        matrix.postScale(1 / size.x(), 1 / size.y());

        if (originMode == CameraOriginMode.CENTER) {
            matrix.postTranslate(w / 2.0f, h / 2.0f);
        }
    }

    private void recalculateArea() {
        if (!areaDirty) return;

        minCameraArea = screenToWorld(Vector2f.ZERO);
        maxCameraArea = minCameraArea.add(getWidth(), getHeight());

        areaDirty = false;
    }
}
