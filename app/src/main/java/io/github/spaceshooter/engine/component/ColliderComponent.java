package io.github.spaceshooter.engine.component;

import io.github.spaceshooter.engine.collision.Collision;
import io.github.spaceshooter.engine.math.Vector2f;

public interface ColliderComponent extends Component {

    Collision testCollision(ColliderComponent other);

    boolean testPoint(Vector2f point);

}
