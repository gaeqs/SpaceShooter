package io.github.spaceshooter.engine.component;

public interface TickableComponent extends Component {

    void tick(float deltaSeconds);

}
