package io.github.spaceshooter.engine.component;

/**
 * A component that requires to be update each game tick.
 */
public interface TickableComponent extends Component {

    /**
     * This method is invoked every game cycle.
     *
     * @param deltaSeconds the seconds elapsed since the last game cycle.
     */
    void tick(float deltaSeconds);

}
