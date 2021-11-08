package io.github.spaceshooter.engine.component.basic;

import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.component.BasicComponent;
import io.github.spaceshooter.engine.component.InputListenerComponent;
import io.github.spaceshooter.engine.input.InputDownEvent;
import io.github.spaceshooter.engine.input.InputMoveEvent;
import io.github.spaceshooter.engine.input.InputUpEvent;

public class InputDebugger extends BasicComponent implements InputListenerComponent {

    public InputDebugger(GameObject gameObject) {
        super(gameObject);
    }

    @Override
    public void onInputDown(InputDownEvent event) {
        System.out.println("DOWN " + event.getPointer());
    }

    @Override
    public void onInputMove(InputMoveEvent event) {
        System.out.println("MOVE");
    }

    @Override
    public void onInputUp(InputUpEvent event) {
        System.out.println("UP " + event.getPointer());
    }
}
