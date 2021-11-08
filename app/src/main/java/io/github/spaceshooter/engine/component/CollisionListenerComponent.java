package io.github.spaceshooter.engine.component;


import io.github.spaceshooter.engine.collision.Collision;

public interface CollisionListenerComponent extends Component {

    void onCollision(Collision collision);

}