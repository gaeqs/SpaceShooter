package io.github.spaceshooter.engine.collision;

import io.github.spaceshooter.engine.component.ColliderComponent;
import io.github.spaceshooter.engine.math.Vector2f;

public abstract class Collision {

    private final ColliderComponent otherCollider;
    private Vector2f collisionPoint;
    private Vector2f otherNormal;
    private float penetration;
    private boolean penetrationCalculated = false;

    public Collision(ColliderComponent otherCollider) {
        this.otherCollider = otherCollider;
    }

    public ColliderComponent getOtherCollider() {
        return otherCollider;
    }

    public Vector2f getCollisionPoint() {
        if (collisionPoint == null) collisionPoint = calculateCollisionPoint();
        return collisionPoint;
    }

    public Vector2f getOtherNormal() {
        if (otherNormal == null) otherNormal = calculateOtherNormal();
        return otherNormal;
    }

    public float getPenetration() {
        if (!penetrationCalculated) {
            penetration = calculatePenetration();
            penetrationCalculated = true;
        }
        return penetration;
    }

    protected abstract Vector2f calculateCollisionPoint();

    protected abstract Vector2f calculateOtherNormal();

    protected abstract float calculatePenetration();
}
