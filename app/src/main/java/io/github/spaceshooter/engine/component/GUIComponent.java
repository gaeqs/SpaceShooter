package io.github.spaceshooter.engine.component;

import android.graphics.Canvas;

import java.util.Comparator;

import io.github.spaceshooter.engine.GameView;

public interface GUIComponent extends Component {

    Comparator<GUIComponent> COMPARATOR = (o1, o2) -> {
        if (o1.getDrawPriority() == o2.getDrawPriority())
            return o1.getId() - o2.getId();
        return o1.getDrawPriority() - o2.getDrawPriority();
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
