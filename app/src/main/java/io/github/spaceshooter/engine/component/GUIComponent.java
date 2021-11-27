package io.github.spaceshooter.engine.component;

import android.graphics.Canvas;

import java.util.Comparator;

import io.github.spaceshooter.engine.GameView;

/**
 * Represents a component that can draw elements.
 * <p>
 * The drawn elements are NOT modified by the
 * {@link io.github.spaceshooter.engine.Scene scene}'s camera.
 * <p>
 * These components are drawn adter the {@link DrawableComponent}s.
 */
public interface GUIComponent extends Component {

    Comparator<GUIComponent> COMPARATOR = (o1, o2) -> {
        if (o2.getDrawPriority() == o1.getDrawPriority())
            return -Integer.compare(o2.getId(), o1.getId());
        return -Integer.compare(o2.getDrawPriority(), o1.getDrawPriority());
    };

    /**
     * It is safe to access the information about the
     * game object and all its components when this
     * method is invoked, but it is not safe to
     * delete the game object or add / remove components!
     *
     * @param canvas the canvas to draw.
     */
    void draw(Canvas canvas, GameView view);

    /**
     * Returns the draw priority of this component.
     * This must be immutable!
     *
     * @return the draw priority.
     */
    int getDrawPriority();
}
