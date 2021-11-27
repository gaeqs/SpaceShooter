package io.github.spaceshooter.engine.component;


import io.github.spaceshooter.engine.collision.Collision;

/**
 * This component listens the collisions created by the {@link ColliderComponent}s inside
 * the same {@link io.github.spaceshooter.engine.GameObject GameObject}.
 */
public interface CollisionListenerComponent extends Component {

    /**
     * This method is called when a collision happens.
     *
     * @param collision the collision.
     */
    void onCollision(Collision collision);

}