package io.github.spaceshooter.engine.component;

import io.github.spaceshooter.engine.collision.Collision;
import io.github.spaceshooter.engine.math.Vector2f;

/**
 * Represents a {@link Component} that other components can collide with.
 */
public interface ColliderComponent extends Component {

    /**
     * Test if this collider collides with the given collider..
     *
     * @param other the other collider.
     * @return the collision or null if not present.
     */
    Collision testCollision(ColliderComponent other);

    /**
     * Returns whether the given point is inside this collider.
     *
     * @param point the point.
     * @return whether the point is inside.
     */
    boolean testPoint(Vector2f point);

}
