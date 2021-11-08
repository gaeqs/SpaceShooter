package io.github.spaceshooter.engine.component;

public interface GameStatusListenerComponent extends Component {

    default void onPause() {
    }

    default void onResume() {
    }

    default void onSceneAttach() {
    }

    default void onSceneDetach() {
    }

}
