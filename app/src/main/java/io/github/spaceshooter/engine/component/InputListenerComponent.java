package io.github.spaceshooter.engine.component;


import io.github.spaceshooter.engine.input.InputDownEvent;
import io.github.spaceshooter.engine.input.InputMoveEvent;
import io.github.spaceshooter.engine.input.InputUpEvent;

public interface InputListenerComponent extends Component {

    void onInputDown(InputDownEvent event);

    void onInputMove(InputMoveEvent event);

    void onInputUp(InputUpEvent event);
}