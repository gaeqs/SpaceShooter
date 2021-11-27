package io.github.spaceshooter.engine.component;


import io.github.spaceshooter.engine.input.InputDownEvent;
import io.github.spaceshooter.engine.input.InputMoveEvent;
import io.github.spaceshooter.engine.input.InputUpEvent;

/**
 * A component that listens to input events.
 */
public interface InputListenerComponent extends Component {

    /**
     * This method is invoked when the player press the touchscreen.
     *
     * @param event the event.
     */
    void onInputDown(InputDownEvent event);

    /**
     * This method is invoked when the player moves its finger along the touchscreen.
     *
     * @param event the event.
     */
    void onInputMove(InputMoveEvent event);

    /**
     * This method is invoked when the player releases this finger from the touchscreen.
     *
     * @param event the event.
     */
    void onInputUp(InputUpEvent event);
}