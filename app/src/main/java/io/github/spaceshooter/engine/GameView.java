package io.github.spaceshooter.engine;

import android.content.Context;

import io.github.spaceshooter.engine.math.Vector2f;

public interface GameView {

    void draw();

    void setEngine(GameEngine engine);

    int getWidth();

    int getHeight();

    int getPaddingLeft();

    int getPaddingRight();

    int getPaddingTop();

    int getPaddingBottom();

    Context getContext();

    default int getFinalWidth() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    default int getFinalHeight() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

    default int getGUIWidth() {
        return getFinalWidth() / getFinalHeight();
    }

    default Vector2f screenToGUI(Vector2f vec) {
        return vec.div(getFinalHeight());
    }

    default Vector2f guiToScreen(Vector2f vec) {
        return vec.mul(getFinalHeight());
    }
}
