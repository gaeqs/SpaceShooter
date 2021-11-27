package io.github.spaceshooter.engine.component;

/**
 * A component that listens to basic events in the game engine.
 */
public interface GameStatusListenerComponent extends Component {

    /**
     * This method is invoked when the game engine is paused.
     */
    default void onPause() {
    }

    /**
     * This method is invoked when the game engine is resumed.
     */
    default void onResume() {
    }

    /**
     * This method is invoked when the scene where this component
     * is inside is attached to the game engine.
     */
    default void onSceneAttach() {
    }

    /**
     * This method is invoked when the scene where this component
     * is inside is detached to the game engine.
     */
    default void onSceneDetach() {
    }

}
